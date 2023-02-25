package pan.affiliation.infrastructure.shared.http.abstractions;

public interface HttpServiceFactory {
    HttpService create(String baseUrl);
}
