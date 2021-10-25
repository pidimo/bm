package com.piramide.elwis.web.dashboard.conf;

import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.dashboard.component.configuration.structure.Component;
import com.piramide.elwis.web.dashboard.component.ui.Converter;
import com.piramide.elwis.web.dashboard.component.ui.velocity.ResourceBundleManager;

import java.util.Date;
import java.util.Map;

/**
 * @author : ivan
 *         Date: Sep 5, 2006
 *         Time: 4:22:18 PM
 */
public class IntegerToDateDashboardConverter implements Converter {
    public Object convert(Object object, Map map, ResourceBundleManager resources, Map map1, Component xmlComponent) {
        Integer date = new Integer(object.toString());
        Date d = DateUtils.integerToDate(date);

        String pattern = resources.getMessage("datePattern");
        if (object.toString().length() < 8) {
            pattern = resources.getMessage("withoutYearPattern");
        }
        return resources.formatColumn(d, pattern);
    }
}
