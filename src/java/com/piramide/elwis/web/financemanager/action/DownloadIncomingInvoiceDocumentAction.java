package com.piramide.elwis.web.financemanager.action;

import com.piramide.elwis.cmd.campaignmanager.DownloadDocumentCmd;
import com.piramide.elwis.cmd.financemanager.IncomingInvoiceCmd;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.web.common.action.AbstractDefaultAction;
import com.piramide.elwis.web.common.util.JSPHelper;
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
 * Action to download incoming invoice document
 * @author Miguel A. Rojas Cardenas
 * @version 6.4
 */
public class DownloadIncomingInvoiceDocumentAction extends AbstractDefaultAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
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
        String fileName = composeFileName(defaultForm, request) + ".pdf";
        int fileSize = textByteWrapper.getFileData().length;

        MimeTypeUtil.formatResponseToDownloadFile(fileName, fileSize, response);

        ServletOutputStream os = response.getOutputStream();
        os.write(textByteWrapper.getFileData());
        os.flush();
        os.close();
        return null;
    }

    private String composeFileName(DefaultForm defaultForm, HttpServletRequest request) {
        String localizedFileName = JSPHelper.getMessage(request, "IncomingInvoice.file.name");

        StringBuffer fileName = new StringBuffer();
        fileName.append(localizedFileName);

        if (defaultForm.getDto("incomingInvoiceId") != null && !GenericValidator.isBlankOrNull((String) defaultForm.getDto("incomingInvoiceId"))) {

            IncomingInvoiceCmd incomingInvoiceCmd = new IncomingInvoiceCmd();
            incomingInvoiceCmd.setOp("lightRead");
            incomingInvoiceCmd.putParam("incomingInvoiceId", defaultForm.getDto("incomingInvoiceId"));
            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(incomingInvoiceCmd, request);
                if (!resultDTO.isFailure()) {
                    fileName.append("_").append(resultDTO.get("invoiceNumber"));
                }
            } catch (AppLevelException e) {
                log.debug("Error in read Invoice with..." + defaultForm.getDtoMap(), e);
            }
        }
        return fileName.toString();
    }
}