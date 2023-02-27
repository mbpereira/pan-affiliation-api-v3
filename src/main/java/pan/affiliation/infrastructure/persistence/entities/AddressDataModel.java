package pan.affiliation.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.*;
import pan.affiliation.domain.modules.customers.entities.Address;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@EqualsAndHashCode(of = "id")
@Table(name = "addresses")
@Entity(name = "Address")
public class AddressDataModel {
    @Id
    private UUID id;
    private String postalCode;
    private String street;
    private int number;
    private String city;
    private String state;
    private String country;
    private String complement;
    private String neighborhood;
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false, updatable = false)
    private CustomerDataModel customer;

    public static AddressDataModel fromDomainEntity(Address address) {
        return new AddressDataModel(
            address.getId(),
            address.getPostalCode(),
            address.getStreet(),
            address.getNumber(),
            address.getCity(),
            address.getState(),
            address.getCountry(),
            address.getComplement(),
            address.getNeighborhood(),
            null
        );
    }

    public Address toDomainEntity() {
        return new Address(
            this.id,
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