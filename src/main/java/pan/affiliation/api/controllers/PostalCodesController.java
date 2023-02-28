package pan.affiliation.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pan.affiliation.api.contracts.GenericResponse;
import pan.affiliation.application.usecases.localization.GetPostalCodeInformationUseCase;
import pan.affiliation.domain.modules.customers.valueobjects.PostalCode;
import pan.affiliation.domain.modules.localization.entities.PostalCodeInformation;
import pan.affiliation.shared.validation.ValidationContext;

@RestController
@RequestMapping("/postal-codes")
public class PostalCodesController extends DefaultController {
    private final GetPostalCodeInformationUseCase getPostalCodeInformationUseCase;

    @Autowired
    public PostalCodesController(ValidationContext context, GetPostalCodeInformationUseCase getPostalCodeInformationUseCase) {
        super(context);
        this.getPostalCodeInformationUseCase = getPostalCodeInformationUseCase;
    }

    @GetMapping("{postalCode}")
    public ResponseEntity<GenericResponse<PostalCodeInformation>> getPostalCodeInformation(@PathVariable String postalCode) {
        return createGenericResponse(getPostalCodeInformationUseCase.getPostalCodeInformation(new PostalCode(postalCode)));
    }
}
