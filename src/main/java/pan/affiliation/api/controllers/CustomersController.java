package pan.affiliation.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pan.affiliation.api.contracts.GenericResponse;
import pan.affiliation.application.usecases.customers.CreateCustomerInput;
import pan.affiliation.application.usecases.customers.CreateCustomerUseCase;
import pan.affiliation.domain.modules.customers.entities.Customer;
import pan.affiliation.shared.validation.ValidationContext;

@RestController
@RequestMapping("/customers")
public class CustomersController extends DefaultController {
    private final CreateCustomerUseCase createCustomerUseCase;

    public CustomersController(ValidationContext context, CreateCustomerUseCase createCustomerUseCase) {
        super(context);
        this.createCustomerUseCase = createCustomerUseCase;
    }

    @PostMapping
    public ResponseEntity<GenericResponse<Customer>> createCustomer(@RequestBody CreateCustomerInput createCustomerInput) {
        return createGenericResponse(createCustomerUseCase.createCustomer(createCustomerInput));
    }
}
