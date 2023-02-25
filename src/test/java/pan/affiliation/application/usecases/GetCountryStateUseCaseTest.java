package pan.affiliation.application.usecases;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import pan.affiliation.domain.modules.localization.entities.State;
import pan.affiliation.domain.modules.localization.queries.GetCountryStatesQueryHandler;
import pan.affiliation.shared.exceptions.QueryException;
import pan.affiliation.shared.validation.ValidationContext;
import pan.affiliation.shared.validation.ValidationStatus;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class GetCountryStateUseCaseTest {
    @SneakyThrows
    @Test
    public void getCountryStates_shouldReturnNullWhenExceptionIsRaised() {
        var queryMock = mock(GetCountryStatesQueryHandler.class);
        var validationContextMock = mock(ValidationContext.class);
        var exception = new QueryException("errorCode", "errorMessage");
        when(queryMock.getCountryStates()).thenThrow(exception);
        var getCountryStateUseCase = new GetCountryStateUseCase(queryMock, validationContextMock);

        var result = getCountryStateUseCase.getCountryStates();

        assertNull(result);
        verify(validationContextMock).setStatus(ValidationStatus.INTEGRATION_ERROR);
        verify(validationContextMock).addNotification(exception.getErrorCode(), exception.getMessage());
        verify(queryMock, times(1)).getCountryStates();
    }

    @SneakyThrows
    @Test
    public void getCountryStates_shouldReturnExpectedStates() {
        var states = new ArrayList<State>();
        states.add(new State(11, "MT", "Mato-Grosso"));
        var queryMock = mock(GetCountryStatesQueryHandler.class);
        var validationContextMock = mock(ValidationContext.class);
        when(queryMock.getCountryStates()).thenReturn(states);
        var getCountryStateUseCase = new GetCountryStateUseCase(queryMock, validationContextMock);

        var result = getCountryStateUseCase.getCountryStates();

        assertEquals(states, result);
        verify(validationContextMock, never()).setStatus(any(ValidationStatus.class));
        verify(queryMock, times(1)).getCountryStates();
    }
}