package pan.affiliation.infrastructure.gateways.ibge;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import pan.affiliation.infrastructure.shared.http.helpers.HttpClientStubBuilder;
import pan.affiliation.infrastructure.shared.http.helpers.HttpGatewayServiceFactory;
import pan.affiliation.shared.environment.helpers.PropertiesReaderStubBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IbgeGatewayServiceTest {

    @SneakyThrows
    @Test
    public void getStates_shouldReturnParsedStates() {
        var baseUrl = "https://servicodados.ibge.gov.br/api/v1/localidades";
        var requestPath = "estados";
        var httpClient = new HttpClientStubBuilder()
                .setBaseUrl(baseUrl)
                .setRequestPath(requestPath)
                .setResponseBody("[{\"id\":11,\"sigla\":\"RO\",\"nome\":\"Rondônia\"}]")
                .setStatusCode(200)
                .build();
        var propertiesReader = new PropertiesReaderStubBuilder()
                .addProp("ibge.baseurl", baseUrl)
                .build();
        var gatewayFactory = new HttpGatewayServiceFactory<>(
                (factory) -> new IbgeGatewayService(factory, propertiesReader),
                httpClient);
        var gatewayService = gatewayFactory.create(baseUrl);

        var states = gatewayService.getCountryStates();

        assertEquals("RO", states.get(0).getAcronym());
        assertEquals("Rondônia", states.get(0).getName());
        assertEquals(11, states.get(0).getId());
    }
}