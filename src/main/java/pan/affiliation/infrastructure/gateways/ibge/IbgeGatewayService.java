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
import pan.affiliation.shared.caching.CacheProvider;
import pan.affiliation.shared.environment.PropertiesReader;
import pan.affiliation.shared.exceptions.QueryException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Component
public class IbgeGatewayService implements GetCountryStatesQueryHandler, GetCitiesFromStatesQueryHandler {
    private final HttpService http;
    private final CacheProvider cacheProvider;

    @Autowired
    public IbgeGatewayService(HttpServiceFactory factory, PropertiesReader propertiesReader, CacheProvider cacheProvider) {
        this.cacheProvider = cacheProvider;
        this.http = factory.create(propertiesReader.get("ibge.baseurl"));
    }

    @Override
    public List<State> getCountryStates() throws QueryException {
        var cacheKey = "country_states";
        var cachedValues = this.cacheProvider.getMany(cacheKey, StateResponse.class);

        Stream<StateResponse> states;

        if (!cachedValues.isEmpty()) {
            states = cachedValues.stream();
        } else {
            var response = this.http.get("estados", StateResponse[].class);
            this.cacheProvider.setMany(cacheKey, List.of(response));
            states = Arrays.stream(response);
        }

        return states.map(s -> new State(s.getId(), s.getAcronym(), s.getName())).toList();
    }

    @Override
    public List<City> getCitiesFromState(int stateId) throws QueryException {
        var cacheKey = String.format("state_%d_cities", stateId);
        var cachedValues = this.cacheProvider.getMany(cacheKey, CityResponse.class);

        Stream<CityResponse> cities;

        if (!cachedValues.isEmpty()) {
            cities = cachedValues.stream();
        } else {
            var requestUrl = String.format("estados/%d/municipios", stateId);
            var response = this.http.get(requestUrl, CityResponse[].class);
            this.cacheProvider.setMany(cacheKey, List.of(response));
            cities = Arrays.stream(response);
        }

        return cities.map(s -> new City(s.getId(), s.getName())).toList();
    }
}
