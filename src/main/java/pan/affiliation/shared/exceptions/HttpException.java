package pan.affiliation.shared.exceptions;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpStatusCodeException;

public class HttpException extends HttpStatusCodeException {
    protected HttpException(HttpStatusCode statusCode) {
        super(statusCode);
    }

    public static HttpException throwException(int statusCode) {
        throw new HttpException(HttpStatusCode.valueOf(statusCode));
    }
}
