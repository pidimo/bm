package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.dto.catalogmanager.CurrencyDTO;
import com.piramide.elwis.utils.SalesConstants;
import com.piramide.elwis.web.common.action.DefaultForwardAction;
import com.piramide.elwis.web.contactmanager.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * @author : ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class ProductContractCreateForwardAction extends DefaultForwardAction {

    @Override
    protected ActionForward validateElementExistence(HttpServletRequest request,
                                                     ActionMapping mapping) {
        if (!Functions.existsAddress(request.getParameter("contactId"))) {
            ActionErrors errors = new ActionErrors();
            errors.add("general", new ActionError("Address.NotFound"));
            saveErrors(request, errors);
            return mapping.findForward("MainSearch");
        }

        return null;
    }

    @Override
    protected void setDTOValues(DefaultForm defaultForm, HttpServletRequest request) {
        String addressId = request.getParameter("contactId");
        if (null != addressId &&
                !"".equals(addressId.trim())) {
            String addressName = com.piramide.elwis.web.contactmanager.el.Functions.getAddressName(addressId);

            CurrencyDTO basicCurrencyDTO =
                    com.piramide.elwis.web.catalogmanager.el.Functions.getBasicCurrency(request);

            if (null != basicCurrencyDTO) {
                defaultForm.setDto("currencyId", basicCurrencyDTO.get("currencyId"));
            }

            defaultForm.setDto("addressId", addressId);
            defaultForm.setDto("addressName", addressName);
            defaultForm.setDto("openAmount", new BigDecimal(0.0));
            defaultForm.setDto("payMethod", SalesConstants.PayMethod.Single.getConstantAsString());
        }
    }
}
