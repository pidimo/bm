package com.piramide.elwis.web.financemanager.action;

import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.financemanager.util.PdfDocumentMergeException;
import com.piramide.elwis.web.financemanager.util.PdfDocumentMergeUtil;
import com.piramide.elwis.web.utils.MimeTypeUtil;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Action to merge selected incoming invoice documents
 *
 * @author Miguel A. Rojas Cardenas
 * @version 6.4
 */
public class IncomingInvoicePrintAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing IncomingInvoicePrintAction........" + request.getParameterMap());
        DefaultForm defaultForm = (DefaultForm) form;
        List documentIdList = new ArrayList();
        ActionErrors errors = new ActionErrors();

        String validDocumentIds = (String) request.getAttribute("invoiceValidDocumentIds");
        if (!GenericValidator.isBlankOrNull(validDocumentIds)) {
            String[] idArray = validDocumentIds.split(",");
            documentIdList = Arrays.asList(idArray);
        }

        byte[] document = mergeDocument(documentIdList, errors);

        if (!errors.isEmpty()) {
            saveErrors(request.getSession(), errors);
            return mapping.findForward("Fail");
        }

        setDocumentInResponse(document, response, request);
        return null;
    }

    /**
     * Merge invoice documents
     *
     * @param documentIdList document ids of invoices
     * @param errors         errors
     * @return byte[]
     */
    private byte[] mergeDocument(List documentIdList, ActionErrors errors) {
        log.debug("Merge documents....." + documentIdList);

        byte[] resultDocument = null;

        PdfDocumentMergeUtil documentMergeUtil = new PdfDocumentMergeUtil();
        try {
            resultDocument = documentMergeUtil.mergeDocument(documentIdList);
        } catch (PdfDocumentMergeException e) {
            log.debug("Error in merge incoming invoices pdf document...");
            errors.add("error", new ActionError("IncomingInvoice.print.mergeError"));
        }

        return resultDocument;
    }

    /**
     * set in response merged document
     *
     * @param mergeDocument
     * @param response
     * @throws Exception
     */
    private void setDocumentInResponse(byte[] mergeDocument, HttpServletResponse response, HttpServletRequest request) throws Exception {
        String localizedFileName = JSPHelper.getMessage(request, "IncomingInvoice.print.mergedFileName");
        String fileName = localizedFileName + ".pdf";
        int fileSize = mergeDocument.length;
        MimeTypeUtil.formatResponseToDownloadFile(fileName, fileSize, response);
        ServletOutputStream os = response.getOutputStream();
        os.write(mergeDocument);
        os.flush();
        os.close();
    }

}
