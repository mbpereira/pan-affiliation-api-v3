package pan.affiliation.shared.caching;

import java.util.List;
import java.util.Optional;

public interface CacheProvider {
    <T> Optional<T> get(String key, Class<T> clazz);
    <T> Boolean set(String key, T data);
    <T> Long setMany(String key, List<T> data);
    <T> List<T> getMany(String key, Class<T> clazz);
}
