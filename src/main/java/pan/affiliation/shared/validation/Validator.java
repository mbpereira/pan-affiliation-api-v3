package pan.affiliation.shared.validation;

public interface Validator<T> {
    ValidationResult validate(T data);
}