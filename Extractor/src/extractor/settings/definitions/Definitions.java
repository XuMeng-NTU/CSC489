
package extractor.settings.definitions;

import java.nio.file.Path;
import java.util.Map;
import util.jaxb.LoadingHelper;

public class Definitions {

    protected Map<String, Definition> definition;

    public Definitions() {
    }

    public Definitions(Map<String, Definition> definitionInit) {
        definition = definitionInit;
    }

    public Map<String, Definition> getDefinition() {
        return definition;
    }

    public void setDefinition(Map<String, Definition> definitionValue) {
        definition = definitionValue;
    }

    public static Definitions load(Path path) {
        return LoadingHelper.load(path, extractor.settings.definitions.xml.Definitions.class, Definitions.class);
    }

    public static void dump(Path path, Definitions object) {
        LoadingHelper.dump(path, extractor.settings.definitions.xml.Definitions.class, Definitions.class, object);
    }

}
