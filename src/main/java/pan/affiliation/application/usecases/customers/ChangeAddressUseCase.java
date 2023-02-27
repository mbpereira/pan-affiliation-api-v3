package pan.affiliation.application.usecases.customers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pan.affiliation.domain.modules.customers.commands.ChangeCustomerCommandHandler;
import pan.affiliation.domain.modules.customers.entities.Address;
import pan.affiliation.domain.modules.customers.queries.GetCustomerByIdQueryHandler;
import pan.affiliation.shared.exceptions.CommandException;
import pan.affiliation.shared.exceptions.QueryException;
import pan.affiliation.shared.validation.ValidationContext;
import pan.affiliation.shared.validation.ValidationStatus;

import static pan.affiliation.shared.constants.Messages.NOT_FOUND_RECORD;

@Service
public class ChangeAddressUseCase {
    private final ChangeCustomerCommandHandler command;
    private final ValidationContext validationContext;
    private final GetCustomerByIdQueryHandler query;

    @Autowired
    public ChangeAddressUseCase(ChangeCustomerCommandHandler command, ValidationContext validationContext, GetCustomerByIdQueryHandler query) {
        this.command = command;
        this.validationContext = validationContext;
        this.query = query;
    }

    public Address changeAddress(ChangeAddressInput input) {
        try {
            var customer = this.query.getCustomerById(input.customerId());

            if (customer == null) {
                this.validationContext.setStatus(ValidationStatus.NOT_FOUND);
                this.validationContext.addNotification(
                        "customer",
                        NOT_FOUND_RECORD
                );

                return null;
            }

            var newAddressData = input.toDomainEntity();
            var addressChanged = customer.changeAddress(newAddressData);

            if(!addressChanged) {
                this.validationContext.setStatus(ValidationStatus.NOT_FOUND);
                this.validationContext.addNotification(
                        "address",
                        NOT_FOUND_RECORD
                );

                return null;
            }

            var validationResult = customer.validate();

            if (!validationResult.isValid()) {
                this.validationContext.addNotifications(validationResult.getErrors());
                return null;
            }

            this.command.changeCustomer(customer);

            return newAddressData;
        } catch (QueryException | CommandException e) {
            this.validationContext.setStatus(ValidationStatus.INTEGRATION_ERROR);
            this.validationContext.addNotification(
                    e.getErrorCode(),
                    e.getMessage()
            );
        }

        return null;
    }
}
