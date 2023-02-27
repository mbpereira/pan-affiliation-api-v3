package pan.affiliation.application.usecases.customers;

import pan.affiliation.domain.modules.customers.entities.Address;

import java.util.UUID;

public record ChangeAddressInput(UUID customerId, UUID addressId, AddressInput address) {
    public Address toDomainEntity() {
        return new Address(
                this.addressId,
                this.address.postalCode(),
                this.address.street(),
                this.address.number(),
                this.address.city(),
                this.address.state(),
                this.address.country(),
                this.address.complement(),
                this.address.neighborhood()
        );
    }
}
