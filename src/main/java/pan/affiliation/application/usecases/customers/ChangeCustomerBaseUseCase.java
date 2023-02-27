package pan.affiliation.application.usecases.customers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pan.affiliation.domain.modules.customers.commands.ChangeCustomerCommandHandler;
import pan.affiliation.domain.modules.customers.entities.Customer;
import pan.affiliation.domain.modules.customers.queries.GetCustomerByIdQueryHandler;
import pan.affiliation.shared.exceptions.CommandException;
import pan.affiliation.shared.exceptions.IntegrationException;
import pan.affiliation.shared.exceptions.QueryException;
import pan.affiliation.shared.validation.ValidationContext;
import pan.affiliation.shared.validation.ValidationStatus;

import java.util.UUID;

import static pan.affiliation.shared.constants.Messages.NOT_FOUND_RECORD;

public abstract class ChangeCustomerBaseUseCase {
    private final static Logger logger = LoggerFactory.getLogger(ChangeCustomerBaseUseCase.class);
    protected final ChangeCustomerCommandHandler command;
    protected final ValidationContext validationContext;
    protected final GetCustomerByIdQueryHandler query;


    protected ChangeCustomerBaseUseCase(ChangeCustomerCommandHandler command, ValidationContext validationContext, GetCustomerByIdQueryHandler query) {
        this.command = command;
        this.validationContext = validationContext;
        this.query = query;
    }

    protected Customer getCustomerById(UUID id) {
        logger.info("Getting customer by id");

        try {
            var customer = this.query.getCustomerById(id);

            if (customer == null) {
                logger.warn("Customer identified by {} does not exists", id);
                notifyNotFound();
                return null;
            }

            return customer;
        } catch (QueryException e) {
            logger.error("Get customer by id failed", e);
            notifyIntegrationError(e);
        }

        return null;
    }

    protected Customer changeCustomer(Customer customer) {
        logger.info("Changing customer");

        try {
            var validationResult = customer.validate();

            if (!validationResult.isValid()) {
                logger.warn("Invalid customer data provided {}", validationResult);
                this.validationContext.addNotifications(validationResult.getErrors());
                return null;
            }

            return this.command.changeCustomer(customer);
        } catch (CommandException e) {
            logger.warn("Change customer failed");
            notifyIntegrationError(e);
        }

        return null;
    }

    private void notifyNotFound() {
        this.validationContext.setStatus(ValidationStatus.NOT_FOUND);
        this.validationContext.addNotification(
                "customer",
                NOT_FOUND_RECORD
        );
    }

    private void notifyIntegrationError(IntegrationException e) {
        this.validationContext.setStatus(ValidationStatus.INTEGRATION_ERROR);
        this.validationContext.addNotification(
                e.getErrorCode(),
                e.getMessage()
        );
    }
}
