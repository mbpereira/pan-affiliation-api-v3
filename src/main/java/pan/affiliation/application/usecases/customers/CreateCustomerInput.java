package pan.affiliation.application.usecases.customers;

import pan.affiliation.domain.modules.customers.entities.Customer;

import java.util.List;

public record CreateCustomerInput(String name, String documentNumber, List<AddressInput> addresses) {
    public Customer toDomainEntity() {
        return new Customer(
            this.documentNumber,
            this.name,
            this.addresses
                .stream()
                .map(AddressInput::toDomainEntity).toList()
        );
    }
}
