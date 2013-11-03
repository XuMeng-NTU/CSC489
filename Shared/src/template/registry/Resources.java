
package template.registry;

import java.util.Map;

public class Resources {

    protected Map<String, Resource> resource;

    public Resources() {
    }

    public Resources(Map<String, Resource> resourceInit) {
        resource = resourceInit;
    }

    public Map<String, Resource> getResource() {
        return resource;
    }

    public void setResource(Map<String, Resource> resourceValue) {
        resource = resourceValue;
    }

}
