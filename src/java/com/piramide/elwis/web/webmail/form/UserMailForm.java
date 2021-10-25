package com.piramide.elwis.web.webmail.form;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * AlfaCentauro Team
 *
 * @author ivan
 * @version $Id: UserMailForm.java 9695 2009-09-10 21:34:43Z fernando ${CLASS_NAME}.java,v 1.2 29-03-2005 01:26:25 PM ivan Exp $
 */
public class UserMailForm extends WebmailDefaultForm {

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        MailAccountForm accountForm = new MailAccountForm();
        accountForm.getDtoMap().putAll(this.getDtoMap());
        ActionErrors errors = accountForm.validate(mapping, request);

        return errors;
    }
}
