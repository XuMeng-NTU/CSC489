
package template.registry;

import java.util.Map;

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

}
