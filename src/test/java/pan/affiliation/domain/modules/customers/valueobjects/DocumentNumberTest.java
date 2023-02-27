package pan.affiliation.domain.modules.customers.valueobjects;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class DocumentNumberTest {

    @ParameterizedTest
    @ValueSource(strings = { "939.767.670-93", "05915193137", "67.298.257/0001-42", "42564779000118" })
    public void isValid_shouldReturnTrueForValidDocuments(String validDocument) {
        var documentNumber = new DocumentNumber(validDocument);

        assertTrue(documentNumber.isValid());
    }

    @ParameterizedTest
    @ValueSource(strings = { "", " ", "67298757000142", "42564779000108" })
    public void isValid_shouldReturnFalseForInvalidDocuments(String invalidDocument) {
        var documentNumber = new DocumentNumber(invalidDocument);

        assertFalse(documentNumber.isValid());
    }
}