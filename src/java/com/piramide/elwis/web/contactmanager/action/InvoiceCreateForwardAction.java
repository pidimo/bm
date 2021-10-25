package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.dto.contactmanager.CustomerDTO;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.ForwardAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class InvoiceCreateForwardAction extends ForwardAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        DefaultForm defaultForm = (DefaultForm) form;
        com.piramide.elwis.web.salesmanager.el.Functions.setDefaultOptionsForInvoice(defaultForm, request);

        String addressId = request.getParameter("contactId");
        if (null != addressId &&
                !"".equals(addressId.trim())) {
            CustomerDTO customerDTO = com.piramide.elwis.web.contactmanager.el.Functions.getCustomer(
                    Integer.valueOf(addressId),
                    request);
            if (null != customerDTO) {
                defaultForm.setDto("payConditionId", customerDTO.get("payConditionId"));

                defaultForm.setDto("sentAddressId", null != customerDTO.get("invoiceAddressId") ? customerDTO.get("invoiceAddressId").toString() : "");
                defaultForm.setDto("sentContactPersonId", null != customerDTO.get("invoiceContactPersonId") ? customerDTO.get("invoiceContactPersonId").toString() : "");
                defaultForm.setDto("additionalAddressId", null != customerDTO.get("additionalAddressId") ? customerDTO.get("additionalAddressId").toString() : "");
            }
            defaultForm.setDto("addressId", addressId);
        }

        return super.execute(mapping, defaultForm, request, response);
    }
}
