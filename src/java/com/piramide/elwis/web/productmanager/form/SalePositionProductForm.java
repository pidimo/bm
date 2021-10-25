package com.piramide.elwis.web.productmanager.form;

import com.piramide.elwis.dto.contactmanager.CustomerDTO;
import com.piramide.elwis.web.common.util.FormatUtils;
import com.piramide.elwis.web.common.validator.FieldChecks;
import com.piramide.elwis.web.productmanager.el.Functions;
import com.piramide.elwis.web.salesmanager.form.SalePositionForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * @author Ivan Alban
 * @version 4.2.2
 */
public class SalePositionProductForm extends SalePositionForm {

    @Override
    protected boolean existMainElement(HttpServletRequest request) {
        Object productId = getDto("productId");
        String op = (String) getDto("op");

        if ("update".equals(op) || "delete".equals(op)) {
            String saleId = (String) getDto("saleId");

            if (!GenericValidator.isBlankOrNull(saleId)) {
                boolean existsSale = com.piramide.elwis.web.salesmanager.el.Functions.existsSale(saleId);

                if (existsSale) {
                    saleDTO = com.piramide.elwis.web.salesmanager.el.Functions.getSaleDTO((String) getDto("saleId"), request);
                }

                return com.piramide.elwis.web.salesmanager.el.Functions.existsSalePosition(getDto("salePositionId")) &&
                        Functions.existsProduct(productId) && existsSale;
            }

            return com.piramide.elwis.web.salesmanager.el.Functions.existsSalePosition(getDto("salePositionId")) &&
                    Functions.existsProduct(productId);
        }

        return Functions.existsProduct(productId);
    }

    @Override
    protected ActionErrors updateForm(HttpServletRequest request) {

        //define el default discount of customer when this is selected
        Integer customerId = null;
        String customerIdFromForm = (String) getDto("customerId");
        if (!GenericValidator.isBlankOrNull(customerIdFromForm)) {
            try {
                customerId = Integer.valueOf(customerIdFromForm);
                CustomerDTO customerDTO = com.piramide.elwis.web.contactmanager.el.Functions.getCustomer(customerId, request);
                if (null != customerDTO) {
                    setDto("discount", customerDTO.get("defaultDiscount"));
                }
            } catch (NumberFormatException e) {
                //number error
            }
        }

        return super.updateForm(request);
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
