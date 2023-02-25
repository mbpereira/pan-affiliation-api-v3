package pan.affiliation.application.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pan.affiliation.domain.modules.localization.entities.City;
import pan.affiliation.domain.modules.localization.queries.GetCitiesFromStatesQuery;
import pan.affiliation.shared.exceptions.QueryException;
import pan.affiliation.shared.validation.ValidationContext;
import pan.affiliation.shared.validation.ValidationStatus;

import java.util.List;

@Service
public class GetCitiesFromStateUseCase {
    private final GetCitiesFromStatesQuery query;
    private final ValidationContext validationContext;

    @Autowired
    public GetCitiesFromStateUseCase(GetCitiesFromStatesQuery query, ValidationContext validationContext) {
        this.query = query;
        this.validationContext = validationContext;
    }

    public List<City> getCitiesFromState(int stateId) {
        try {
            return this.query.getCitiesFromState(stateId);
        } catch (QueryException e) {
            this.validationContext.setStatus(ValidationStatus.INTEGRATION_ERROR);
            this.validationContext.addNotification(e.getErrorCode(), e.getMessage());
        }

        return null;
    }
}
