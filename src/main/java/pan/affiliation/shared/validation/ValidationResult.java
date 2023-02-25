package pan.affiliation.shared.validation;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class ValidationResult {
    private List<Error> errors;

    public ValidationResult(List<Error> errors) {
        this.errors = Collections.unmodifiableList(errors);
    }

    public ValidationResult() {
        this.errors = new ArrayList<>();
    }

    public Boolean isValid() {
        return errors.size() == 0;
    }
}