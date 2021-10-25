/**
 * Jatun S.R.L
 *
 * @author ivan
 */
package com.piramide.elwis.service.webmail;

import com.piramide.elwis.service.webmail.downloadlog.MyCustomEvent;
import com.piramide.elwis.service.webmail.downloadlog.MyCustomEventHandler;
import com.piramide.elwis.service.webmail.downloadlog.MyCustomListener;
import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.utils.webmail.jms.WebmailAccountMessage;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.transaction.SystemException;

public class DownloadEmailMDB implements MessageDrivenBean, MessageListener, MyCustomListener {
    private static final Log LOG = LogFactory.getLog(DownloadEmailMDB.class);

    private MessageDrivenContext messageContext = null;

    public DownloadEmailMDB() {
    }

    public void onMessage(Message message) {
        ObjectMessage objectMessage = (ObjectMessage) message;

        Object jmsMessageBody;
        try {
            jmsMessageBody = objectMessage.getObject();
        } catch (JMSException e) {
            LOG.error("Read JMS Message Body FAIL", e);
            return;
        }

        if (!(jmsMessageBody instanceof WebmailAccountMessage)) {
            return;
        }

        WebmailAccountMessage accountMessage = (WebmailAccountMessage) jmsMessageBody;

        unQueueAccountDownloadLog(accountMessage);
        try {
            downloadEmails(accountMessage);
        } catch (SystemException e) {
            LOG.warn("-> Download emails for mailAccountId=" + accountMessage.getMailAccountId() + " FAIL ");
        }
    }

    private void downloadEmails(WebmailAccountMessage accountMessage) throws SystemException {
        DownloadEmailService downloadEmailService = getDownloadEmailService();
        downloadEmailService.downloadEmails(accountMessage);
    }

    private DownloadEmailService getDownloadEmailService() {
        DownloadEmailServiceHome downloadEmailServiceHome =
                (DownloadEmailServiceHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_DOWNLOAD_EMAIL_SERVICE);
        try {
            return downloadEmailServiceHome.create();
        } catch (CreateException e) {
            LOG.error("-> Create DownloadEmailService FAIL", e);
        }

        return null;
    }

    public void ejbRemove() throws EJBException {
    }

    public void setMessageDrivenContext(MessageDrivenContext messageDrivenContext) throws EJBException {
        this.messageContext = messageDrivenContext;
    }

    public void ejbCreate() {
    }

    private void unQueueAccountDownloadLog(WebmailAccountMessage message) {
        MyCustomEvent myCustomEvent = new MyCustomEvent(this, "UNQUEUE_ACCOUNT", message.getMillisKey(), message.getMailAccountId());
        fire(myCustomEvent);
    }

    public void fire(MyCustomEvent event) {
        MyCustomEventHandler.fire(event);
    }

}
