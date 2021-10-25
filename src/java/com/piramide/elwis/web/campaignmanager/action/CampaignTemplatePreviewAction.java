package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.campaignmanager.form.CampaignTemplateForm;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @autor : ivan
 * Date: 17-01-2007 - 10:29:45 AM
 */
public class CampaignTemplatePreviewAction extends CampaignManagerAction {

    private Log log = LogFactory.getLog(this.getClass());


    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        ActionForward forward = super.execute(mapping, form, request, response);
        if ("Preview".equals(forward.getName())) {
            request.setAttribute("jsLoad", "onload=\"showPreviewWindow()\"");
            CampaignTemplateForm templateForm = (CampaignTemplateForm) form;
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
