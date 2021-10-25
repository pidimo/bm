package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.cmd.campaignmanager.CampaignSentLogCmd;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.9
 */
public class CampaignRetryLightEmailSendForwardAction extends CampaignSentLogAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing CampaignRetryLightEmailSendForwardAction........" + request.getParameterMap());

        DefaultForm defaultForm = (DefaultForm) form;

        //set the generationId related to this campaign sent log
        defaultForm.setDto("generationId", findRelatedGenerationId(request));

        ActionForward forward = super.execute(mapping, defaultForm, request, response);

        log.warn("After of cmd execute...................." + request.getAttribute("dto"));
        Map dtoValues = (Map) request.getAttribute("dto");
        if ("Success".equals(forward.getName())) {
            processGenerationAttachment(dtoValues, request);
        }

        return forward;
    }

    private Integer findRelatedGenerationId(HttpServletRequest request) {
        Integer generationId = null;

        CampaignSentLogCmd campaignSentLogCmd = new CampaignSentLogCmd();
        campaignSentLogCmd.putParam("op", "read");
        campaignSentLogCmd.putParam("campaignSentLogId", request.getParameter("campaignSentLogId"));

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(campaignSentLogCmd, request);
            if (!resultDTO.isFailure()) {
                generationId = (Integer) resultDTO.get("generationId");
            }
        } catch (AppLevelException e) {
            log.debug("Error in read Campaign sent log cmd...", e);
        }

        return generationId;
    }

    private void processGenerationAttachment(Map resultDtoValues, HttpServletRequest request) {
        List<LabelValueBean> genAttachments = new ArrayList<LabelValueBean>();

        if (resultDtoValues.containsKey("genAttachmentList")) {
            List<Map> sourceAttachmentList = (List<Map>) resultDtoValues.get("genAttachmentList");
            for (Map attachmentMap : sourceAttachmentList) {
                LabelValueBean labelValueBean = new LabelValueBean(attachmentMap.get("fileName").toString(), attachmentMap.get("attachmentId").toString());
                genAttachments.add(labelValueBean);
            }
        }

        request.setAttribute("previousAttachList", genAttachments);
    }
}
