package com.piramide.elwis.cmd.common;

import com.piramide.elwis.cmd.utils.Email;
import com.piramide.elwis.utils.JMSUtil;
import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.utils.configuration.ConfigurationFactory;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.EJBCommand;
import org.joda.time.DateTimeZone;

import javax.ejb.SessionContext;
import javax.jms.JMSException;
import javax.naming.NamingException;
import java.util.TimeZone;

/**
 * AlfaCentauro Team
 *
 * @author Ivan
 * @version $Id: SendEmailCmd.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class SendEmailCmd extends EJBCommand {
    public void executeInStateless(SessionContext ctx) throws AppLevelException {
        String to = paramDTO.getAsString("emailTo");
        String from = paramDTO.getAsString("emailFrom");
        Integer userId = EJBCommandUtil.i.getValueAsInteger(this, "userId");
        if (from == null || (from != null && from.trim().length() == 0)) {
            from = ConfigurationFactory.getConfigurationManager().getValue("elwis.system.fromEmailSender");
        }

        String subject = paramDTO.getAsString("subject");
        String message = paramDTO.getAsString("message");
        Email email = new Email(to, from, subject, message, Email.HTML_MAIL_TYPE);
        email.setUserId(userId);
        if (paramDTO.containsKey("timeZone")) {
            Object timeZone = paramDTO.get("timeZone");
            if (timeZone instanceof DateTimeZone) {
                timeZone = ((DateTimeZone) timeZone).toTimeZone();
            }
            email.setTimeZone((TimeZone) timeZone);
        }
        if (paramDTO.containsKey("fromPersonal")) {
            email.setFromPersonal(paramDTO.getAsString("fromPersonal"));
        }
        try {
            JMSUtil.sendToJMSQueue(WebMailConstants.JNDI_SENDSIMPLEMAILMDB, email, false);
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public boolean isStateful() {
        return false;
    }


}
