
package template.configurations;

import java.util.Map;

public class Parameters {

    protected Map<String, Parameter> parameter;

    public Parameters() {
    }

    public Parameters(Map<String, Parameter> parameterInit) {
        parameter = parameterInit;
    }

    public Map<String, Parameter> getParameter() {
        return parameter;
    }

    public void setParameter(Map<String, Parameter> parameterValue) {
        parameter = parameterValue;
    }

}
