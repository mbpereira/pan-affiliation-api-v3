package pan.affiliation.infrastructure.gateways.ibge;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import pan.affiliation.domain.modules.localization.queries.GetCitiesFromStatesQueryHandler;
import pan.affiliation.infrastructure.shared.http.helpers.HttpClientStubBuilder;
import pan.affiliation.infrastructure.shared.http.helpers.HttpGatewayServiceFactory;
import pan.affiliation.infrastructure.shared.http.helpers.ServiceCreator;
import pan.affiliation.shared.environment.PropertiesReader;
import pan.affiliation.shared.environment.helpers.PropertiesReaderStubBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IbgeGatewayServiceTest {

    @SneakyThrows
    @Test
    public void getStates_shouldReturnParsedStates() {
        var baseUrl = "https://servicodados.ibge.gov.br/api/v1/localidades";
        var requestPath = "estados";
        var responseBody = "[{\"id\":11,\"sigla\":\"RO\",\"nome\":\"Rondônia\"}]";
        var statusCode = 200;
        var gatewayService = getGatewayService(
                baseUrl,
                requestPath,
                responseBody,
                statusCode,
                factory -> new IbgeGatewayService(factory, getPropertiesReader(baseUrl))
        );

        var states = gatewayService.getCountryStates();

        assertEquals("RO", states.get(0).getAcronym());
        assertEquals("Rondônia", states.get(0).getName());
        assertEquals(11, states.get(0).getId());
    }

    @SneakyThrows
    @Test
    public void getCitiesFromState_shouldReturnParsedCities() {
        var baseUrl = "https://servicodados.ibge.gov.br/api/v1/localidades";
        var requestPath = "estados/51/municipios";
        var requestbody = "[{\"id\":11,\"nome\":\"Cuiabá\"}]";
        var statusCode = 200;
        GetCitiesFromStatesQueryHandler gatewayService = getGatewayService(
                baseUrl,
                requestPath,
                requestbody,
                statusCode,
                factory -> new IbgeGatewayService(factory, getPropertiesReader(baseUrl)));

        var cities = gatewayService.getCitiesFromState(51);

        assertEquals("Cuiabá", cities.get(0).getName());
        assertEquals(11, cities.get(0).getId());
    }

    private static<T> T getGatewayService(
            String baseUrl,
            String requestPath,
            String requestBody,
            int statusCode,
            ServiceCreator<T> createService) {
        var httpClient = new HttpClientStubBuilder()
                .setBaseUrl(baseUrl)
                .setRequestPath(requestPath)
                .setResponseBody(requestBody)
                .setStatusCode(statusCode)
                .build();
        var gatewayFactory = new HttpGatewayServiceFactory<>(
                createService,
                httpClient);
        return gatewayFactory.create(baseUrl);
    }

    private static PropertiesReader getPropertiesReader(String baseUrl) {
        return new PropertiesReaderStubBuilder()
                .addProp("ibge.baseurl", baseUrl)
                .build();
    }
}