package pan.affiliation.api.controllers;

import jakarta.transaction.Transactional;
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
    private final SaveAddressUseCase saveAddressUseCase;
    private final RemoveAddressUseCase removeAddressUseCase;

    @Autowired
    public CustomersController(ValidationContext context, CreateCustomerUseCase createCustomerUseCase, GetCustomerByDocumentNumberUseCase getCustomerByDocumentNumberUseCase, SaveAddressUseCase saveAddressUseCase, RemoveAddressUseCase removeAddressUseCase) {
        super(context);
        this.createCustomerUseCase = createCustomerUseCase;
        this.getCustomerByDocumentNumberUseCase = getCustomerByDocumentNumberUseCase;
        this.saveAddressUseCase = saveAddressUseCase;
        this.removeAddressUseCase = removeAddressUseCase;
    }

    @PostMapping
    public ResponseEntity<GenericResponse<Customer>> createCustomer(@RequestBody CreateCustomerInput createCustomerInput) {
        return createGenericResponse(createCustomerUseCase.createCustomer(createCustomerInput));
    }

    @GetMapping
    public ResponseEntity<GenericResponse<Customer>> getCustomerByDocumentNumber(@RequestParam String documentNumber) {
        return createGenericResponse(getCustomerByDocumentNumberUseCase.getCustomerByDocumentNumber(new DocumentNumber(documentNumber)));
    }

    @Transactional
    @PutMapping("/{customerId}/addresses/{addressId}")
    public ResponseEntity<GenericResponse<Address>> getCustomerByDocumentNumber(@PathVariable UUID customerId, @PathVariable UUID addressId, @RequestBody AddressInput address) {
        return createGenericResponse(this.saveAddressUseCase.saveAddress(new SaveAddressInput(customerId, addressId, address)));
    }

    @Transactional
    @PostMapping("/{customerId}/addresses")
    public ResponseEntity<GenericResponse<Address>> createAddress(@PathVariable UUID customerId, @RequestBody AddressInput address) {
        return createGenericResponse(this.saveAddressUseCase.saveAddress(new SaveAddressInput(customerId, null, address)));
    }

    @Transactional
    @DeleteMapping("/{customerId}/addresses/{addressId}")
    public ResponseEntity<GenericResponse<Customer>> removeAddress(@PathVariable UUID customerId, @PathVariable UUID addressId) {
        return createGenericResponse(this.removeAddressUseCase.removeAddress(new RemoveAddressInput(customerId, addressId)));
    }
}
