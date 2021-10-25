/**
 * @author ivan
 *
 * Jatun S.R.L. 
 */
package com.piramide.elwis.service.webmail;

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

public class SearchEmailInBackgroundMDB implements MessageDrivenBean, MessageListener {
    private static final Log LOG = LogFactory.getLog(SearchEmailInBackgroundMDB.class);

    private MessageDrivenContext messageContext = null;

    public SearchEmailInBackgroundMDB() {
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
        sentEmailMessages(userMessage);
    }

    private void sentEmailMessages(UserMessage userMessage) {
        UtilServiceHome utilServiceHome =
                (UtilServiceHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_UTIL_SERVICE);
        try {
            UtilService utilService = utilServiceHome.create();
            utilService.sentEmailMessages(userMessage);
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
}
