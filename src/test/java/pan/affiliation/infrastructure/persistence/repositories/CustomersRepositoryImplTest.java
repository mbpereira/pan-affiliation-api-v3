package pan.affiliation.infrastructure.persistence.repositories;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pan.affiliation.domain.modules.customers.entities.Customer;
import pan.affiliation.domain.modules.customers.valueobjects.DocumentNumber;
import pan.affiliation.infrastructure.persistence.entities.CustomerDataModel;
import pan.affiliation.shared.exceptions.CommandException;
import pan.affiliation.shared.exceptions.QueryException;
import pan.affiliation.shared.validation.ValidationStatus;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

public class CustomersRepositoryImplTest {
    private final CustomerRepository repository;

    public CustomersRepositoryImplTest() {
        this.repository = Mockito.mock(CustomerRepository.class);
    }

    @Test
    public void createCustomer_shouldThrowsCommandExceptionWhenAnyExceptionIsRaised() {
        var repositoryImpl = getRepositoryImpl();

        var ex = assertThrows(CommandException.class,
                () -> repositoryImpl.createCustomer(new Customer(null, "00000000000", "Mateus", null)));

        assertEquals(ValidationStatus.INTEGRATION_ERROR.toString(), ex.getErrorCode());
    }

    @SneakyThrows
    @Test
    public void createCustomer_shouldReturnsCreatedCustomerData() {
        var repositoryImpl = getRepositoryImpl();
        var customer = new Customer("00000000000", "Mateus");
        var generatedId = customer.getId();
        var documentNumber = customer.getDocumentNumber();

        var createdCustomer = repositoryImpl.createCustomer(customer);

        assertEquals(generatedId, createdCustomer.getId());
        assertEquals(documentNumber, createdCustomer.getDocumentNumber());
    }

    @SneakyThrows
    @Test
    public void getCustomerByDocumentNumber_shouldThrowsQueryExceptionWhenAnyExceptionIsRaised() {
        var repositoryImpl = getRepositoryImpl();
        var documentNumber = new DocumentNumber("0000000000");
        Mockito.when(this.repository.findByDocumentNumber(any()))
                .thenReturn(new CustomerDataModel(
                        UUID.randomUUID(),
                        documentNumber.getValue(),
                        "Mateus",
                        null
                ));

        var ex = assertThrows(QueryException.class,
                () -> repositoryImpl.getCustomerByDocumentNumber(documentNumber));

        assertEquals(ValidationStatus.INTEGRATION_ERROR.toString(), ex.getErrorCode());
    }

    @SneakyThrows
    @Test
    public void getCustomerByDocumentNumber_shouldReturnNullIfProvidedDocumentDoesNotExists() {
        var repositoryImpl = getRepositoryImpl();
        var documentNumber = new DocumentNumber("0000000000");

        var result = repositoryImpl.getCustomerByDocumentNumber(documentNumber);

        assertNull(result);
    }

    private CustomersRepositoryImpl getRepositoryImpl() {
        return new CustomersRepositoryImpl(this.repository);
    }
}