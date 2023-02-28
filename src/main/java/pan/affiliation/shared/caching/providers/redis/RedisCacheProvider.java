package pan.affiliation.shared.caching.providers.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.api.StatefulRedisConnection;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pan.affiliation.shared.caching.CacheProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

@Component
@Scope("singleton")
public class RedisCacheProvider implements CacheProvider {
    private final ObjectMapper mapper = new ObjectMapper().disable(FAIL_ON_UNKNOWN_PROPERTIES);
    private final StatefulRedisConnection<String, String> connection;

    public RedisCacheProvider(RedisConnectionFactory factory) {
        connection = factory.create();
    }

    @Override
    public <T> Optional<T> get(String key, Class<T> clazz) {
        var value = this.connection.sync().get(key);

        if (value == null) return Optional.empty();

        return fromJson(value, clazz);
    }

    @Override
    public <T> Boolean set(String key, T data) {
        var value = toJson(data);
        if (value.isEmpty()) return false;
        this.connection.sync().set(key, value.get());
        return true;
    }

    @Override
    public <T> Long setMany(String key, List<T> items) {
        var values = new ArrayList<String>();

        for (var item : items) {
            var value = toJson(item);

            if (value.isEmpty()) return 0L;

            values.add(value.get());
        }

        return this.connection.sync().sadd(key, values.toArray(String[]::new));
    }

    @Override
    public <T> List<T> getMany(String key, Class<T> clazz) {
        var items = this.connection.sync().smembers(key);
        var values = new ArrayList<T>();

        for (var item : items) {
            var data = this.fromJson(item, clazz);
            if (data.isEmpty()) continue;
            values.add(data.get());
        }

        return values;
    }

    private <T> Optional<String> toJson(T data) {
        try {
            var value = this.mapper.writeValueAsString(data);
            return Optional.of(value);
        } catch (Exception ignored) {
            return Optional.empty();
        }
    }

    private <T> Optional<T> fromJson(String value, Class<T> clazz) {
        try {
            return Optional.of(this.mapper.readValue(value, clazz));
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }
}
