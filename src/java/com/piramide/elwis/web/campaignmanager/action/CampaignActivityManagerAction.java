package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: CampaignActivityManagerAction.java 9950 2010-03-10 15:28:23Z fernando $
 */
public class CampaignActivityManagerAction extends CampaignManagerAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing CampaignActivityManagerAction............." + request.getParameterMap());
        if (isCancelled(request)) {
            log.debug("action was canceled");
            return (mapping.findForward("Cancel"));
        }

        ActionForward forward = validateActivityExistence(request, mapping);
        if (forward != null) {
            return forward;
        } else {
            return super.execute(mapping, form, request, response);
        }
    }

    protected ActionForward validateActivityExistence(HttpServletRequest request, ActionMapping mapping) {
        ActionErrors errors = new ActionErrors();
        if (null != request.getParameter("activityId")) {
            errors = ForeignkeyValidator.i.validate(CampaignConstants.TABLE_CAMPAIGNACTIVITY, "activityid",
                    request.getParameter("activityId"), errors, new ActionError("Campaign.activity.NotFound"));
            if (!errors.isEmpty()) {
                saveErrors(request.getSession(), errors);
                return mapping.findForward("Fail_");
            }

        } else {
            errors.add("activityid", new ActionError("Campaign.activity.NotFound"));
            saveErrors(request.getSession(), errors);
            return mapping.findForward("Fail_");
        }
        return null;// everything ok, no validation error.
    }

}
