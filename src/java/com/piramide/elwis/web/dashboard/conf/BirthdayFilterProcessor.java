package com.piramide.elwis.web.dashboard.conf;

import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.dashboard.component.configuration.structure.Component;
import com.piramide.elwis.web.dashboard.component.configuration.structure.ConfigurableFilter;
import com.piramide.elwis.web.dashboard.component.configuration.structure.StaticFilter;
import com.piramide.elwis.web.dashboard.component.execute.FilterPreProcessor;
import com.piramide.elwis.web.dashboard.component.util.Constant;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * @author : ivan
 *         Date: Sep 8, 2006
 *         Time: 12:14:03 PM
 */
public class BirthdayFilterProcessor extends FilterPreProcessor {

    public void processConfigurableFilter(Component cloneComponent, Map params) {
        ConfigurableFilter confFilter = cloneComponent.getConfigurableFilter("rangeOfDate");

        Calendar beforeC = null;
        Calendar afterC = null;
        Calendar actualC = Calendar.getInstance();

        Integer before = null;
        Integer after = null;

        if (null != confFilter) {

            int initialValue = new Integer(confFilter.getInitialValue());
            int finalValue = new Integer(confFilter.getFinalValue());

            beforeC = createDate(getCurrentDate(params), (-1 * initialValue));
            afterC = createDate(getCurrentDate(params), finalValue);

            before = DateUtils.dateToInteger(beforeC.getTime()) % 10000;
            after = DateUtils.dateToInteger(afterC.getTime()) % 10000;


            int beforeYear = beforeC.get(Calendar.YEAR);
            int afterYear = afterC.get(Calendar.YEAR);
            int actualYear = actualC.get(Calendar.YEAR);

            ConfigurableFilter specialFilter = new ConfigurableFilter("special", "", "");
            ConfigurableFilter normalFilter = new ConfigurableFilter("normal", "", "");
            if (beforeYear != actualYear || afterYear != actualYear) {
                specialFilter.setInitialValue("true");
                normalFilter.setInitialValue("false");
            } else {
                specialFilter.setInitialValue("false");
                normalFilter.setInitialValue("true");
            }

            ConfigurableFilter date1Filter = new ConfigurableFilter("date1", "", "");
            date1Filter.setInitialValue(before.toString());

            ConfigurableFilter date2Filter = new ConfigurableFilter("date2", "", "");
            date2Filter.setInitialValue(after.toString());

            cloneComponent.addConfigurableFilter(specialFilter);
            cloneComponent.addConfigurableFilter(normalFilter);
            cloneComponent.addConfigurableFilter(date1Filter);
            cloneComponent.addConfigurableFilter(date2Filter);
        }

        //Employee view configuration
        StaticFilter birthdayViewTypeFilter = cloneComponent.getStaticFilter("birthdayViewType");
        if (birthdayViewTypeFilter != null && Constant.BirthdayViewType.SELECTED_EMPLOYEE.getConstantAsString().equals(birthdayViewTypeFilter.getValue())) {
            StaticFilter enableByEmployeeFilter = new StaticFilter("enableByEmployee", "true");
            StaticFilter filterName = new StaticFilter("employeeIdsFilterName", "birthdayEmployeeIds");

            cloneComponent.addStaticFilter(enableByEmployeeFilter);
            cloneComponent.addStaticFilter(filterName);
        }

        StaticFilter viewNotRelatedEmployeeFilter = cloneComponent.getStaticFilter("viewNotRelatedEmployee");
        if (viewNotRelatedEmployeeFilter != null && "true".equals(viewNotRelatedEmployeeFilter.getValue())) {
            StaticFilter enableNotRelatedEmployeeFilter = new StaticFilter("enableNotRelatedEmployee", "true");
            cloneComponent.addStaticFilter(enableNotRelatedEmployeeFilter);
        }
    }

    public Calendar createDate(Date date, int inc) {
        Calendar c = Calendar.getInstance();
        if (null == date) {
            return c;
        }

        c.setTimeInMillis(date.getTime());
        c.add(Calendar.DATE, inc);

        return c;
    }

    private Date getCurrentDate(Map params) {
        DateTimeZone dateTimeZone = (DateTimeZone) params.get("dateTimeZone");
        if (dateTimeZone == null) {
            dateTimeZone = DateTimeZone.getDefault();
        }
        DateTime currentDateTime = new DateTime(dateTimeZone);

        return DateUtils.integerToDate(DateUtils.dateToInteger(currentDateTime));
    }
}
