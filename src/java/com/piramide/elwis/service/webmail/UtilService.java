/**
 * @author ivan
 *
 * Jatun S.R.L. 
 */
package com.piramide.elwis.service.webmail;

import com.jatun.commons.email.connection.exeception.ConnectionException;
import com.piramide.elwis.domain.webmailmanager.MailAccount;
import com.piramide.elwis.utils.webmail.jms.EmailMessage;
import com.piramide.elwis.utils.webmail.jms.UserMessage;
import com.piramide.elwis.utils.webmail.jms.WebmailAccountMessage;

import javax.ejb.EJBLocalObject;
import java.util.List;

public interface UtilService extends EJBLocalObject {

    public void sentEmailMessages(UserMessage userMessage);

    public void downloadEmails(UserMessage userMessage);

    public List<WebmailAccountMessage> buildWebmailAccountMessages(Integer userMailId,
                                                                   Boolean isConnected);

    public void savePopErrors(Integer userMailId,
                              MailAccount mailAccount,
                              Integer companyId,
                              ConnectionException exception) throws Exception;

    public void saveSmtpErrors(Integer userMailId,
                               MailAccount mailAccount,
                               Integer mailId,
                               Integer companyId,
                               ConnectionException exception) throws Exception;

    public List<EmailMessage> getOutBoxEmails(Integer userMailId,
                                              Integer companyId,
                                              Boolean isConnected);
}
