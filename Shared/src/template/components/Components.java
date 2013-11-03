
package template.components;

import java.nio.file.Path;
import java.util.Map;
import util.jaxb.LoadingHelper;

public class Components {

    protected Map<String, Component> component;

    public Components() {
    }

    public Components(Map<String, Component> componentInit) {
        component = componentInit;
    }

    public Map<String, Component> getComponent() {
        return component;
    }

    public void setComponent(Map<String, Component> componentValue) {
        component = componentValue;
    }

    public static Components load(Path path) {
        return LoadingHelper.load(path, template.components.xml.Components.class, Components.class);
    }

    public static void dump(Path path, Components object) {
        LoadingHelper.dump(path, template.components.xml.Components.class, Components.class, object);
    }

}
