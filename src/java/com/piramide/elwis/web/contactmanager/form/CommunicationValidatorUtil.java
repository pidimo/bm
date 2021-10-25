package com.piramide.elwis.web.contactmanager.form;

import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionErrors;

import javax.servlet.http.HttpServletRequest;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public interface CommunicationValidatorUtil {

    public void aditionalValidations(HttpServletRequest request, ActionErrors errors, DefaultForm form);

    public void settingUpOldValues(HttpServletRequest request, DefaultForm form);

}
