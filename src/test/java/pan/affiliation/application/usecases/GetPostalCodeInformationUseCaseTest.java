package pan.affiliation.application.usecases;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import pan.affiliation.domain.modules.customers.valueobjects.PostalCode;
import pan.affiliation.domain.modules.localization.entities.PostalCodeInformation;
import pan.affiliation.domain.modules.localization.queries.GetPostalCodeInformationQueryHandler;
import pan.affiliation.shared.exceptions.QueryException;
import pan.affiliation.shared.validation.ValidationContext;
import pan.affiliation.shared.validation.ValidationStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;
import static pan.affiliation.shared.constants.Messages.INVALID_POSTALCODE;

public class GetPostalCodeInformationUseCaseTest {

    @SneakyThrows
    @Test
    public void getPostalCodeInformation_shouldReturnNullIfProvidedPostalCodeIsNotValid() {
        var queryMock = mock(GetPostalCodeInformationQueryHandler.class);
        var validationContextMock = mock(ValidationContext.class);
        var getPostalCodeInformationUseCase = new GetPostalCodeInformationUseCase(queryMock, validationContextMock);
        var postalCode = new PostalCode("");

        var result = getPostalCodeInformationUseCase.getPostalCodeInformation(postalCode);

        assertNull(result);
        verify(validationContextMock).addNotification("postalCode", INVALID_POSTALCODE);
    }

    @SneakyThrows
    @Test
    public void getPostalCodeInformation_shouldReturnNullIfExceptionIsRaised() {
        var postalCode = new PostalCode("78085630");
        var queryMock = mock(GetPostalCodeInformationQueryHandler.class);
        var validationContextMock = mock(ValidationContext.class);
        var queryException = new QueryException("errorCode", "message");
        when(queryMock.getPostalCodeInformation(postalCode)).thenThrow(queryException);
        var getPostalCodeInformationUseCase = new GetPostalCodeInformationUseCase(queryMock, validationContextMock);

        var result = getPostalCodeInformationUseCase.getPostalCodeInformation(postalCode);

        assertNull(result);
        verify(validationContextMock).setStatus(ValidationStatus.INTEGRATION_ERROR);
        verify(validationContextMock).addNotification(queryException.getErrorCode(), queryException.getMessage());
        verify(queryMock, times(1)).getPostalCodeInformation(postalCode);
    }

    @SneakyThrows
    @Test
    public void getPostalCodeInformation_shouldReturnPostalCodeInformation() {
        var information = new PostalCodeInformation();
        information.setPostalCode("78085630");
        var postalCode = new PostalCode(information.getPostalCode());
        var queryMock = mock(GetPostalCodeInformationQueryHandler.class);
        when(queryMock.getPostalCodeInformation(postalCode)).thenReturn(information);
        var validationContextMock = mock(ValidationContext.class);
        var getPostalCodeInformationUseCase = new GetPostalCodeInformationUseCase(queryMock, validationContextMock);

        var result = getPostalCodeInformationUseCase.getPostalCodeInformation(postalCode);

        assertEquals(information, result);
        verify(validationContextMock, never()).setStatus(any(ValidationStatus.class));
        verify(queryMock, times(1)).getPostalCodeInformation(postalCode);
    }
}