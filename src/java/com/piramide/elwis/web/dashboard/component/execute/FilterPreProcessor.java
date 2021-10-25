package com.piramide.elwis.web.dashboard.component.execute;

import com.piramide.elwis.web.dashboard.component.configuration.structure.Component;

import java.util.Map;

/**
 * @author : ivan
 */
public abstract class FilterPreProcessor {
    public abstract void processConfigurableFilter(Component cloneComponent, Map params);
}
