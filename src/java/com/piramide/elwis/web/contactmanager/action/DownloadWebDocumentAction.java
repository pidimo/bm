package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.cmd.campaignmanager.DownloadDocumentCmd;
import com.piramide.elwis.cmd.contactmanager.LightlyContactReadCmd;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.web.common.action.AbstractDefaultAction;
import com.piramide.elwis.web.common.util.MessagesUtil;
import com.piramide.elwis.web.utils.MimeTypeUtil;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Action to download web document
 *
 * @author Miguel A. Rojas Cardenas
 * @version 5.5
 */
public class DownloadWebDocumentAction extends AbstractDefaultAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing DownloadWebDocumentAction........" + request.getParameterMap());

        DefaultForm defaultForm = (DefaultForm) form;
        DownloadDocumentCmd downloadCmd = new DownloadDocumentCmd();
        downloadCmd.putParam("freeTextId", defaultForm.getDto("freeTextId"));

        ResultDTO resultDTO = BusinessDelegate.i.execute(downloadCmd, request);

        if (resultDTO.isFailure()) {
            log.debug("Fail Download Document");
            ActionErrors errors = MessagesUtil.i.convertToActionErrors(mapping, request, resultDTO);
            saveErrors(request, errors);
            return mapping.findForward("Fail");
        }

        ArrayByteWrapper textByteWrapper = (ArrayByteWrapper) resultDTO.get("freeText");
        String fileName = composeFileName(defaultForm, request);
        int fileSize = textByteWrapper.getFileData().length;

        MimeTypeUtil.formatResponseToDownloadFile(fileName, fileSize, response);

        ServletOutputStream os = response.getOutputStream();
        os.write(textByteWrapper.getFileData());
        os.flush();
        os.close();
        return null;
    }

    private String composeFileName(DefaultForm defaultForm, HttpServletRequest request) {
        String fileName = null;

        if (defaultForm.getDto("contactId") != null && !GenericValidator.isBlankOrNull((String) defaultForm.getDto("contactId"))) {
            fileName = getCommunicationWebDocumentName(new Integer(defaultForm.getDto("contactId").toString()), request);
        }

        if (fileName == null) {
            fileName = "unknown.pdf";
        }

        return fileName;
    }

    public static String getCommunicationWebDocumentName(Integer contactId, HttpServletRequest request) {
        String fileName = null;

        if (contactId != null) {

            LightlyContactReadCmd contactReadCmd = new LightlyContactReadCmd();
            contactReadCmd.putParam("contactId", contactId);
            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(contactReadCmd, request);
                if (!resultDTO.isFailure()) {

                    if (resultDTO.get("documentFileName") != null) {
                        fileName = resultDTO.get("documentFileName").toString();
                    } else if (resultDTO.get("note") != null) {
                        fileName = resultDTO.get("note").toString() + ".pdf";
                    }
                }
            } catch (AppLevelException e) {
            }
        }

        return fileName;
    }
}