package pan.affiliation.domain.modules.customers.valueobjects;

import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;
import jakarta.validation.constraints.AssertTrue;
import lombok.Getter;
import pan.affiliation.domain.modules.customers.enums.DocumentType;
import pan.affiliation.domain.shared.valueobjects.ValueObject;
import pan.affiliation.shared.helpers.StringHelpers;

@Getter
public class DocumentNumber extends ValueObject {
    private final String originalValue;
    private final String value;
    @AssertTrue
    private Boolean isValid;
    private DocumentType documentType;

    public DocumentNumber(String documentNumber) {
        this.originalValue = documentNumber;
        this.value = StringHelpers.keepOnlyNumbers(documentNumber);
        this.validate();
    }

    private void validate() {
        if (StringHelpers.isNullOrEmpty(this.value)) {
            this.isValid = false;
            return;
        }

        this.documentType = getDocumentType();

        if (DocumentType.CNPJ.equals(documentType)) {
            this.isValid = isCnpjValid(this.value);
            return;
        }

        this.isValid = isCpfValid(this.value);
    }

    private Boolean isCpfValid(String value) {
        try {
            new CPFValidator().assertValid(value);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private Boolean isCnpjValid(String cnpj) {
        try {
            new CNPJValidator().assertValid(cnpj);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private DocumentType getDocumentType() {
        return value.length() > 11
                ? DocumentType.CNPJ
                : DocumentType.CPF;
    }

    @Override
    public Boolean isValid() {
        return this.isValid;
    }
}
