package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.cmd.campaignmanager.CampaignTemplateUtilCmd;
import com.piramide.elwis.web.common.action.DefaultAction;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.3.5
 */
public class CampaignGenerationBodySizeAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing CampaignGenerationBodySizeAction................" + request.getParameterMap());
        ActionForward forward = null;

        DefaultForm defaultForm = (DefaultForm) form;

        //verify if this from retry generation
        if (!GenericValidator.isBlankOrNull(request.getParameter("generationId"))) {
            Integer generationId = new Integer(request.getParameter("generationId"));
            retryGeneratedTemplateSize(generationId, response, request);
        } else {
            sendGenerationTemplateSize(defaultForm, response, request);
        }

        return forward;
    }

    private void sendGenerationTemplateSize(DefaultForm defaultForm, HttpServletResponse response, HttpServletRequest request) throws Exception {

        CampaignTemplateUtilCmd campaignTemplateUtilCmd = new CampaignTemplateUtilCmd();
        campaignTemplateUtilCmd.putParam("op","readTemplateBodySize");
        campaignTemplateUtilCmd.putParam("templateId", defaultForm.getDto("templateId"));
        campaignTemplateUtilCmd.putParam("attachIds", defaultForm.getDto("genAttachIds"));

        List<Map> templateInfoList = new ArrayList<Map>();
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(campaignTemplateUtilCmd, null);
            log.debug("Result Dto..:" + resultDTO);

            if (!resultDTO.isFailure()) {
                if (resultDTO.containsKey("templateSizeInfoList")) {
                    templateInfoList = (List<Map>) resultDTO.get("templateSizeInfoList");
                }
            }
        } catch (AppLevelException e) {
            log.error("-> Execute cmd FAIL", e);
        }

        setXmlResponse(templateInfoList, request, response);
    }

    private void retryGeneratedTemplateSize(Integer generationId, HttpServletResponse response, HttpServletRequest request) throws Exception {

        CampaignTemplateUtilCmd campaignTemplateUtilCmd = new CampaignTemplateUtilCmd();
        campaignTemplateUtilCmd.putParam("op","readGeneratedBodySize");
        campaignTemplateUtilCmd.putParam("generationId", generationId);

        List<Map> templateInfoList = new ArrayList<Map>();
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(campaignTemplateUtilCmd, null);
            log.debug("Result Dto..:" + resultDTO);

            if (!resultDTO.isFailure()) {
                if (resultDTO.containsKey("templateSizeInfoList")) {
                    templateInfoList = (List<Map>) resultDTO.get("templateSizeInfoList");
                }
            }
        } catch (AppLevelException e) {
            log.error("-> Execute cmd FAIL", e);
        }

        setXmlResponse(templateInfoList, request, response);
    }


    private void setXmlResponse(List<Map> templateInfoList, HttpServletRequest request, HttpServletResponse response) {
        StringBuffer xmlResponse = new StringBuffer();

        xmlResponse.append("<?xml version=\"1.0\" ?>\n");
        xmlResponse.append("<templateBodySize>");

        for (Map map : templateInfoList) {

            xmlResponse.append("<bodySize ")
                    .append(" size=\"").append(map.get("templateSize")).append("\"")
                    .append(">\n");
            xmlResponse.append(com.piramide.elwis.web.common.el.Functions.ajaxResponseFilter(map.get("languageName").toString()));
            xmlResponse.append("</bodySize>");

        }

        xmlResponse.append("</templateBodySize>");

        setDataInResponse(response, "text/xml", xmlResponse.toString());
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
