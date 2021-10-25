package com.piramide.elwis.web.financemanager.action;

import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.financemanager.util.PdfDocumentMergeException;
import com.piramide.elwis.web.financemanager.util.PdfDocumentMergeUtil;
import com.piramide.elwis.web.salesmanager.el.Functions;
import com.piramide.elwis.web.utils.MimeTypeUtil;
import org.apache.struts.action.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Jatun S.R.L.
 * Action to merge documents created to invoices related to contracts
 *
 * @author Miky
 * @version $Id: ContractInvoicedMergeDocumentsAction.java 12-dic-2008 11:19:40 $
 */
public class ContractInvoicedMergeDocumentsAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing ContractInvoicedMergeDocumentsAction........" + request.getParameterMap());

        ActionErrors errors = new ActionErrors();

        List invoiceDocumentIds = new ArrayList();
        //recovery documents ids from session
        String key = Functions.composeMergeInvoiceDocumentsSessionKey(request);
        if (request.getSession().getAttribute(key) != null) {
            invoiceDocumentIds = (List) request.getSession().getAttribute(key);
            request.getSession().removeAttribute(key);
        }

        if (!invoiceDocumentIds.isEmpty()) {
            PdfDocumentMergeUtil documentMergeUtil = new PdfDocumentMergeUtil();
            try {
                byte[] document = documentMergeUtil.mergeDocument(invoiceDocumentIds);
                setDocumentInResponse(document, response, request);
            } catch (PdfDocumentMergeException e) {
                log.debug("Error in merge pdf document...");
                errors.add("error", new ActionError("ContractInvoice.generate.mergeError"));
                saveErrors(request.getSession(), errors);
                return mapping.findForward("Fail");
            }
        } else {
            return mapping.getInputForward();
        }
        return null;
    }


    /**
     * set in response merged document
     *
     * @param mergeDocument
     * @param response
     * @throws Exception
     */
    private void setDocumentInResponse(byte[] mergeDocument, HttpServletResponse response, HttpServletRequest request) throws Exception {

        log.debug("Sending document..");
        String localizedFileName = JSPHelper.getMessage(request, "InvoiceDocuments.file.name");
        String fileName = localizedFileName + ".pdf";
        int fileSize = mergeDocument.length;
        MimeTypeUtil.formatResponseToDownloadFile(fileName, fileSize, response);

        ServletOutputStream os = response.getOutputStream();
        os.write(mergeDocument);
        os.flush();
        os.close();
    }

}
