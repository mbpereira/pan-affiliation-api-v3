package pan.affiliation.infrastructure.gateways.viacep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pan.affiliation.domain.modules.customers.valueobjects.PostalCode;
import pan.affiliation.domain.modules.localization.entities.PostalCodeInformation;
import pan.affiliation.domain.modules.localization.queries.GetPostalCodeInformationQueryHandler;
import pan.affiliation.infrastructure.gateways.viacep.contracts.PostalCodeInformationResponse;
import pan.affiliation.infrastructure.shared.http.abstractions.HttpService;
import pan.affiliation.infrastructure.shared.http.abstractions.HttpServiceFactory;
import pan.affiliation.shared.caching.CacheProvider;
import pan.affiliation.shared.environment.PropertiesReader;
import pan.affiliation.shared.exceptions.QueryException;

@Component
public class ViaCepGatewayService implements GetPostalCodeInformationQueryHandler {
    private final HttpService http;
    private final CacheProvider cacheProvider;

    @Autowired
    public ViaCepGatewayService(HttpServiceFactory factory, PropertiesReader propertiesReader, CacheProvider cacheProvider) {
        this.cacheProvider = cacheProvider;
        this.http = factory.create(propertiesReader.get("viacep.baseurl"));
    }

    @Override
    public PostalCodeInformation getPostalCodeInformation(PostalCode postalCode) throws QueryException {
        var cacheKey = String.format("postalcode_%s", postalCode.getValue());
        var cachedValue = this.cacheProvider.get(cacheKey, PostalCodeInformationResponse.class);

        PostalCodeInformationResponse information;

        if (cachedValue.isPresent()) {
            information = cachedValue.get();
        } else {
            var requestUrl = String.format("ws/%s/json", postalCode.getValue());
            var response = this.http.get(requestUrl, PostalCodeInformationResponse.class);
            this.cacheProvider.set(cacheKey, response);
            information = response;
        }

        return new PostalCodeInformation(
                information.getPostalCode(),
                information.getStreet(),
                information.getNeighborhood(),
                information.getState(),
                information.getCity()
        );
    }
}
