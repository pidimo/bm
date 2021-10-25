package com.piramide.elwis.web.campaignmanager.form;

import com.piramide.elwis.cmd.campaignmanager.CampaignContactTaskReadCmd;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.SchedulerConstants;
import com.piramide.elwis.web.common.util.JSPHelper;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import javax.servlet.http.HttpServletRequest;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: CampaignTaskForm.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class CampaignTaskForm extends DefaultForm {
    // the logger
    private Log log = LogFactory.getLog(this.getClass());

    /**
     * Validate the input fields and set defaults values to dtoMap.
     */
    public ActionErrors validate(org.apache.struts.action.ActionMapping mapping,
                                 HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        errors = super.validate(mapping, request);// validating with super class

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
        }

        //is user create type is campaign responsible
        if (CampaignConstants.CAMPAIGNRESPONSIBLE_IS_NOT_VALIDUSER.equals(getDto("taskCreateUserId"))) {
            log.debug("campaign responsible is not user valid......");
            errors.add("notUser", new ActionError("Activity.taskCreate.campaignResponsible.notValidUser", getDto("taskCreateUserName")));
        }

        if (errors.isEmpty()) {
            CampaignContactTaskReadCmd cmd = new CampaignContactTaskReadCmd();
            cmd.putParam("activityId", getDto("activityId"));
            cmd.putParam("op", "readAll");

            ResultDTO resultDto = new ResultDTO();
            try {
                resultDto = BusinessDelegate.i.execute(cmd, request);
            } catch (AppLevelException e) {
            }
            if (resultDto.containsKey("allWithTask")) {
                errors.add("alltask", new ActionError("Campaign.activity.task.notPosibleCreate"));
            }
        }

        //////errors.add("startDate", new ActionError("Common.greaterThan.eeeeeeeeeee"));

        if (!errors.isEmpty()) {
            request.setAttribute("jsLoad", "onLoad=\"habilita()\"");
            if (SchedulerConstants.FALSE_VALUE.equals(getDto("date"))) {
                request.setAttribute("jsLoad", "onLoad=\"deshabilita()\"");
            }
        }

        return errors;
    }
}
