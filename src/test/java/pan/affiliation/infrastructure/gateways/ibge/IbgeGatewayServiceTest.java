package pan.affiliation.infrastructure.gateways.ibge;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pan.affiliation.domain.modules.localization.queries.GetCitiesFromStatesQueryHandler;
import pan.affiliation.infrastructure.gateways.ibge.contracts.CityResponse;
import pan.affiliation.infrastructure.gateways.ibge.contracts.StateResponse;
import pan.affiliation.infrastructure.shared.http.helpers.HttpClientStubBuilder;
import pan.affiliation.infrastructure.shared.http.helpers.HttpGatewayServiceFactory;
import pan.affiliation.infrastructure.shared.http.helpers.ServiceCreator;
import pan.affiliation.shared.caching.CacheProvider;
import pan.affiliation.shared.environment.PropertiesReader;
import pan.affiliation.shared.environment.helpers.PropertiesReaderStubBuilder;

import java.util.List;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class IbgeGatewayServiceTest {
    private final CacheProvider cacheProvider;
    private final ObjectMapper mapper = new ObjectMapper()
            .disable(FAIL_ON_UNKNOWN_PROPERTIES);

    public IbgeGatewayServiceTest() {
        cacheProvider = Mockito.mock(CacheProvider.class);
    }

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
                factory -> new IbgeGatewayService(factory, getPropertiesReader(baseUrl), cacheProvider)
        );

        var states = gatewayService.getCountryStates();

        assertEquals("RO", states.get(0).getAcronym());
        assertEquals("Rondônia", states.get(0).getName());
        assertEquals(11, states.get(0).getId());
    }

    @SneakyThrows
    @Test
    public void getStates_shouldReturnCachedStates() {
        var baseUrl = "https://servicodados.ibge.gov.br/api/v1/localidades";
        var requestPath = "estados";
        var responseBody = "[{\"id\":11,\"sigla\":\"RO\",\"nome\":\"Rondônia\"}]";
        var states = this.mapper.readValue(responseBody, StateResponse[].class);
        var statusCode = 200;
        var gatewayService = getGatewayService(
                baseUrl,
                requestPath,
                responseBody,
                statusCode,
                factory -> new IbgeGatewayService(factory, getPropertiesReader(baseUrl), cacheProvider)
        );
        Mockito.when(this.cacheProvider.getMany(Mockito.any(), Mockito.any()))
                .thenReturn(List.of(states));

        var result = gatewayService.getCountryStates();

        Mockito.verify(this.cacheProvider, Mockito.never())
                .setMany(Mockito.any(), Mockito.any());
        assertEquals("RO", result.get(0).getAcronym());
        assertEquals("Rondônia", result.get(0).getName());
        assertEquals(11, result.get(0).getId());
    }

    @SneakyThrows
    @Test
    public void getCitiesFromState_shouldReturnCachedCities() {
        var baseUrl = "https://servicodados.ibge.gov.br/api/v1/localidades";
        var requestPath = "estados/51/municipios";
        var responseBody = "[{\"id\":11,\"nome\":\"Cuiabá\"}]";
        var cities = this.mapper.readValue(responseBody, CityResponse[].class);
        var statusCode = 200;
        GetCitiesFromStatesQueryHandler gatewayService = getGatewayService(
                baseUrl,
                requestPath,
                responseBody,
                statusCode,
                factory -> new IbgeGatewayService(factory, getPropertiesReader(baseUrl), cacheProvider));
        Mockito.when(this.cacheProvider.getMany(Mockito.any(), Mockito.any()))
                .thenReturn(List.of(cities));

        var result = gatewayService.getCitiesFromState(51);

        Mockito.verify(this.cacheProvider, Mockito.never())
                .setMany(Mockito.any(), Mockito.any());
        assertEquals("Cuiabá", result.get(0).getName());
        assertEquals(11, result.get(0).getId());
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
                factory -> new IbgeGatewayService(factory, getPropertiesReader(baseUrl), cacheProvider));

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