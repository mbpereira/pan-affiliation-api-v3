package pan.affiliation.shared.validation;

public class ValidatorFactory {
    public static <T> Validator<T> createValidator() {
        return new ValidatorImpl<>();
    }
}
