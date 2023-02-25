package pan.affiliation.application.usecases;

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
    private final GetCountryStatesQueryHandler query;
    private final ValidationContext validationContext;

    @Autowired
    public GetCountryStatesUseCase(GetCountryStatesQueryHandler query, ValidationContext validationContext) {
        this.query = query;
        this.validationContext = validationContext;
    }

    public List<State> getCountryStates() {
        try {
            var states = this.query.getCountryStates();
            return this.sort(states);
        } catch (QueryException e) {
            this.validationContext.setStatus(ValidationStatus.INTEGRATION_ERROR);
            this.validationContext.addNotification(e.getErrorCode(), e.getMessage());
        }

        return null;
    }

    private List<State> sort(List<State> states) {
        var priorityStates = getPriorityStates();
        var statesMap = states.stream().collect(Collectors.toMap(State::getAcronym, Function.identity()));
        var orderedStates = new ArrayList<State>();
        priorityStates.forEach(p -> orderedStates.add(statesMap.get(p.toUpperCase())));
        orderedStates.addAll(states
                .stream()
                .filter(s -> !priorityStates.contains(s.getAcronym()))
                .sorted(Comparator.comparing(State::getName))
                .toList());
        return orderedStates;
    }

    private List<String> getPriorityStates() {
        var priorityStates = new ArrayList<String>();
        priorityStates.add("SP");
        priorityStates.add("RJ");
        return priorityStates;
    }
}
