package pan.affiliation.domain.modules.customers.queries;

import pan.affiliation.domain.modules.customers.entities.Customer;
import pan.affiliation.shared.exceptions.QueryException;

import java.util.UUID;

public interface GetCustomerByIdQueryHandler {
    Customer getCustomerById(UUID id) throws QueryException;
}
