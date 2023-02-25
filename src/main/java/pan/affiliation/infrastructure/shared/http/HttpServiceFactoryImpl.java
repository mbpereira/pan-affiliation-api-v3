package pan.affiliation.infrastructure.shared.http;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pan.affiliation.infrastructure.shared.http.abstractions.HttpService;
import pan.affiliation.infrastructure.shared.http.abstractions.HttpServiceFactory;

import java.net.http.HttpClient;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
@Scope("singleton")
public class HttpServiceFactoryImpl implements HttpServiceFactory {
    private static final ConcurrentMap<String, HttpService> services = new ConcurrentHashMap<>();

    @Override
    public HttpService create(String baseUrl) {
        if (services.containsKey(baseUrl))
            return services.get(baseUrl);

        var httpService = new HttpServiceImpl(baseUrl, HttpClient.newHttpClient());
        services.put(baseUrl, httpService);
        return httpService;
    }
}
