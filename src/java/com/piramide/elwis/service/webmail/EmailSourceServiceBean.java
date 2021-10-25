/**
 * @author : Ivan
 *
 * Jatun S.R.L
 */
package com.piramide.elwis.service.webmail;

import com.jatun.commons.email.connection.Account;
import com.jatun.commons.email.connection.ConnectionFactory;
import com.jatun.commons.email.connection.exeception.ConnectionException;
import com.jatun.commons.email.connection.pop.POPConnection;
//import com.jatun.commons.email.connection.pop.POPFactory;
import com.piramide.elwis.cmd.webmailmanager.POPFactory;
import com.piramide.elwis.domain.webmailmanager.*;
import com.piramide.elwis.service.exception.webmail.EmailNotFoundException;
import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.utils.webmail.EmailSourceUtil;
import com.piramide.elwis.utils.webmail.WebmailAccountUtil;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.*;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class EmailSourceServiceBean implements SessionBean {
    private Log log = LogFactory.getLog(this.getClass());

    private SessionContext ctx;

    public EmailSourceServiceBean() {
    }

    public void ejbCreate() throws CreateException {
    }

    public void setSessionContext(SessionContext sessionContext) throws EJBException {
        this.ctx = sessionContext;
    }

    public void ejbRemove() throws EJBException {
    }

    public void ejbActivate() throws EJBException {
    }

    public void ejbPassivate() throws EJBException {
    }

    public void storeEmailSource(Integer mailId,
                                 Integer userMailId) throws ConnectionException, EmailNotFoundException {

        UserMail userMail = WebmailAccountUtil.i.getUserMail(userMailId);

        Mail mail = getMail(mailId);
        if (null == mail) {
            throw new EmailNotFoundException("Email mailId=" + mailId + " was deleted from data base");
        }

        //the mail object contain emailSource, however the mail must be re-generated from this
        if (null == mail.getIsCompleteEmail()) {
            try {
                reBuildEmailFromEmailSource(mail, userMail);
            } catch (Exception e) {
                log.error("-> Rebuild Mail object from EmailSource FAIL ", e);
            }
        }


        // the mail contain only headers, and must be download email source object.
        if (!mail.getIsCompleteEmail()) {
            try {
                downloadEmailSource(mail, userMail);
            } catch (Exception e) {
                log.error("-> Download EmailSource FAIL", e);
            }
        }
    }

    private void reBuildEmailFromEmailSource(Mail mail,
                                             UserMail userMail) throws EmailNotFoundException,
            ConnectionException, SystemException {


        EmailSource emailSource = EmailSourceUtil.i.getEmailSource(mail.getMailId(), mail.getCompanyId());

        if (null == emailSource) {
            downloadEmailSource(mail, userMail);
            return;
        }

        UserTransaction transaction = ctx.getUserTransaction();
        try {
            transaction.begin();
            EmailSourceUtil.i.buildAndCompleteMail(mail, emailSource, userMail);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
    }

    private void downloadEmailSource(Mail mail,
                                     UserMail userMail) throws EmailNotFoundException,
            ConnectionException, SystemException {

        MailAccount mailAccount = WebmailAccountUtil.i.getMailAccount(mail.getMailAccount(), userMail.getUserMailId());
        if (null == mailAccount) {
            throw new ConnectionException("-> Read the account associated to Mail FAIL");
        }


        boolean deleteEmailFromServer = true;
        if (null != mailAccount.getKeepEmailOnServer()) {
            deleteEmailFromServer = !mailAccount.getKeepEmailOnServer();
        }

        ByteArrayOutputStream emailStream = getEmail(mail.getUidl(), mail.getMessageId(), mailAccount, deleteEmailFromServer);
        if (null == emailStream) {
            throw new EmailNotFoundException("Email with messageID=" + mail.getMessageId() +
                    "and uild=" + mail.getUidl() + " for the account " + mail.getMailAccount() +
                    " is no longer in the POP server, so its source cannot be downloaded");
        }

        UserTransaction transaction = ctx.getUserTransaction();
        try {
            transaction.begin();

            EmailSource emailSource = EmailSourceUtil.i.createEmailSource(mail.getMailId(),
                    mail.getCompanyId(),
                    emailStream.toByteArray());

            EmailSourceUtil.i.buildAndCompleteMail(mail, emailSource, userMail);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        }
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

    private ByteArrayOutputStream getEmail(String uidl,
                                           String messageId,
                                           MailAccount mailAccount,
                                           boolean deleteEmail) throws ConnectionException {

        Account popAccount = WebmailAccountUtil.i.getPOPAccount(mailAccount);

        ConnectionFactory popConnectionFactory = new POPFactory();
        POPConnection connection = null;

        ByteArrayOutputStream outputStream = null;
        try {
            connection = (POPConnection) popConnectionFactory.getConnection(popAccount);
            if (null != uidl) {
                outputStream = connection.getEmailByUidl(uidl, deleteEmail);
            } else {
                outputStream = connection.getEmail(messageId, deleteEmail);
            }
        } finally {
            if (null != connection) {
                connection.close();
            }
        }

        return outputStream;
    }

    /**
     * batch process to rebuild email with your emailsource and delete old attachments with blob data.
     * @param mailIdList mail ids
     */
    public void rebuildEmailBodyAttachInBackgroundBatchProcess(List<Integer> mailIdList) {
        UserTransaction userTransaction = ctx.getUserTransaction();
        for (int i = 0; i < mailIdList.size(); i++) {
            Integer mailId = mailIdList.get(i);
            try {
                userTransaction.begin();

                EmailSourceUtil.i.rebuildAndUpdateMail(mailId);

                userTransaction.commit();
            }  catch (Exception e) {
                log.error("There was an exception trying to rebuild email from emailsource:  " + mailId, e);
                try {
                    userTransaction.rollback();
                } catch (SystemException e1) {
                    log.error("Rollback Fail in rebuild email", e);
                }
            }
        }
    }

}
