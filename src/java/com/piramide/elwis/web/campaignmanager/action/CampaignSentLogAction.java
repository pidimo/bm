package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.9
 */
public class CampaignSentLogAction extends CampaignManagerAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.debug("Executing CampaignSentLogAction...." + request.getParameterMap());

        if (isCancelled(request)) {
            return (mapping.findForward("Cancel"));
        }

        ActionForward forward = validateCampaignSentLogExistence(request, mapping);
        if (forward != null) {
            return forward;
        } else {
            return super.execute(mapping, form, request, response);
        }
    }

    protected ActionForward validateCampaignSentLogExistence(HttpServletRequest request, ActionMapping mapping) {
        log.debug("campaignSentLogId for user session  = " + request.getParameter("campaignSentLogId"));
        ActionErrors errors = new ActionErrors();
        errors = ForeignkeyValidator.i.validate(CampaignConstants.TABLE_CAMPAIGNSENTLOG, "campsentlogid",
                request.getParameter("campaignSentLogId"), errors, new ActionError("Campaign.sentLog.notFound"));

        if (!errors.isEmpty()) {
            saveErrors(request.getSession(), errors);
            return mapping.findForward("Fail");
        }
        return null;// everything ok, no validation error.
    }
}
