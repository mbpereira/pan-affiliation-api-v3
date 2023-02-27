package pan.affiliation.domain.modules.customers.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import pan.affiliation.domain.modules.customers.valueobjects.DocumentNumber;
import pan.affiliation.domain.shared.AggregateRoot;
import pan.affiliation.shared.validation.ValidationResult;
import pan.affiliation.shared.validation.ValidatorFactory;
import pan.affiliation.shared.validation.jakarta.annotations.ValidVo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static pan.affiliation.shared.constants.Messages.INVALID_DOCUMENT;

@SuppressWarnings("unused")
public class Customer extends AggregateRoot {
    @Valid
    private final List<Address> addresses;
    @ValidVo(message = INVALID_DOCUMENT)
    @JsonIgnore
    private DocumentNumber documentNumber;
    @Getter
    @Setter
    @NotBlank
    @Size(min = 3, max = 300)
    private String name;

    public Customer(UUID id, String documentNumber, String name, List<Address> addresses) {
        super.setId(id);
        this.documentNumber = new DocumentNumber(documentNumber);
        this.name = name;
        this.addresses = new ArrayList<>(addresses);
    }

    public Customer(String documentNumber, String name) {
        super.generateId();
        this.documentNumber = new DocumentNumber(documentNumber);
        this.name = name;
        this.addresses = new ArrayList<>();
    }

    @JsonIgnore
    public DocumentNumber getDocumentNumberVo() {
        return this.documentNumber;
    }

    @JsonGetter("documentNumber")
    public String getDocumentNumber() {
        return this.documentNumber.getValue();
    }

    @JsonSetter("documentNumber")
    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = new DocumentNumber(documentNumber);
    }

    public void addAddress(Address address) {
        address.generateId();
        this.addresses.add(address);
    }

    public List<Address> getAddresses() {
        return getAddresses(false);
    }

    @JsonIgnore
    public List<Address> getRemovedAddresses() {
        return this.getAddresses(true);
    }

    private List<Address> getAddresses(Boolean removed) {
        return this.addresses
                .stream()
                .filter(a -> a.isRemoved().equals(removed))
                .toList();
    }

    public Boolean removeAddress(UUID addressId) {
        var address = this
                .addresses
                .stream()
                .filter(a -> a.getId().equals(addressId))
                .findFirst();

        if (address.isEmpty())
            return false;

        address.get().setRemoved(true);
        return true;
    }

    public Boolean changeAddress(Address address) {
        int position = getAddressPosition(address);

        if (position == -1) return false;

        this.addresses.set(position, address);

        return true;
    }

    private int getAddressPosition(Address address) {
        for (var position = 0; position < this.addresses.size(); position++) {
            var old = this.addresses.get(position);
            if (old.getId().equals(address.getId())) {
                return position;
            }
        }
        return -1;
    }

    @Override
    public ValidationResult validate() {
        var validator = ValidatorFactory.<Customer>create();
        return validator.validate(this);
    }

    public void addAddresses(List<Address> addresses) {
        addresses.forEach(this::addAddress);
    }
}
