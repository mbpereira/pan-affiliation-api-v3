package pan.affiliation.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.*;
import pan.affiliation.domain.modules.customers.entities.Customer;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Table(name = "customers")
@Entity(name = "Customer")
public class CustomerDataModel {
    @Id
    private UUID id;
    private String documentNumber;
    private String name;
    @OneToMany(mappedBy = "customer", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<AddressDataModel> addresses;

    public static CustomerDataModel fromDomainEntity(Customer customer) {
        return new CustomerDataModel(
            customer.getId(),
            customer.getDocumentNumber(),
            customer.getName(),
            customer.getAddresses().stream().map(AddressDataModel::fromDomainEntity).toList()
        );
    }

    public Customer toDomainEntity() {
        return new Customer(
            this.id,
            this.documentNumber,
            this.name,
            this.addresses.stream().map(AddressDataModel::toDomainEntity).toList()
        );
    }
}
