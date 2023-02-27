package pan.affiliation.shared.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;

public class ValidatorImpl<T> implements Validator<T> {
    private static final jakarta.validation.Validator validator =
            Validation.buildDefaultValidatorFactory().getValidator();

    @Override
    public ValidationResult validate(T data) {
        var violations = validator.validate(data);

        var errors = violations.stream().map(v -> new Error(
                getFieldName(v),
                v.getMessage())).toList();

        return new ValidationResult(errors);
    }

    private String getFieldName(ConstraintViolation<T> violation) {
        String field = null;
        for (var node : violation.getPropertyPath())
            field = node.getName();

        return field;
    }
}
