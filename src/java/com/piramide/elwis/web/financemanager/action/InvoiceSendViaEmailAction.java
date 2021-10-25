package com.piramide.elwis.web.financemanager.action;

import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.financemanager.util.InvoiceSendViaEmailUtil;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Action to send invoices via email
 * @author Miguel A. Rojas Cardenas
 * @version 6.1
 */
public class InvoiceSendViaEmailAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing InvoiceSendViaEmailAction........" + request.getParameterMap());
        ActionForward cancelForward = checkCancel(mapping, request);
        if (cancelForward != null) {
            return cancelForward;
        }

        DefaultForm defaultForm = (DefaultForm) form;

        Integer telecomTypeId = new Integer(defaultForm.getDto("telecomTypeId").toString());
        Integer mailAccountId = new Integer(defaultForm.getDto("mailAccountId").toString());
        List<Integer> invoiceIdList = getInvoiceIdList(defaultForm);

        InvoiceSendViaEmailUtil invoiceSendViaEmailUtil = new InvoiceSendViaEmailUtil();
        Map summaryMap = invoiceSendViaEmailUtil.sendEmailsForInvoices(invoiceIdList, mailAccountId, telecomTypeId, request);

        request.setAttribute("sendInvoicesSummaryMap", summaryMap);

        return mapping.findForward("Success");
    }

    private List<Integer> getInvoiceIdList(DefaultForm defaultForm) {
        List<Integer> invoiceIdList = new ArrayList<Integer>();

        String invoiceIds = (String) defaultForm.getDto("sendViaEmailInvoices");
        if (!GenericValidator.isBlankOrNull(invoiceIds)) {
            String[] idsArray = invoiceIds.split(",");
            for (int i = 0; i < idsArray.length; i++) {
                String id = idsArray[i];
                Integer invoiceId = null;
                try {
                    invoiceId = Integer.valueOf(id);
                } catch (NumberFormatException ignore) {
                }

                if (invoiceId != null) {
                    invoiceIdList.add(invoiceId);
                }
            }
        }
        return invoiceIdList;
    }
}
