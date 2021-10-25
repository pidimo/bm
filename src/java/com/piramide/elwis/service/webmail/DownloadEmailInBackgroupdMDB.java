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
import com.piramide.elwis.utils.webmail.jms.UserMessage;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.*;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

public class DownloadEmailInBackgroupdMDB implements MessageDrivenBean, MessageListener, MyCustomListener {
    private static final Log LOG = LogFactory.getLog(DownloadEmailInBackgroupdMDB.class);

    private MessageDrivenContext messageContext = null;

    public DownloadEmailInBackgroupdMDB() {
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

        if (!(jmsMessageBody instanceof UserMessage)) {
            return;
        }

        UserMessage userMessage = (UserMessage) jmsMessageBody;

        unQueueDownloadLog(userMessage);

        downloadEmailForUser(userMessage);
    }

    private void downloadEmailForUser(UserMessage userMessage) {
        UtilServiceHome utilServiceHome =
                (UtilServiceHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_UTIL_SERVICE);
        try {
            UtilService utilService = utilServiceHome.create();
            utilService.downloadEmails(userMessage);
            utilService.remove();
        } catch (CreateException e) {
            LOG.error("Create UtilService FAIL ", e);
        } catch (RemoveException e) {
            LOG.error("Remove UtilService FAIL ", e);
        }
    }

    public void ejbRemove() throws EJBException {
    }

    public void setMessageDrivenContext(MessageDrivenContext messageDrivenContext) throws EJBException {
        this.messageContext = messageDrivenContext;
    }

    public void ejbCreate() {
    }

    private void unQueueDownloadLog(UserMessage userMessage) {
        MyCustomEvent myCustomEvent = new MyCustomEvent(this, "UNQUEUE_USERMAIL", userMessage.getMillisKey(), userMessage.getUserMailId());
        fire(myCustomEvent);
    }

    public void fire(MyCustomEvent event) {
        MyCustomEventHandler.fire(event);
    }
}
