package pan.affiliation.infrastructure.gateways.ibge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pan.affiliation.domain.modules.localization.entities.State;
import pan.affiliation.domain.modules.localization.queries.GetCountryStatesQuery;
import pan.affiliation.infrastructure.gateways.ibge.contracts.StateResponse;
import pan.affiliation.infrastructure.gateways.shared.Http;
import pan.affiliation.shared.environment.PropertiesReader;
import pan.affiliation.shared.exceptions.HttpException;

import java.io.IOException;
import java.util.List;

@Component
public class IbgeGatewayService implements GetCountryStatesQuery {

    private Http http;
    private PropertiesReader propertiesReader;

    private static final String getStatesPath = "estados";

    @Autowired
    public IbgeGatewayService(Http http, PropertiesReader propertiesReader) {
        this.http = http;
        this.propertiesReader = propertiesReader;
        this.http.setBaseUrl(propertiesReader.get("ibge.baseurl"));
    }

    @Override
    public List<State> getCountryStates() throws IOException, InterruptedException, HttpException {
        var states = (List<StateResponse>) this.http.get("estados", List.class);
        return states.stream().map(s -> new State(s.id(), s.name(), s.acronym())).toList();
    }
}
