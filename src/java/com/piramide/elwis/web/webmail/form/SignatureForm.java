package com.piramide.elwis.web.webmail.form;

import com.piramide.elwis.web.common.util.JSPHelper;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : ivan
 * @version $Id: SignatureForm.java 9703 2009-09-12 15:46:08Z fernando ${time}
 */
public class SignatureForm extends WebmailDefaultForm {
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        errors = super.validate(mapping, request);
        if (isHtmlMessageEmpty()) {
            errors.add("htmlMessageError",
                    new ActionError("errors.required",
                            JSPHelper.getMessage(request, "Webmail.htmlSignature.messageLabel")));
        }
        return errors;
    }

    private boolean isHtmlMessageEmpty() {
        String htmlMessage = getDto("signatureHtmlMessage").toString();
        String testMessage = new String(htmlMessage);
        testMessage = testMessage.replaceAll("&nbsp;", "");
        testMessage = testMessage.replaceAll("<br>|<br/>|<BR>|<BR/>|<br />|<BR />", "");

        return "".equals(testMessage.trim());

    }
}
