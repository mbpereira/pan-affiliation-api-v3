package pan.affiliation.api.contracts;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import pan.affiliation.shared.validation.ValidationContextImpl;
import pan.affiliation.shared.validation.ValidationStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GenericResponseFactoryTest {

    @Test
    public void create_shouldReturnOkWhenHasErrorsReturnsFalseAndDataIsNotNull() {
        var context = new ValidationContextImpl();
        var genericResponseFactory = new GenericResponseFactory<>(context, new Object());

        var response = genericResponseFactory.create();

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }

    @Test
    public void create_shouldReturnNoContentWhenHasErrorsReturnsFalseAndDataIsNull() {
        var context = new ValidationContextImpl();
        var genericResponseFactory = new GenericResponseFactory<>(context, null);

        var response = genericResponseFactory.create();

        assertEquals(HttpStatusCode.valueOf(204), response.getStatusCode());
    }

    @Test
    public void create_shouldReturnBadRequestIfValidationStatusIsSetToFailed() {
        var context = new ValidationContextImpl();
        context.setStatus(ValidationStatus.FAILED);
        var genericResponseFactory = new GenericResponseFactory<>(context, null);

        var response = genericResponseFactory.create();

        assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
    }

    @Test
    public void create_shouldReturnPartialContentIfStatusIsSetToPartialContent() {
        var context = new ValidationContextImpl();
        context.setStatus(ValidationStatus.PARTIAL_SUCCESS);
        var genericResponseFactory = new GenericResponseFactory<>(context, null);

        var response = genericResponseFactory.create();

        assertEquals(HttpStatusCode.valueOf(206), response.getStatusCode());
    }

    @Test
    public void create_shouldReturnOkIfStatusIsSetToSuccess() {
        var context = new ValidationContextImpl();
        context.setStatus(ValidationStatus.SUCCESS);
        var genericResponseFactory = new GenericResponseFactory<>(context, new Object());

        var response = genericResponseFactory.create();

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }

    @Test
    public void create_shouldNoContentIfStatusIsSetToSuccessButDataIsNull() {
        var context = new ValidationContextImpl();
        context.setStatus(ValidationStatus.SUCCESS);
        var genericResponseFactory = new GenericResponseFactory<>(context, null);

        var response = genericResponseFactory.create();

        assertEquals(HttpStatusCode.valueOf(204), response.getStatusCode());
    }
}