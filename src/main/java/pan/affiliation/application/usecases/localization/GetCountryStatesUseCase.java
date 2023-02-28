package pan.affiliation.application.usecases.localization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pan.affiliation.domain.modules.localization.entities.State;
import pan.affiliation.domain.modules.localization.queries.GetCountryStatesQueryHandler;
import pan.affiliation.shared.exceptions.QueryException;
import pan.affiliation.shared.validation.ValidationContext;
import pan.affiliation.shared.validation.ValidationStatus;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class GetCountryStatesUseCase {
    private final static Logger logger = LoggerFactory.getLogger(GetCountryStatesUseCase.class);
    private final GetCountryStatesQueryHandler query;
    private final ValidationContext validationContext;

    @Autowired
    public GetCountryStatesUseCase(GetCountryStatesQueryHandler query, ValidationContext validationContext) {
        this.query = query;
        this.validationContext = validationContext;
    }

    public List<State> getCountryStates() {
        logger.info("Getting states");

        try {
            var states = this.query.getCountryStates();
            return this.sort(states);
        } catch (QueryException e) {
            logger.error("get states failed", e);
            this.validationContext.setStatus(ValidationStatus.INTEGRATION_ERROR);
            this.validationContext.addNotification(e.getErrorCode(), e.getMessage());
        }

        return null;
    }

    private List<State> sort(List<State> states) {
        var priorityStates = getPriorityStates();
        var statesMap = states.stream().collect(Collectors.toMap(State::getAcronym, Function.identity()));
        var orderedStates = new ArrayList<State>();
        priorityStates.forEach(p -> {
            if (statesMap.containsKey(p))
                orderedStates.add(statesMap.get(p.toUpperCase()));
        });
        orderedStates.addAll(states
                .stream()
                .filter(s -> !priorityStates.contains(s.getAcronym()))
                .sorted(Comparator.comparing(State::getName))
                .toList());
        return orderedStates;
    }

    private List<String> getPriorityStates() {
        return List.of("SP", "RJ");
    }
}
