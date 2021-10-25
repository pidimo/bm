package com.piramide.elwis.web.contactmanager.form;

import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericTypeValidator;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.PropertyMessageResources;
import org.apache.struts.validator.Resources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.jstl.core.Config;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class CommunicationFieldValidatorUtil {

    public static void validateSubject(HttpServletRequest request, ActionErrors errors, DefaultForm form) {
        String subject = (String) form.getDto("note");
        if (GenericValidator.isBlankOrNull(subject)) {
            errors.add("requiredError", new ActionError("errors.required", JSPHelper.getMessage(request, "Document.subject")));
        }
    }

    public static void validateContent(HttpServletRequest request, ActionErrors errors, DefaultForm form) {
        String content = (String) form.getDto("freeText");
        if (GenericValidator.isBlankOrNull(content)) {
            errors.add("requiredError", new ActionError("errors.required", JSPHelper.getMessage(request, "Document.content")));
        }
    }

    public static void validateDate(HttpServletRequest request, ActionErrors errors, DefaultForm form) {
        String date = (String) form.getDto("dateStart");

        if (GenericValidator.isBlankOrNull(date)) {
            errors.add("required", new ActionError("errors.required", JSPHelper.getMessage(request, "Document.date")));
            return;
        }

        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        MessageResources messages = (PropertyMessageResources)
                request.getAttribute(org.apache.struts.Globals.MESSAGES_KEY);

        String datePattern = Resources.getMessage(messages, locale, "datePattern");

        Date dateObject = GenericTypeValidator.formatDate(date, datePattern.trim(), true);
        if (null == dateObject) {
            errors.add("dateError", new ActionError("errors.date", date, datePattern));
            return;
        }

        Integer result = DateUtils.dateToInteger(dateObject);
        form.setDto("dateStart", result);
    }

    public static void validateContactPerson(HttpServletRequest request, ActionErrors errors, DefaultForm form) {
        String contactPersonId = (String) form.getDto("contactPersonId");
        String addressId = (String) form.getDto("addressId");
        String contactPersonName = (String) form.getDto("contactPersonName");
        if (!GenericValidator.isBlankOrNull(contactPersonName)) {
            Map values = new HashMap();
            values.put("contactPersonId", contactPersonId);
            values.put("addressid", addressId);
            ForeignkeyValidator.i.validate("contactperson", values, errors,
                    new ActionError("error.SelectedNotFound", contactPersonName));
        }
    }

    public static void validateEmployee(HttpServletRequest request, ActionErrors errors, DefaultForm form) {
        String employeeId = (String) form.getDto("employeeId");
        if (!GenericValidator.isBlankOrNull(employeeId)) {
            Map values = new HashMap();
            values.put("employeeid", employeeId);
            ForeignkeyValidator.i.validate("employee",
                    values,
                    errors,
                    new ActionError("error.SelectedNotFound", JSPHelper.getMessage(request, "SalesProcess.employee")));
        }
    }

    public static void validateSalesProcess(HttpServletRequest request, ActionErrors errors, DefaultForm form) {
        String salesProcessId = (String) form.getDto("processId");
        if (!GenericValidator.isBlankOrNull(salesProcessId)) {
            Map values = new HashMap();
            values.put("processid", salesProcessId);
            ForeignkeyValidator.i.validate("salesprocess",
                    values,
                    errors,
                    new ActionError("error.SelectedNotFound", JSPHelper.getMessage(request, "SalesProcess")));
        }
    }

    public static void validateAddress(HttpServletRequest request, ActionErrors errors, DefaultForm form) {
        String contactName = (String) form.getDto("contact");
        String addressId = (String) form.getDto("addressId");

        if (GenericValidator.isBlankOrNull(contactName)) {
            errors.add("requiredError", new ActionError("errors.required",
                    JSPHelper.getMessage(request, "Contact.title")));
        } else {
            Map values = new HashMap();
            values.put("addressid", addressId);
            ForeignkeyValidator.i.validate("address", values, errors,
                    new ActionError("error.SelectedNotFound", contactName));
        }
    }

    public static enum FormButtonProperties {
        Save("save"),
        Generate("generate"),
        Send("sendButton");

        private String key;

        FormButtonProperties(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    public static boolean isButtonPressed(String key, HttpServletRequest request) {
        return null != request.getParameter(key);
    }
}
