package pan.affiliation.infrastructure.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pan.affiliation.infrastructure.persistence.entities.AddressDataModel;

import java.util.UUID;

public interface AddressesRepository extends JpaRepository<AddressDataModel, UUID> {
}
