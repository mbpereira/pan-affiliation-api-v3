package pan.affiliation.shared.environment;

public interface PropertiesReader {
    String get(String key, String defaultValue);
    String get(String key);
}
