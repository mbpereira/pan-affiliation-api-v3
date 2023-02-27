package pan.affiliation.application.usecases.customers;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pan.affiliation.domain.modules.customers.commands.CreateCustomerCommandHandler;
import pan.affiliation.domain.modules.customers.entities.Customer;
import pan.affiliation.domain.modules.customers.queries.GetCustomerByDocumentNumberQueryHandler;
import pan.affiliation.domain.modules.customers.valueobjects.DocumentNumber;
import pan.affiliation.shared.exceptions.CommandException;
import pan.affiliation.shared.exceptions.QueryException;
import pan.affiliation.shared.validation.Error;
import pan.affiliation.shared.validation.ValidationContext;
import pan.affiliation.shared.validation.ValidationContextImpl;
import pan.affiliation.shared.validation.ValidationStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CreateCustomerUseCaseTest {
    private final ValidationContext validationContext;
    private final CreateCustomerCommandHandler commandHandlerMock;
    private final GetCustomerByDocumentNumberQueryHandler queryHandlerMock;

    public CreateCustomerUseCaseTest() {
        this.commandHandlerMock = Mockito.mock(CreateCustomerCommandHandler.class);
        this.validationContext = new ValidationContextImpl();
        this.queryHandlerMock = Mockito.mock(GetCustomerByDocumentNumberQueryHandler.class);
    }

    @Test
    public void createCustomer_shouldReturnNullIfCustomerIsNotValid() {
        var input = getInvalidInput();
        var useCase = getUseCase();

        var result = useCase.createCustomer(input);
        var notValidatedFields = getNotValidatedFields();

        assertNull(result);
        assertEquals(0, notValidatedFields.size());
    }

    @SneakyThrows
    @Test
    public void createCustomer_shouldReturnNullIfProvidedDocumentNumberAlreadyExists() {
        var input = getValidInput();
        var useCase = getUseCase();
        Mockito.when(this.queryHandlerMock.getCustomerByDocumentNumber(Mockito.any(DocumentNumber.class)))
                .thenReturn(input.toDomainEntity());

        var result = useCase.createCustomer(input);

        assertNull(result);
        assertEquals(ValidationStatus.CONFLICT, this.validationContext.getValidationStatus());
    }

    @SneakyThrows
    @Test
    public void createCustomer_shouldReturnNullIfCommandHandlerRaisesException() {
        var input = getValidInput();
        var useCase = getUseCase();
        var commandException = new CommandException(
                ValidationStatus.INTEGRATION_ERROR.toString(),
                "errorCode");
        Mockito.when(this.commandHandlerMock.createCustomer(Mockito.any(Customer.class)))
                .thenThrow(commandException);

        var result = useCase.createCustomer(input);

        assertNull(result);
        assertEquals(ValidationStatus.INTEGRATION_ERROR, this.validationContext.getValidationStatus());
    }

    @SneakyThrows
    @Test
    public void createCustomer_shouldReturnNullIfQueryHandlerRaisesException() {
        var input = getValidInput();
        var useCase = getUseCase();
        var queryException = new QueryException(
                ValidationStatus.INTEGRATION_ERROR.toString(),
                "errorCode");
        Mockito.when(this.queryHandlerMock.getCustomerByDocumentNumber(Mockito.any(DocumentNumber.class)))
                .thenThrow(queryException);

        var result = useCase.createCustomer(input);

        assertNull(result);
        assertEquals(ValidationStatus.INTEGRATION_ERROR, this.validationContext.getValidationStatus());
    }

    @SneakyThrows
    @Test
    public void createCustomer_shouldReturnCreatedCustomer() {
        var input = getValidInput();
        var customer = input.toDomainEntity();
        var useCase = getUseCase();

        var id = UUID.randomUUID();
        Mockito.when(this.commandHandlerMock.createCustomer(Mockito.any(Customer.class)))
                .thenReturn(new Customer(
                        id,
                        customer.getDocumentNumber(),
                        customer.getName(),
                        customer.getAddresses()
                ));

        var result = useCase.createCustomer(input);

        assertEquals(customer.getName(), result.getName());
        assertEquals(id, result.getId());
    }

    private CreateCustomerInput getValidInput() {
        var addresses = getValidAddresses();
        return new CreateCustomerInput(
                "Mateus",
                "858.493.850-88",
                addresses
        );
    }

    private List<AddressInput> getValidAddresses() {
        var addresses = new ArrayList<AddressInput>();
        addresses.add(getValidAddress());
        return addresses;
    }

    private AddressInput getValidAddress() {
        return new AddressInput(
                "78085630",
                "Av. Sto Antonio",
                348,
                "Cuiabá",
                "MT",
                "Brasil",
                null,
                "Chácara dos pinheiros"
        );
    }

    private List<String> getNotValidatedFields() {
        var expectedValidatedFields = getExpectedValidatedFields();
        var invalidFields = this.validationContext.getErrors().stream().map(Error::key).toList();
        return Arrays.stream(expectedValidatedFields)
                .filter(e -> !invalidFields.contains(e))
                .toList();
    }

    private static String[] getExpectedValidatedFields() {
        return new String[]{
                "documentNumber",
                "name",
                "street",
                "city",
                "state",
                "country",
                "neighborhood"
        };
    }

    private CreateCustomerUseCase getUseCase() {
        return new CreateCustomerUseCase(
                this.commandHandlerMock,
                this.queryHandlerMock,
                this.validationContext);
    }

    private static CreateCustomerInput getInvalidInput() {
        var address = getInvalidAddressInput();
        var addresses = new ArrayList<AddressInput>();
        addresses.add(address);

        return new CreateCustomerInput(
                "M",
                "",
                addresses
        );
    }

    private static AddressInput getInvalidAddressInput() {
        return new AddressInput(
                null,
                null,
                0,
                null,
                null,
                null,
                null,
                null
        );
    }
}