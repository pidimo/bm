package com.piramide.elwis.web.salesmanager.action;

import com.piramide.elwis.dto.catalogmanager.CurrencyDTO;
import com.piramide.elwis.web.common.action.DefaultForwardAction;
import net.java.dev.strutsejb.web.DefaultForm;

import javax.servlet.http.HttpServletRequest;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class ProductContractCreateForwardAction extends DefaultForwardAction {
    @Override
    protected void setDTOValues(DefaultForm defaultForm, HttpServletRequest request) {
        CurrencyDTO basicCurrencyDTO =
                com.piramide.elwis.web.catalogmanager.el.Functions.getBasicCurrency(request);

        if (null != basicCurrencyDTO) {
            defaultForm.setDto("currencyId", basicCurrencyDTO.get("currencyId"));
        }
    }
}
