package pan.affiliation.infrastructure.gateways.shared;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import pan.affiliation.infrastructure.gateways.shared.bodyhandlers.JsonBodyHandler;
import pan.affiliation.shared.exceptions.HttpException;

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
    public <TOutput> TOutput get(String path, Class<TOutput> responseClass) throws IOException, InterruptedException, HttpException {
        // create a request
        var url = String.format("%s/%s", baseUrl, path);
        var request = HttpRequest
                .newBuilder(URI.create(url))
                .build();
        var response = this.http.send(request, HttpResponse.BodyHandlers.ofString());

        ensureSuccessStatusCode(response);

        return new Gson().fromJson(response.body(), responseClass);
    }

    private static void ensureSuccessStatusCode(HttpResponse<String> response) {
        if (response.statusCode() < 200 || response.statusCode() > 399) {
            HttpException.throwException(response.statusCode());
        }
    }
}
