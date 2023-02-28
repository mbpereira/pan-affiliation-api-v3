package pan.affiliation.application.usecases.customers;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pan.affiliation.domain.modules.customers.commands.ChangeCustomerCommandHandler;
import pan.affiliation.domain.modules.customers.entities.Address;
import pan.affiliation.domain.modules.customers.entities.Customer;
import pan.affiliation.domain.modules.customers.queries.GetCustomerByIdQueryHandler;
import pan.affiliation.shared.validation.ValidationContext;
import pan.affiliation.shared.validation.ValidationContextImpl;
import pan.affiliation.shared.validation.ValidationStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RemoveAddressUseCaseTest {
    private final ChangeCustomerCommandHandler command;
    private final ValidationContext validationContext;
    private final GetCustomerByIdQueryHandler query;

    public RemoveAddressUseCaseTest() {
        this.query = Mockito.mock(GetCustomerByIdQueryHandler.class);
        this.validationContext = new ValidationContextImpl();
        this.command = Mockito.mock(ChangeCustomerCommandHandler.class);
    }

    @Test
    public void removeAddress_shouldReturnNullIfCustomerDoesNotExists() {
        var removeAddressInput = new RemoveAddressInput(UUID.randomUUID(), UUID.randomUUID());
        var useCase = getUseCase();

        var result = useCase.removeAddress(removeAddressInput);

        assertNull(result);
        assertEquals(ValidationStatus.NOT_FOUND, this.validationContext.getValidationStatus());
        assertEquals("customer", this.validationContext.getErrors().get(0).key());
    }

    @SneakyThrows
    @Test
    public void removeAddress_shouldReturnNullIfAddressNotExists() {
        var removeAddressInput = new RemoveAddressInput(UUID.randomUUID(), UUID.randomUUID());
        Mockito.when(this.query.getCustomerById(Mockito.any())).thenReturn(new Customer(UUID.randomUUID(), "00000000000", "Mateus", new ArrayList<>()));
        var useCase = getUseCase();

        var result = useCase.removeAddress(removeAddressInput);

        assertNull(result);
        assertEquals(ValidationStatus.NOT_FOUND, this.validationContext.getValidationStatus());
        assertEquals("address", this.validationContext.getErrors().get(0).key());
    }

    @SneakyThrows
    @Test
    public void removeAddress_shouldReturnCustomerDataWithoutRemovedAddress() {
        var addressId = UUID.randomUUID();
        var customerId = UUID.randomUUID();
        var address = getValidAddress(addressId);
        var addresses = List.of(address);
        var removeAddressInput = new RemoveAddressInput(customerId, addressId);
        var customer = new Customer(
                customerId,
                "658.524.250-52",
                "Mateus",
                addresses
        );
        Mockito.when(this.query.getCustomerById(Mockito.any()))
                .thenReturn(customer);
        Mockito.when(this.command.changeCustomer(Mockito.any()))
                .thenReturn(customer);
        var useCase = getUseCase();

        var result = useCase.removeAddress(removeAddressInput);

        assertEquals(0, result.getAddresses().size());
    }

    private Address getValidAddress(UUID id) {
        return new Address(
                id,
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

    private RemoveAddressUseCase getUseCase() {
        return new RemoveAddressUseCase(this.command, this.validationContext, this.query);
    }
}