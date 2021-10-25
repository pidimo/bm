package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.dto.contactmanager.CustomerDTO;
import com.piramide.elwis.utils.SalesConstants;
import com.piramide.elwis.web.common.action.DefaultForwardAction;
import net.java.dev.strutsejb.web.DefaultForm;

import javax.servlet.http.HttpServletRequest;

/**
 * Stting up in salePositionForm default values for
 * quantity
 * quantity = 1
 *
 * @author : ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class SalePositionCreateForwardAction extends DefaultForwardAction {

    @Override
    protected void setDTOValues(DefaultForm defaultForm, HttpServletRequest request) {
        String addressId = request.getParameter("contactId");
        if (null != addressId &&
                !"".equals(addressId.trim())) {
            defaultForm.setDto("quantity", "1");
            defaultForm.setDto("active", "true");
            defaultForm.setDto("payMethod", SalesConstants.PayMethod.SingleWithoutContract.getConstant());

            CustomerDTO customerDTO = com.piramide.elwis.web.contactmanager.el.Functions.getCustomer(Integer.valueOf(addressId), request);
            if (null != customerDTO) {
                defaultForm.setDto("discount", customerDTO.get("defaultDiscount"));
            }
        }
    }
}
