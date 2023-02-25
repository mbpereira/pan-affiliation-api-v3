package pan.affiliation.domain.modules.localization.queries;

import pan.affiliation.domain.modules.localization.entities.City;
import pan.affiliation.shared.exceptions.QueryException;

import java.util.List;

public interface GetCitiesFromStatesQuery {
    List<City> getCitiesFromState(int stateId) throws QueryException;
}
