package pan.affiliation.application.usecases.customers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pan.affiliation.domain.modules.customers.commands.ChangeCustomerCommandHandler;
import pan.affiliation.domain.modules.customers.entities.Customer;
import pan.affiliation.domain.modules.customers.queries.GetCustomerByIdQueryHandler;
import pan.affiliation.shared.validation.ValidationContext;
import pan.affiliation.shared.validation.ValidationStatus;

import static pan.affiliation.shared.constants.Messages.NOT_FOUND_RECORD;

@Service
public class RemoveAddressUseCase extends ChangeCustomerBaseUseCase {
    private final static Logger logger = LoggerFactory.getLogger(RemoveAddressUseCase.class);

    protected RemoveAddressUseCase(ChangeCustomerCommandHandler command, ValidationContext validationContext, GetCustomerByIdQueryHandler query) {
        super(command, validationContext, query);
    }

    public Customer removeAddress(RemoveAddressInput input) {
        var customer = this.getCustomerById(input.customerId());

        if (customer == null) {
            return null;
        }

        if (!customer.removeAddress(input.addressId())) {
            logger.warn("Address identified by {} does not exists", input.addressId());

            this.validationContext.setStatus(ValidationStatus.NOT_FOUND);
            this.validationContext.addNotification(
                    "address",
                    NOT_FOUND_RECORD
            );

            return null;
        }

        return this.changeCustomer(customer);
    }
}
