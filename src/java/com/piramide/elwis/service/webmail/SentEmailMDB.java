/**
 * @author ivan
 *
 * Jatun S.R.L. 
 */
package com.piramide.elwis.service.webmail;

import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.utils.webmail.jms.EmailMessage;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.*;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

public class SentEmailMDB implements MessageDrivenBean, MessageListener {
    private static final Log LOG = LogFactory.getLog(SentEmailMDB.class);

    private MessageDrivenContext messageContext = null;

    public SentEmailMDB() {
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

        if (!(jmsMessageBody instanceof EmailMessage)) {
            return;
        }

        EmailMessage emailMessage = (EmailMessage) jmsMessageBody;

        deliveryMessage(emailMessage.getMailId(),
                emailMessage.getUserMailId(),
                emailMessage.getFolderId(),
                emailMessage.getConnected());
    }

    private void deliveryMessage(Integer mailId,
                                 Integer userMailId,
                                 Integer folderId,
                                 Boolean isConnected) {

        SentEmailServiceHome serviceHome =
                (SentEmailServiceHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_SENT_EMAIL_SERVICE);

        try {
            SentEmailService service = serviceHome.create();
            service.sentEmail(mailId, userMailId, folderId, isConnected);
            service.remove();
        } catch (CreateException e) {
            LOG.error("Cannot create a new SentEmailService instance.", e);
        } catch (RemoveException e) {
            LOG.error("Cannot remove the current SentEmailService instance.", e);
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
