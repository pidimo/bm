package com.piramide.elwis.web.schedulermanager.form;

import com.piramide.elwis.utils.SalesConstants;
import com.piramide.elwis.utils.SchedulerConstants;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import javax.servlet.http.HttpServletRequest;

/**
 * User: yumi
 * Date: Apr 8, 2005
 * Time: 12:20:26 PM
 * To change this template use File | Settings | File Templates.
 */

public class TaskForm extends DefaultForm {
    // the logger
    private Log log = LogFactory.getLog(TaskForm.class);

    /**
     * Validate the input fields and set defaults values to dtoMap.
     */
    public ActionErrors validate(org.apache.struts.action.ActionMapping mapping,
                                 HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (request.getParameter("dto(save)") != null) {
            errors = super.validate(mapping, request);// validating with super class

            //validate assigned to, required only when task is created
            if ("create".equals(getDto("op"))) {
                if (GenericValidator.isBlankOrNull((String) getDto("userTaskId"))) {
                    errors.add("assigned", new ActionError("errors.required", JSPHelper.getMessage(request, "Task.AssignedA")));
                }
            }

            validateSalesProcessContact(errors);

            if (SchedulerConstants.TRUE_VALUE.equals(getDto("date")) && errors.isEmpty()) {
                Integer startDate = new Integer(getDto("startDate").toString());
                Integer endDate = (Integer) getDto("expireDate");
                StringBuffer startTime = new StringBuffer();
                StringBuffer endTime = new StringBuffer();
                startTime.append(getDto("startHour")).append(getDto("startMin"));
                endTime.append(getDto("expireHour")).append(getDto("expireMin"));

                if (startDate.intValue() > endDate.intValue()) {

                    errors.add("startDate", new ActionError("Common.greaterThan", JSPHelper.getMessage(request,
                            "Task.expireDate"), JSPHelper.getMessage(request, "Task.startDate")));
                }
                if ((new Integer(startTime.toString()).intValue() > new Integer(endTime.toString()).intValue()) && (startDate.intValue() == endDate.intValue())) {

                    errors.add(SchedulerConstants.EMPTY_VALUE, new ActionError("Task.greater",
                            JSPHelper.getMessage(request, "Appointment.startTime"),
                            JSPHelper.getMessage(request, "Appointment.endTime")));
                }
            } else {
                if (SchedulerConstants.FALSE_VALUE.equals(getDto("date")) && !errors.isEmpty()
                        && (!"delete".equals(request.getParameter("operation")) && !SchedulerConstants.TRUE_VALUE.equals(request.getParameter("isParticipant")))) {
                    request.setAttribute("jsLoad", "onLoad=\"deshabilita()\"");
                }
            }

            if (!errors.isEmpty() && !request.getParameter("module").equals("scheduler")) {
                request.setAttribute("noSch", SchedulerConstants.TRUE_VALUE);
            }
            request.setAttribute("jsLoad", "onLoad=\"habilita()\"");
        } else if (SchedulerConstants.FALSE_VALUE.equals(getDto("date")) && (!"delete".equals(request.getParameter("operation")) && !SchedulerConstants.TRUE_VALUE.equals(request.getParameter("isParticipant")))) {
            request.setAttribute("jsLoad", "onLoad=\"deshabilita()\"");
        }
        return errors;
    }

    private void validateSalesProcessContact(ActionErrors errors) {
        Object addressId = getDto("addressId");
        Object processId = getDto("processId");

        if (addressId != null && !GenericValidator.isBlankOrNull(addressId.toString()) && processId != null && !GenericValidator.isBlankOrNull(processId.toString())) {

            ForeignkeyValidator.i.validate(SalesConstants.TABLE_SALESPROCESS, "addressid", "processid", addressId, processId,
                    errors, new ActionError("Task.error.salesProcessRelatedContact"));
        }
    }
}
