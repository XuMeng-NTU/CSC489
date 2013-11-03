
package template.registry;


public class Configuration {

    protected Resources resources;
    protected String name;
    protected Class repr;

    public Configuration() {
    }

    public Configuration(Resources resourcesInit, String nameInit, Class reprInit) {
        resources = resourcesInit;
        name = nameInit;
        repr = reprInit;
    }

    public Resources getResources() {
        return resources;
    }

    public void setResources(Resources resourcesValue) {
        resources = resourcesValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String nameValue) {
        name = nameValue;
    }

    public Class getRepr() {
        return repr;
    }

    public void setRepr(Class reprValue) {
        repr = reprValue;
    }

}
