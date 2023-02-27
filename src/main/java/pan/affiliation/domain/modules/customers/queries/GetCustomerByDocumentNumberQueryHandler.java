package pan.affiliation.domain.modules.customers.queries;

import pan.affiliation.domain.modules.customers.entities.Customer;
import pan.affiliation.domain.modules.customers.valueobjects.DocumentNumber;
import pan.affiliation.shared.exceptions.QueryException;

public interface GetCustomerByDocumentNumberQueryHandler {
    Customer getCustomerByDocumentNumber(DocumentNumber documentNumber) throws QueryException;
}
