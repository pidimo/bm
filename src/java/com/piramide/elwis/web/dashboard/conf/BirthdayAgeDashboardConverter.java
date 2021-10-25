package com.piramide.elwis.web.dashboard.conf;

import com.piramide.elwis.web.dashboard.component.configuration.structure.Component;
import com.piramide.elwis.web.dashboard.component.ui.Converter;
import com.piramide.elwis.web.dashboard.component.ui.velocity.ResourceBundleManager;

import java.util.Map;

/**
 * @author : ivan
 *         Date: Sep 8, 2006
 *         Time: 3:19:56 PM
 */
public class BirthdayAgeDashboardConverter implements Converter {
    public Object convert(Object object,
                          Map map,
                          ResourceBundleManager resourceBundleManager,
                          Map map1,
                          Component xmlComponent) {

        String ageAsString = "";
        if (object.toString().length() < 4) {
            ageAsString = object.toString();
        }
        return ageAsString;
    }

}
