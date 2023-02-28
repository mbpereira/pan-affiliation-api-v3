package pan.affiliation.shared.validation;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class ValidationResult {
    private final List<Error> errors;

    public ValidationResult(List<Error> errors) {
        this.errors = Collections.unmodifiableList(errors);
    }

    @Override
    public String toString() {
        return String.join(", ", this.errors.stream().map(Error::message).toList());
    }

    public Boolean isValid() {
        return errors.size() == 0;
    }
}