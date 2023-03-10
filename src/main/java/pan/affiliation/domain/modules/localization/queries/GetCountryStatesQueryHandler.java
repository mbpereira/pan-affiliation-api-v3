package pan.affiliation.domain.modules.localization.queries;

import pan.affiliation.domain.modules.localization.entities.State;
import pan.affiliation.shared.exceptions.QueryException;

import java.util.List;

public interface GetCountryStatesQueryHandler {
    List<State> getCountryStates() throws QueryException;
}
