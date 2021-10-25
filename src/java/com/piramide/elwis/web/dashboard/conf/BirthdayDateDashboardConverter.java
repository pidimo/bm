package com.piramide.elwis.web.dashboard.conf;


import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.dashboard.component.configuration.structure.Component;
import com.piramide.elwis.web.dashboard.component.ui.Converter;
import com.piramide.elwis.web.dashboard.component.ui.velocity.ResourceBundleManager;

import java.util.Date;
import java.util.Map;

/**
 * @author: ivan
 * Date: 25-11-2006: 03:07:46 PM
 */
public class BirthdayDateDashboardConverter implements Converter {
    public Object convert(Object object, Map map, ResourceBundleManager resources, Map map1, Component component) {
        Integer date = new Integer(map1.get("birtdayComplete").toString());
        Date d = DateUtils.integerToDate(date);

        String pattern = resources.getMessage("datePattern");
        if (date.toString().length() < 8) {
            pattern = resources.getMessage("withoutYearPattern");
        }
        return resources.formatColumn(d, pattern);
    }
}
