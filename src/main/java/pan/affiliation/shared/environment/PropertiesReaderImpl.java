package pan.affiliation.shared.environment;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class PropertiesReaderImpl implements PropertiesReader {
    private final Environment environment;

    public PropertiesReaderImpl(Environment environment) {
        this.environment = environment;
    }

    @Override
    public String get(String key, String defaultValue) {
        return this.environment.getProperty(key, defaultValue);
    }

    @Override
    public String get(String key) {
        return this.environment.getProperty(key);
    }
}
