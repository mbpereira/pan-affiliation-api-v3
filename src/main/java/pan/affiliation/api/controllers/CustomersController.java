package pan.affiliation.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pan.affiliation.api.contracts.GenericResponse;
import pan.affiliation.application.usecases.customers.*;
import pan.affiliation.domain.modules.customers.entities.Address;
import pan.affiliation.domain.modules.customers.entities.Customer;
import pan.affiliation.domain.modules.customers.valueobjects.DocumentNumber;
import pan.affiliation.shared.validation.ValidationContext;

import java.util.UUID;

@RestController
@RequestMapping("/customers")
public class CustomersController extends DefaultController {
    private final CreateCustomerUseCase createCustomerUseCase;
    private final GetCustomerByDocumentNumberUseCase getCustomerByDocumentNumberUseCase;
    private final ChangeAddressUseCase changeAddressUseCase;

    @Autowired
    public CustomersController(ValidationContext context, CreateCustomerUseCase createCustomerUseCase, GetCustomerByDocumentNumberUseCase getCustomerByDocumentNumberUseCase, ChangeAddressUseCase changeAddressUseCase) {
        super(context);
        this.createCustomerUseCase = createCustomerUseCase;
        this.getCustomerByDocumentNumberUseCase = getCustomerByDocumentNumberUseCase;
        this.changeAddressUseCase = changeAddressUseCase;
    }

    @PostMapping
    public ResponseEntity<GenericResponse<Customer>> createCustomer(@RequestBody CreateCustomerInput createCustomerInput) {
        return createGenericResponse(createCustomerUseCase.createCustomer(createCustomerInput));
    }

    @GetMapping
    public ResponseEntity<GenericResponse<Customer>> getCustomerByDocumentNumber(@RequestParam String documentNumber) {
        return createGenericResponse(getCustomerByDocumentNumberUseCase.getCustomerByDocumentNumber(new DocumentNumber(documentNumber)));
    }

    @PutMapping("/customers/{customerId}/addresses/{addressId}")
    public ResponseEntity<GenericResponse<Address>> getCustomerByDocumentNumber(@PathVariable UUID customerId, @PathVariable UUID addressId, @RequestBody AddressInput address) {
        return createGenericResponse(this.changeAddressUseCase.changeAddress(new ChangeAddressInput(customerId, addressId, address)));
    }
}
