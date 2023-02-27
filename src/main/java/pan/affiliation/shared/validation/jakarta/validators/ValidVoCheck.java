package pan.affiliation.shared.validation.jakarta.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pan.affiliation.domain.shared.valueobjects.ValueObject;
import pan.affiliation.shared.validation.jakarta.annotations.ValidVo;

public class ValidVoCheck implements ConstraintValidator<ValidVo, ValueObject> {
    @Override
    public boolean isValid(ValueObject valueObject, ConstraintValidatorContext constraintValidatorContext) {
        return valueObject.isValid();
    }
}
