package com.piramide.elwis.web.catalogmanager.form;

import com.piramide.elwis.web.utils.VoucherSequenceRuleFormatUtil;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class ActionTypeForm extends DefaultForm {
    @Override
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = super.validate(mapping, request);
        List<ActionError> customValidationErrors = validateSequenceRule(request);
        if (null != customValidationErrors && !customValidationErrors.isEmpty()) {
            for (int i = 0; i < customValidationErrors.size(); i++) {
                errors.add("error_" + i, customValidationErrors.get(i));
            }
        }

        return errors;
    }

    private List<ActionError> validateSequenceRule(HttpServletRequest request) {
        List<ActionError> errors = new ArrayList<ActionError>();

        String format = (String) getDto("format");
        String resetType = (String) getDto("resetType");
        String startNumber = (String) getDto("startNumber");

        if (GenericValidator.isBlankOrNull(format) ||
                GenericValidator.isBlankOrNull(resetType) ||
                GenericValidator.isBlankOrNull(startNumber)) {
            return errors;
        }

        VoucherSequenceRuleFormatUtil util = new VoucherSequenceRuleFormatUtil(format, startNumber, resetType);
        errors.addAll(util.formatValidator(request));

        return errors;
    }
}
