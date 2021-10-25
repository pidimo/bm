package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 * set onLoad js property to generate document from popup
 *
 * @author Miky
 * @version $Id: CampaignDocumentValidateAction.java 9695 2009-09-10 21:34:43Z fernando $
 */

public class CampaignDocumentValidateAction extends DefaultAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.debug("Executing CampaignDocumentValidateAction................" + request.getParameterMap());

        //concurrency validation
        ActionErrors errors = new ActionErrors();
        errors = ForeignkeyValidator.i.validate(CampaignConstants.TABLE_CAMPAIGN, "campaignid",
                request.getParameter("campaignId"), errors, new ActionError("error.CampaignSession.NotFound"));

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("MainSearch");
        }

        //activity concurrency validation
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

        //setting parameters to onload popup
        DefaultForm docForm = (DefaultForm) form;
        log.debug("Form:" + docForm.getDtoMap());

        String js = null;
        StringBuffer parameters = new StringBuffer();
        parameters.append("campaignId=").append(request.getParameter("campaignId"));
        parameters.append("&activityId=").append(request.getParameter("activityId"));
        parameters.append("&languageName=").append(docForm.getDto("languageName"));
        parameters.append("&language=").append(docForm.getDto("language"));
        parameters.append("&page=").append(docForm.getDto("page"));
        parameters.append("&templateId=").append(docForm.getDto("templateId"));
        parameters.append("&responsibleType=").append(docForm.getDto("responsibleType"));

        js = "onLoad=\"window.open('" + response.encodeRedirectURL(request.getContextPath() + "/campaign/Campaign/PopUpDownload.do?" + parameters.toString()) + "','', 'width=420,height=180,top=200,left=350,scrollbars=no');\"";
        request.setAttribute("jsLoad", js);

        return mapping.findForward("Success");
    }
}
