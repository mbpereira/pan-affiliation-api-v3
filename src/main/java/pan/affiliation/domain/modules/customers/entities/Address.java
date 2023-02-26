package pan.affiliation.domain.modules.customers.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pan.affiliation.domain.modules.customers.valueobjects.PostalCode;
import pan.affiliation.shared.validation.jakarta.annotations.ValidVo;

@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Getter
    private Long id;
    @ValidVo
    @JsonIgnore
    private PostalCode postalCode;
    @Getter @Setter @Size(min = 3, max = 300) @NotBlank
    public String street;
    @Getter @Setter
    public int number;
    @Getter @Setter @Size(min = 3, max = 100) @NotBlank
    public String city;
    @Getter @Setter @Size(max = 2) @NotBlank
    public String state;
    @Getter @Setter @Size(min = 3, max = 50) @NotBlank
    public String country;
    @Getter @Setter
    public String complement;
    @Getter @Setter @Size(min = 3, max = 150) @NotBlank
    public String neighborhood;

    @JsonGetter("postalCode")
    public String getPostalCode() {
        return this.postalCode.getValue();
    }

    @JsonSetter("postalCode")
    public void setPostalCode(String postalCode) {
        this.postalCode = new PostalCode(postalCode);
    }
}
