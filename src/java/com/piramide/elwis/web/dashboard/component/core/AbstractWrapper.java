package com.piramide.elwis.web.dashboard.component.core;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : ivan
 */
public abstract class AbstractWrapper {
    private Map element = new HashMap();

    public void put(String key, Object value) {
        element.put(key, value);
    }

    public Object get(String key) {
        return element.get(key);
    }
}
