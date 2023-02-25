package pan.affiliation.shared.environment.helpers;

import org.mockito.Mockito;
import pan.affiliation.shared.environment.PropertiesReader;

import java.util.HashMap;
import java.util.Map;

public class PropertiesReaderStubBuilder {
    private final Map<String, String> propertiesMap = new HashMap<>();

    public PropertiesReaderStubBuilder addProp(String key, String value) {
        this.propertiesMap.put(key, value);
        return this;
    }

    public PropertiesReader build() {
        var propertiesReader = Mockito.mock(PropertiesReader.class);
        this.propertiesMap.forEach((key, value) -> {
            Mockito.when(propertiesReader.get(key)).thenReturn(value);
            Mockito.when(propertiesReader.get(key, value)).thenReturn(value);
        });
        return propertiesReader;
    }
}
