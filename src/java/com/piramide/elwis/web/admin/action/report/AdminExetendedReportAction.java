package com.piramide.elwis.web.admin.action.report;

import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.RequestUtils;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author : ivan
 * @version : $Id AdminExetendedReportAction ${time}
 */
public class AdminExetendedReportAction extends AdminReportAction {
    private Log log = LogFactory.getLog(AdminExetendedReportAction.class);

    String init = "init";
    String end = "end";
    String delimiter = "_";
    String range = "Range";
    String single = "Single";

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        User user = RequestUtils.getUser(request);
        DateTimeZone timeZone = null;
        if (user != null) {
            timeZone = (DateTimeZone) user.getValue("dateTimeZone");
        } else {
            timeZone = DateTimeZone.getDefault();
        }


        SearchForm myForm = (SearchForm) form;

        Map myFormParams = myForm.getParams();

        List allRangeValues = new ArrayList();

        Iterator it = myFormParams.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();

            Map mapKeys = process(key);
            if (!mapKeys.isEmpty()) {
                mapKeys.put("value", value);
                allRangeValues.add(mapKeys);
            }
        }

        List filtertToAdd = filtersToAdd(allRangeValues);

        List valuesToAdd = valuesToAdd(allRangeValues);

        for (int i = 0; i < filtertToAdd.size(); i++) {
            String filter = (String) filtertToAdd.get(i);
            addFilter(filter, "true");
        }


        for (int i = 0; i < valuesToAdd.size(); i++) {
            Map values = (Map) valuesToAdd.get(i);

            log.debug("los values son :  " + values);
            String value = (String) values.get("value");


            if (null != values.get("type") && "long".equals(values.get("type").toString()) && null != value) {
                String key = values.get("key").toString();
                String hour = "00";
                String minute = "00";


                if (key.indexOf(init) > -1) {
                    hour = "00";
                    minute = "00";
                }

                if (key.indexOf(end) > -1) {
                    hour = "23";
                    minute = "59";
                }

                value = new Long(convertToLongValue(timeZone, value, hour, minute)).toString();
            }
            myForm.setParameter(values.get("key").toString(), value);

        }


        return super.execute(mapping, myForm, request, response);
    }

    private Map process(String key) {
        Map result = new HashMap();

        int indexOfInit = key.indexOf(init);
        int indexOfEnd = key.indexOf(end);
        int indexOfDelimiter = key.indexOf("_");
        int indexOfType = key.indexOf("@");

        if (indexOfDelimiter > -1 && indexOfInit > -1) {
            result.put("key", key.substring(indexOfInit + init.length(), indexOfDelimiter));
            result.put("position", key.substring(indexOfDelimiter + 1));
            result.put("isInitial", Boolean.valueOf(true));
            if (indexOfType > -1) {
                result.put("type", key.substring(0, indexOfType));
            }
        }
        if (indexOfDelimiter > -1 && indexOfEnd > -1) {
            result.put("key", key.substring(indexOfEnd + end.length(), indexOfDelimiter));
            result.put("position", key.substring(indexOfDelimiter + 1));
            result.put("isFinal", Boolean.valueOf(true));
            if (indexOfType > -1) {
                result.put("type", key.substring(0, indexOfType));
            }
        }
        return result;
    }

    private List filtersToAdd(List mapList) {

        List result = new ArrayList();

        for (int i = 0; i < mapList.size(); i++) {
            Map actual = (Map) mapList.get(i);

            if (null != actual.get("isFinal") &&
                    null != actual.get("value") &&
                    !"".equals(actual.get("value").toString().trim())) {
                String fieldFilter = "_" + actual.get("key") + range;

                if (!result.contains(fieldFilter)) {
                    result.add(fieldFilter);
                }
            }

            if (null != actual.get("isInitial") &&
                    null != actual.get("value") &&
                    !"".equals(actual.get("value").toString().trim())) {
                String fieldFilter = "_" + actual.get("key") + range;

                if (!result.contains(fieldFilter)) {
                    result.add(fieldFilter);
                }
            }

        }

        return result;
    }


    private List valuesToAdd(List mapList) {
        List result = new ArrayList();
        for (int i = 0; i < mapList.size(); i++) {
            Map item = new HashMap(3);
            Map actual = (Map) mapList.get(i);
            String name = "";
            if (null != actual.get("isInitial") && ((Boolean) actual.get("isInitial")).booleanValue()) {
                name = init + actual.get("key").toString();
            }
            if (null != actual.get("isFinal") && ((Boolean) actual.get("isFinal")).booleanValue()) {
                name = end + actual.get("key").toString();
            }

            String value = null;
            if (null != actual.get("value") &&
                    !"".equals(actual.get("value").toString())) {
                value = (String) actual.get("value");

            }

            if (actual.get("position").toString().length() == 1) {
                item.put("key", name);
                item.put("value", value);
                item.put("type", actual.get("type"));
                result.add(item);
            }
        }

        return result;
    }

    private long convertToLongValue(DateTimeZone dateTimeZone, String value, String hour, String minute) {
        try {
            return DateUtils.integerToDateTime(Integer.valueOf(value),
                    Integer.valueOf(hour).intValue(),
                    Integer.valueOf(minute).intValue(), dateTimeZone).getMillis();
        } catch (NumberFormatException e) {
            return new DateTime().getMillis();
        }
    }
}
