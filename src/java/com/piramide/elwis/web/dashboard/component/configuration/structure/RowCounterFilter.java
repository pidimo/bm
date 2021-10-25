package com.piramide.elwis.web.dashboard.component.configuration.structure;

/**
 * @author ivan
 */
public class RowCounterFilter extends ConfigurableFilter implements Prototype {
    private String numberOfElements = "0";

    public RowCounterFilter(String name, String resourceKey) {
        super(name);
        setView(com.piramide.elwis.web.dashboard.component.util.Constant.VIEW_TEXT);
        setDataType(com.piramide.elwis.web.dashboard.component.util.Constant.TYPE_INTEGER);
        setResourceKey(resourceKey);
    }

    public String getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(String numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public Object clone() {
        RowCounterFilter clone = (RowCounterFilter) super.clone();
        if (null != clone) {
            clone.setNumberOfElements(numberOfElements);
        }
        return clone;
    }
}
