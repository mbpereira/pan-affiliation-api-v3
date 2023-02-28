package pan.affiliation.shared.caching.providers.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pan.affiliation.shared.environment.PropertiesReader;

@Component
@Scope("singleton")
public class RedisConnectionFactoryImp implements RedisConnectionFactory {
    private final PropertiesReader reader;

    @Autowired
    public RedisConnectionFactoryImp(PropertiesReader reader) {
        this.reader = reader;
    }

    public StatefulRedisConnection<String, String> create() {
        var host = reader.get("redis.host");
        var port = Integer.parseInt(reader.get("redis.port"));
        var database = Integer.parseInt(reader.get("redis.database"));
        var connectionString = String.format("redis://%s:%d/%d", host, port, database);
        var redisClient = RedisClient.create(connectionString);
        return redisClient.connect();
    }
}
