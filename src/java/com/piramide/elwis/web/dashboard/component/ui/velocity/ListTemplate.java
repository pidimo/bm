package com.piramide.elwis.web.dashboard.component.ui.velocity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : ivan
 */
public class ListTemplate implements Template {
    public StringBuilder merge(List<Map> elements, Map data) {
        String templatePath = "com/piramide/elwis/web/dashboard/component/ui/velocity/templates";
        String templateName = "listTemplate.vm";

        if (data.get("isDashboardBootstrapUI") != null && (Boolean) data.get("isDashboardBootstrapUI")) {
            templateName = "listTemplateBootstrap.vm";
        }

        Map templateElements = new HashMap();

        templateElements.putAll(data);
        templateElements.put("elements", elements);
        StringBuilder strBuilder = new StringBuilder(VelocityManager.processTemplate(templatePath, templateName, templateElements));
        return strBuilder;
    }
}
