package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.web.campaignmanager.form.CampaignCriterionForm;
import com.piramide.elwis.web.common.action.CheckEntriesForwardAction;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: yumi
 * Date: 09-oct-2006
 * Time: 14:55:01
 */

public class CampaignCriterionCreateForwardAction extends CheckEntriesForwardAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        ActionForward forward = mapping.findForward("Success");

        CampaignCriterionForm defaultForm = (CampaignCriterionForm) form;
        log.debug(" ... campaignCriterionCreateForwardAction .... execute ...");

        ActionErrors errors = new ActionErrors();
        errors = ForeignkeyValidator.i.validate(CampaignConstants.TABLE_CAMPAIGN, "campaignid",
                request.getParameter("campaignId"), errors, new ActionError("error.CampaignSession.NotFound"));

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("MainSearch");
        }
        request.setAttribute("jsLoad", "onLoad=\"empty();\"");
        if (!CampaignConstants.TRUEVALUE.equals(request.getAttribute("error"))) {
            defaultForm.setDto("fieldType", -100);
        }
        return super.execute(mapping, form, request, response);
    }
}
