/**
 * Jatun S.R.L
 *
 * @author ivan
 */
package com.piramide.elwis.service.webmail;

import com.piramide.elwis.utils.webmail.jms.DeleteEmailMessage;

import javax.ejb.EJBLocalObject;

public interface EmailService extends EJBLocalObject {
    void sentEmails(Integer userMailId);

    void sentEmailsInBackGround();

    void downloadEmailInBackground();

    void deleteEmailsFromPOPServer(DeleteEmailMessage deleteEmailMessage);
}
