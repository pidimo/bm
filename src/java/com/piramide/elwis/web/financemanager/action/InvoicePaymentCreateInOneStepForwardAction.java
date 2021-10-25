package com.piramide.elwis.web.financemanager.action;

import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.ForwardAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 0.2
 */
public class InvoicePaymentCreateInOneStepForwardAction extends ForwardAction {
    private Log log = LogFactory.getLog(this.getClass());

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Executing InvoiceCustomerDataExportReportAction.... " );

        setDefaultBankAccount((DefaultForm) form, request);
        return super.execute(mapping, form, request, response);
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
