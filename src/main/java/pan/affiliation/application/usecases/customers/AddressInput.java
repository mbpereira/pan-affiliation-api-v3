package pan.affiliation.application.usecases.customers;

import pan.affiliation.domain.modules.customers.entities.Address;

public record AddressInput(
        String postalCode,
        String street,
        int number,
        String city,
        String state,
        String country,
        String complement,
        String neighborhood
) {
    public Address toDomainEntity() {
        return new Address(
            this.postalCode,
            this.street,
            this.number,
            this.city,
            this.state,
            this.country,
            this.complement,
            this.neighborhood
        );
    }
}
