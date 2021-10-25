package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.campaignmanager.form.TemplateFileForm;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : ivan
 * @version : $Id CampaignTemplateAction ${time}
 */
public class CampaignTemplateAction extends CampaignManagerAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        ActionErrors errors = new ActionErrors();
        errors = ForeignkeyValidator.i.validate(CampaignConstants.TABLE_CAMPAIGN_TEMPLATE, "templateid",
                request.getParameter("templateId"), errors, new ActionError("template.NotFound"));

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("MainList");
        }

        ActionForward forward = super.execute(mapping, form, request, response);
        if ("Preview".equals(forward.getName())) {
            request.setAttribute("jsLoad", "onload=\"showPreviewWindow();\"");
            TemplateFileForm templateForm = (TemplateFileForm) form;
            User sessionUser = (User) request.getSession().getAttribute(Constants.USER_KEY);


            ActionForwardParameters updateFileParameters = new ActionForwardParameters();
            updateFileParameters.add("templateId", templateForm.getDto("templateId").toString()).
                    add("documentType", templateForm.getDto("documentType").toString()).
                    add("dto(templateId)", templateForm.getDto("templateId").toString()).
                    add("dto(languageId)", templateForm.getDto("languageId").toString()).
                    add("dto(documentType)", templateForm.getDto("documentType").toString()).
                    add("campaignId", templateForm.getDto("campaignId").toString()).
                    add("dto(campaignId)", templateForm.getDto("campaignId").toString()).
                    add("dto(userId)", sessionUser.getValue("userId").toString()).
                    add("dto(op)", "read");

            forward = updateFileParameters.forward(forward);
        }
        return forward;
    }
}
