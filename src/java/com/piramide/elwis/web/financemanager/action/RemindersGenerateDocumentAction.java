package com.piramide.elwis.web.financemanager.action;

import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import com.piramide.elwis.web.financemanager.util.InvoiceGenerateDocumentUtil;
import com.piramide.elwis.web.salesmanager.el.Functions;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 * Generate or download reminder document
 *
 * @author Miky
 * @version $Id: RemindersGenerateDocumentAction.java 12-nov-2008 13:07:45 $
 */
public class RemindersGenerateDocumentAction extends DownloadReminderGenDocumentAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        String reminderId = request.getParameter("reminderId");
        ActionErrors errors = new ActionErrors();
        ActionForward forward = validateConcurrenceParameters(reminderId, mapping, request, errors);

        if (forward != null) {
            return forward;
        }

        if (!GenericValidator.isBlankOrNull(request.getParameter("dto(freeTextId)"))) {
            //download document
            forward = super.execute(mapping, form, request, response);
        } else {
            //process with the generation
            InvoiceGenerateDocumentUtil generateUtil = new InvoiceGenerateDocumentUtil();
            errors = generateUtil.generateReminderDocument(new Integer(reminderId), mapping, request);
            if (errors.isEmpty()) {
                //onload to download document
                String js = "onLoad=\"downloadReminder(\'" + generateUtil.getReminderDocumentId() + "\');\"";
                request.setAttribute("jsLoad", js);
                forward = mapping.findForward("Success");
            } else {
                saveErrors(request.getSession(), errors);
                forward = mapping.findForward("Fail");
            }
        }

        return forward;
    }

    private ActionForward validateConcurrenceParameters(String reminderId, ActionMapping mapping, HttpServletRequest request, ActionErrors errors) {

        //validate invoice existence
        if (!Functions.existsInvoice(request.getParameter("invoiceId"))) {
            errors.add("InvoiceNotFound", new ActionError("Invoice.NotFound"));
            saveErrors(request.getSession(), errors);
            //return main list, invoiceSingleList is gloval forward
            return mapping.findForward("MainInvoiceList");
        }

        //validate remainder existence
        if (reminderId != null) {
            errors = ForeignkeyValidator.i.validate(FinanceConstants.TABLE_INVOICEREMINDER, "reminderid", reminderId,
                    errors, new ActionError("InvoiceReminder.NotFound"));

            if (!errors.isEmpty()) {
                saveErrors(request.getSession(), errors);
                return mapping.findForward("Fail");
            }
        } else {
            errors.add("fail", new ActionError("InvoiceReminder.NotFound"));
            saveErrors(request.getSession(), errors);
            return mapping.findForward("Fail");
        }

        return null;
    }

}
