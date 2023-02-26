package pan.affiliation.application.usecases.customers;

import pan.affiliation.domain.modules.customers.entities.Address;
import pan.affiliation.domain.modules.customers.valueobjects.PostalCode;

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
                null,
                new PostalCode(this.postalCode),
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
