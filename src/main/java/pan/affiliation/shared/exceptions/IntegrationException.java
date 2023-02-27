package pan.affiliation.shared.exceptions;

import lombok.Getter;

@Getter
public class IntegrationException extends Exception {
    private final String errorCode;

    public IntegrationException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
