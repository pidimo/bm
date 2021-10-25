package com.piramide.elwis.web.admin.form;

import com.piramide.elwis.web.common.validator.EmailValidator;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import javax.servlet.http.HttpServletRequest;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Sep 22, 2005
 * Time: 4:43:25 PM
 * To change this template use File | Settings | File Templates.
 */

public class NotificationForm extends DefaultForm {

    private Log log = LogFactory.getLog(NotificationForm.class);

    public ActionErrors validate(org.apache.struts.action.ActionMapping mapping,
                                 HttpServletRequest request) {
        ActionErrors errors = super.validate(mapping, request);
        validateMail(errors, "notificationSchedulerTaskEmail");
        validateMail(errors, "notificationAppointmentEmail");
        validateMail(errors, "notificationSupportCaseEmail");
        validateMail(errors, "notificationQuestionEmail");

        return errors;
    }

    public void validateMail(ActionErrors errors, String value) {
        String mails = (String) getDto(value);
        String questionMail = null;
        StringBuffer emails = new StringBuffer();
        int error = 0;
        StringTokenizer to = new StringTokenizer(mails.trim(), ",");

        while (to.hasMoreTokens()) {
            questionMail = "";
            questionMail = to.nextToken();
            if (!EmailValidator.i.isValid(questionMail.trim())) {
                error++;
                errors.add("error", new ActionError("errors.email", questionMail.toString()));
            } else {
                emails.append(questionMail.trim());
            }
            if (to.hasMoreTokens()) {
                emails.append(", ");
            }
        }
        if (error < 1) {
            this.setDto(value, emails.toString());
        }
    }
}

