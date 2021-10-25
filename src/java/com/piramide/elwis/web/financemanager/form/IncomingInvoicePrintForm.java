package com.piramide.elwis.web.financemanager.form;

import com.piramide.elwis.web.financemanager.util.PdfDocumentMergeUtil;
import com.piramide.elwis.web.financemanager.util.PdfDocumentProtectedException;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Form to manage list selected items
 * @author Miguel A. Rojas Cardenas
 * @version 6.4
 */
public class IncomingInvoicePrintForm extends DefaultForm {
    private Log log = LogFactory.getLog(this.getClass());

    public Object[] getSelected() {
        List list = (List) this.getDto("selected");
        if (list != null) {
            return list.toArray();
        }
        return new Object[]{};
    }

    public void setSelected(Object[] checkArray) {
        if (checkArray != null) {
            this.setDto("selected", Arrays.asList(checkArray));
        }
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("Excecuting validate IncomingInvoicePrintForm......." + getDtoMap());

        ActionErrors errors = super.validate(mapping, request);

        if (isGenerateInvoiceValidDocuments()) {
            setValidDocumentIds(getInvoiceValidDocumentIds(), request);
            return errors;
        }

        List validDocumentIdList = validateInvoiceDocuments(errors);
        setValidDocumentIds(validDocumentIdList, request);

        if (!errors.isEmpty() && !validDocumentIdList.isEmpty()) {
            //to generate valid documents in onLoad event
            request.setAttribute("jsLoad", "onLoad=\"generateValidDocuments()\"");
        }

        return errors;
    }

    private List validateInvoiceDocuments(ActionErrors errors) {
        List validDocumentIdList = new ArrayList();

        List documentIdList = getSelectedInvoiceDocumentIds();
        if (isPrintAllInvoices()) {
            documentIdList = getAllInvoiceDocumentIds();
        }

        if (documentIdList.isEmpty()) {
            errors.add("empty", new ActionError("IncomingInvoice.print.emptyError"));
        } else {
            validDocumentIdList = checkForPDFProtectedDocuments(documentIdList, errors);
        }
        return validDocumentIdList;
    }

    private List checkForPDFProtectedDocuments(List documentIdList, ActionErrors errors) {
        List validDocumentIdList = documentIdList;

        if (!documentIdList.isEmpty() && documentIdList.size() > 1) {
            validDocumentIdList = new ArrayList();
            PdfDocumentMergeUtil pdfDocumentUtil = new PdfDocumentMergeUtil();

            for (int i = 0; i < documentIdList.size(); i++) {
                String freeTextId = documentIdList.get(i).toString();
                try {
                    if (pdfDocumentUtil.isProtectedPDFDocument(new Integer(freeTextId))) {
                        errors.add("error" + i, new ActionError("IncomingInvoice.print.pdfProtectedError", getDto(freeTextId)));
                    } else {
                        validDocumentIdList.add(freeTextId);
                    }
                } catch (PdfDocumentProtectedException e) {
                    errors.add("error" + i, new ActionError("IncomingInvoice.print.pdfProtectedError", getDto(freeTextId)));
                }
            }
        }
        return validDocumentIdList;
    }

    private boolean isPrintAllInvoices() {
        return Boolean.valueOf(getDto("isPrintAll").toString());
    }

    private boolean isGenerateInvoiceValidDocuments() {
        return Boolean.valueOf(getDto("isGenerateValidDocuments").toString());
    }

    private List getSelectedInvoiceDocumentIds() {
        return (getDto("selected") == null) ? new ArrayList() : (List) getDto("selected");
    }

    private List getAllInvoiceDocumentIds() {
        List allDocumentIdList = new ArrayList();

        String allPrintIds = (String) getDto("printAllIds");
        if (!GenericValidator.isBlankOrNull(allPrintIds)) {
            String[] printArray = allPrintIds.split(",");
            allDocumentIdList = Arrays.asList(printArray);
        }
        return allDocumentIdList;
    }

    private List getInvoiceValidDocumentIds() {
        List validDocumentIdList = new ArrayList();

        String validIds = (String) getDto("validDocumentIds");
        if (!GenericValidator.isBlankOrNull(validIds)) {
            String[] printArray = validIds.split(",");
            validDocumentIdList = Arrays.asList(printArray);
        }
        return validDocumentIdList;
    }

    private void setValidDocumentIds(List documentIdList, HttpServletRequest request) {
        String ids = "";

        for (Iterator iterator = documentIdList.iterator(); iterator.hasNext();) {
            String freeTextId = iterator.next().toString();
            ids += freeTextId;
            if (iterator.hasNext()) {
                ids += ",";
            }
        }
        request.setAttribute("invoiceValidDocumentIds", ids);
    }

}

