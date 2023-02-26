package pan.affiliation.domain.shared;

import pan.affiliation.shared.validation.ValidationResult;

public abstract class BaseEntitiy {
    public abstract ValidationResult validate();
}
