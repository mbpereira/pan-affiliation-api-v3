package pan.affiliation.application.usecases.customers;

import org.springframework.stereotype.Service;
import pan.affiliation.domain.modules.customers.entities.Customer;
import pan.affiliation.domain.modules.customers.queries.GetCustomerByDocumentNumberQueryHandler;
import pan.affiliation.domain.modules.customers.valueobjects.DocumentNumber;
import pan.affiliation.shared.exceptions.QueryException;
import pan.affiliation.shared.validation.ValidationContext;
import pan.affiliation.shared.validation.ValidationStatus;

import static pan.affiliation.shared.constants.Messages.INVALID_DOCUMENT;

@Service
public class GetCustomerByDocumentNumberUseCase {
    private final GetCustomerByDocumentNumberQueryHandler query;
    private final ValidationContext validationContext;

    public GetCustomerByDocumentNumberUseCase(GetCustomerByDocumentNumberQueryHandler query, ValidationContext validationContext) {
        this.query = query;
        this.validationContext = validationContext;
    }

    public Customer getCustomerByDocumentNumber(DocumentNumber documentNumber) {
        try {
            if (!documentNumber.isValid()) {
                this.validationContext.addNotification(
                        "documentNumber",
                        INVALID_DOCUMENT
                );

                return null;
            }
            var customer = this.query.getCustomerByDocumentNumber(documentNumber);

            if (customer == null) {
                this.validationContext.setStatus(ValidationStatus.NOT_FOUND);
                return null;
            }

            return customer;
        } catch (QueryException e) {
            this.validationContext.setStatus(ValidationStatus.INTEGRATION_ERROR);
            this.validationContext.addNotification(
                    e.getErrorCode(),
                    e.getMessage()
            );
        }

        return null;
    }
}
