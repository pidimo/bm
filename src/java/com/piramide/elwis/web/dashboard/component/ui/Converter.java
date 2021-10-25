package com.piramide.elwis.web.dashboard.component.ui;

import com.piramide.elwis.web.dashboard.component.configuration.structure.Component;
import com.piramide.elwis.web.dashboard.component.ui.velocity.ResourceBundleManager;

import java.util.Map;

/**
 * @author : ivan
 */
public interface Converter {
    public Object convert(Object value, Map params, ResourceBundleManager resources, Map row, Component xmlComponent);
}
