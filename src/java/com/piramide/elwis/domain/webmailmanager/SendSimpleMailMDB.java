package com.piramide.elwis.domain.webmailmanager;

import com.jatun.commons.email.connection.exeception.ConnectionException;
import com.jatun.commons.email.model.Address;
import com.jatun.commons.email.model.Header;
import com.piramide.elwis.cmd.utils.Email;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.webmail.WebmailAccountUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.io.File;
import java.util.Map;
import java.util.StringTokenizer;


/**
 * Send simple mail Message Driven Bean service
 */

public class SendSimpleMailMDB implements MessageDrivenBean, MessageListener {
    private final static Log log = LogFactory.getLog(SendSimpleMailMDB.class);
    private MessageDrivenContext messageContext = null;

    public SendSimpleMailMDB() {
    }

    public void onMessage(Message message) {
        try {
            ObjectMessage msg = (ObjectMessage) message;
            if (msg.getObject() instanceof Email) {
                Email email = (Email) msg.getObject();
                if (!email.isManyRecipients()) {
                    sendEmail(email);
                } else {
                    for (StringTokenizer stringTokenizer = new StringTokenizer(email.getTo(), ","); stringTokenizer.hasMoreTokens();) {
                        email.setTo(stringTokenizer.nextToken().trim());
                        sendEmail(email);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error sending the mail", e);
        }
    }

    private void sendEmail(Email email) throws Exception {
        System.setProperty("mail.mime.charset", Constants.CHARSET_ENCODING);
        if ("text/html".equals(email.getBodyType())) {
            sent(email);
        } else {
            sentAsText(email);
        }
    }

    private void sent(Email applicationEmail) {
        Header header = new Header();
        header.setXMailer("bm.bm-od.com");
        String fromEmail = applicationEmail.getFrom();
        String fromPersonal = null;
        if (null != applicationEmail.getFromPersonal()) {
            fromPersonal = applicationEmail.getFromPersonal();
        }

        header.setFrom(new Address(fromEmail, fromPersonal));

        header.addToAddress(new Address(applicationEmail.getTo(), null));
        header.setSubject(applicationEmail.getSubject());

        com.jatun.commons.email.model.Body body =
                new com.jatun.commons.email.model.Body(applicationEmail.getMessage(),
                        com.jatun.commons.email.model.Body.Type.HTML);

        com.jatun.commons.email.model.Email email = new com.jatun.commons.email.model.Email();
        email.setHeader(header);
        email.addPart(body);

        for (Object object : applicationEmail.getImages().entrySet()) {
            Map.Entry entry = (Map.Entry) object;
            DataSource dataSource = new FileDataSource(((String) entry.getValue()).trim());
            com.jatun.commons.email.model.Attach attach =
                    new com.jatun.commons.email.model.Attach(dataSource.getName());

            attach.setDataSource(dataSource);
            attach.setCid(entry.getKey().toString().trim());
            email.addPart(attach);
        }

        if (applicationEmail.getAttachPaths() != null) {
            for (int i = 0; i < applicationEmail.getAttachPaths().size(); i++) {
                String pathFile = (String) applicationEmail.getAttachPaths().get(i);
                File file = new File(pathFile);

                DataSource dataSource = new FileDataSource(file);
                com.jatun.commons.email.model.Attach attach =
                        new com.jatun.commons.email.model.Attach(file.getName());
                attach.setDataSource(dataSource);
                email.addPart(attach);
            }
        }

        try {
            WebmailAccountUtil.i.deliveryEmailToDefaultSMTPServer(email, applicationEmail.getUserId());
        } catch (ConnectionException e) {
            log.warn("Sent Email From Default SMTP Server FAIL ", e);
        }
    }

    private void sentAsText(Email applicationEmail) {
        Header header = new Header();
        header.setXMailer("bm.bm-od.com");
        String fromEmail = applicationEmail.getFrom();
        String fromPersonal = null;
        if (null != applicationEmail.getFromPersonal()) {
            fromPersonal = applicationEmail.getFromPersonal();
        }

        header.setFrom(new Address(fromEmail, fromPersonal));

        header.addToAddress(new Address(applicationEmail.getTo(), null));
        header.setSubject(applicationEmail.getSubject());

        com.jatun.commons.email.model.Body body =
                new com.jatun.commons.email.model.Body(applicationEmail.getMessage(),
                        com.jatun.commons.email.model.Body.Type.TEXT);

        com.jatun.commons.email.model.Email email = new com.jatun.commons.email.model.Email();
        email.setHeader(header);
        email.addPart(body);

        try {
            WebmailAccountUtil.i.deliveryEmailToDefaultSMTPServer(email, applicationEmail.getUserId());
        } catch (ConnectionException e) {
            log.warn("Sent Email From Default SMTP Server FAIL ", e);
        }
    }


    public void ejbRemove() {
        messageContext = null;

    }

    public void setMessageDrivenContext(MessageDrivenContext messageDrivenContext) throws EJBException {
        this.messageContext = messageDrivenContext;
    }

    public void ejbCreate() {
    }
}
