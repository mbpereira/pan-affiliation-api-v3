package pan.affiliation.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pan.affiliation.api.contracts.GenericResponse;
import pan.affiliation.application.usecases.GetCitiesFromStateUseCase;
import pan.affiliation.application.usecases.GetCountryStatesUseCase;
import pan.affiliation.domain.modules.localization.entities.City;
import pan.affiliation.domain.modules.localization.entities.State;
import pan.affiliation.shared.validation.ValidationContext;

import java.util.List;

@RestController
@RequestMapping("/states")
public class StatesController extends DefaultController {
    private final GetCountryStatesUseCase getCountryStateUseCase;
    private final GetCitiesFromStateUseCase getCitiesFromStateUseCase;

    @Autowired
    public StatesController(GetCountryStatesUseCase useCase, ValidationContext validationContext, GetCitiesFromStateUseCase getCitiesFromStateUseCase) {
        super(validationContext);
        this.getCountryStateUseCase = useCase;
        this.getCitiesFromStateUseCase = getCitiesFromStateUseCase;
    }

    @GetMapping
    public ResponseEntity<GenericResponse<List<State>>> getCountryStates() {
        return createGenericResponse(getCountryStateUseCase.getCountryStates());
    }

    @GetMapping("/{stateId}/cities")
    public ResponseEntity<GenericResponse<List<City>>> getCitiesFromState(@PathVariable int stateId) {
        return createGenericResponse(getCitiesFromStateUseCase.getCitiesFromState(stateId));
    }
}
