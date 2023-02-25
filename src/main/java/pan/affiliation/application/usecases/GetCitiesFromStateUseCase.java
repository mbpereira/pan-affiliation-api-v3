package pan.affiliation.application.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pan.affiliation.domain.modules.localization.entities.City;
import pan.affiliation.domain.modules.localization.queries.GetCitiesFromStatesQueryHandler;
import pan.affiliation.shared.exceptions.QueryException;
import pan.affiliation.shared.validation.ValidationContext;
import pan.affiliation.shared.validation.ValidationStatus;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetCitiesFromStateUseCase {
    private final GetCitiesFromStatesQueryHandler query;
    private final ValidationContext validationContext;

    @Autowired
    public GetCitiesFromStateUseCase(GetCitiesFromStatesQueryHandler query, ValidationContext validationContext) {
        this.query = query;
        this.validationContext = validationContext;
    }

    public List<City> getCitiesFromState(int stateId) {
        try {
            var cities = this.query.getCitiesFromState(stateId);

            if (cities == null || cities.size() == 0) {
                this.validationContext.setStatus(ValidationStatus.NOT_FOUND);
                return null;
            }

            return cities
                    .stream()
                    .sorted(Comparator.comparing(City::getName))
                    .collect(Collectors.toList());
        } catch (QueryException e) {
            this.validationContext.setStatus(ValidationStatus.INTEGRATION_ERROR);
            this.validationContext.addNotification(e.getErrorCode(), e.getMessage());
        }

        return null;
    }
}
