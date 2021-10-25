package com.piramide.elwis.web.dashboard.conf;

import com.piramide.elwis.web.dashboard.component.configuration.structure.Component;
import com.piramide.elwis.web.dashboard.component.ui.Converter;
import com.piramide.elwis.web.dashboard.component.ui.velocity.ResourceBundleManager;

import java.util.Map;

/**
 * @author : ivan
 *         Date: Sep 25, 2006
 *         Time: 12:08:53 PM
 */
public class InboxSizeDashboardConverter implements Converter {
    public Object convert(Object object, Map map, ResourceBundleManager resourceBundleManager, Map map1, Component component) {

        String str = "";
        int size = new Integer(object.toString());
        if (size < 1024) {
            str = "1" + " " + resourceBundleManager.getMessage("Webmail.mailTray.Kb");
        } else {
            int v = (size / 1024);
            str = v + " " + resourceBundleManager.getMessage("Webmail.mailTray.Kb");
        }

        return str;
    }
}
