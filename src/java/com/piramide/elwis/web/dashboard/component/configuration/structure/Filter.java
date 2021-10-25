package com.piramide.elwis.web.dashboard.component.configuration.structure;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author : ivan
 */
public class Filter implements Prototype {
    private String name;
    private Log log = LogFactory.getLog(this.getClass());

    public Filter(String name) {
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Object clone() {
        Filter clone = null;
        try {
            clone = (Filter) super.clone();
        } catch (CloneNotSupportedException e) {
            log.error("Cannot clone 'Filter' object ", e);
        }
        return clone;
    }
}
