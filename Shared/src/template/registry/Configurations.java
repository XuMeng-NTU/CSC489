
package template.registry;

import java.util.Map;

public class Configurations {

    protected Map<String, Configuration> configuration;

    public Configurations() {
    }

    public Configurations(Map<String, Configuration> configurationInit) {
        configuration = configurationInit;
    }

    public Map<String, Configuration> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Map<String, Configuration> configurationValue) {
        configuration = configurationValue;
    }

}
