package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.web.common.action.ListAction;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 * general campaign list action, if campaign was deleted, go to MainSearch forward
 *
 * @author Miky
 * @version $Id: CampaignManagerListAction.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class CampaignManagerListAction extends ListAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing CampaignManagerListAction........");

        //cheking if was not deleted by other user.
        log.debug("CampaignId = " + request.getParameter("campaignId"));
        ActionErrors errors = new ActionErrors();
        if (request.getParameter("campaignId") != null) {

            errors = ForeignkeyValidator.i.validate(CampaignConstants.TABLE_CAMPAIGN, "campaignid",
                    request.getParameter("campaignId"), errors, new ActionError("error.CampaignSession.NotFound"));

            if (!errors.isEmpty()) {
                saveErrors(request.getSession(), errors);
                return mapping.findForward("MainSearch");
            }
        } else {
            errors.add("campaignid", new ActionError("error.CampaignSession.NotFound"));
            saveErrors(request.getSession(), errors);
            return mapping.findForward("MainSearch");
        }
        log.debug("Final AddSeach:");
        addFilter("campaignId", request.getParameter("campaignId"));

        return super.execute(mapping, form, request, response);
    }
}
