package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Jatun S.R.L.
 * action to assign responsible to an activity campaign contact
 *
 * @author Miky
 * @version $Id: ResponsibleAssignAction.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class ResponsibleAssignAction extends CampaignActivityManagerAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing ResponsibleAssignAction........");
        ActionForward forward = null;
        ActionErrors errors = new ActionErrors();

        //activity user recurrency validation...
        errors = ForeignkeyValidator.i.validate(CampaignConstants.TABLE_CAMPAIGNACTIVITYUSER, "activityid", "userid",
                request.getParameter("activityId"), request.getParameter("userId"), errors, new ActionError("Campaign.activity.user.notFound"));
        if (!errors.isEmpty()) {
            saveErrors(request.getSession(), errors);
            return mapping.findForward("Fail");
        }

        forward = super.execute(mapping, form, request, response);

        log.debug("After of cmd execute...................." + request.getAttribute("dto"));
        Map dtoValues = (Map) request.getAttribute("dto");
        if ("Success".equals(forward.getName())) {
            log.debug("show messages if exist....");

            if (dtoValues.containsKey("someModified")) {
                errors.add("someExist", new ActionError("Campaign.activity.campContact.userAssign.someModified"));
            }
            if (dtoValues.containsKey("someDeleted")) {
                errors.add("someDeleted", new ActionError("Campaign.activity.campContact.userAssign.someDeleted"));
            }
            if (!errors.isEmpty()) {
                saveErrors(request.getSession(), errors);
            }
        }

        return forward;
    }
}
