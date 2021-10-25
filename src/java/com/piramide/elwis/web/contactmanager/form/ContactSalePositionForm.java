package com.piramide.elwis.web.contactmanager.form;

import com.piramide.elwis.web.common.util.FormatUtils;
import com.piramide.elwis.web.common.validator.FieldChecks;
import com.piramide.elwis.web.salesmanager.el.Functions;
import com.piramide.elwis.web.salesmanager.form.SalePositionForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * @author Ivan Alban
 * @version 4.3
 */
public class ContactSalePositionForm extends SalePositionForm {
    @Override
    protected boolean existMainElement(HttpServletRequest request) {
        String op = (String) getDto("op");

        String saleId = (String) getDto("saleId");
        if (!GenericValidator.isBlankOrNull(saleId)) {
            boolean existSale = Functions.existsSale(saleId);
            if (existSale) {
                this.saleDTO = Functions.getSaleDTO(saleId, request);
            }

            if ("create".equals(op)) {
                return existSale;
            }

            boolean existSalePosition = Functions.existsSalePosition(getDto("salePositionId"));
            return existSale && existSalePosition;
        }

        if ("update".equals(op) || "delete".equals(op)) {
            return Functions.existsSalePosition(getDto("salePositionId"));
        }

        return true;
    }

    @Override
    protected ActionError validateUnitPrice(HttpServletRequest request) {
        if (null == saleDTO) {
            String price = (String) getDto("unitPrice");
            if (GenericValidator.isBlankOrNull(price)) {
                return null;
            }

            String msg = "SalePosition.unitPrice";

            ActionError decimalValidation =
                    FieldChecks.validateDecimalNumber(price, msg, 10, 4, request);
            if (null == decimalValidation) {
                setDto("unitPrice", new BigDecimal(FormatUtils.unformatDecimalNumber(price, 10, 4, request)));
            }

            return decimalValidation;
        }

        return super.validateUnitPrice(request);
    }

    @Override
    protected ActionError validateSaleNetGross(HttpServletRequest request) {
        //In products exists some salepositions than are not related with sale
        if (null == saleDTO) {
            return null;
        }

        return super.validateSaleNetGross(request);
    }
}
