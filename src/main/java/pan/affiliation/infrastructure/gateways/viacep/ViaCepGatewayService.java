package pan.affiliation.infrastructure.gateways.viacep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pan.affiliation.domain.modules.customers.valueobjects.PostalCode;
import pan.affiliation.domain.modules.localization.entities.PostalCodeInformation;
import pan.affiliation.domain.modules.localization.queries.GetPostalCodeInformationQueryHandler;
import pan.affiliation.infrastructure.gateways.viacep.contracts.PostalCodeInformationResponse;
import pan.affiliation.infrastructure.shared.http.abstractions.HttpService;
import pan.affiliation.infrastructure.shared.http.abstractions.HttpServiceFactory;
import pan.affiliation.shared.environment.PropertiesReader;
import pan.affiliation.shared.exceptions.QueryException;

@Component
public class ViaCepGatewayService implements GetPostalCodeInformationQueryHandler {
    private static final String GET_POSTACODE_INFORMATION_PATH = "ws/%s/json";
    private final HttpService http;

    @Autowired
    public ViaCepGatewayService(HttpServiceFactory factory, PropertiesReader propertiesReader) {
        this.http = factory.create(propertiesReader.get("viacep.baseurl"));
    }

    @Override
    public PostalCodeInformation getPostalCodeInformation(PostalCode postalCode) throws QueryException {
        var requestUrl = String.format(GET_POSTACODE_INFORMATION_PATH, postalCode.getValue());
        var information = this.http.get(requestUrl, PostalCodeInformationResponse.class);

        return new PostalCodeInformation(
                information.getPostalCode(),
                information.getStreet(),
                information.getNeighborhood(),
                information.getState(),
                information.getCity()
        );
    }
}
