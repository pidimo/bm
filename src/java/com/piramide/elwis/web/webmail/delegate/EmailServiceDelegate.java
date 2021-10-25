package com.piramide.elwis.web.webmail.delegate;

import com.jatun.commons.email.connection.exeception.ConnectionException;
import com.piramide.elwis.service.exception.webmail.EmailNotFoundException;
import com.piramide.elwis.service.webmail.*;
import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class EmailServiceDelegate {
    private Log log = LogFactory.getLog(EmailServiceDelegate.class);

    public static final EmailServiceDelegate i = new EmailServiceDelegate();

    private EmailServiceDelegate() {
    }

    public void sentEmailsSync(Integer userMailId, Integer companyId) {
        SentEmailService sentEmailService = getSentEmailService();
        sentEmailService.sentEmailsSync(userMailId, companyId);
    }

    public void sentEmails(Integer userMailId) {
        EmailService emailService = getEmailService();
        emailService.sentEmails(userMailId);
    }

    public void downloadEmails(Integer userMailId) {
        DownloadEmailService downloadEmailService = getDownloadEmailService();
        downloadEmailService.downloadEmails(userMailId);
    }

    public void downloadEmailSource(Integer mailId,
                                    Integer userMailId) throws EmailNotFoundException,
            ConnectionException {
        EmailSourceService emailSourceService = getEmailSourceService();
        emailSourceService.storeEmailSource(mailId, userMailId);
    }

    private SentEmailService getSentEmailService() {
        SentEmailServiceHome sentEmailServiceHome =
                (SentEmailServiceHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_SENT_EMAIL_SERVICE);

        try {
            return sentEmailServiceHome.create();
        } catch (CreateException e) {
            log.error("-> Cannot Create SentEmailService ", e);
            return null;
        }
    }

    private EmailService getEmailService() {
        EmailServiceHome emailServiceHome =
                (EmailServiceHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_EMAIL_SERVICE);
        try {
            return emailServiceHome.create();
        } catch (CreateException e) {
            log.error("-> Create EmailService FAIL", e);
        }
        return null;
    }

    private DownloadEmailService getDownloadEmailService() {
        DownloadEmailServiceHome downloadEmailServiceHome =
                (DownloadEmailServiceHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_DOWNLOAD_EMAIL_SERVICE);
        try {
            return downloadEmailServiceHome.create();
        } catch (CreateException e) {
            log.error("-> Creaye DownloadEmailService FAIL", e);
        }

        return null;
    }

    private EmailSourceService getEmailSourceService() {
        EmailSourceServiceHome emailSourceServiceHome =
                (EmailSourceServiceHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_EMAIL_SOURCE_SERVICE);
        try {
            return emailSourceServiceHome.create();
        } catch (CreateException e) {
            log.error("-> Create EmailSourceService FAIL", e);
        }

        return null;
    }


}
