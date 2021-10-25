package com.piramide.elwis.web.schedulermanager.form;

import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * AlfaCentauro Team
 *
 * @author ivan
 * @version $Id: GrantAccessForm.java 8123 2008-04-01 04:41:02Z miguel ${CLASS_NAME}.java,v 1.2 06-05-2005 10:31:08 AM ivan Exp $
 */
public class GrantAccessForm extends DefaultForm {
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("Executing validate method...");

        ActionErrors errors = new ActionErrors();
        errors = super.validate(mapping, request);

        String read = (String) getDto("read");
        String add = (String) getDto("add");
        String edit = (String) getDto("edit");
        String delete = (String) getDto("delete");
        String anonym = (String) getDto("anonym");

        String privateRead = (String) getDto("privateRead");
        String privateAdd = (String) getDto("privateAdd");
        String privateEdit = (String) getDto("privateEdit");
        String privateDelete = (String) getDto("privateDelete");
        String privateAnonym = (String) getDto("privateAnonym");

        if (GenericValidator.isBlankOrNull(read) &&
                GenericValidator.isBlankOrNull(add) &&
                GenericValidator.isBlankOrNull(edit) &&
                GenericValidator.isBlankOrNull(delete) &&
                GenericValidator.isBlankOrNull(anonym) &&
                GenericValidator.isBlankOrNull(privateRead) &&
                GenericValidator.isBlankOrNull(privateAdd) &&
                GenericValidator.isBlankOrNull(privateEdit) &&
                GenericValidator.isBlankOrNull(privateDelete) &&
                GenericValidator.isBlankOrNull(privateAnonym)) {
            errors.add("grantAccessForm", new ActionError("Scheduler.grantAccess.atLeastOne"));
        }

        if (errors.isEmpty()) {
            //public appointment validation
            if ((!GenericValidator.isBlankOrNull(add) ||
                    !GenericValidator.isBlankOrNull(edit) ||
                    !GenericValidator.isBlankOrNull(delete)) && GenericValidator.isBlankOrNull(read)) {
                errors.add("grantAccessForm", new ActionError(("Scheduler.grantAccess.publicReadRequired")));
            }

            //private appointment validation
            if ((!GenericValidator.isBlankOrNull(privateAdd) ||
                    !GenericValidator.isBlankOrNull(privateEdit) ||
                    !GenericValidator.isBlankOrNull(privateDelete)) && GenericValidator.isBlankOrNull(privateRead)) {
                errors.add("grantAccessForm", new ActionError(("Scheduler.grantAccess.privateReadRequired")));
            }
        }

        return errors;
    }
}
