package com.piramide.elwis.web.salesmanager.action;

import com.piramide.elwis.web.salesmanager.el.Functions;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Action to invoice sale from sales process
 *
 * @author Miky
 * @version $Id: ContractInvoiceFromSaleBySalesProcessAction.java 2009-08-03 04:30:48 PM $
 */
public class ContractInvoiceFromSaleBySalesProcessAction extends ContractInvoiceFromSaleAction {
    @Override
    protected ActionForward validateElementExistence(HttpServletRequest request, ActionMapping mapping) {

        //verify sales process
        ActionErrors errors = Functions.existSalesProcess(request);
        if (null != errors) {
            saveErrors(request.getSession(), errors);
            return mapping.findForward("MainSearch");
        }

        //verify sale
        errors = Functions.existsSale(request);
        if (null != errors) {
            saveErrors(request.getSession(), errors);
            return mapping.findForward("MainSaleBySalesProcessList");
        }

        return null;
    }

}
