package pan.affiliation.api.contracts;

import org.springframework.http.ResponseEntity;
import pan.affiliation.shared.validation.ValidationContext;
import pan.affiliation.shared.validation.ValidationStatus;

public class GenericResponseFactory<T> {
    private final ValidationContext validationContext;
    private final T data;

    public GenericResponseFactory(ValidationContext validationContext, T data) {
        this.validationContext = validationContext;
        this.data = data;
    }

    public ResponseEntity<GenericResponse<T>> create() {
        var validationStatus = this.validationContext.getValidationStatus();

        if (validationStatus == null) {
            if (validationContext.hasErrors())
                return createGenericResponse(400);

            if (this.data != null)
                return createGenericResponse(200);

            return createGenericResponse(204);
        }

        if (ValidationStatus.FAILED.equals(validationStatus))
            return createGenericResponse(400);

        if (ValidationStatus.PARTIAL_SUCCESS.equals(validationStatus))
            return createGenericResponse(206);

        if (ValidationStatus.SUCCESS.equals(validationStatus) && this.data != null)
            return createGenericResponse(200);

        if (ValidationStatus.NOT_FOUND.equals(validationStatus))
            return createGenericResponse(404);

        if (ValidationStatus.CONFLICT.equals(validationStatus))
            return createGenericResponse(409);

        if (ValidationStatus.INTEGRATION_ERROR.equals(validationStatus))
            return createGenericResponse(500);

        return createGenericResponse(204);
    }

    private ResponseEntity<GenericResponse<T>> createGenericResponse(int statusCode) {
        return ResponseEntity.status(statusCode)
                .body(new GenericResponse<>(this.data, validationContext.getErrors()));
    }
}
