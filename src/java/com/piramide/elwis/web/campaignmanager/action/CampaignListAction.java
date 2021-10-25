package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Yumi
 * @version CampaignListAction.java, v 2.0 May 13, 2004 10:48:26 AM
 */
public class CampaignListAction extends com.piramide.elwis.web.common.action.ListAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        if ("true".equals(request.getParameter("cancel"))) {
            log.debug("cancel ....  CampaignContactImportAction");
            return mapping.findForward("Cancel");
        }

        log.debug(" ...       Campaign list action execution   ...");
        //cheking if working address was not deleted by other user.

        log.debug("CampaignId for user session  = " + request.getParameter("campaignId"));
        ActionErrors errors = new ActionErrors();
        SearchForm defaultForm = (SearchForm) form;
        //check addressId not has deleted another user.

        errors = ForeignkeyValidator.i.validate(CampaignConstants.TABLE_CAMPAIGN, "campaignid",
                request.getParameter("campaignId"), errors, new ActionError("error.CampaignSession.NotFound"));

        if (!errors.isEmpty()) {
            saveErrors(request.getSession(), errors);
            return mapping.findForward("Fail_");
        }

        log.debug("Final AddSeach:");
        addFilter("campaignId", request.getParameter("campaignId"));
        return super.execute(mapping, defaultForm, request, response);
    }
}
