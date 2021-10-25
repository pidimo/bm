package com.piramide.elwis.cmd.campaignmanager;

import com.jatun.commons.email.connection.ConnectionFactory;
import com.jatun.commons.email.connection.exeception.ConnectionException;
import com.jatun.commons.email.connection.smtp.SMTPConnection;
//import com.jatun.commons.email.connection.smtp.SMTPFactory;
import com.piramide.elwis.cmd.webmailmanager.SMTPFactory;
import com.jatun.commons.email.model.Address;
import com.jatun.commons.email.model.Email;
import com.jatun.commons.email.model.Header;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.webmail.WebmailAccountUtil;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.ejb.FinderException;
import javax.mail.Session;
import javax.naming.InitialContext;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * This is a class helper that is used to send massive emails from a campaign using just one SMTP Connection
 * per all process.
 *
 * @author Fernando Javier Montaño Torrico
 * @version 1.0
 */
public class CampaignMailer {
    private Log log = LogFactory.getLog(CampaignMailer.class);
    private ConnectionFactory smtpFactory;
    private SMTPConnection smtpConnection;
    private Session defaultSession;

    public CampaignMailer() throws Exception {
        smtpFactory = new SMTPFactory();
        InitialContext initialContext;
        initialContext = new InitialContext();
        defaultSession = (Session) initialContext.lookup(Constants.JNDI_MAILSESSION);
    }

    /**
     * Starts a connection
     *
     * @throws Exception if something fails
     */
    public void startConnection() throws Exception {
        smtpConnection = (SMTPConnection) smtpFactory.getConnection(null);
    }


    /**
     * Sends the email using the default session
     *
     * @param email  the email
     * @param userId the userId
     * @return true if everything was ok, false otherwise
     */
    private void sendMail(Email email, Integer userId) throws ConnectionException {
        if (userId != null) {
            User user = getUser(userId);
            WebmailAccountUtil.i.addApplicationSignature(email, user.getFavoriteLanguage());
        }
        smtpConnection.send(email, defaultSession);
    }

    /**
     * Close the connection
     */
    public void closeConnection() {
        if (null != smtpConnection) {
            smtpConnection.close();
        }
    }

    private User getUser(Integer userId) {
        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
        try {
            return userHome.findByPrimaryKey(userId);
        } catch (FinderException e) {
            return null;
        }
    }

    /**
     * Process the application email and the sends the email
     *
     * @param from           from
     * @param fromPersonal   from presonal
     * @param to             recipient
     * @param subject        the subject
     * @param body           the body
     * @param attachments    list of attachments
     * @param userId         the userId
     * @param emailImagesMap images to include in the mail
     * @return true if everything is ok, false otherwise
     */
    public void sendMail(String from, String fromPersonal, String to, String subject, String body, List attachments, Integer userId, Map emailImagesMap) throws ConnectionException {

        if (to != null && to.length() > 3) {
            com.piramide.elwis.cmd.utils.Email email = new com.piramide.elwis.cmd.utils.Email(emailImagesMap);
            email.setSubject(subject);
            email.setFrom(from);
            email.setFromPersonal(fromPersonal);
            email.setTo(to);
            email.setMessage(body);
            email.setAttachPaths(attachments);
            email.setBodyType("text/html");
            email.setUserId(userId);
            System.setProperty("mail.mime.charset", Constants.CHARSET_ENCODING);
            sendMail(convertToModelEmail(email), userId);
        }
    }

    /**
     * Converts an application email to a commons email object
     *
     * @param applicationEmail the application email to convert
     * @return the Email to be sent
     */
    private Email convertToModelEmail(com.piramide.elwis.cmd.utils.Email applicationEmail) {
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
        return email;
    }

}
