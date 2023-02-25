package pan.affiliation.shared.environment;

import org.springframework.stereotype.Component;
import pan.affiliation.AffiliationApplication;

import java.io.IOException;
import java.util.Properties;

@Component
public class PropertiesReaderImpl implements PropertiesReader {
    private final Properties properties = new Properties();

    public PropertiesReaderImpl() {
        load();
    }

    private void load() {
        try {
            properties.load(AffiliationApplication.class.getClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException e) {
        }
    }

    @Override
    public String get(String key, String defaultValue) {
        return this.properties.getProperty(key, defaultValue);
    }

    @Override
    public String get(String key) {
        return this.properties.getProperty(key);
    }
}
