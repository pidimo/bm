package com.piramide.elwis.cmd.webmailmanager;

import com.jatun.commons.email.connection.exeception.BuilderException;
import com.jatun.commons.email.connection.smtp.MessageBuilder;
import com.jatun.commons.email.model.Email;
import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.domain.webmailmanager.EmailSource;
import com.piramide.elwis.domain.webmailmanager.EmailSourceHome;
import com.piramide.elwis.domain.webmailmanager.Mail;
import com.piramide.elwis.domain.webmailmanager.MailHome;
import com.piramide.elwis.service.webmail.SaveEmailService;
import com.piramide.elwis.service.webmail.SaveEmailServiceHome;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.utils.webmail.EmailSourceUtil;
import com.piramide.elwis.utils.webmail.WebmailAccountUtil;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.io.ByteArrayOutputStream;

/**
 * @author ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class DownloadEmailCmd extends EJBCommand {
    private Log log = LogFactory.getLog(DownloadEmailCmd.class);

    @Override
    public void executeInStateless(SessionContext sessionContext) {
        Integer mailId = EJBCommandUtil.i.getValueAsInteger(this, "mailId");
        read(mailId);
    }

    private void read(Integer mailId) {
        Mail mail = getMail(mailId);
        if (null == mail) {
            resultDTO.setForward("Fail");
            return;
        }

        ArrayByteWrapper wrapper = new ArrayByteWrapper();
        EmailSource emailSource = getEmailSource(mail.getMailId(), mail.getCompanyId());
        if (null == emailSource) {
            Email email = WebmailAccountUtil.i.buildEmailMessage(mail, mail.getFolder().getUserMailId());
            MessageBuilder messageBuilder;
            try {
                messageBuilder = new MessageBuilder(email, null, WebmailAccountUtil.i.getServerSmtpSession());
            } catch (BuilderException e) {
                e.printStackTrace();
                return;
            }

            ByteArrayOutputStream outputStream = messageBuilder.getMimeMessage();
            try {
                emailSource = saveEmailSource(outputStream.toByteArray(), mail);
            } catch (CreateException e) {
                log.debug("Rebuild EmailSource FAIL ", e);
                return;
            }
        }

        wrapper.setFileData(emailSource.getSource());
        resultDTO.put("data", wrapper);
        resultDTO.put("fileName", buildFileName(mail.getMailSubject()));
        resultDTO.put("fileSize", emailSource.getFileSize());
    }

    private String buildFileName(String subject) {
        String fileName = "email_message";
        if (null != subject && !"".equals(subject.trim())) {
            fileName = subject;
        }
        return fileName + ".eml";
    }

    private EmailSource saveEmailSource(byte[] data, Mail mail) throws CreateException {

        return EmailSourceUtil.i.createEmailSource(mail.getMailId(), mail.getCompanyId(), data);
    }

    private Mail getMail(Integer mailId) {
        MailHome mailHome =
                (MailHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_MAIL);
        try {
            return mailHome.findByPrimaryKey(mailId);
        } catch (FinderException e) {
            return null;
        }
    }

    private EmailSource getEmailSource(Integer mailId, Integer companyId) {
        EmailSourceHome emailSourceHome =
                (EmailSourceHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_EMAILSOURCE);
        try {
            return emailSourceHome.findByMailId(mailId, companyId);
        } catch (FinderException e) {
            return null;
        }
    }

    private SaveEmailService getSaveEmailService() {
        SaveEmailServiceHome saveEmailServiceHome =
                (SaveEmailServiceHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_SAVE_EMAIL_SERVICE);
        try {
            return saveEmailServiceHome.create();
        } catch (CreateException e) {
            log.error("Create SaveEmailService FAIL ", e);
            return null;
        }
    }

    public boolean isStateful() {
        return false;
    }
}
