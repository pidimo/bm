package com.piramide.elwis.web.catalogmanager.form;

import com.piramide.elwis.web.common.util.JSPHelper;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * AlfaCentauro Team
 *
 * @author Ivan
 * @version $Id: VatRateForm.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class VatRateForm extends DefaultForm {
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = super.validate(mapping, request);
        ActionError vatRateError = validateVatRateValue(request, errors);
        if (null != vatRateError) {
            errors.add("vatRateError", vatRateError);
        }

        return errors;
    }

    private ActionError validateVatRateValue(HttpServletRequest request, ActionErrors errors) {
        if (!errors.isEmpty()) {
            return null;
        }

        BigDecimal vatRate = (BigDecimal) getDto("vatRate");

        if (vatRate.floatValue() > 100.00 || vatRate.floatValue() < 0) {
            return new ActionError("errors.decimal.percent_range",
                    JSPHelper.getMessage(request, "VatRate.vatRate"));
        }

        return null;
    }
}
