package pan.affiliation.application.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pan.affiliation.domain.modules.localization.entities.State;
import pan.affiliation.domain.modules.localization.queries.GetCountryStatesQuery;
import pan.affiliation.shared.exceptions.HttpException;
import pan.affiliation.shared.validation.ValidationContext;
import pan.affiliation.shared.validation.ValidationStatus;

import java.io.IOException;
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
        } catch (HttpException e) {
            this.validationContext.setStatus(ValidationStatus.INTEGRATION_ERROR);
            this.validationContext.addNotification(e.getStatusCode().toString(), e.getMessage());
        } catch (InterruptedException e) {
            this.validationContext.setStatus(ValidationStatus.INTEGRATION_ERROR);
            this.validationContext.addNotification(ValidationStatus.INTEGRATION_ERROR.toString(), e.getMessage());
        } catch (IOException e) {
            this.validationContext.setStatus(ValidationStatus.INTEGRATION_ERROR);
            this.validationContext.addNotification(ValidationStatus.INTEGRATION_ERROR.toString(), e.getMessage());
        }

        return null;
    }
}
