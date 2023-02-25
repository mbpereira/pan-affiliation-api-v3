package pan.affiliation.api.controllers;

import org.springframework.http.ResponseEntity;
import pan.affiliation.api.contracts.GenericResponse;
import pan.affiliation.api.contracts.GenericResponseFactory;
import pan.affiliation.shared.validation.ValidationContext;

public class DefaultController {
    private final ValidationContext context;

    public DefaultController(ValidationContext context) {
        this.context = context;
    }

    protected <T> ResponseEntity<GenericResponse<T>> createGenericResponse(T data) {
        return new GenericResponseFactory<>(this.context, data).create();
    }
}
