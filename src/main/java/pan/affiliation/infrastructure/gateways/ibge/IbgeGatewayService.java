package pan.affiliation.infrastructure.gateways.ibge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pan.affiliation.domain.modules.localization.entities.State;
import pan.affiliation.domain.modules.localization.queries.GetCountryStatesQuery;
import pan.affiliation.infrastructure.gateways.ibge.contracts.StateResponse;
import pan.affiliation.infrastructure.gateways.shared.Http;
import pan.affiliation.shared.environment.PropertiesReader;
import pan.affiliation.shared.exceptions.QueryException;

import java.util.Arrays;
import java.util.List;

@Component
public class IbgeGatewayService implements GetCountryStatesQuery {
    private final Http http;
    private static final String getStatesPath = "estados";

    @Autowired
    public IbgeGatewayService(Http http, PropertiesReader propertiesReader) {
        this.http = http;
        this.http.setBaseUrl(propertiesReader.get("ibge.baseurl"));
    }

    @Override
    public List<State> getCountryStates() throws QueryException {
        var states = this.http.get(getStatesPath, StateResponse[].class);
        return Arrays.stream(states)
                .map(s -> new State(s.getId(), s.getName(), s.getAcronym())).toList();
    }
}
