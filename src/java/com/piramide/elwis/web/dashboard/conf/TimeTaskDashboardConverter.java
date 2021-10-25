package com.piramide.elwis.web.dashboard.conf;

import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.dashboard.component.configuration.structure.Component;
import com.piramide.elwis.web.dashboard.component.ui.Converter;
import com.piramide.elwis.web.dashboard.component.ui.velocity.ResourceBundleManager;
import org.joda.time.DateTimeZone;

import java.util.Map;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public class TimeTaskDashboardConverter implements Converter {
    public Object convert(Object object,
                          Map map,
                          ResourceBundleManager resources,
                          Map row,
                          Component component) {
        DateTimeZone timeZoneId = (DateTimeZone) map.get("timeZone");

        String c = "";
        if (null != object && !"".equals(object.toString())) {
            Long dateTimeLong = new Long(object.toString());
            c = DateUtils.getFormattedDateTimeWithTimeZone(dateTimeLong, timeZoneId, resources.getMessage("timePattern"));
        }
        return c;
    }
}
