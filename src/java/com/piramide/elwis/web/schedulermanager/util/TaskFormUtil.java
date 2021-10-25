package com.piramide.elwis.web.schedulermanager.util;

import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.RequestUtils;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ivan Alban
 * @version 4.4
 */
public class TaskFormUtil {
    public static final TaskFormUtil i = new TaskFormUtil();

    public static final Integer LAST_HOUR_OF_DAY = 24;

    private TaskFormUtil() {
    }

    @SuppressWarnings(value = "unchecked")
    public Map getDefaultValuesForCreate(HttpServletRequest request) {
        Map result = new HashMap();
        result.put("date", true);
        result.put("status", 2);
        result.put("percent", 0);

        return result;
    }

    @SuppressWarnings(value = "unchecked")
    public Map getDefaultValuesFromSessionUser(HttpServletRequest request) {
        User user = RequestUtils.getUser(request);

        DateTimeZone dateTimeZone = (DateTimeZone) user.getValue("dateTimeZone");

        Integer currentHour = DateUtils.getCurrentHour(dateTimeZone);

        Integer nextHour = currentHour + 1;
        if (LAST_HOUR_OF_DAY.equals(currentHour)) {
            nextHour = currentHour;
        }

        Map result = new HashMap();
        result.put("userId", user.getValue("userId"));
        result.put("userTaskId", user.getValue("userId"));

        result.put("createDateTime", DateUtils.createDate((new Date()).getTime(), dateTimeZone.getID()).getMillis());
        result.put("startHour", currentHour);
        result.put("expireHour", nextHour);

        return result;
    }
}
