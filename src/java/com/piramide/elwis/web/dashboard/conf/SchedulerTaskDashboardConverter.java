package com.piramide.elwis.web.dashboard.conf;

import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.dashboard.component.configuration.structure.Component;
import com.piramide.elwis.web.dashboard.component.ui.Converter;
import com.piramide.elwis.web.dashboard.component.ui.velocity.ResourceBundleManager;
import org.joda.time.DateTimeZone;

import java.util.Date;
import java.util.Map;

/**
 * @author : ivan
 *         Date: Sep 12, 2006
 *         Time: 11:39:42 AM
 */
public class SchedulerTaskDashboardConverter implements Converter {
    public Object convert(Object object,
                          Map map,
                          ResourceBundleManager resources,
                          Map row,
                          Component component) {
        DateTimeZone timeZoneId = (DateTimeZone) map.get("timeZone");
        String startDateTime = component.getColumn(7).getName();
        String expireDateTime = component.getColumn(8).getName();

        String startDate = component.getColumn(2).getName();
        String expireDate = component.getColumn(3).getName();

        Object startDateTimeValue = row.get(startDateTime);
        Object expireDateTimeValue = row.get(expireDateTime);


        Object startDateValue = row.get(startDate);
        Object expireDateValue = row.get(expireDate);

        String c = "";
        if (object.equals(startDateValue)) {
            if (null != startDateTimeValue && !"".equals(startDateTimeValue.toString())) {
                Long l = new Long(startDateTimeValue.toString());
                c = DateUtils.parseDate(l, resources.getMessage("datePattern"), timeZoneId.getID());
            } else {
                Integer date = new Integer(object.toString());
                Date d = DateUtils.integerToDate(date);
                c = resources.formatColumn(d, resources.getMessage("datePattern"));
            }
        } else if (object.equals(expireDateValue)) {
            if (null != expireDateTimeValue && !"".equals(expireDateTimeValue.toString())) {
                Long l = new Long(expireDateTimeValue.toString());
                c = DateUtils.getFormattedDateTimeWithTimeZone(l, timeZoneId, resources.getMessage("datePattern"));
            } else {
                Integer date = new Integer(object.toString());
                Date d = DateUtils.integerToDate(date);
                c = resources.formatColumn(d, resources.getMessage("datePattern"));
            }
        }
        return c;
    }
}
