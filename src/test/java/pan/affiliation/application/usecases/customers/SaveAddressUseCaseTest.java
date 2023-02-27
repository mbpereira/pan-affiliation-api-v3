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

public class SaveAddressUseCaseTest {
    private final ChangeCustomerCommandHandler command;
    private final ValidationContext validationContext;
    private final GetCustomerByIdQueryHandler query;
    private final Faker faker = new Faker();

    public SaveAddressUseCaseTest() {
        this.query = Mockito.mock(GetCustomerByIdQueryHandler.class);
        this.validationContext = new ValidationContextImpl();
        this.command = Mockito.mock(ChangeCustomerCommandHandler.class);
    }

    @Test
    public void saveAddress_shouldReturnNullIfCustomerDoesNotExists() {
        var saveAddressInput = new SaveAddressInput(UUID.randomUUID(), UUID.randomUUID(), getValidAddressInput());
        var useCase = getUseCase();

        var result = useCase.saveAddress(saveAddressInput);

        assertNull(result);
        assertEquals(ValidationStatus.NOT_FOUND, this.validationContext.getValidationStatus());
        assertEquals("customer", this.validationContext.getErrors().get(0).key());
    }

    @SneakyThrows
    @Test
    public void saveAddress_shouldReturnNullIfAddressNotExists() {
        var saveAddressInput = new SaveAddressInput(UUID.randomUUID(), UUID.randomUUID(), getValidAddressInput());
        Mockito.when(this.query.getCustomerById(Mockito.any())).thenReturn(new Customer(UUID.randomUUID(), "00000000000", "Mateus", new ArrayList<>()));
        var useCase = getUseCase();

        var result = useCase.saveAddress(saveAddressInput);

        assertNull(result);
        assertEquals(ValidationStatus.NOT_FOUND, this.validationContext.getValidationStatus());
        assertEquals("address", this.validationContext.getErrors().get(0).key());
    }

    @SneakyThrows
    @Test
    public void saveAddress_shouldReturnNullIfNewAddressIsNotValid() {
        var addressId = UUID.randomUUID();
        var customerId = UUID.randomUUID();
        var addressInput = getInvalidAddressInput();
        var saveAddressInput = new SaveAddressInput(customerId, addressId, addressInput);
        var address = saveAddressInput.toDomainEntity();
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

        var result = useCase.saveAddress(saveAddressInput);

        assertNull(result);
        assertTrue(this.validationContext.hasErrors());
        assertTrue(this.validationContext.getErrors().size() > 1);
    }

    @SneakyThrows
    @Test
    public void saveAddress_shouldReturnNullIfCommandExceptionIsRaised() {
        var addressId = UUID.randomUUID();
        var customerId = UUID.randomUUID();
        var addressInput = getValidAddressInput();
        var saveAddressInput = new SaveAddressInput(customerId, addressId, addressInput);
        var queryException = new QueryException("error", "message");
        Mockito.when(this.query.getCustomerById(Mockito.any()))
                .thenThrow(queryException);
        var useCase = getUseCase();

        var result = useCase.saveAddress(saveAddressInput);

        assertNull(result);
        assertEquals(ValidationStatus.INTEGRATION_ERROR, this.validationContext.getValidationStatus());
    }

    @SneakyThrows
    @Test
    public void saveAddress_shouldReturnChangedData() {
        var addressId = UUID.randomUUID();
        var customerId = UUID.randomUUID();
        var addressInput = getValidAddressInput();
        var saveAddressInput = new SaveAddressInput(customerId, addressId, addressInput);
        var address = saveAddressInput.toDomainEntity();
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

        var result = useCase.saveAddress(saveAddressInput);

        assertEquals(addressId, result.getId());
    }

    @SneakyThrows
    @Test
    public void saveAddress_shouldCreateNewAddressToMerchantWhenAddressIdIsNull() {
        var customerId = UUID.randomUUID();
        var addressInput = getValidAddressInput();
        var saveAddressInput = new SaveAddressInput(customerId, null, addressInput);
        var customer = new Customer(
                customerId,
                "658.524.250-52",
                "Mateus",
                new ArrayList<>()
        );
        Mockito.when(this.query.getCustomerById(Mockito.any()))
                .thenReturn(customer);
        var useCase = getUseCase();

        var result = useCase.saveAddress(saveAddressInput);

        assertNotNull(result.getId());
        assertTrue(customer.getAddresses().size() > 0);
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

    private SaveAddressUseCase getUseCase() {
        return new SaveAddressUseCase(this.command, this.validationContext, this.query);
    }
}