package com.piramide.elwis.web.dashboard.conf;

import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.dashboard.component.configuration.structure.Condition;
import com.piramide.elwis.web.dashboard.component.execute.ConditionEvaluator;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: ivan
 * Date: 27-11-2006: 03:52:12 PM
 */
public class Evaluator extends ConditionEvaluator {
    public String evaluate(Map map, List<Condition> list, String componentName) {
        if ("birthday".equals(componentName)) {
            Condition c = getCondition(list, "date");
            if (null != c) {
                Object o = map.get("birtdayComplete");

                Date d = DateUtils.integerToDate(new Integer(o.toString()));
                Calendar dc = Calendar.getInstance();
                dc.setTime(d);
                Calendar now = Calendar.getInstance();
                if (now.get(Calendar.DATE) == dc.get(Calendar.DATE) &&
                        now.get(Calendar.MONTH) == dc.get(Calendar.MONTH)) {
                    return c.getStyle();
                }
            }
        }
        if ("task".equals(componentName)) {
            Condition c = getCondition(list, "date");
            if (null != c) {
                Object o = map.get("expireDate");
                if (null != o && !"".equals(o.toString().trim())) {
                    Date dbDate = DateUtils.integerToDate(new Integer(o.toString()));
                    Date actualDate = new Date();

                    if (dbDate.compareTo(actualDate) <= 0) {
                        return c.getStyle();
                    }
                }
            }
        }
        return "";
    }

    private Condition getCondition(List<Condition> conditions, String type) {
        for (Condition c : conditions) {
            if (c.getType().equals(type)) {
                return c;
            }
        }
        return null;
    }
}
