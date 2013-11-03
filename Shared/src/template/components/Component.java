
package template.components;


public class Component {

    protected String name;
    protected Class repr;
    protected Parameters parameters;

    public Component() {
    }

    public Component(String nameInit, Class reprInit, Parameters parametersInit) {
        name = nameInit;
        repr = reprInit;
        parameters = parametersInit;
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

    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parametersValue) {
        parameters = parametersValue;
    }

}
