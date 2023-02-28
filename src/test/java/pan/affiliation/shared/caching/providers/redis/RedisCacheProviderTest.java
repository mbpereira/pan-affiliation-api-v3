package pan.affiliation.shared.caching.providers.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pan.affiliation.infrastructure.gateways.ibge.contracts.CityResponse;
import pan.affiliation.shared.caching.CacheProvider;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static org.junit.jupiter.api.Assertions.*;

public class RedisCacheProviderTest {
    private final RedisConnectionFactory redisConnectionFactory;
    private final RedisCommands<String, String> commands;
    private final Faker faker = new Faker();
    private final ObjectMapper mapper = new ObjectMapper()
            .disable(FAIL_ON_UNKNOWN_PROPERTIES);

    public RedisCacheProviderTest() {
        var connection = Mockito.mock(StatefulRedisConnection.class);
        this.redisConnectionFactory = Mockito.mock(RedisConnectionFactory.class);
        //noinspection unchecked
        this.commands = Mockito.mock(RedisCommands.class);
        //noinspection unchecked
        Mockito.when(this.redisConnectionFactory.create())
                .thenReturn(connection);
        Mockito.when(connection.sync())
                .thenReturn(this.commands);
    }

    @Test
    public void get_shouldReturnNullIfKeyDoesNotExists() {
        Mockito.when(this.commands.get(Mockito.any()))
                .thenReturn(null);
        var provider = getCacheProvider();

        var result = provider.get("key", String.class);

        assertTrue(result.isEmpty());
    }

    @SneakyThrows
    private<T> String toJson(T data) {
        return this.mapper.writeValueAsString(data);
    }

    @Test
    public void get_shouldReturnValue() {
        var city = getCity();
        Mockito.when(this.commands.get(Mockito.any()))
                .thenReturn(toJson(city));
        var provider = getCacheProvider();

        var result = provider.get("key", CityResponse.class);

        assertTrue(result.isPresent());
        assertEquals(city.getId(), result.get().getId());
    }

    @Test
    public void getMany_shouldReturnEmptyListIfKeyDoesNotExists() {
        Mockito.when(this.commands.smembers(Mockito.any()))
                .thenReturn(new HashSet<>());
        var provider = getCacheProvider();

        var result = provider.getMany("key", CityResponse.class);

        assertTrue(result.isEmpty());
    }

    @Test
    public void getMany_shouldReturnAllValuesIfKeyExists() {
        var cities = Set.of(toJson(getCity()), toJson(getCity()));
        Mockito.when(this.commands.smembers(Mockito.any()))
                .thenReturn(cities);
        var provider = getCacheProvider();

        var result = provider.getMany("key", CityResponse.class);

        assertFalse(result.isEmpty());
    }

    @Test
    public void setMany_shouldReturnNumberOfSavedItems() {
        var cities = List.of(getCity(), getCity());
        var provider = getCacheProvider();
        Mockito.when(this.commands.sadd(Mockito.any(), Mockito.any(String[].class)))
                .thenReturn(Long.valueOf(cities.size()));

        var result = provider.setMany("key", cities);

        assertEquals(cities.size(), result);
    }

    @Test
    public void set_shouldReturnTrue() {
        var value = this.faker.lorem().word();
        var provider = getCacheProvider();
        Mockito.when(this.commands.set(Mockito.any(), Mockito.any()))
                .thenReturn(value);

        var result = provider.set("key", value);

        assertTrue(result);
    }
    private CityResponse getCity() {
        return new CityResponse(this.faker.random().nextInt(0, 10), this.faker.address().city());
    }

    public CacheProvider getCacheProvider() {
        return new RedisCacheProvider(this.redisConnectionFactory);
    }
}