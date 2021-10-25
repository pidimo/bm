package com.piramide.elwis.web.financemanager.action;

import com.piramide.elwis.dto.financemanager.InvoiceDTO;
import com.piramide.elwis.web.common.action.DefaultForwardAction;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.salesmanager.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class InvoicePaymentCreateForwardAction extends DefaultForwardAction {

    @Override
    protected ActionForward validateElementExistence(HttpServletRequest request,
                                                     ActionMapping mapping) {
        if (!Functions.existsInvoice(request.getParameter("invoiceId"))) {
            ActionErrors errors = new ActionErrors();
            errors.add("InvoiceNotFound", new ActionError("Invoice.NotFound"));
            saveErrors(request.getSession(), errors);

            //return main list, invoiceSingleList is gloval forward
            return mapping.findForward("MainInvoiceList");
        }

        return null;
    }

    @Override
    protected void setDTOValues(DefaultForm defaultForm, HttpServletRequest request) {
        setDefaultBankAccount(defaultForm, request);

        String invoiceId = request.getParameter("invoiceId");

        boolean invoiceIsCreditNote =
                com.piramide.elwis.web.financemanager.el.Functions.invoiceIsCreditNote(invoiceId, request);

        if (invoiceIsCreditNote) {
            InvoiceDTO invoiceDTO = (InvoiceDTO) request.getAttribute("invoiceDTO");
            if (null != invoiceDTO) {
                String message = JSPHelper.getMessage(request, "InvoicePayment.text.creditNoteMessage", invoiceDTO.get("number"));
                defaultForm.setDto("text", message);
            }

            InvoiceDTO creditNoteDTO = (InvoiceDTO) request.getAttribute("creditNoteDTO");
            if (null != creditNoteDTO) {
                //set default amount
                defaultForm.setDto("amount", creditNoteDTO.get("openAmount"));
            }

        } else {
            InvoiceDTO invoiceDTO = (InvoiceDTO) request.getAttribute("invoiceDTO");
            if (null != invoiceDTO) {
                //set default amount
                defaultForm.setDto("amount", invoiceDTO.get("openAmount"));
            }
        }
    }

    /**
     * set the bank account only if the company has registered only one bank account
     * @param defaultForm
     * @param request
     */
    private void setDefaultBankAccount(DefaultForm defaultForm, HttpServletRequest request) {
        Integer bankAccountId = com.piramide.elwis.web.contactmanager.el.Functions.getOnlyOneCompanyBankAccountId(request);

        if (bankAccountId != null) {
            defaultForm.setDto("bankAccountId", bankAccountId);
        }
    }
}
