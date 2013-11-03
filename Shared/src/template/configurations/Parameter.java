
package template.configurations;


public class Parameter {

    protected String name;
    protected String value;

    public Parameter() {
    }

    public Parameter(String nameInit, String valueInit) {
        name = nameInit;
        value = valueInit;
    }

    public String getName() {
        return name;
    }

    public void setName(String nameValue) {
        name = nameValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String valueValue) {
        value = valueValue;
    }

}
