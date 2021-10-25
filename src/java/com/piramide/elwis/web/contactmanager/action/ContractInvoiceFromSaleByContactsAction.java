package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import com.piramide.elwis.web.salesmanager.action.ContractInvoiceFromSaleAction;
import com.piramide.elwis.web.salesmanager.el.Functions;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Action to invoice sale from contacts
 *
 * @author Miky
 * @version $Id: ContractInvoiceFromSaleByContactsAction.java 2009-08-04 03:36:28 PM $
 */
public class ContractInvoiceFromSaleByContactsAction extends ContractInvoiceFromSaleAction {
    @Override
    protected ActionForward validateElementExistence(HttpServletRequest request, ActionMapping mapping) {

        ActionErrors errors = new ActionErrors();

        //verify for address
        if (request.getParameter("contactId") != null) {
            errors = ForeignkeyValidator.i.validate(ContactConstants.TABLE_ADDRESS, "addressid",
                    request.getParameter("contactId"), errors, new ActionError("Address.NotFound"));
            if (!errors.isEmpty()) {
                saveErrors(request.getSession(), errors);
                return mapping.findForward("MainSearch");
            }
        } else {
            errors.add("general", new ActionError("Address.NotFound"));
            saveErrors(request.getSession(), errors);
            return mapping.findForward("MainSearch");
        }

        //verify sale
        errors = Functions.existsSale(request);
        if (null != errors) {
            saveErrors(request.getSession(), errors);
            return mapping.findForward("SaleList");
        }

        return null;
    }

}
