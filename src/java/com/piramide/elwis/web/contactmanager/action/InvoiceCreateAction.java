package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Ivan Alban
 * @version 4.3
 */
public class InvoiceCreateAction extends ContactManagerAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        if (isCancelled(request)) {
            return mapping.findForward("Cancel");
        }

        ActionForward forward = super.execute(mapping, form, request, response);
        return processForward(forward, (DefaultForm) form, mapping, request);
    }

    /**
     * Process the <code>ActionForward</code> object after of EJB command execution.
     * For credit notes, the method verifies if the credit note does have associated a invoice with invoice positions
     * in this case the <code>ActionForward</code> object will change.
     *
     * @param forward     <code>ActionForward</code> object.
     * @param defaultForm <code>DefaultForm</code> object than contain the information about of recently created invoice.
     * @param mapping     <code>ActionMapping</code> object to select the new <code>ActionForward</code>.
     * @param request     <code>HttpServletRequest</code> object for general purposes.
     * @return <code>ActionForward</code> object for the next page.
     */
    @Override
    protected ActionForward processForward(ActionForward forward,
                                           DefaultForm defaultForm,
                                           ActionMapping mapping,
                                           HttpServletRequest request) {

        ActionForwardParameters parameters = new ActionForwardParameters();
        parameters.add("invoiceId", defaultForm.getDto("invoiceId").toString());
        parameters.add("dto(op)", "read");

        if (FinanceConstants.InvoiceType.CreditNote.equal((Integer) defaultForm.getDto("type"))) {
            Boolean creditNoteInvoiceHaveInvoicePositions =
                    (Boolean) defaultForm.getDto("creditNoteInvoiceHaveInvoicePositions");

            if (null != creditNoteInvoiceHaveInvoicePositions && creditNoteInvoiceHaveInvoicePositions) {
                return parameters.forward(mapping.findForward("CreateCreditNotePosition"));
            }
        }

        return parameters.forward(forward);
    }
}
