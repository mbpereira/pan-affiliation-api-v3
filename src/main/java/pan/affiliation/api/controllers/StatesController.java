package pan.affiliation.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pan.affiliation.api.contracts.GenericResponse;
import pan.affiliation.application.usecases.GetCountryStateUseCase;
import pan.affiliation.domain.modules.localization.entities.State;
import pan.affiliation.shared.validation.ValidationContext;

import java.util.List;

@RestController
@RequestMapping("/states")
public class StatesController extends DefaultController {
    private final GetCountryStateUseCase useCase;

    @Autowired
    public StatesController(GetCountryStateUseCase useCase, ValidationContext validationContext) {
        super(validationContext);
        this.useCase = useCase;
    }

    @GetMapping
    public ResponseEntity<GenericResponse<List<State>>> getCountryStates() {
        return createGenericResponse(useCase.getCountryStates());
    }
}
