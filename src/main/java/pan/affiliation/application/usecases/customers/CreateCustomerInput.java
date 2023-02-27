package pan.affiliation.application.usecases.customers;

import pan.affiliation.domain.modules.customers.entities.Customer;

import java.util.List;

public record CreateCustomerInput(String name, String documentNumber, List<AddressInput> addresses) {
    public Customer toDomainEntity() {
        var customer = new Customer(
            this.documentNumber,
            this.name
        );
        customer.addAddresses(this.addresses
                .stream()
                .map(AddressInput::toDomainEntity).toList());
        return customer;
    }
}
