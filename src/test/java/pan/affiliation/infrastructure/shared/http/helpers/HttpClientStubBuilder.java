package pan.affiliation.infrastructure.shared.http.helpers;

import lombok.SneakyThrows;
import org.mockito.Mockito;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpClientStubBuilder {
    private String baseUrl;
    private String requestPath;
    private String responseBody;
    private int statusCode;

    public HttpClientStubBuilder setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public HttpClientStubBuilder setRequestPath(String requestPath) {
        this.requestPath = requestPath;
        return this;
    }

    public HttpClientStubBuilder setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public HttpClientStubBuilder setResponseBody(String responseBody) {
        this.responseBody = responseBody;
        return this;
    }

    @SneakyThrows
    public HttpClient build() {
        var requestUrl = String.format("%s/%s", this.baseUrl, this.requestPath);
        var request = HttpRequest.newBuilder(URI.create(requestUrl))
                .build();
        var httpClient = Mockito.mock(HttpClient.class);
        @SuppressWarnings("unchecked")
        var httpResponse = (HttpResponse<String>) Mockito.mock(HttpResponse.class);
        Mockito.when(httpResponse.body()).thenReturn(this.responseBody);
        Mockito.when(httpResponse.statusCode()).thenReturn(this.statusCode);
        Mockito.when(httpClient.send(request, HttpResponse.BodyHandlers.ofString()))
                .thenReturn(httpResponse);

        return httpClient;
    }
}
