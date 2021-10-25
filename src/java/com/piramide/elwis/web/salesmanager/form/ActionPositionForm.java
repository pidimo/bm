package com.piramide.elwis.web.salesmanager.form;

import com.piramide.elwis.dto.salesmanager.ActionDTO;
import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.web.common.util.FormatUtils;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.validator.FieldChecks;
import com.piramide.elwis.web.salesmanager.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * @author Ivan Alban
 * @author 4.2.2
 */
public class ActionPositionForm extends DefaultForm {

    private ActionDTO actionDTO;

    @Override
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        if (!isSaveAndNewButtonPressed(request) && !isSaveButtonPressed(request)) {
            return new ActionErrors();
        }

        String contactId = (String) getDto("contactId");
        String processId = (String) getDto("processId");

        actionDTO = Functions.getActionDTO(contactId, processId, request);
        if (null == actionDTO) {
            return new ActionErrors();
        }

        ActionErrors errors = super.validate(mapping, request);
        ActionError actionNetGrossChangeError = validateActionNetGross(request);
        if (null != actionNetGrossChangeError) {
            errors.add("actionNetGrossChangeError", actionNetGrossChangeError);
        } else {
            ActionError unitPriceError = validateUnitPrice(request);
            if (null != unitPriceError) {
                errors.add("unitPriceError", unitPriceError);
            }
        }


        return errors;
    }

    /**
     * Validate the unit prices (net price or gross price), the validation depends of <code>actionNetGross</code>
     * property, after the validation (if do not exists invalid values for the prices) the method updates
     * the <code>dto</code> values from <code>String</code> object to <code>BigDecimal</code> object.
     * <p/>
     * The dto properties are:
     * <p/>
     * <code>price</code>: It is unit price net
     * <code>unitPriceGross</code>: It is unit price gross
     *
     * @param request <code>HttpServletRequest</code> object used to format and unformat the numbers.
     * @return <code>ActionError</code> object when the values associated to <code>dto</code> properties contains
     *         invalid values, in other case return <code>null</code>.
     */
    private ActionError validateUnitPrice(HttpServletRequest request) {
        String price = null;
        String msg = "";

        String actualNetGross = (String) getDto("actionNetGross");

        if (FinanceConstants.NetGrossFLag.NET.equal(actualNetGross)) {
            price = (String) getDto("price");
            msg = "ActionPosition.unitPriceNet";
        }

        if (FinanceConstants.NetGrossFLag.GROSS.equal(actualNetGross)) {
            price = (String) getDto("unitPriceGross");
            msg = "ActionPosition.unitPriceGross";
        }

        if (GenericValidator.isBlankOrNull(price)) {
            return null;
        }

        ActionError decimalValidation =
                FieldChecks.validateDecimalNumber(price, msg, 10, 4, request);

        if (null == decimalValidation) {
            if (FinanceConstants.NetGrossFLag.NET.equal(actualNetGross)) {
                setDto("price", new BigDecimal(FormatUtils.unformatDecimalNumber(price, 10, 4, request)));
                String unitPriceGross = (String) getDto("unitPriceGross");
                if (!GenericValidator.isBlankOrNull(unitPriceGross)) {
                    setDto("unitPriceGross", new BigDecimal(unitPriceGross));
                }
            }

            if (FinanceConstants.NetGrossFLag.GROSS.equal(actualNetGross)) {
                setDto("unitPriceGross", new BigDecimal(FormatUtils.unformatDecimalNumber(price, 10, 4, request)));
                String unitPriceNet = (String) getDto("price");
                if (!GenericValidator.isBlankOrNull(unitPriceNet)) {
                    setDto("price", new BigDecimal(unitPriceNet));
                }
            }
        }

        return decimalValidation;
    }

    private ActionError validateActionNetGross(HttpServletRequest request) {
        String actionNetGross = (String) getDto("actionNetGross");
        Integer actualActionNetGross = (Integer) actionDTO.get("netGross");

        if (actualActionNetGross.toString().equals(actionNetGross)) {
            return null;
        }

        return new ActionError("ActionPosition.actionNetGrossChange.error",
                JSPHelper.getMessage(request, "SalesProcessAction.netGross"));
    }

    private boolean isSaveButtonPressed(HttpServletRequest request) {
        return null != request.getParameter("dto(save)");
    }

    private boolean isSaveAndNewButtonPressed(HttpServletRequest request) {
        return null != request.getParameter("SaveAndNew");
    }
}
