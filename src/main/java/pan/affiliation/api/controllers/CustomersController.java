package pan.affiliation.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pan.affiliation.api.contracts.GenericResponse;
import pan.affiliation.application.usecases.customers.CreateCustomerInput;
import pan.affiliation.application.usecases.customers.CreateCustomerUseCase;
import pan.affiliation.application.usecases.customers.GetCustomerByDocumentNumberUseCase;
import pan.affiliation.domain.modules.customers.entities.Customer;
import pan.affiliation.domain.modules.customers.valueobjects.DocumentNumber;
import pan.affiliation.shared.validation.ValidationContext;

@RestController
@RequestMapping("/customers")
public class CustomersController extends DefaultController {
    private final CreateCustomerUseCase createCustomerUseCase;
    private final GetCustomerByDocumentNumberUseCase getCustomerByDocumentNumberUseCase;

    public CustomersController(ValidationContext context, CreateCustomerUseCase createCustomerUseCase, GetCustomerByDocumentNumberUseCase getCustomerByDocumentNumberUseCase) {
        super(context);
        this.createCustomerUseCase = createCustomerUseCase;
        this.getCustomerByDocumentNumberUseCase = getCustomerByDocumentNumberUseCase;
    }

    @PostMapping
    public ResponseEntity<GenericResponse<Customer>> createCustomer(@RequestBody CreateCustomerInput createCustomerInput) {
        return createGenericResponse(createCustomerUseCase.createCustomer(createCustomerInput));
    }

    @GetMapping
    public ResponseEntity<GenericResponse<Customer>> getCustomerByDocumentNumber(@RequestParam String documentNumber) {
        return createGenericResponse(getCustomerByDocumentNumberUseCase.getCustomerByDocumentNumber(new DocumentNumber(documentNumber)));
    }
}
