package pan.affiliation.shared.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryException extends IntegrationException {

    public QueryException(String errorCode, String message) {
        super(errorCode, message);
    }
}
