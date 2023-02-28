package pan.affiliation.shared.caching.providers.redis;

import io.lettuce.core.api.StatefulRedisConnection;

public interface RedisConnectionFactory {
    StatefulRedisConnection<String, String> create();
}
