package com.piramide.elwis.plugins.jboss.scheduler;

import com.piramide.elwis.service.webmail.EmailService;
import com.piramide.elwis.service.webmail.EmailServiceHome;
import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.varia.scheduler.Schedulable;

import javax.ejb.CreateException;
import java.util.Date;

/**
 * @author ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class SchedulableEmailSender implements Schedulable {
    private static final Log LOG = LogFactory.getLog(SchedulableEmailSender.class);

    private long intervalBetweenChecks;

    public SchedulableEmailSender(long intervalBetweenChecks) {
        this.intervalBetweenChecks = intervalBetweenChecks;
    }

    public void perform(Date date, long remainingRepetitions) {
        sentEmails();
        downloadEmails();
    }

    private void sentEmails() {
        try {
            EmailService emailService = getEmailService();
            emailService.sentEmailsInBackGround();
            emailService.remove();
        } catch (Exception e) {
            LOG.error("A unexpected exception happen when trying send the emails in background.", e);
        }
    }

    private void downloadEmails() {
        try {
            EmailService emailService = getEmailService();
            emailService.downloadEmailInBackground();
            emailService.remove();
        } catch (Exception e) {
            LOG.error("A unexpected exception happen when trying download the emails in background.", e);
        }
    }

    private EmailService getEmailService() throws CreateException {
        EmailServiceHome emailServiceHome =
                (EmailServiceHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_EMAIL_SERVICE);
        return emailServiceHome.create();
    }
}
