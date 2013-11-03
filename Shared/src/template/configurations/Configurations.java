
package template.configurations;

import java.nio.file.Path;
import java.util.Map;
import util.jaxb.LoadingHelper;

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

    public static Configurations load(Path path) {
        return LoadingHelper.load(path, template.configurations.xml.Configurations.class, Configurations.class);
    }

    public static void dump(Path path, Configurations object) {
        LoadingHelper.dump(path, template.configurations.xml.Configurations.class, Configurations.class, object);
    }

}
