package com.piramide.elwis.web.dashboard.component.configuration.structure;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author: ivan
 */
public class Condition implements Prototype {
    private String type;
    private String operator;
    private String value;
    private String style;

    private Log log = LogFactory.getLog(this.getClass());

    public Condition() {
    }


    public Condition(String type, String operator, String value, String style) {
        this.type = type;
        this.operator = operator;
        this.value = value;
        this.style = style;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }


    public Object clone() {
        Condition clone = null;
        try {
            clone = (Condition) super.clone();
        } catch (CloneNotSupportedException e) {
            log.error("Cannot clone 'Condition' object ", e);
        }
        return clone;
    }
}
