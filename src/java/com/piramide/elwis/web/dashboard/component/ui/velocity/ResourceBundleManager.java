package com.piramide.elwis.web.dashboard.component.ui.velocity;

/**
 * @author : ivan
 */
public interface ResourceBundleManager {
    public String getMessage(String key);

    public String getMessage(String key, Object[] args);

    public String formatColumn(Object value, String pattern);

    public String calculateSizeColum(Object columnSize);
}
