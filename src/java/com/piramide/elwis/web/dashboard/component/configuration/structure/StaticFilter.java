package com.piramide.elwis.web.dashboard.component.configuration.structure;

/**
 * @author : ivan
 */
public class StaticFilter extends Filter {
    private String value;
    private Boolean multipleValue = false;

    public StaticFilter(String name) {
        super(name);
    }

    public StaticFilter(String name, String value) {
        super(name);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getMultipleValue() {
        return multipleValue;
    }

    public void setMultipleValue(Boolean multipleValue) {
        this.multipleValue = multipleValue;
    }

    public String toString() {
        return "name=" + getName() + " value=" + getValue();
    }


    public Object clone() {
        StaticFilter clone = null;
        clone = (StaticFilter) super.clone();

        if (null != clone) {
            clone.setValue(value);
            clone.setMultipleValue(multipleValue);
        }

        return clone;
    }
}
