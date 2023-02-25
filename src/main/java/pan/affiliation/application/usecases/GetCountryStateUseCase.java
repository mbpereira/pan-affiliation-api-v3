package pan.affiliation.application.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pan.affiliation.domain.modules.localization.entities.State;
import pan.affiliation.domain.modules.localization.queries.GetCountryStatesQuery;
import pan.affiliation.shared.exceptions.QueryException;
import pan.affiliation.shared.validation.ValidationContext;
import pan.affiliation.shared.validation.ValidationStatus;

import java.util.List;

@Service
public class GetCountryStateUseCase {
    private final GetCountryStatesQuery query;
    private final ValidationContext validationContext;

    @Autowired
    public GetCountryStateUseCase(GetCountryStatesQuery query, ValidationContext validationContext) {
        this.query = query;
        this.validationContext = validationContext;
    }

    public List<State> getCountryStates() {
        try {
            return this.query.getCountryStates();
        } catch (QueryException e) {
            this.validationContext.setStatus(ValidationStatus.INTEGRATION_ERROR);
            this.validationContext.addNotification(e.getErrorCode(), e.getMessage());
        }

        return null;
    }
}
