package com.piramide.elwis.web.financemanager.action;

import com.piramide.elwis.cmd.campaignmanager.DownloadDocumentCmd;
import com.piramide.elwis.cmd.financemanager.InvoiceReadCmd;
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
 * Jatun S.R.L.
 * Action to download invoice generated document
 *
 * @author Miky
 * @version $Id: DownloadInvoiceGenDocumentAction.java 18-sep-2008 9:08:18 $
 */
public class DownloadInvoiceGenDocumentAction extends AbstractDefaultAction {

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
        String localizedFileName = JSPHelper.getMessage(request, "Invoice.file.name");

        StringBuffer fileName = new StringBuffer();
        fileName.append(localizedFileName);

        if (defaultForm.getDto("invoiceId") != null && !GenericValidator.isBlankOrNull((String) defaultForm.getDto("invoiceId"))) {

            InvoiceReadCmd invoiceReadCmd = new InvoiceReadCmd();
            invoiceReadCmd.putParam("invoiceId", defaultForm.getDto("invoiceId"));
            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(invoiceReadCmd, request);
                if (!resultDTO.isFailure()) {
                    fileName.append("_").append(resultDTO.get("number"));
                }
            } catch (AppLevelException e) {
                log.debug("Error in read Invoice with..." + defaultForm.getDtoMap(), e);
            }
        }
        return fileName.toString();
    }
}