
package extractor.settings.mappings;


public class Mapping {

    protected String name;
    protected String desc;
    protected String symbol;
    protected Class repr;

    public Mapping() {
    }

    public Mapping(String nameInit, String descInit, String symbolInit, Class reprInit) {
        name = nameInit;
        desc = descInit;
        symbol = symbolInit;
        repr = reprInit;
    }

    public String getName() {
        return name;
    }

    public void setName(String nameValue) {
        name = nameValue;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String descValue) {
        desc = descValue;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbolValue) {
        symbol = symbolValue;
    }

    public Class getRepr() {
        return repr;
    }

    public void setRepr(Class reprValue) {
        repr = reprValue;
    }

}
