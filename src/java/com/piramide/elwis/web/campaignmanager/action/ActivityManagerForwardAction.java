package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;
import org.apache.struts.actions.ForwardAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 * control concurrency to capaign and activity
 *
 * @author Miky
 * @version $Id: ActivityManagerForwardAction.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class ActivityManagerForwardAction extends ForwardAction {
    public Log log = LogFactory.getLog(ActivityManagerForwardAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing ActivityManagerForwardAction........");
        ActionForward forward;

        //campaign validation
        log.debug("campaignId for user session  = " + request.getParameter("campaignId"));
        ActionErrors errors = new ActionErrors();
        errors = ForeignkeyValidator.i.validate(CampaignConstants.TABLE_CAMPAIGN, "campaignid",
                request.getParameter("campaignId"), errors, new ActionError("error.CampaignSession.NotFound"));

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("MainSearch");
        }

        //activity validation
        if (null != request.getParameter("activityId")) {
            errors = ForeignkeyValidator.i.validate(CampaignConstants.TABLE_CAMPAIGNACTIVITY, "activityid",
                    request.getParameter("activityId"), errors, new ActionError("Campaign.activity.NotFound"));
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return mapping.findForward("Fail_");
            }
        } else {
            errors.add("activityid", new ActionError("Campaign.activity.NotFound"));
            saveErrors(request, errors);
            return mapping.findForward("Fail_");
        }

        return super.execute(mapping, form, request, response);
    }
}
