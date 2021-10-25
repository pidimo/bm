package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.cmd.campaignmanager.DownloadFileCmd;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.el.Functions;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import com.piramide.elwis.web.utils.MimeTypeUtil;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author: ivan
 * Date: 26-10-2006: 10:35:48 AM
 */
public class DownloadFileAction extends DefaultAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        ActionErrors errors = new ActionErrors();
        errors = ForeignkeyValidator.i.validate(CampaignConstants.TABLE_CAMPAIGN, "campaignid",
                request.getParameter("campaignId"), errors, new ActionError("error.CampaignSession.NotFound"));

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("MainSearch");
        }

        DefaultForm f = (DefaultForm) form;


        String fileNameAsStr = "";
        String header = "";
        String contentType = "";

        String mod = (String) f.getDto("mod");
        if ("campaignTemplate".equals(mod)) {
            errors = ForeignkeyValidator.i.validate(CampaignConstants.TABLE_CAMPAIGN_TEMPLATE, "templateid",
                    request.getParameter("templateId"), errors, new ActionError("template.NotFound"));

            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return mapping.findForward("MainList");
            }
        }

        int freeTextId = new Integer(f.getDto("freeTextId").toString());
        String fileName = (String) f.getDto("fileName");

        DownloadFileCmd cmd = new DownloadFileCmd();
        cmd.putParam("freeTextId", freeTextId);
        cmd.putParam("documentType", request.getParameter("documentType"));


        ResultDTO resulDTO = BusinessDelegate.i.execute(cmd, request);

        if (!resulDTO.isFailure()) {
            InputStream in = (InputStream) resulDTO.get("inputStream");

            int fileSize = (Integer) resulDTO.get("fileSize");
            if ("campaignTemplate".equals(mod)) {
                fileName = getCampaignTemplateDocumentName(freeTextId);
            }

            MimeTypeUtil.formatResponseToDownloadFile(fileName, fileSize, response);

            ServletOutputStream outStream = response.getOutputStream();
            //Writing the blob
            try {
                MimeTypeUtil.copy(outStream, in);
            }
            catch (IOException ioex) {
                log.debug("ERROR IN ReadAttachAction, IN Writing the attach");
            } finally {
                in.close();
                outStream.close();
            }

            return null;
        } else {
            errors.add("filenotfoud", new ActionError("TemplateFile.error.find"));
            saveErrors(request, errors);
            return mapping.findForward("Fail");
        }
    }

    /**
     * Get document name from freetext id
     * @param freeTextId id
     * @return Strign
     */
    private String getCampaignTemplateDocumentName(Integer freeTextId) {
        log.debug("Get campaign template document name with freetext...." + freeTextId);
        String fileName = null;
        if (freeTextId != null) {
            fileName = Functions.getFreTextDocumentName(freeTextId);
        }
        if (fileName == null) {
            fileName = "Letter.doc";
        }
        return fileName;
    }

}
