package com.piramide.elwis.web.salesmanager.form;

import com.piramide.elwis.dto.salesmanager.SaleDTO;
import com.piramide.elwis.web.salesmanager.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class SaleForm extends DefaultForm {
    protected SaleDTO saleDTO;

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        if ("update".equals(getDto("op").toString()) &&
                !Functions.existsSale(getDto("saleId"))) {
            return new ActionErrors();
        }


        if (!isSaveButtonPressed(request)) {
            ActionErrors errors = new ActionErrors();
            errors.add("emptyError", new ActionError("Admin.Company.new"));
            request.setAttribute("skipErrors", "true");

            if (isSubmitFromSearchContactPopup(request)) {
                updateDefaultCustomerInfo(request);
            }

            return errors;
        }

        ActionErrors errors = super.validate(mapping, request);

        com.piramide.elwis.web.common.el.Functions.emptyOrOnlyOneSelectedValidation(this, "sentAddressId", "additionalAddressId", "Sale.sentAddress", "Sale.additionalAddress", errors, request);

        ActionError netGrossChangeError = Functions.validateNetGrossField(this, request);
        if (null != netGrossChangeError) {
            errors.add("netGrossChangeError", netGrossChangeError);
        }

        if (errors.isEmpty()) {
            List<ActionError> errorList = Functions.checkNetGrossChange(this, request);
            for (int i = 0; i < errorList.size(); i++) {
                ActionError actionError = errorList.get(i);
                errors.add("error_" + i, actionError);
            }
        }
        return errors;
    }


    private boolean isSaveButtonPressed(HttpServletRequest request) {
        return null != request.getParameter("save") || null != request.getParameter("invoiceFromSale");
    }

    private boolean isSubmitFromSearchContactPopup(HttpServletRequest request) {
        return "searchAddress".equals(request.getParameter("submitPopupName"));
    }

    private void updateDefaultCustomerInfo(HttpServletRequest request) {
        String addressId = (String) getDto("addressId");
        if (addressId != null && !"".equals(addressId.trim())) {
            Functions.setSaleCustomerDefaultValues(this, Integer.valueOf(addressId), request);
        }
    }

}
