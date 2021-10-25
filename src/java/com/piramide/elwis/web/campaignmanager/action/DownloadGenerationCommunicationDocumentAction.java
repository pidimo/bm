package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.cmd.campaignmanager.GenerationCommunicationTextReadCmd;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.web.common.action.AbstractDefaultAction;
import com.piramide.elwis.web.common.util.MessagesUtil;
import com.piramide.elwis.web.utils.MimeTypeUtil;
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
 * Jatun S.R.L.
 * Action to download communication document generated from campaign generation
 *
 * @author Miky
 * @version $Id: DownloadGenerationCommunicationDocumentAction.java 9761 2009-09-29 19:43:01Z miguel $
 */
public class DownloadGenerationCommunicationDocumentAction extends AbstractDefaultAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        GenerationCommunicationTextReadCmd templateTextReadCmd = new GenerationCommunicationTextReadCmd();
        templateTextReadCmd.putParam("contactId", ((DefaultForm) form).getDto("contactId"));
        ResultDTO resultDTO = BusinessDelegate.i.execute(templateTextReadCmd, null);

        if (resultDTO.isFailure() || resultDTO.get("freeText") == null) {
            log.debug("Fail Download Document");
            ActionErrors errors = MessagesUtil.i.convertToActionErrors(mapping, request, resultDTO);
            saveErrors(request, errors);
            return mapping.findForward("Fail");
        }

        log.debug("Sending document");
        ArrayByteWrapper textByteWrapper = (ArrayByteWrapper) resultDTO.get("freeText");
        String fileName = getDocumentName((String) resultDTO.get("communicationSubject"));
        int fileSize = textByteWrapper.getFileData().length;
        MimeTypeUtil.formatResponseToDownloadFile(fileName, fileSize, response);

        ServletOutputStream os = response.getOutputStream();
        os.write(textByteWrapper.getFileData());
        os.flush();
        os.close();
        return null;
    }

    private String getDocumentName(String commSubject) {
        String fileName = null;
        if (!GenericValidator.isBlankOrNull(commSubject)) {
            fileName = commSubject + ".doc";
        } else {
            fileName = "Letter.doc";
        }
        return fileName;
    }

}