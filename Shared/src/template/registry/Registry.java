
package template.registry;

import java.nio.file.Path;
import util.jaxb.LoadingHelper;

public class Registry {

    protected Configurations configurations;
    protected Components components;
    protected Data data;

    public Registry() {
    }

    public Registry(Configurations configurationsInit, Components componentsInit, Data dataInit) {
        configurations = configurationsInit;
        components = componentsInit;
        data = dataInit;
    }

    public Configurations getConfigurations() {
        return configurations;
    }

    public void setConfigurations(Configurations configurationsValue) {
        configurations = configurationsValue;
    }

    public Components getComponents() {
        return components;
    }

    public void setComponents(Components componentsValue) {
        components = componentsValue;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data dataValue) {
        data = dataValue;
    }

    public static Registry load(Path path) {
        return LoadingHelper.load(path, template.registry.xml.Registry.class, Registry.class);
    }

    public static void dump(Path path, Registry object) {
        LoadingHelper.dump(path, template.registry.xml.Registry.class, Registry.class, object);
    }

}
