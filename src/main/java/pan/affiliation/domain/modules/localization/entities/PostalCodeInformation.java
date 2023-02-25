package pan.affiliation.domain.modules.localization.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostalCodeInformation {
    private String postalCode;
    private String street;
    private String neighborhood;
    private String state;
    private String city;
}
