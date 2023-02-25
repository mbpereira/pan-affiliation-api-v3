package pan.affiliation.shared.validation;

import java.util.List;

public interface ValidationContext {
    List<Error> getErrors();
    Boolean hasErrors();
    ValidationStatus getValidationStatus();
    void setStatus(ValidationStatus status);
    void addNotification(String key, String message);
    void addNotification(Error error);
    void addNotifications(List<Error> notifications);
    void addNotifications(ValidationResult validationResult);
}
