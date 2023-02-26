package pan.affiliation.application.usecases.customers;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pan.affiliation.domain.modules.customers.entities.Customer;
import pan.affiliation.domain.modules.customers.queries.GetCustomerByDocumentNumberQueryHandler;
import pan.affiliation.domain.modules.customers.valueobjects.DocumentNumber;
import pan.affiliation.shared.exceptions.QueryException;
import pan.affiliation.shared.validation.ValidationContext;
import pan.affiliation.shared.validation.ValidationContextImpl;
import pan.affiliation.shared.validation.ValidationStatus;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static pan.affiliation.shared.constants.Messages.INVALID_DOCUMENT;

public class GetCustomerByDocumentNumberUseCaseTest {
    private final GetCustomerByDocumentNumberQueryHandler queryHandlerMock;
    private final ValidationContext validationContext;

    public GetCustomerByDocumentNumberUseCaseTest() {
        this.queryHandlerMock = Mockito.mock(GetCustomerByDocumentNumberQueryHandler.class);
        this.validationContext = new ValidationContextImpl();
    }

    @Test
    public void getCustomerByDocumentNumber_shouldReturnNullIfDocumentNumberIsNotValid() {
        var useCase = getUseCase();

        var result = useCase.getCustomerByDocumentNumber(new DocumentNumber(""));

        assertNull(result);
        assertEquals(INVALID_DOCUMENT, this.validationContext.getErrors().get(0).message());
    }

    @Test
    public void getCustomerByDocumentNumber_shouldReturnNullIfDocumentNumberDoesNotExists() {
        var useCase = getUseCase();

        var result = useCase.getCustomerByDocumentNumber(new DocumentNumber("317.581.640-18"));

        assertNull(result);
        assertEquals(ValidationStatus.NOT_FOUND, this.validationContext.getValidationStatus());
    }

    @SneakyThrows
    @Test
    public void getCustomerByDocumentNumber_shouldReturnNullWhenExceptionIsRaised() {
        var useCase = getUseCase();
        var queryException = new QueryException("errorCode", "message");
        var documentNumber = new DocumentNumber("317.581.640-18");
        Mockito.when(this.queryHandlerMock.getCustomerByDocumentNumber(documentNumber))
                .thenThrow(queryException);
        var result = useCase.getCustomerByDocumentNumber(documentNumber);

        assertNull(result);
        assertEquals(ValidationStatus.INTEGRATION_ERROR, this.validationContext.getValidationStatus());
    }

    @SneakyThrows
    @Test
    public void getCustomerByDocumentNumber_shouldReturnCustomerData() {
        var useCase = getUseCase();
        var documentNumber = new DocumentNumber("317.581.640-18");
        var id = UUID.randomUUID();
        Mockito.when(this.queryHandlerMock.getCustomerByDocumentNumber(documentNumber))
                .thenReturn(new Customer(
                        id,
                        documentNumber.getValue(),
                        "Mateus",
                        null
                ));
        var result = useCase.getCustomerByDocumentNumber(documentNumber);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    private GetCustomerByDocumentNumberUseCase getUseCase() {
        return new GetCustomerByDocumentNumberUseCase(this.queryHandlerMock, this.validationContext);
    }
}