package pan.affiliation.infrastructure.persistence.repositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import pan.affiliation.domain.modules.customers.commands.ChangeCustomerCommandHandler;
import pan.affiliation.domain.modules.customers.commands.CreateCustomerCommandHandler;
import pan.affiliation.domain.modules.customers.entities.Customer;
import pan.affiliation.domain.modules.customers.queries.GetCustomerByDocumentNumberQueryHandler;
import pan.affiliation.domain.modules.customers.queries.GetCustomerByIdQueryHandler;
import pan.affiliation.domain.modules.customers.valueobjects.DocumentNumber;
import pan.affiliation.domain.shared.BaseEntitiy;
import pan.affiliation.infrastructure.persistence.entities.CustomerDataModel;
import pan.affiliation.shared.exceptions.CommandException;
import pan.affiliation.shared.exceptions.QueryException;
import pan.affiliation.shared.validation.ValidationStatus;

import java.util.UUID;

@Component
@RequestScope
public class CustomersRepositoryImpl implements
        GetCustomerByDocumentNumberQueryHandler,
        CreateCustomerCommandHandler,
        ChangeCustomerCommandHandler,
        GetCustomerByIdQueryHandler {
    private final static Logger logger = LoggerFactory.getLogger(CustomersRepositoryImpl.class);
    private final CustomersRepository customersRepository;
    private final AddressesRepository addressesRepository;

    @Autowired
    public CustomersRepositoryImpl(CustomersRepository customersRepository, AddressesRepository addressesRepository) {
        this.customersRepository = customersRepository;
        this.addressesRepository = addressesRepository;
    }

    @Override
    public Customer createCustomer(Customer customer) throws CommandException {
        try {
            var customerDataModel = CustomerDataModel.fromDomainEntity(customer);
            this.customersRepository.save(customerDataModel);
            return customerDataModel.toDomainEntity();
        } catch (Exception ex) {
            logger.error("Create customer failed", ex);
            throw new CommandException(
                    ValidationStatus.INTEGRATION_ERROR.toString(),
                    ex.getMessage());
        }
    }

    @Override
    public Customer getCustomerByDocumentNumber(DocumentNumber documentNumber) throws QueryException {
        try {
            var customer = this.customersRepository.findByDocumentNumber(documentNumber.getValue());

            if (customer == null)
                return null;

            return customer.toDomainEntity();
        } catch (Exception ex) {
            logger.error("Get customer by document number failed", ex);
            throw new QueryException(
                    ValidationStatus.INTEGRATION_ERROR.toString(),
                    ex.getMessage());
        }
    }

    @Override
    public Customer changeCustomer(Customer customer) throws CommandException {
        try {
            var addressesToRemove = customer
                    .getRemovedAddresses()
                    .stream()
                    .map(BaseEntitiy::getId).toList();
            var customerDataModel = CustomerDataModel.fromDomainEntity(customer);
            this.customersRepository.save(customerDataModel);
            this.addressesRepository.deleteAllById(addressesToRemove);
            return customerDataModel.toDomainEntity();
        } catch (Exception ex) {
            logger.error("Change customer failed", ex);
            throw new CommandException(
                    ValidationStatus.INTEGRATION_ERROR.toString(),
                    ex.getMessage());
        }
    }

    @Override
    public Customer getCustomerById(UUID id) throws QueryException {
        try {
            var customer = this.customersRepository.findById(id);

            return customer.map(CustomerDataModel::toDomainEntity).orElse(null);
        } catch (Exception ex) {
            logger.error("Get customer by id failed", ex);
            throw new QueryException(
                    ValidationStatus.INTEGRATION_ERROR.toString(),
                    ex.getMessage());
        }
    }
}
