package pan.affiliation.application.usecases.customers;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pan.affiliation.domain.modules.customers.commands.ChangeCustomerCommandHandler;
import pan.affiliation.domain.modules.customers.entities.Address;
import pan.affiliation.domain.modules.customers.entities.Customer;
import pan.affiliation.domain.modules.customers.queries.GetCustomerByIdQueryHandler;
import pan.affiliation.shared.exceptions.QueryException;
import pan.affiliation.shared.validation.ValidationContext;
import pan.affiliation.shared.validation.ValidationContextImpl;
import pan.affiliation.shared.validation.ValidationStatus;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ChangeAddressUseCaseTest {
    private final ChangeCustomerCommandHandler command;
    private final ValidationContext validationContext;
    private final GetCustomerByIdQueryHandler query;
    private final Faker faker = new Faker();

    public ChangeAddressUseCaseTest() {
        this.query = Mockito.mock(GetCustomerByIdQueryHandler.class);
        this.validationContext = new ValidationContextImpl();
        this.command = Mockito.mock(ChangeCustomerCommandHandler.class);
    }

    @Test
    public void changeAddress_shouldReturnNullIfCustomerDoesNotExists() {
        var changeAddressInput = new ChangeAddressInput(UUID.randomUUID(), UUID.randomUUID(), getValidAddressInput());
        var useCase = getUseCase();

        var result = useCase.changeAddress(changeAddressInput);

        assertNull(result);
        assertEquals(ValidationStatus.NOT_FOUND, this.validationContext.getValidationStatus());
        assertEquals("customer", this.validationContext.getErrors().get(0).key());
    }

    @SneakyThrows
    @Test
    public void changeAddress_shouldReturnNullIfAddressNotExists() {
        var changeAddressInput = new ChangeAddressInput(UUID.randomUUID(), UUID.randomUUID(), getValidAddressInput());
        Mockito.when(this.query.getCustomerById(Mockito.any())).thenReturn(new Customer(UUID.randomUUID(), "00000000000", "Mateus", new ArrayList<>()));
        var useCase = getUseCase();

        var result = useCase.changeAddress(changeAddressInput);

        assertNull(result);
        assertEquals(ValidationStatus.NOT_FOUND, this.validationContext.getValidationStatus());
        assertEquals("address", this.validationContext.getErrors().get(0).key());
    }

    @SneakyThrows
    @Test
    public void changeAddress_shouldReturnNullIfNewAddressIsNotValid() {
        var addressId = UUID.randomUUID();
        var customerId = UUID.randomUUID();
        var addressInput = getInvalidAddressInput();
        var changeAddressInput = new ChangeAddressInput(customerId, addressId, addressInput);
        var address = changeAddressInput.toDomainEntity();
        var addresses = new ArrayList<Address>();
        addresses.add(address);
        Mockito.when(this.query.getCustomerById(Mockito.any()))
                .thenReturn(new Customer(
                        customerId,
                        "658.524.250-52",
                        "Mateus",
                        addresses
                ));
        var useCase = getUseCase();

        var result = useCase.changeAddress(changeAddressInput);

        assertNull(result);
        assertTrue(this.validationContext.hasErrors());
        assertTrue(this.validationContext.getErrors().size() > 1);
    }

    @SneakyThrows
    @Test
    public void changeAddress_shouldReturnNullIfCommandExceptionIsRaised() {
        var addressId = UUID.randomUUID();
        var customerId = UUID.randomUUID();
        var addressInput = getValidAddressInput();
        var changeAddressInput = new ChangeAddressInput(customerId, addressId, addressInput);
        var queryException = new QueryException("error", "message");
        Mockito.when(this.query.getCustomerById(Mockito.any()))
                .thenThrow(queryException);
        var useCase = getUseCase();

        var result = useCase.changeAddress(changeAddressInput);

        assertNull(result);
        assertEquals(ValidationStatus.INTEGRATION_ERROR, this.validationContext.getValidationStatus());
    }

    @SneakyThrows
    @Test
    public void changeAddress_shouldReturnChangedData() {
        var addressId = UUID.randomUUID();
        var customerId = UUID.randomUUID();
        var addressInput = getValidAddressInput();
        var changeAddressInput = new ChangeAddressInput(customerId, addressId, addressInput);
        var address = changeAddressInput.toDomainEntity();
        var addresses = new ArrayList<Address>();
        addresses.add(address);
        Mockito.when(this.query.getCustomerById(Mockito.any()))
                .thenReturn(new Customer(
                        customerId,
                        "658.524.250-52",
                        "Mateus",
                        addresses
                ));
        var useCase = getUseCase();

        var result = useCase.changeAddress(changeAddressInput);

        assertEquals(addressId, result.getId());
    }

    private AddressInput getValidAddressInput() {
        return new AddressInput(
                "78085630",
                this.faker.address().streetAddress(),
                this.faker.random().nextInt(0, 10),
                this.faker.address().city(),
                this.faker.address().stateAbbr(),
                this.faker.address().country(),
                this.faker.address().secondaryAddress(),
                this.faker.lorem().word());
    }

    private AddressInput getInvalidAddressInput() {
        return new AddressInput(
                "",
                "",
                0,
                "",
                "",
                "",
                "",
                "");
    }

    private ChangeAddressUseCase getUseCase() {
        return new ChangeAddressUseCase(this.command, this.validationContext, this.query);
    }
}