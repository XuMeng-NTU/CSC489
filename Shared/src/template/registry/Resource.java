
package template.registry;

import java.nio.file.Path;

public class Resource {

    protected Path path;
    protected String name;

    public Resource() {
    }

    public Resource(Path pathInit, String nameInit) {
        path = pathInit;
        name = nameInit;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path pathValue) {
        path = pathValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String nameValue) {
        name = nameValue;
    }

}
