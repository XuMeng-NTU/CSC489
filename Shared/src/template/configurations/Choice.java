
package template.configurations;


public class Choice {

    protected String name;
    protected String cluster;
    protected String worker;
    protected Parameters parameters;

    public Choice() {
    }

    public Choice(String nameInit, String clusterInit, String workerInit, Parameters parametersInit) {
        name = nameInit;
        cluster = clusterInit;
        worker = workerInit;
        parameters = parametersInit;
    }

    public String getName() {
        return name;
    }

    public void setName(String nameValue) {
        name = nameValue;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String clusterValue) {
        cluster = clusterValue;
    }

    public String getWorker() {
        return worker;
    }

    public void setWorker(String workerValue) {
        worker = workerValue;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parametersValue) {
        parameters = parametersValue;
    }

}
