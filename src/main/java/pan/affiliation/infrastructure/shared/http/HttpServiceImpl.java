package pan.affiliation.infrastructure.shared.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pan.affiliation.infrastructure.shared.http.abstractions.HttpService;
import pan.affiliation.shared.exceptions.QueryException;
import pan.affiliation.shared.validation.ValidationStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

public class HttpServiceImpl implements HttpService {
    private final static Logger logger = LoggerFactory.getLogger(HttpServiceImpl.class);

    private final HttpClient http;
    private final String baseUrl;

    public HttpServiceImpl(String baseUrl, HttpClient httpClient) {
        this.baseUrl = baseUrl;
        this.http = httpClient;
    }

    @Override
    public <T> T get(String path, Class<T> clazz) throws QueryException {
        var url = String.format("%s/%s", baseUrl, path);
        var request = HttpRequest
                .newBuilder(URI.create(url))
                .build();
        try {
            logger.info("sending http GET {}", url);

            var response = this.http.send(request, HttpResponse.BodyHandlers.ofString());
            ensureSuccessStatusCode(response);

            logger.info("received success response from http GET {}", url);

            var mapper = new ObjectMapper()
                    .disable(FAIL_ON_UNKNOWN_PROPERTIES);

            return mapper.readValue(response.body(), clazz);
        } catch (IOException | InterruptedException e) {
            logger.error("http GET {} failed", url);
            logger.error("Request failed", e);
            throw new QueryException(ValidationStatus.INTEGRATION_ERROR.toString(), e.getMessage());
        }
    }

    private static void ensureSuccessStatusCode(HttpResponse<String> response) throws QueryException {
        if (response.statusCode() < 200 || response.statusCode() > 399) {
            var message = String.format("HttpStatusCode: %s; Response: %s",
                    response.statusCode(),
                    response.body());
            logger.warn("Request failed. response: {}", message);
            throw new QueryException(ValidationStatus.INTEGRATION_ERROR.toString(), message);
        }
    }
}
