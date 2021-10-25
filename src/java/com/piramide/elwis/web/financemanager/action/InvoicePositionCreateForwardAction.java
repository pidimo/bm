package com.piramide.elwis.web.financemanager.action;

import com.piramide.elwis.cmd.financemanager.InvoicePositionCmd;
import com.piramide.elwis.dto.financemanager.InvoiceDTO;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.DefaultForwardAction;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.salesmanager.el.Functions;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class InvoicePositionCreateForwardAction extends DefaultForwardAction {
    private Log log = LogFactory.getLog(InvoicePositionCreateForwardAction.class);

    @Override
    protected ActionForward validateElementExistence(HttpServletRequest request, ActionMapping mapping) {
        ActionForward forward;
        if (null != (forward = validateInvoiceExistence(request, mapping))) {
            return forward;
        }

        if (null != (forward = validateInvoicePayments(request, mapping))) {
            return forward;
        }

        return null;
    }

    @Override
    protected void setDTOValues(DefaultForm defaultForm, HttpServletRequest request) {
        defaultForm.setDto("quantity", 1);
        defaultForm.setDto("number", getNextPositionNumber(request));
    }

    private Integer getNextPositionNumber(HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

        InvoicePositionCmd invoicePositionCmd = new InvoicePositionCmd();
        invoicePositionCmd.putParam("invoiceId", Integer.valueOf(request.getParameter("invoiceId")));
        invoicePositionCmd.putParam("companyId", companyId);
        invoicePositionCmd.setOp("getLastPositionNumber");

        Integer result = null;
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(invoicePositionCmd, request);
            result = (Integer) resultDTO.get("getLastPositionNumber");
        } catch (AppLevelException e) {
            log.error("-> Execute " + InvoicePositionCmd.class.getName() + " FAIL", e);
        }

        if (null == result) {
            result = 0;
        }

        return result + 1;
    }

    private ActionForward validateInvoicePayments(HttpServletRequest request, ActionMapping mapping) {
        String invoiceId = request.getParameter("invoiceId");
        InvoiceDTO invoiceDTO =
                com.piramide.elwis.web.financemanager.el.Functions.getInvoice(invoiceId, request);

        boolean haveInvoicePayments = (Boolean) invoiceDTO.get("haveInvoicePayments");

        if (FinanceConstants.InvoiceType.Invoice.equal((Integer) invoiceDTO.get("type")) &&
                haveInvoicePayments) {
            ActionErrors errors = new ActionErrors();
            errors.add("haveInvoicePaymentsError", new ActionError("InvoicePosition.haveInvoicePaymentsError"));
            saveErrors(request, errors);
            return mapping.findForward("Fail");
        }

        return null;
    }

    protected ActionForward validateInvoiceExistence(HttpServletRequest request,
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

}
