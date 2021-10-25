package com.piramide.elwis.web.campaignmanager.action;

import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Jatun S.R.L.
 * Action to manage copy contacts from other activity
 *
 * @author Miky
 * @version $Id: CampContactCopyFromActivityAction.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class CampContactCopyFromActivityAction extends CampaignActivityManagerAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing CampContactCopyFromActivityAction........" + request.getParameterMap());
        if (isCancelled(request)) {
            log.debug("action was canceled");
            return (mapping.findForward("Cancel"));
        }

        ActionForward forward = null;
        ActionErrors errors = new ActionErrors();

        //set values in dto form
        DefaultForm defaultForm = (DefaultForm) form;
        defaultForm.setDto("op", "createActivities");
        defaultForm.setDto("campaignId", request.getParameter("campaignId"));
        defaultForm.setDto("activityId", request.getParameter("activityId"));
        defaultForm.setDto("copyActivityId", request.getParameter("copyActivityId"));

        forward = super.execute(mapping, defaultForm, request, response);

        log.debug("After of cmd execute...................." + request.getAttribute("dto"));
        Map dtoValues = (Map) request.getAttribute("dto");
        if (dtoValues.containsKey("successful")) {

            if (dtoValues.containsKey("someExist")) {
                errors.add("someExist", new ActionError("Campaign.activity.campContact.someAlreadyExist"));
            }
            if (dtoValues.containsKey("someDeleted")) {
                errors.add("someExist", new ActionError("Campaign.activity.campContact.someDeleted"));
            }
            if (dtoValues.containsKey("copyActivityDeleted")) {
                errors.add("someExist", new ActionError("Campaign.activity.campContact.copyActivityDeleted"));
            }
            if (dtoValues.containsKey("emptyRecipient")) {
                errors.add("empty", new ActionError("Campaign.activity.campContact.campaignEmptyRecipient"));
            }
            if (!errors.isEmpty()) {
                saveErrors(request.getSession(), errors);
            }
        }

        return forward;
    }

}
