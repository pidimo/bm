package com.piramide.elwis.web.dashboard.component.configuration.structure;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author : ivan
 */
public class Parameter implements Prototype {
    private Log log = LogFactory.getLog(this.getClass());
    private String name;
    private String value;
    private String columnId;

    public Parameter(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return "name=" + name + " value=" + value + " columnId=" + columnId;
    }

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }


    public Object clone() {
        Parameter clone = null;
        try {
            clone = (Parameter) super.clone();
        } catch (CloneNotSupportedException e) {
            log.error("Cannot clone 'Parameter' object ", e);
        }

        return clone;
    }
}
