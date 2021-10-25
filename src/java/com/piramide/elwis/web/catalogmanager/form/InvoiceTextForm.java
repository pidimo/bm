package com.piramide.elwis.web.catalogmanager.form;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.common.validator.DataBaseValidator;
import com.piramide.elwis.web.utils.UploadFileFormUtil;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class InvoiceTextForm extends DefaultForm {
    private UploadFileFormUtil util = new UploadFileFormUtil("file",
            "InvoiceText.template",
            "languageId",
            "InvoiceText.language");

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = super.validate(mapping, request);

        ActionError duplicatedValidation = validateDuplicated(request);
        if (null != duplicatedValidation) {
            errors.add("duplicatedError", duplicatedValidation);
        }

        util.validateUploadWordTemplate(request, this, errors);

        if (errors.isEmpty()) {
            setDto("template", util.getWrappedFile());
        }

        return errors;
    }

    private ActionError validateDuplicated(HttpServletRequest request) {
        String op = (String) getDto("op");
        if (!"create".equals(op)) {
            return null;
        }

        User user = RequestUtils.getUser(request);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

        String languageId = (String) getDto("languageId");
        String templateId = (String) getDto("templateId");

        Map conditions = new HashMap();
        conditions.put("templateid", templateId);
        conditions.put("companyid", companyId);
        boolean isDuplicated = DataBaseValidator.i.isDuplicate("invoicetext",
                "languageid", languageId,
                "templateid", templateId,
                conditions, false);

        if (isDuplicated) {
            return new ActionError("TemplateFile.error.duplicate");
        }

        return null;
    }
}
