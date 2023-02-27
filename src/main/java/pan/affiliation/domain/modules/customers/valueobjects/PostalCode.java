package pan.affiliation.domain.modules.customers.valueobjects;

import jakarta.validation.constraints.AssertTrue;
import lombok.Getter;
import pan.affiliation.domain.shared.valueobjects.ValueObject;
import pan.affiliation.shared.helpers.StringHelpers;

import java.util.regex.Pattern;

@Getter
public class PostalCode extends ValueObject {
    private final String originalValue;
    private final String value;
    @AssertTrue
    private Boolean isValid;

    public PostalCode(String postalCode) {
        this.originalValue = postalCode;
        this.value = StringHelpers.keepOnlyNumbers(postalCode);
        validate();
    }

    private void validate() {
        if (StringHelpers.isNullOrEmpty(this.value)) {
            this.isValid = false;
            return;
        }

        var pattern = Pattern.compile("^[0-9]{5}-?[0-9]{3}$", Pattern.CASE_INSENSITIVE);
        var matcher = pattern.matcher(value);
        this.isValid = matcher.matches();
    }

    @AssertTrue
    public Boolean isValid() {
        return isValid;
    }
}