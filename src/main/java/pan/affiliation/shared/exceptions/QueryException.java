package pan.affiliation.shared.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryException extends Exception {
    private String errorCode;

    public QueryException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
