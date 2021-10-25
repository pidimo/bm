package com.piramide.elwis.web.dashboard.conf;

import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.dashboard.component.configuration.structure.Component;
import com.piramide.elwis.web.dashboard.component.ui.Converter;
import com.piramide.elwis.web.dashboard.component.ui.velocity.ResourceBundleManager;
import org.joda.time.DateTimeZone;

import java.util.Map;

/**
 * @author : ivan
 *         Date: Sep 7, 2006
 *         Time: 7:02:45 PM
 */
public class LongToDateDashboardConverter implements Converter {
    public Object convert(Object object, Map map, ResourceBundleManager resources, Map map1, Component xmlComponent) {
        Long dateInMillis = new Long(object.toString());
        DateTimeZone timeZoneId = (DateTimeZone) map.get("timeZone");

        String f = DateUtils.getFormattedDateTimeWithTimeZone(dateInMillis,
                timeZoneId, resources.getMessage("dateTimePattern"));
        return f;
    }
}
