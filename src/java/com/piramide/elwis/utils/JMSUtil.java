package com.piramide.elwis.utils;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.Serializable;

/**
 * Class  provide  util methods to sent JMS messages to <code>javax.jms.Queue</code> objects.
 *
 * @author Alejandro Ruiz
 * @version 4.2.1
 */

public class JMSUtil {

    /**
     * Sent JMS message to <code>javax.jms.Queue</code> object.
     *
     * @param queueName  <code>javax.jms.Queue</code> name.
     * @param obj        <code>Serializable</code> object that is the message to sent.
     * @param transacted <code>boolean</code> value to define if <code>createQueueSession</code> are transacted or not.
     * @throws NamingException If cannnot find the <code>queueName</code>.
     * @throws JMSException    if cannot sent JMS message.
     */
    public static void sendToJMSQueue(String queueName,
                                      Serializable obj,
                                      boolean transacted)
            throws NamingException, JMSException {

        InitialContext initialcontext = null;
        QueueConnection cnn = null;
        QueueSender sender = null;
        QueueSession session = null;

        try {
            initialcontext = new InitialContext();

            Queue queue = (Queue) initialcontext.lookup(queueName);

            QueueConnectionFactory factory = (QueueConnectionFactory) initialcontext.lookup("ConnectionFactory");
            cnn = factory.createQueueConnection();
            session = cnn.createQueueSession(transacted, QueueSession.AUTO_ACKNOWLEDGE);

            ObjectMessage msg = session.createObjectMessage(obj);

            sender = session.createSender(queue);

            //time to live in queue 290000 milliseconds
            sender.send(msg, DeliveryMode.NON_PERSISTENT, Message.DEFAULT_PRIORITY, 290000);
        } finally {
            if (sender != null) {
                sender.close();
            }

            if (session != null) {
                session.close();
            }

            if (cnn != null) {
                cnn.close();
            }

            if (initialcontext != null) {
                initialcontext.close();
            }
        }
    }
}
