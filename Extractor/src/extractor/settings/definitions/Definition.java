
package extractor.settings.definitions;


public class Definition {

    protected String name;
    protected String desc;
    protected int size;
    protected Class repr;
    protected boolean reverse;
    protected boolean skip;

    public Definition() {
    }

    public Definition(String nameInit, String descInit, int sizeInit, Class reprInit, boolean reverseInit, boolean skipInit) {
        name = nameInit;
        desc = descInit;
        size = sizeInit;
        repr = reprInit;
        reverse = reverseInit;
        skip = skipInit;
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

    public int getSize() {
        return size;
    }

    public void setSize(int sizeValue) {
        size = sizeValue;
    }

    public Class getRepr() {
        return repr;
    }

    public void setRepr(Class reprValue) {
        repr = reprValue;
    }

    public boolean getReverse() {
        return reverse;
    }

    public void setReverse(boolean reverseValue) {
        reverse = reverseValue;
    }

    public boolean getSkip() {
        return skip;
    }

    public void setSkip(boolean skipValue) {
        skip = skipValue;
    }

}
