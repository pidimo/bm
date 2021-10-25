package com.piramide.elwis.web.dashboard.component.web.util;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author : ivan
 */
public class TemplateResourceBundleFactory {
    private List<TemplateResourceBundle> cache = new ArrayList<TemplateResourceBundle>();

    public static TemplateResourceBundleFactory i = new TemplateResourceBundleFactory();

    private TemplateResourceBundleFactory() {
    }

    public TemplateResourceBundle getTemplateResourceBundle(HttpServletRequest request,
                                                            String applicationResourcesPath) {
        if (cache.isEmpty()) {
            TemplateResourceBundle e = new TemplateResourceBundle(request, applicationResourcesPath);
            cache.add(e);
        }
        return cache.get(0);
    }

    public TemplateResourceBundle getTemplateResourceBundle(String applicationResourcesPath, Locale locale) {

        for (TemplateResourceBundle element : cache) {
            if (element.getLocale().equals(locale)) {
                return element;
            }
        }

        TemplateResourceBundle bundle;
        bundle = new TemplateResourceBundle(applicationResourcesPath, locale);
        cache.add(bundle);
        return bundle;
    }
}
