
package template.configurations;


public class Configuration {

    protected String name;
    protected Class repr;
    protected Choices choices;

    public Configuration() {
    }

    public Configuration(String nameInit, Class reprInit, Choices choicesInit) {
        name = nameInit;
        repr = reprInit;
        choices = choicesInit;
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

    public Choices getChoices() {
        return choices;
    }

    public void setChoices(Choices choicesValue) {
        choices = choicesValue;
    }

}
