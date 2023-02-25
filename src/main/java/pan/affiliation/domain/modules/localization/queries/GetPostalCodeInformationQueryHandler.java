package pan.affiliation.domain.modules.localization.queries;

import pan.affiliation.domain.modules.customers.valueobjects.PostalCode;
import pan.affiliation.domain.modules.localization.entities.PostalCodeInformation;
import pan.affiliation.shared.exceptions.QueryException;

public interface GetPostalCodeInformationQueryHandler {
    PostalCodeInformation getPostalCodeInformation(PostalCode postalCode) throws QueryException;
}
