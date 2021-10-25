package com.piramide.elwis.web.admin.form;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.jstl.core.Config;
import java.util.Locale;

/**
 * Alfacentauro Team
 *
 * @author miky
 * @version $Id: UserForm.java 12716 2017-11-16 19:15:52Z miguel $
 */
public class UserForm extends DefaultForm {


    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("Validating UserForm.............................." + getDtoMap());

        ActionErrors errors = super.validate(mapping, request);

        if ("0".equals(getDto("rowsPerPage")) ||
                "0".equals(getDto("maxRecentList")) ||
                "0".equals(getDto("timeout"))) {
            errors.add("range", new ActionError("User.range"));
        }

        //validate the timeout limit
        validateTimeoutLimit(errors);

        if (errors.isEmpty()) {
            int timeout = (new Integer((String) getDto("timeout"))).intValue();

            User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
            user.setValue("maxRecentList", getDto("maxRecentList"));
            user.setValue("rowsPerPage", getDto("rowsPerPage"));
            user.setValue("timeout", new Integer(timeout * 60));
            request.getSession(false).setMaxInactiveInterval(timeout * 60);
            user.setValue("locale", getDto("favoriteLanguage"));

            Config.set(request.getSession(), Config.FMT_LOCALE, new Locale(getDto("favoriteLanguage").toString()));
            request.getSession().setAttribute(org.apache.struts.Globals.LOCALE_KEY, new Locale(getDto("favoriteLanguage").toString()));
            if (!GenericValidator.isBlankOrNull((String) getDto("timeZone"))) {
                user.setValue("dateTimeZone", DateTimeZone.forID((String) getDto("timeZone")));
            } else {
                user.setValue("dateTimeZone", DateTimeZone.getDefault());
            }

            //need to update webmail systemFolders, used in update preferences for user; in home page
            setDto("uiFolderNames", JSPHelper.getSystemFolderNames(request));
        }

        return errors;
    }

    private void validateTimeoutLimit(ActionErrors errors) {
        Integer limitInHours = 24;
        Integer limitInMinutes = limitInHours * 60;

        Integer timeoutAsMinutes = getTimeoutValue();

        if (timeoutAsMinutes != null && timeoutAsMinutes > limitInMinutes) {
            errors.add("timeoutLimit", new ActionError("User.error.timeoutLimit", limitInHours, limitInMinutes));
        }
    }

    private Integer getTimeoutValue() {
        Integer value = null;
        if (!GenericValidator.isBlankOrNull((String) getDto("timeout"))) {
            try {
                value = Integer.valueOf((String) getDto("timeout"));
            } catch (NumberFormatException ignore) {
            }
        }

        return value;
    }


}
