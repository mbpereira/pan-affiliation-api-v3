package pan.affiliation.infrastructure.shared.http;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pan.affiliation.infrastructure.gateways.ibge.contracts.StateResponse;
import pan.affiliation.infrastructure.shared.http.helpers.HttpClientStubBuilder;
import pan.affiliation.shared.exceptions.QueryException;
import pan.affiliation.shared.validation.ValidationStatus;

import static org.junit.jupiter.api.Assertions.*;

public class HttpServiceImplTest {

    @SneakyThrows
    @Test
    public void get_shouldReturnParsedResposeBodyOnSuccessStatusCode() {
        var baseUrl = "https://servicodados.ibge.gov.br/api/v1/localidades";
        var requestPath = "estados";
        var httpClient = new HttpClientStubBuilder()
                .setBaseUrl(baseUrl)
                .setRequestPath(requestPath)
                .setResponseBody("[{\"id\":11,\"sigla\":\"RO\",\"nome\":\"Rondônia\"}]")
                .setStatusCode(200)
                .build();
        var httpService = new HttpServiceImpl(baseUrl, httpClient);

        var response = httpService.get(requestPath, StateResponse[].class);

        assertEquals(11, response[0].getId());
        assertEquals("RO", response[0].getAcronym());
        assertEquals("Rondônia", response[0].getName());
    }

    @SneakyThrows
    @ParameterizedTest()
    @ValueSource(ints = { 400, 500 })
    public void get_shouldThrowsQueryExceptionOnErrorStatusCodes(int statusCode) {
        var baseUrl = "https://servicodados.ibge.gov.br/api/v1/localidades";
        var responseBody = "{\"errors\": [\"one or more errors\"]}";
        var requestPath = "estados";
        var httpClient = new HttpClientStubBuilder()
                .setBaseUrl(baseUrl)
                .setRequestPath(requestPath)
                .setResponseBody(responseBody)
                .setStatusCode(statusCode)
                .build();
        var httpService = new HttpServiceImpl(baseUrl, httpClient);

        var exp = assertThrows(
                QueryException.class,
                () -> httpService.get(requestPath, StateResponse[].class));

        assertEquals(exp.getErrorCode(), ValidationStatus.INTEGRATION_ERROR.toString());
        assertTrue(exp.getMessage().contains(responseBody));
    }
}