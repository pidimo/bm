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
public class CampaignSentLogListAction extends CampaignListAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.debug("Executing CampaignSentLogListAction...." + request.getParameterMap());

        if (isCancelled(request)) {
            return (mapping.findForward("Cancel"));
        }

        //cheking if was not deleted by other user.
        log.debug("campaignSentLogId = " + request.getParameter("campaignSentLogId"));
        ActionErrors errors = new ActionErrors();
        if (null != request.getParameter("campaignSentLogId")) {
            errors = ForeignkeyValidator.i.validate(CampaignConstants.TABLE_CAMPAIGNSENTLOG, "campsentlogid",
                    request.getParameter("campaignSentLogId"), errors, new ActionError("Campaign.sentLog.notFound"));
            if (!errors.isEmpty()) {
                saveErrors(request.getSession(), errors);
                return mapping.findForward("Fail");
            }

        } else {
            errors.add("campaignSentLog", new ActionError("Campaign.sentLog.notFound"));
            saveErrors(request.getSession(), errors);
            return mapping.findForward("Fail");
        }

        addFilter("campaignSentLogId", request.getParameter("campaignSentLogId"));

        return super.execute(mapping, form, request, response);
    }

}
