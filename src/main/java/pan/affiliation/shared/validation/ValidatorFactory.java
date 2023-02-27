package pan.affiliation.shared.validation;

public class ValidatorFactory {

    public static <T> Validator<T> create() {
        return new ValidatorImpl<>();
    }
}
