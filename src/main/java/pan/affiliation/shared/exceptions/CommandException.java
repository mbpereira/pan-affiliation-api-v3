package pan.affiliation.shared.exceptions;

public class CommandException extends IntegrationException {
    public CommandException(String errorCode, String message) {
        super(errorCode, message);
    }
}
