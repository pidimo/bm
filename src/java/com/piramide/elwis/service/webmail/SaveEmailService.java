/**
 * @author ivan
 *
 * Jatun S.R.L. 
 */
package com.piramide.elwis.service.webmail;

import com.jatun.commons.email.model.Email;
import com.jatun.commons.email.model.Header;
import com.piramide.elwis.domain.webmailmanager.Mail;
import com.piramide.elwis.domain.webmailmanager.MailAccount;
import com.piramide.elwis.utils.webmail.jms.WebmailAccountMessage;

import javax.ejb.EJBLocalObject;
import javax.ejb.RemoveException;
import java.io.ByteArrayOutputStream;

public interface SaveEmailService extends EJBLocalObject {
    public Mail storeEmail(Header header,
                           WebmailAccountMessage accountMessage,
                           MailAccount mailAccount,
                           ByteArrayOutputStream emailSource);

    public void storeEmail(Email parsedEmail,
                           WebmailAccountMessage accountMessage,
                           MailAccount mailAccount, ByteArrayOutputStream emailSource);

    public void storeEmailWithoutBody(Header header,
                           WebmailAccountMessage accountMessage,
                           MailAccount mailAccount, ByteArrayOutputStream emailSource);

    public void deleteEmail(Mail dataBaseEmail) throws RemoveException;

}
