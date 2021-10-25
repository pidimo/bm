package com.piramide.elwis.web.schedulermanager.form;

import com.piramide.elwis.utils.SchedulerConstants;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.util.LabelValueBean;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Apr 12, 2005
 * Time: 9:49:55 AM
 * To change this template use File | Settings | File Templates.
 */

public class AppointmentForm extends DefaultForm {
    // the logger
    private Log log = LogFactory.getLog(AppointmentForm.class);

    public Object[] getToList() {
        return (Object[]) this.getDto("toList");
    }

    public void setToList(String[] list1) {
        this.setDto("toList", Arrays.asList(list1));
    }

    public Object[] getArray2() {
        List list = (List) getDto("array2");
        if (list == null) {
            return new Object[]{};
        }
        return list.toArray();
    }

    public Object[] getArray2AsLabelBean() {
        List list = (List) getDto("array2");
        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            Object o = iterator.next();
            LabelValueBean bean = new LabelValueBean(o.toString(), o.toString());
            list.add(bean);
        }
        return list.toArray();
    }

    public void setArray2(Object[] array21) {

        if (array21 != null) {
            setDto("array2", Arrays.asList(array21));
        }
    }

    public ActionErrors validate(org.apache.struts.action.ActionMapping mapping,
                                 HttpServletRequest request) {
        log.debug("...  validate appointmentForm .... ");
        ActionErrors errors = super.validate(mapping, request);
        log.debug("Errors:" + errors.isEmpty());

        if (SchedulerConstants.TRUE_VALUE.equals(getDto("reminder")) && (SchedulerConstants.EMPTY_VALUE.equals(request.getParameter("dto(timeBefore_2)"))) && !getDto("reminderType").equals("1")) {
            errors.add(SchedulerConstants.EMPTY_VALUE, new ActionError("Appointment.timeBeforeIsRequired"));
        }
        if (SchedulerConstants.ZERO_VALUE.equals(request.getParameter("dto(timeBefore_2)"))) {
            errors.add(SchedulerConstants.EMPTY_VALUE, new ActionError("Appointment.timeBefore.cero"));
        }
        //if isRecurrence is TRUE then ...
        if ("on".equals(getDto("isRecurrence"))) {
            errors.add(validateRecurrence(mapping, request));
        }

        if (!errors.isEmpty()) {
            List list = new ArrayList(0);
            ArrayList arrayList = new ArrayList(0);
            if (request.getParameterValues("array2") != null) {
                list = Arrays.asList(request.getParameterValues("array2"));
            }
            for (Iterator iterator = list.iterator(); iterator.hasNext();) {
                String date = (String) iterator.next();
                arrayList.add(new LabelValueBean(date, date));
            }
            request.setAttribute("array2", arrayList);
        }
        return errors;
    }

    public ActionErrors validateRecurrence(org.apache.struts.action.ActionMapping mapping,
                                           HttpServletRequest request) {
        ActionErrors e = new ActionErrors();
        log.debug("... validateRecurrence function in AppointmentForm ... ");
        if (SchedulerConstants.ZERO_VALUE.equals(getDto("recurEveryDay")) && getDto("ruleType").equals("1")) {
            e.add("emptyValue", new ActionError("Appointment.Recurrence.cero"));
        } else if (SchedulerConstants.ZERO_VALUE.equals(getDto("recurEveryWeek")) && getDto("ruleType").equals("2")) {
            e.add("emptyValue", new ActionError("Appointment.Recurrence.cero"));
        } else if (SchedulerConstants.ZERO_VALUE.equals(getDto("recurEveryMonth")) && getDto("ruleType").equals("3")) {
            e.add("emptyValue", new ActionError("Appointment.Recurrence.cero"));
        } else if (SchedulerConstants.ZERO_VALUE.equals(getDto("recurEveryYear")) && getDto("ruleType").equals("4")) {
            e.add("emptyValue", new ActionError("Appointment.Recurrence.cero"));
        }

        if (SchedulerConstants.RECUR_RANGE_AFTER_OCCURRENCE.equals(getDto("rangeType")) && (SchedulerConstants.ZERO_VALUE.equals(getDto("rangeValueText")))) {
            e.add("notEmpty", new ActionError("Appointment.Recurrence.cero"));
        }
        if (SchedulerConstants.RECURRENCE_WEEKLY.equals(getDto("ruleType"))) {
            boolean b = false;
            for (int i = 0; i < 8; i++) {
                if ("on".equals(getDto(new Integer(i).toString()))) {
                    b = true;
                }
            }
            if (!b) {
                e.add("daysEmpty", new ActionError("Appointment.Recurrence.dayWeek"));
            }
        }
        return e;
    }

    public String toString() {
        return getDtoMap().toString();
    }
}

