package pan.affiliation.application.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pan.affiliation.domain.modules.customers.valueobjects.PostalCode;
import pan.affiliation.domain.modules.localization.entities.PostalCodeInformation;
import pan.affiliation.domain.modules.localization.queries.GetPostalCodeInformationQueryHandler;
import pan.affiliation.shared.exceptions.QueryException;
import pan.affiliation.shared.validation.ValidationContext;
import pan.affiliation.shared.validation.ValidationStatus;

import static pan.affiliation.shared.constants.Messages.INVALID_POSTALCODE;

@Service
public class GetPostalCodeInformationUseCase {
    private final GetPostalCodeInformationQueryHandler query;
    private final ValidationContext validationContext;

    @Autowired
    public GetPostalCodeInformationUseCase(GetPostalCodeInformationQueryHandler query, ValidationContext validationContext) {
        this.query = query;
        this.validationContext = validationContext;
    }

    public PostalCodeInformation getPostalCodeInformation(PostalCode postalCode) {
        if (!postalCode.isValid()) {
            this.validationContext.addNotification("postalCode", INVALID_POSTALCODE);
            return null;
        }

        try {
            return this.query.getPostalCodeInformation(postalCode);
        } catch (QueryException e) {
            this.validationContext.setStatus(ValidationStatus.INTEGRATION_ERROR);
            this.validationContext.addNotification(e.getErrorCode(), e.getMessage());
        }

        return null;
    }
}
