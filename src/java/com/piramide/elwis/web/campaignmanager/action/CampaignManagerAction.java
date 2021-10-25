package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.AbstractDefaultAction;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * AlfaCentauro Team
 *
 * @author Yumi
 * @version $Id: CampaignManagerAction.java 9950 2010-03-10 15:28:23Z fernando $
 */

public class CampaignManagerAction extends AbstractDefaultAction {

    /**
     * Check if campaignId exists, if exists continue, otherwise cancel operation and return campaign page
     */

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.debug("CampaignManagerAction execution  ...");

        if (isCancelled(request)) {
            log.debug("action was canceled");
            return (mapping.findForward("Cancel"));
        }
        ActionForward forward = validateCampaignExistence(request, mapping);
        if (forward != null) {
            return forward;
        } else {
            setDtoAttributes(request, form);
            return super.execute(mapping, form, request, response);
        }
    }

    protected ActionForward validateCampaignExistence(HttpServletRequest request, ActionMapping mapping) {
        //user in the session
        //cheking if working product was not deleted by other user.
        log.debug("campaignId for user session  = " + request.getParameter("campaignId"));
        ActionErrors errors = new ActionErrors();
        errors = ForeignkeyValidator.i.validate(CampaignConstants.TABLE_CAMPAIGN, "campaignid",
                request.getParameter("campaignId"), errors, new ActionError("error.CampaignSession.NotFound"));

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("MainSearch");
        }
        return null;// everything ok, no validation error.
    }

    protected void setDtoAttributes(HttpServletRequest request, ActionForm form) {
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        ((DefaultForm) form).setDto("currentUserId", user.getValue(Constants.USERID).toString());
        ((DefaultForm) form).setDto("companyId", user.getValue(Constants.COMPANYID).toString());
        ((DefaultForm) form).setDto("campaignId", request.getParameter("campaignId"));
    }
}