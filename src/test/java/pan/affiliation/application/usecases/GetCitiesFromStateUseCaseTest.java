package pan.affiliation.application.usecases;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import pan.affiliation.domain.modules.localization.entities.City;
import pan.affiliation.domain.modules.localization.queries.GetCitiesFromStatesQueryHandler;
import pan.affiliation.shared.exceptions.QueryException;
import pan.affiliation.shared.validation.ValidationContext;
import pan.affiliation.shared.validation.ValidationStatus;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GetCitiesFromStateUseCaseTest {
    @SneakyThrows
    @Test
    public void getCitiesFromState_shouldReturnNullWhenExceptionIsRaised() {
        var queryMock = mock(GetCitiesFromStatesQueryHandler.class);
        var validationContextMock = mock(ValidationContext.class);
        var exception = new QueryException("errorCode", "errorMessage");
        when(queryMock.getCitiesFromState(51)).thenThrow(exception);
        var getCitiesFromStateUseCase = new GetCitiesFromStateUseCase(queryMock, validationContextMock);

        var result = getCitiesFromStateUseCase.getCitiesFromState(51);

        assertNull(result);
        verify(validationContextMock).setStatus(ValidationStatus.INTEGRATION_ERROR);
        verify(validationContextMock).addNotification(exception.getErrorCode(), exception.getMessage());
        verify(queryMock, times(1)).getCitiesFromState(51);
    }

    @SneakyThrows
    @Test
    public void getCitiesFromState_shouldReturnNullIfProvidedStateDoesNotExists() {
        var queryMock = mock(GetCitiesFromStatesQueryHandler.class);
        var validationContextMock = mock(ValidationContext.class);
        when(queryMock.getCitiesFromState(51)).thenReturn(null);
        var getCitiesFromStateUseCase = new GetCitiesFromStateUseCase(queryMock, validationContextMock);

        var result = getCitiesFromStateUseCase.getCitiesFromState(51);

        assertNull(result);
        verify(validationContextMock).setStatus(ValidationStatus.NOT_FOUND);
        verify(queryMock, times(1)).getCitiesFromState(51);
    }

    @SneakyThrows
    @Test
    public void getCitiesFromState_shouldReturnExpectedCities() {
        var cities = new ArrayList<City>();
        cities.add(new City(10, "Sinop"));
        cities.add(new City(11, "Cuiabá"));
        var queryMock = mock(GetCitiesFromStatesQueryHandler.class);
        var validationContextMock = mock(ValidationContext.class);
        when(queryMock.getCitiesFromState(51)).thenReturn(cities);
        var getCitiesFromStateUseCase = new GetCitiesFromStateUseCase(queryMock, validationContextMock);

        var result = getCitiesFromStateUseCase.getCitiesFromState(51);

        assertEquals(result.get(0).getName(), "Cuiabá");
        assertEquals(result.get(1).getName(), "Sinop");
        verify(validationContextMock, never()).setStatus(any(ValidationStatus.class));
        verify(queryMock, times(1)).getCitiesFromState(51);
    }
}