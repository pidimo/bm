package com.piramide.elwis.web.dashboard.conf;

import com.piramide.elwis.web.dashboard.component.configuration.structure.Component;
import com.piramide.elwis.web.dashboard.component.configuration.structure.ConfigurableFilter;
import com.piramide.elwis.web.dashboard.component.configuration.structure.Parameter;
import com.piramide.elwis.web.dashboard.component.execute.FilterPreProcessor;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.*;

/**
 * @author : ivan
 *         Date: Sep 25, 2006
 *         Time: 4:04:05 PM
 */
public class ArticleFilterProcessor extends FilterPreProcessor {
    public void processConfigurableFilter(Component cloneComponent, Map params) {

        Parameter timeZoneParameter = cloneComponent.getParameter("timeZone");
        Parameter userTypeParameter = cloneComponent.getParameter("userType");
        Integer userType = -1;
        if (null != userTypeParameter) {
            userType = new Integer(userTypeParameter.getValue());
        }

        ConfigurableFilter filter = cloneComponent.getConfigurableFilter("numberOfDaysBefore");
        if (null != filter) {
            DateTimeZone timeZone;
            if (null != timeZoneParameter) {
                timeZone = DateTimeZone.forID(timeZoneParameter.getValue());
            } else {
                timeZone = DateTimeZone.getDefault();
            }
            int value = new Integer(filter.getInitialValue());
            Date now = new Date();
            List<Long> dateList = getDateX(now.getTime(), value, 0, timeZone);

            long firstDateAsLong = dateList.get(0);
            long secondDateAsLong = dateList.get(1);

            ConfigurableFilter firstDate = new ConfigurableFilter("creationDateFrom");
            firstDate.setInitialValue(String.valueOf(firstDateAsLong));

            ConfigurableFilter secondDate = new ConfigurableFilter("");
            secondDate.setInitialValue(String.valueOf(secondDateAsLong));

            cloneComponent.addConfigurableFilter(firstDate);
            cloneComponent.addConfigurableFilter(secondDate);
        }
    }

    private List<Long> getDateX(long now, int beforeValue, int afterValue, DateTimeZone timeZone) {

        Calendar v1 = Calendar.getInstance();
        v1.setTimeInMillis(now);
        v1.add(Calendar.DATE, (-1) * beforeValue);

        Calendar v2 = Calendar.getInstance();
        v2.setTimeInMillis(now);
        v2.add(Calendar.DATE, afterValue);

        DateTime before = new DateTime(v1.get(Calendar.YEAR), v1.get(Calendar.MONTH) + 1, v1.get(Calendar.DAY_OF_MONTH), 0, 0, 0, 0, timeZone);
        DateTime after = new DateTime(v2.get(Calendar.YEAR), v2.get(Calendar.MONTH) + 1, v2.get(Calendar.DAY_OF_MONTH), 23, 59, 59, 999, timeZone);

        List<Long> l = new ArrayList<Long>();
        l.add(before.getMillis());
        l.add(after.getMillis());

        return l;
    }


}
