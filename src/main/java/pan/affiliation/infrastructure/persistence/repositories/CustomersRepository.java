package pan.affiliation.infrastructure.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pan.affiliation.infrastructure.persistence.entities.CustomerDataModel;

import java.util.UUID;

public interface CustomersRepository extends JpaRepository<CustomerDataModel, UUID> {
    CustomerDataModel findByDocumentNumber(String documentNumber);
}
