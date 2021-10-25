package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.cmd.campaignmanager.CampaignTemplateCmd;
import com.piramide.elwis.cmd.catalogmanager.LanguageCmd;
import com.piramide.elwis.cmd.utils.ElwisCacheManager;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.utils.MimeTypeUtil;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultAction;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Jatun S.R.L.
 * Download campaign generate document form elwis cache directory
 *
 * @author Miky
 * @version $Id: CampaignDocumentDownloadAction.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class CampaignDocumentDownloadAction extends DefaultAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing CampaignDocumentDownloadAction...." + request.getParameterMap());
        String campaignId = request.getParameter("campaignId");
        String templateId = request.getParameter("templateId");
        String languageId = request.getParameter("languageId");

        String templateName = null;
        String languageName = null;

        User user = RequestUtils.getUser(request);
        String userId = user.getValue("userId").toString();
        String companyId = user.getValue("companyId").toString();

        //read language and template name
        LanguageCmd languageCmd = new LanguageCmd();
        languageCmd.putParam("languageId", languageId);
        languageCmd.putParam("op", "read");

        CampaignTemplateCmd templateCmd = new CampaignTemplateCmd();
        templateCmd.putParam("templateId", templateId);
        templateCmd.putParam("op", "read");

        ResultDTO resultDTO = null;
        try {
            resultDTO = BusinessDelegate.i.execute(languageCmd, request);
            languageName = (String) resultDTO.get("languageName");

            resultDTO = BusinessDelegate.i.execute(templateCmd, request);
            templateName = (String) resultDTO.get("description");
        } catch (AppLevelException e) {
            log.debug("Error in read language and template names...", e);
        }

        String generatedFilePath = ElwisCacheManager.pathGeneratedCampaignDocumentFile(companyId, campaignId, userId, templateId, languageId);

        //read file
        byte[] fileData = new byte[0];
        try {
            FileInputStream fileInputStream = new FileInputStream(generatedFilePath);
            fileData = new byte[fileInputStream.available()];
            fileInputStream.read(fileData);
            fileInputStream.close();
        } catch (IOException e) {
            log.debug("Can not read File..." + generatedFilePath, e);
            return null;
        }

        //set in response
        String filename = composeDocumentName(templateName, languageName);
        int fileSize = fileData.length;
        MimeTypeUtil.formatResponseToDownloadFile(filename, fileSize, response);
        ServletOutputStream os = response.getOutputStream();
        os.write(fileData);
        os.flush();
        os.close();

        return null;
    }

    private String composeDocumentName(String templateName, String languageName) {
        Date date = new Date();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYYMMdd");
        if (templateName == null) {
            templateName = "";
        }
        if (languageName == null) {
            languageName = "";
        }
        return templateName + "_" + languageName + "_" + formatter.print(date.getTime()) + ".doc";
    }
}