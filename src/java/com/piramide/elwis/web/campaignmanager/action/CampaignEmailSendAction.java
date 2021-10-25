package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.cmd.campaignmanager.CampaignEmailSendCmd;
import com.piramide.elwis.service.campaign.CampaignMailerService;
import com.piramide.elwis.web.campaignmanager.delegate.CampaignMailerServiceDelegate;
import com.piramide.elwis.web.common.util.MessagesUtil;
import net.java.dev.strutsejb.dto.ParamDTO;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.*;
import org.apache.struts.util.LabelValueBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Jatun S.R.L.
 * Action to manage send campaign emails
 *
 * @author Miky
 * @version $Id: CampaignEmailSendAction.java 10517 2015-02-26 01:45:04Z miguel $
 */
public class CampaignEmailSendAction extends CampaignActivityManagerAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        CampaignMailerService mailerService = CampaignMailerServiceDelegate.i.getCampaignMailerService();

        log.debug("Executing CampaignEmailSendAction...." + request.getParameterMap());

        if (isCancelled(request)) {
            return (mapping.findForward("Cancel"));
        }

        ActionErrors errors;
        DefaultForm defaultForm = (DefaultForm) form;

        ActionForward forward;
        forward = validateActivityExistence(request, mapping);
        if (forward != null) {
            return forward; //activity was removed
        }
        forward = validateCampaignExistence(request, mapping);
        if (forward != null) {
            return forward; //campaign was removed
        }
        setDtoAttributes(request, defaultForm);

        defaultForm.setDto("requestLocale", request.getLocale().toString());

        ParamDTO params = new ParamDTO();
        params.putAll(defaultForm.getDtoMap());

        ResultDTO resultDTO = mailerService.sendMassiveMails(params, CampaignEmailSendCmd.class);

        if (resultDTO == null) {
            errors = new ActionErrors();
            errors.add("unexpectedError", new ActionError("Campaign.mailing.unexpectedError"));
            saveErrors(request.getSession(), errors);
            return mapping.findForward("Cancel");
        }

        errors = MessagesUtil.i.convertToActionErrors(mapping, request, resultDTO);
        saveErrors(request, errors);

        if (resultDTO.isFailure()) {
            return mapping.getInputForward();
        }

        forward = mapping.findForward(resultDTO.getForward());

        Map dtoValues = resultDTO;
        if ("Success".equals(forward.getName())) {

            if (isSendInBackground(defaultForm)) {
                campaignIsSendInBackgroundSummary(defaultForm, request);
            } else {
                summaryOfSendCampaign(dtoValues, request);
            }
        }
        return forward;
    }

    private boolean isSendInBackground(DefaultForm defaultForm) {
        return "true".equals(defaultForm.getDto("sendInBackground"));
    }

    private void summaryOfSendCampaign(Map dtoValues, HttpServletRequest request) {
        List summaryList = new ArrayList();

        if (dtoValues.containsKey("generationResume")) {
            Map generationResumeMap = (Map) dtoValues.get("generationResume");
            for (Iterator iterator = generationResumeMap.keySet().iterator(); iterator.hasNext();) {
                String language = (String) iterator.next();
                summaryList.add(new LabelValueBean(language, generationResumeMap.get(language).toString()));
            }
        }
        request.setAttribute("summaryList", summaryList);

        request.setAttribute("totalFail", dtoValues.get("totalFail"));
        request.setAttribute("totalSuccess", dtoValues.get("totalSuccess"));
        request.setAttribute("totalRecipients", dtoValues.get("totalRecipients"));
        request.setAttribute("withoutMailList", dtoValues.get("withoutMailList"));
        request.setAttribute("failSentList", dtoValues.get("failSentList"));
        request.setAttribute("templateId", dtoValues.get("templateId"));
    }

    private void campaignIsSendInBackgroundSummary(DefaultForm defaultForm, HttpServletRequest request) {
        request.setAttribute("sendInBackgroundSummary", "true");
        request.setAttribute("notificationMail", defaultForm.getDto("notificationMail"));
    }

}
