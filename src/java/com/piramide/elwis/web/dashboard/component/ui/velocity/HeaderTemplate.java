package com.piramide.elwis.web.dashboard.component.ui.velocity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : ivan
 */
public class HeaderTemplate implements Template {
    public StringBuilder merge(List<Map> elements, Map data) {
        String templatePath = "com/piramide/elwis/web/dashboard/component/ui/velocity/templates";
        String templateName = "headertemplate.vm";

        if (data.get("isDashboardBootstrapUI") != null && (Boolean) data.get("isDashboardBootstrapUI")) {
            templateName = "headertemplateBootstrap.vm";
        }

        Map templateElements = new HashMap();

        templateElements.putAll(data);
        templateElements.put("elements", elements);
        StringBuilder strBuilder = new StringBuilder(VelocityManager.processTemplate(templatePath, templateName, templateElements));
        return strBuilder;
    }
}
