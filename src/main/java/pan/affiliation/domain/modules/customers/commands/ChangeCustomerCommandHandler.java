package pan.affiliation.domain.modules.customers.commands;

import pan.affiliation.domain.modules.customers.entities.Customer;
import pan.affiliation.shared.exceptions.CommandException;

public interface ChangeCustomerCommandHandler {
    Customer changeCustomer(Customer Customer) throws CommandException;
}
