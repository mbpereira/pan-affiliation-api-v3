package pan.affiliation.infrastructure.persistence.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import pan.affiliation.domain.modules.customers.commands.ChangeCustomerCommandHandler;
import pan.affiliation.domain.modules.customers.commands.CreateCustomerCommandHandler;
import pan.affiliation.domain.modules.customers.queries.GetCustomerByDocumentNumberQueryHandler;
import pan.affiliation.domain.modules.customers.queries.GetCustomerByIdQueryHandler;
import pan.affiliation.domain.modules.customers.valueobjects.DocumentNumber;
import pan.affiliation.infrastructure.persistence.entities.CustomerDataModel;
import pan.affiliation.shared.exceptions.CommandException;
import pan.affiliation.shared.exceptions.QueryException;
import pan.affiliation.domain.modules.customers.entities.Customer;
import pan.affiliation.shared.validation.ValidationStatus;

import java.util.UUID;

@Component
@RequestScope
public class CustomersRepositoryImpl implements
        GetCustomerByDocumentNumberQueryHandler,
        CreateCustomerCommandHandler,
        ChangeCustomerCommandHandler,
        GetCustomerByIdQueryHandler {
    private final CustomerRepository repository;

    @Autowired
    public CustomersRepositoryImpl(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Customer createCustomer(Customer customer) throws CommandException {
        try {
            var customerDataModel = CustomerDataModel.fromDomainEntity(customer);
            this.repository.save(customerDataModel);
            return customerDataModel.toDomainEntity();
        } catch (Exception ex) {
            throw new CommandException(
                    ValidationStatus.INTEGRATION_ERROR.toString(),
                    ex.getMessage());
        }
    }

    @Override
    public Customer getCustomerByDocumentNumber(DocumentNumber documentNumber) throws QueryException {
        try {
            var customer = this.repository.findByDocumentNumber(documentNumber.getValue());

            if (customer == null)
                return null;

            return customer.toDomainEntity();
        } catch (Exception ex) {
            throw new QueryException(
                    ValidationStatus.INTEGRATION_ERROR.toString(),
                    ex.getMessage());
        }
    }

    @Override
    public Customer changeCustomer(Customer customer) throws CommandException {
        try {
            var customerDataModel = CustomerDataModel.fromDomainEntity(customer);

            this.repository.save(customerDataModel);
            return customerDataModel.toDomainEntity();
        } catch (Exception ex) {
            throw new CommandException(
                    ValidationStatus.INTEGRATION_ERROR.toString(),
                    ex.getMessage());
        }
    }

    @Override
    public Customer getCustomerById(UUID id) throws QueryException {
        try {
            var customer = this.repository.findById(id);

            return customer.map(CustomerDataModel::toDomainEntity).orElse(null);
        } catch (Exception ex) {
            throw new QueryException(
                    ValidationStatus.INTEGRATION_ERROR.toString(),
                    ex.getMessage());
        }
    }
}
