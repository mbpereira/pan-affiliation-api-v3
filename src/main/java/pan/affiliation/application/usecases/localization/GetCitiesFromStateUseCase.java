package pan.affiliation.application.usecases.localization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final static Logger logger = LoggerFactory.getLogger(GetCitiesFromStateUseCase.class);
    private final GetCitiesFromStatesQueryHandler query;
    private final ValidationContext validationContext;

    @Autowired
    public GetCitiesFromStateUseCase(GetCitiesFromStatesQueryHandler query, ValidationContext validationContext) {
        this.query = query;
        this.validationContext = validationContext;
    }

    public List<City> getCitiesFromState(int stateId) {
        logger.info("Getting cities from state {}", stateId);

        try {
            var cities = this.query.getCitiesFromState(stateId);

            if (cities == null || cities.size() == 0) {
                logger.warn("State identified by {} not found", stateId);
                this.validationContext.setStatus(ValidationStatus.NOT_FOUND);
                return null;
            }

            return this.sort(cities);
        } catch (QueryException e) {
            logger.error("Get cities failed", e);
            this.validationContext.setStatus(ValidationStatus.INTEGRATION_ERROR);
            this.validationContext.addNotification(e.getErrorCode(), e.getMessage());
        }

        return null;
    }

    private List<City> sort(List<City> cities) {
        return cities
                .stream()
                .sorted(Comparator.comparing(City::getName))
                .collect(Collectors.toList());
    }
}
