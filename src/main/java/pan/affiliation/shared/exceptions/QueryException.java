package pan.affiliation.shared.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryException extends Exception {
    private String errorCode;

    public QueryException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
