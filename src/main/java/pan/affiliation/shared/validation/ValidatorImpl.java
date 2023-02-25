package pan.affiliation.shared.validation;

import static javax.validation.Validation.buildDefaultValidatorFactory;

public class ValidatorImpl<T> implements Validator<T> {
    private static final javax.validation.Validator validator = buildDefaultValidatorFactory()
            .getValidator();

    @Override
    public ValidationResult validate(T data) {
        var violations = validator.validate(data);

        var errors = violations.stream().map(v -> new Error(
                v.getConstraintDescriptor()
                        .getAnnotation()
                        .annotationType()
                        .getName(),
                v.getMessage())).toList();

        return new ValidationResult(errors);
    }
}
