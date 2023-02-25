package pan.affiliation.domain.modules.localization.queries;

import pan.affiliation.domain.modules.localization.entities.State;
import pan.affiliation.shared.exceptions.HttpException;

import java.io.IOException;
import java.util.List;

public interface GetCountryStatesQuery {
    List<State> getCountryStates() throws IOException, InterruptedException, HttpException;
}
