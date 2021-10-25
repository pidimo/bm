package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.cmd.campaignmanager.CampaignSentLogCmd;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.util.JSPHelper;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.9
 */
public class CampaignGenerationProgressBarAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing CampaignGenerationProgressBarAction................" + request.getParameterMap());
        ActionForward forward = null;

        DefaultForm defaultForm = (DefaultForm) form;

        generationStatus(defaultForm, response, request);

        return forward;
    }

    private void generationStatus(DefaultForm defaultForm, HttpServletResponse response, HttpServletRequest request) throws Exception {

        CampaignSentLogCmd campaignSentLogCmd = new CampaignSentLogCmd();
        campaignSentLogCmd.putParam("op","generationStatus");
        campaignSentLogCmd.putParam("generationKey", defaultForm.getDto("generationKey"));

        Integer total = 0;
        Integer success = 0;
        Integer fail = 0;

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(campaignSentLogCmd, null);
            log.debug("Result Dto..:" + resultDTO);

            if (!resultDTO.isFailure()) {
                if (resultDTO.containsKey("totalSent")) {
                    total = (Integer) resultDTO.get("totalSent");
                    success = (Integer) resultDTO.get("totalSuccess");
                    fail = (Integer) resultDTO.get("totalFail");
                }
            }
        } catch (AppLevelException e) {
            log.error("-> Execute cmd FAIL", e);
        }

        setXmlSummaryResponse(total, success, fail, request, response);
    }

    private void setXmlSummaryResponse(Integer total, Integer success, Integer fail, HttpServletRequest request, HttpServletResponse response) {
        StringBuffer xmlResponse = new StringBuffer();

        xmlResponse.append("<?xml version=\"1.0\" ?>\n");
        xmlResponse.append("<progressBarSummary>");
        xmlResponse.append("<summary ")
                .append(" total=\"").append(total).append("\"")
                .append(" success=\"").append(success).append("\"")
                .append(" fail=\"").append(fail).append("\"")
                .append(">\n");
        xmlResponse.append(com.piramide.elwis.web.common.el.Functions.ajaxResponseFilter(composeProgressBarMessage(total, success, fail, request)));
        xmlResponse.append("</summary>");
        xmlResponse.append("</progressBarSummary>");

        setDataInResponse(response, "text/xml", xmlResponse.toString());
    }


    private String composeProgressBarMessage(Integer total, Integer success, Integer fail, HttpServletRequest request) {
        String message = "<div> " + JSPHelper.getMessage(request, "Campaign.mailing.processed") + ": " + (success + fail) + "/" + total + "</div>";
        message = message + "<div> " + JSPHelper.getMessage(request, "Campaign.mailing.success") + ": " + success + "</div>";
        message = message + "<div> " + JSPHelper.getMessage(request, "Campaign.mailing.failed") + ": " + fail + "</div>";

        return message;
    }

    private void setDataInResponse(HttpServletResponse response, String contentType, String data) {
        log.debug("Response Value:\n" + data);

        response.setContentType(contentType);
        try {
            PrintWriter write = response.getWriter();
            write.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
