package com.piramide.elwis.web.dashboard.component.configuration.structure;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author : ivan
 */
public class Constant implements Prototype {
    private Log log = LogFactory.getLog(this.getClass());
    private String resourceKey;
    private String value;

    public Constant() {
    }

    public String getResourceKey() {
        return resourceKey;
    }

    public void setResourceKey(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public Object clone() {
        Constant clone = null;
        try {
            clone = (Constant) super.clone();
        } catch (CloneNotSupportedException e) {
            log.error("Cannot clone 'Constant' object ", e);
        }

        return clone;
    }

    public String toString() {
        return "(value=" + value + " resourceKey=" + resourceKey + ")";
    }
}
