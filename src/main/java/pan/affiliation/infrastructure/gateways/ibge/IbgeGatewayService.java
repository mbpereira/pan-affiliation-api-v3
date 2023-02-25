package pan.affiliation.infrastructure.gateways.ibge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pan.affiliation.domain.modules.localization.entities.City;
import pan.affiliation.domain.modules.localization.entities.State;
import pan.affiliation.domain.modules.localization.queries.GetCitiesFromStatesQueryHandler;
import pan.affiliation.domain.modules.localization.queries.GetCountryStatesQueryHandler;
import pan.affiliation.infrastructure.gateways.ibge.contracts.CityResponse;
import pan.affiliation.infrastructure.gateways.ibge.contracts.StateResponse;
import pan.affiliation.infrastructure.shared.http.abstractions.HttpService;
import pan.affiliation.infrastructure.shared.http.abstractions.HttpServiceFactory;
import pan.affiliation.shared.environment.PropertiesReader;
import pan.affiliation.shared.exceptions.QueryException;

import java.util.Arrays;
import java.util.List;

@Component
public class IbgeGatewayService implements GetCountryStatesQueryHandler, GetCitiesFromStatesQueryHandler {
    private static final String GET_STATES_PATH = "estados";
    private static final String GET_CITIES_FROM_STATE_PATH = "estados/%s/municipios";
    private final HttpService http;

    @Autowired
    public IbgeGatewayService(HttpServiceFactory factory, PropertiesReader propertiesReader) {
        this.http = factory.create(propertiesReader.get("ibge.baseurl"));
    }

    @Override
    public List<State> getCountryStates() throws QueryException {
        var states = this.http.get(GET_STATES_PATH, StateResponse[].class);
        return Arrays.stream(states)
                .map(s -> new State(s.getId(), s.getAcronym(), s.getName())).toList();
    }

    @Override
    public List<City> getCitiesFromState(int stateId) throws QueryException {
        var requestUrl = String.format(GET_CITIES_FROM_STATE_PATH, stateId);
        var states = this.http.get(requestUrl, CityResponse[].class);
        return Arrays.stream(states)
                .map(s -> new City(s.getId(), s.getName())).toList();
    }
}
