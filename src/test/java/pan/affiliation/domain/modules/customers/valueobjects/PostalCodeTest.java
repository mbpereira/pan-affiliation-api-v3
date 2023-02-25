package pan.affiliation.domain.modules.customers.valueobjects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PostalCodeTest {
    @ParameterizedTest
    @ValueSource(strings = { "78085630", "78085-630" })
    public void isValid_shouldReturnTrueIfPostalCodeIsValid(String validCep) {
        var postalCode = new PostalCode(validCep);

        assertTrue(postalCode.isValid());
    }

    @ParameterizedTest
    @ValueSource(strings = { "", "7808563110"  })
    public void isValid_shouldReturnFalseIfPostalCodeIsNotValid(String invalidCep) {
        var postalCode = new PostalCode(invalidCep);

        assertFalse(postalCode.isValid());
    }

    @Test
    public void isValid_shouldReturnFalseIfPostalCodeIsNull() {
        var postalCode = new PostalCode(null);

        assertFalse(postalCode.isValid());
    }
}