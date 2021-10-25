package com.piramide.elwis.web.catalogmanager.form;

import com.piramide.elwis.web.utils.UploadFileFormUtil;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class ReminderLevelForm extends DefaultForm {
    private UploadFileFormUtil util = new UploadFileFormUtil("file",
            "ReminderLevel.template",
            "languageId",
            "ReminderLevel.language");

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = super.validate(mapping, request);
        String op = (String) getDto("op");
        if ("create".equals(op)) {
            util.validateUploadWordTemplate(request, this, errors);
            if (errors.isEmpty()) {
                setDto("template", util.getWrappedFile());
            }
        }

        return errors;
    }
}
