package pan.affiliation.infrastructure.gateways.shared;

import com.google.gson.Gson;
import org.springframework.stereotype.Service;
import pan.affiliation.shared.exceptions.QueryException;
import pan.affiliation.shared.validation.ValidationStatus;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class HttpImpl implements Http {
    private final HttpClient http;
    private String baseUrl;

    public HttpImpl() {
        this.http = HttpClient.newHttpClient();
    }

    @Override
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public <T> T get(String path, Class<T> responseClass) throws QueryException {
        var url = String.format("%s/%s", baseUrl, path);
        var request = HttpRequest
                .newBuilder(URI.create(url))
                .build();
        try {
            var response = this.http.send(request, HttpResponse.BodyHandlers.ofString());
            ensureSuccessStatusCode(response);
            return new Gson().fromJson(response.body(), responseClass);
        } catch (IOException | InterruptedException e) {
            throw new QueryException(ValidationStatus.INTEGRATION_ERROR.toString(), e.getMessage());
        }
    }

    private static void ensureSuccessStatusCode(HttpResponse<String> response) throws QueryException {
        if (response.statusCode() < 200 || response.statusCode() > 399) {
            var message = String.format("HttpStatusCode: %s; Response: %s",
                    response.statusCode(),
                    response.body());
            throw new QueryException(ValidationStatus.INTEGRATION_ERROR.toString(), message);
        }
    }
}
