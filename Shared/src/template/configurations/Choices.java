
package template.configurations;

import java.util.Map;

public class Choices {

    protected Map<String, Choice> choice;

    public Choices() {
    }

    public Choices(Map<String, Choice> choiceInit) {
        choice = choiceInit;
    }

    public Map<String, Choice> getChoice() {
        return choice;
    }

    public void setChoice(Map<String, Choice> choiceValue) {
        choice = choiceValue;
    }

}
