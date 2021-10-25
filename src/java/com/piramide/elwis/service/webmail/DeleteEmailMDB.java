/**
 * @author : Ivan
 *
 * Jatun S.R.L
 */
package com.piramide.elwis.service.webmail;

import com.jatun.commons.email.connection.Account;
import com.jatun.commons.email.connection.ConnectionFactory;
import com.jatun.commons.email.connection.exeception.ConnectionException;
import com.jatun.commons.email.connection.pop.POPConnection;
//import com.jatun.commons.email.connection.pop.POPFactory;
import com.piramide.elwis.cmd.webmailmanager.POPFactory;
import com.piramide.elwis.domain.webmailmanager.MailAccount;
import com.piramide.elwis.utils.webmail.WebmailAccountUtil;
import com.piramide.elwis.utils.webmail.jms.DeleteEmailMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.util.List;
import java.util.Map;

public class DeleteEmailMDB implements MessageDrivenBean, MessageListener {
    private Log log = LogFactory.getLog(DeleteEmailMDB.class);

    private MessageDrivenContext messageContext = null;

    public DeleteEmailMDB() {
    }


    public void onMessage(Message message) {
        ObjectMessage objectMessage = (ObjectMessage) message;

        Object jmsMessageBody;
        try {
            jmsMessageBody = objectMessage.getObject();
        } catch (JMSException e) {
            log.error("Read JMS Message Body FAIL", e);
            return;
        }

        if (!(jmsMessageBody instanceof DeleteEmailMessage)) {
            return;
        }

        DeleteEmailMessage emailMessage = (DeleteEmailMessage) jmsMessageBody;

        deleteEmails(emailMessage);
    }

    private void deleteEmails(DeleteEmailMessage deleteEmailMessage) {
        for (Object object : deleteEmailMessage.getAccounts().entrySet()) {
            Map.Entry entry = (Map.Entry) object;
            String account = (String) entry.getKey();
            List<String> messageIds = (List<String>) entry.getValue();
            deleteEmailsFromPopServer(account, messageIds, deleteEmailMessage.getUserMailId());
        }
    }

    private void deleteEmailsFromPopServer(String emailAccount, List<String> messageIds, Integer userMailId) {

        MailAccount mailAccount = WebmailAccountUtil.i.getMailAccount(emailAccount, userMailId);
        if (null == mailAccount) {
            return;
        }

        if (mailAccount.getKeepEmailOnServer() != null && mailAccount.getKeepEmailOnServer()) {
            return;
        }

        Account popAccount = WebmailAccountUtil.i.getPOPAccount(mailAccount);

        ConnectionFactory popConnectionFactory = new POPFactory();
        POPConnection connection = null;

        try {
            connection = (POPConnection) popConnectionFactory.getConnection(popAccount);
            connection.deleteEmails(messageIds);
        } catch (ConnectionException e) {
            log.warn("Messages not deleted from POP Server, Connection Fail ");
        } finally {
            if (null != connection) {
                connection.close();
            }
        }
    }


    public void ejbRemove() throws EJBException {
    }

    public void setMessageDrivenContext(MessageDrivenContext messageDrivenContext) throws EJBException {
        this.messageContext = messageDrivenContext;
    }

    public void ejbCreate() {
    }
}
