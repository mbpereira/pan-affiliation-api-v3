package pan.affiliation.infrastructure.shared.http.helpers;

import org.mockito.Mockito;
import pan.affiliation.infrastructure.shared.http.HttpServiceImpl;
import pan.affiliation.infrastructure.shared.http.abstractions.HttpServiceFactory;

import java.net.http.HttpClient;

public class HttpGatewayServiceFactory<T> {
    private final ServiceCreator<T> serviceCreator;
    private final HttpClient httpClient;

    public HttpGatewayServiceFactory(ServiceCreator<T> serviceCreator, HttpClient httpClient) {
        this.serviceCreator = serviceCreator;
        this.httpClient = httpClient;
    }

    public T create(String baseUrl) {
        var httpServiceFactory = Mockito.mock(HttpServiceFactory.class);
        Mockito.when(httpServiceFactory.create(baseUrl))
                .thenReturn(new HttpServiceImpl(baseUrl, this.httpClient));
        return this.serviceCreator.createService(httpServiceFactory);
    }
}
