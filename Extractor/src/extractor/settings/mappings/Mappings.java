
package extractor.settings.mappings;

import java.nio.file.Path;
import java.util.Map;
import util.jaxb.LoadingHelper;

public class Mappings {

    protected Map<String, Mapping> mapping;

    public Mappings() {
    }

    public Mappings(Map<String, Mapping> mappingInit) {
        mapping = mappingInit;
    }

    public Map<String, Mapping> getMapping() {
        return mapping;
    }

    public void setMapping(Map<String, Mapping> mappingValue) {
        mapping = mappingValue;
    }

    public static Mappings load(Path path) {
        return LoadingHelper.load(path, extractor.settings.mappings.xml.Mappings.class, Mappings.class);
    }

    public static void dump(Path path, Mappings object) {
        LoadingHelper.dump(path, extractor.settings.mappings.xml.Mappings.class, Mappings.class, object);
    }

}
