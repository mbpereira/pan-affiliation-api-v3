package pan.affiliation.application.usecases.customers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pan.affiliation.domain.modules.customers.commands.CreateCustomerCommandHandler;
import pan.affiliation.domain.modules.customers.entities.Customer;
import pan.affiliation.domain.modules.customers.queries.GetCustomerByDocumentNumberQueryHandler;
import pan.affiliation.shared.constants.Messages;
import pan.affiliation.shared.exceptions.CommandException;
import pan.affiliation.shared.exceptions.QueryException;
import pan.affiliation.shared.validation.ValidationContext;
import pan.affiliation.shared.validation.ValidationStatus;

@Service
public class CreateCustomerUseCase {
    private final static Logger logger = LoggerFactory.getLogger(CreateCustomerUseCase.class);
    private final CreateCustomerCommandHandler command;
    private final GetCustomerByDocumentNumberQueryHandler query;
    private final ValidationContext validationContext;

    @Autowired
    public CreateCustomerUseCase(CreateCustomerCommandHandler command, GetCustomerByDocumentNumberQueryHandler query, ValidationContext validationContext) {
        this.command = command;
        this.query = query;
        this.validationContext = validationContext;
    }

    public Customer createCustomer(CreateCustomerInput input) {
        logger.info("Creating customer");

        var customer = input.toDomainEntity();

        var validationResult = customer.validate();

        if (!validationResult.isValid()) {
            logger.warn("Invalid customer data provided {}", validationResult);
            this.validationContext.addNotifications(validationResult.getErrors());
            return null;
        }

        try {
            var existingCustomer = query.getCustomerByDocumentNumber(customer.getDocumentNumberVo());

            if (existingCustomer != null) {
                logger.warn("Customer {} already exists", customer.getDocumentNumber());
                this.validationContext.setStatus(ValidationStatus.CONFLICT);
                this.validationContext.addNotification(
                        ValidationStatus.CONFLICT.toString(),
                        Messages.CONFLICT
                );

                return null;
            }

            return this.command.createCustomer(customer);
        } catch (QueryException | CommandException e) {
            logger.error("Create customer failed", e);
            this.validationContext.setStatus(ValidationStatus.INTEGRATION_ERROR);
            this.validationContext.addNotification(
                    e.getErrorCode(),
                    e.getMessage()
            );
        }

        return null;
    }
}
