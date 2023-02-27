package pan.affiliation.domain.modules.customers.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import pan.affiliation.domain.modules.customers.valueobjects.PostalCode;
import pan.affiliation.domain.shared.BaseEntitiy;
import pan.affiliation.shared.validation.jakarta.annotations.ValidVo;

import java.util.UUID;

public class Address extends BaseEntitiy {
    @ValidVo
    @JsonIgnore
    private PostalCode postalCode;
    @Getter
    @Setter
    @Size(min = 3, max = 300)
    @NotBlank
    private String street;
    @Getter
    @Setter
    private int number;
    @Getter
    @Setter
    @Size(min = 3, max = 100)
    @NotBlank
    private String city;
    @Getter
    @Setter
    @Size(max = 2)
    @NotBlank
    private String state;
    @Getter
    @Setter
    @Size(min = 3, max = 50)
    @NotBlank
    private String country;
    @Getter
    @Setter
    private String complement;
    @Getter
    @Setter
    @Size(min = 3, max = 150)
    @NotBlank
    private String neighborhood;

    @JsonIgnore
    private Boolean removed;

    public Address(UUID id, String postalCode, String street, int number, String city, String state, String country, String complement, String neighborhood) {
        super.setId(id);
        this.postalCode = new PostalCode(postalCode);
        this.street = street;
        this.number = number;
        this.city = city;
        this.state = state;
        this.country = country;
        this.complement = complement;
        this.neighborhood = neighborhood;
        this.removed = false;
    }

    public Address(String postalCode, String street, int number, String city, String state, String country, String complement, String neighborhood) {
        super.generateId();
        this.postalCode = new PostalCode(postalCode);
        this.street = street;
        this.number = number;
        this.city = city;
        this.state = state;
        this.country = country;
        this.complement = complement;
        this.neighborhood = neighborhood;
        this.removed = false;
    }

    @JsonGetter("postalCode")
    public String getPostalCode() {
        return this.postalCode.getValue();
    }

    @JsonSetter("postalCode")
    public void setPostalCode(String postalCode) {
        this.postalCode = new PostalCode(postalCode);
    }

    public void setRemoved(Boolean removed) {
        this.removed = removed;
    }

    public Boolean isRemoved() {
        return this.removed;
    }
}
