
package template.metadata;

import java.nio.file.Path;
import java.util.Map;
import util.jaxb.LoadingHelper;

public class Metadata {

    protected Map<String, Meta> meta;

    public Metadata() {
    }

    public Metadata(Map<String, Meta> metaInit) {
        meta = metaInit;
    }

    public Map<String, Meta> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, Meta> metaValue) {
        meta = metaValue;
    }

    public static Metadata load(Path path) {
        return LoadingHelper.load(path, template.metadata.xml.Metadata.class, Metadata.class);
    }

    public static void dump(Path path, Metadata object) {
        LoadingHelper.dump(path, template.metadata.xml.Metadata.class, Metadata.class, object);
    }

}
