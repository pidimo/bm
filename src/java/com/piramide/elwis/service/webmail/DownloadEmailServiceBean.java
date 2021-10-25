/**
 * @author : Ivan
 *
 * Jatun S.R.L
 */
package com.piramide.elwis.service.webmail;

import com.jatun.commons.email.connection.exeception.AuthenticationException;
import com.jatun.commons.email.connection.exeception.ConnectionException;
import com.jatun.commons.email.connection.exeception.ProviderException;
import com.piramide.elwis.domain.admin.UserSessionLogHome;
import com.piramide.elwis.domain.webmailmanager.FolderHome;
import com.piramide.elwis.domain.webmailmanager.MailAccount;
import com.piramide.elwis.domain.webmailmanager.MailHome;
import com.piramide.elwis.domain.webmailmanager.UserMail;
import com.piramide.elwis.service.exception.webmail.EmailNotFoundException;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.utils.webmail.DownloadEmailUtil;
import com.piramide.elwis.utils.webmail.WebmailAccountUtil;
import com.piramide.elwis.utils.webmail.jms.UserMessage;
import com.piramide.elwis.utils.webmail.jms.WebmailAccountMessage;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.*;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import java.util.ArrayList;
import java.util.List;

public class DownloadEmailServiceBean implements SessionBean {
    private Log log = LogFactory.getLog(this.getClass());

    private SessionContext ctx;

    public DownloadEmailServiceBean() {
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

    public void downloadEmails(Integer userMailId) {
        UserMail userMail = WebmailAccountUtil.i.getUserMail(userMailId);

        UserMessage userMessage = new UserMessage(userMail.getUserMailId(), true);

        try {
            downloadEmailSync(userMessage, userMail.getCompanyId());
        } catch (SystemException e) {
            log.error("-> Error when execute downloadEmailSync Method ", e);
        }
    }

    public void downloadEmails(WebmailAccountMessage accountMessage) throws SystemException {
        UserTransaction userTransaction = ctx.getUserTransaction();

        MailAccount mailAccount = WebmailAccountUtil.i.getMailAccount(accountMessage.getMailAccountId());
        if (null == mailAccount) {
            return;
        }

        ConnectionException connectionException = null;

        DownloadEmailUtil downloadEmailUtil = new DownloadEmailUtil(accountMessage, userTransaction);

        try {
            downloadEmailUtil.downloadEmails();
        } catch (AuthenticationException e) {
            connectionException = e;
        } catch (ProviderException e) {
            connectionException = e;
        } catch (ConnectionException e) {
            connectionException = e;
        } catch (Exception e) {
            String exceptionMessage = "";
            if (null != e.getCause()) {
                exceptionMessage = e.getCause().toString();
            }
            if (!"".equals(exceptionMessage.trim())) {
                log.error("-> Unexpected exception has happened. DownloadEmailService was unable to download the emails for mailAccountId=" +
                        accountMessage.getMailAccountId() + " and userMailId=" + accountMessage.getUserMailId() + " cause : " + exceptionMessage);
            } else {
                log.error("-> Unexpected exception has happened. DownloadEmailService was unable to download the emails for mailAccountId=" +
                        accountMessage.getMailAccountId() + " and userMailId=" + accountMessage.getUserMailId(), e);
            }
        }

        if (null != connectionException) {
            //log.debug("Error when downloading of mailAccountId=" + mailAccount.getMailAccountId(), connectionException);
            DownloadLogFactory.i.log(this.getClass(), "Error when downloading of mailAccountId=" + mailAccount.getMailAccountId() + " " + connectionException);

            if (accountMessage.getConnected()) {
                try {
                    userTransaction.begin();
                    saveAccountErrors(accountMessage.getUserMailId(),
                            mailAccount,
                            accountMessage.getCompanyId(),
                            connectionException);
                    userTransaction.commit();
                } catch (Exception e) {
                    userTransaction.rollback();
                }
            }
        }
    }


    private void downloadEmailSync(UserMessage userMessage, Integer companyId) throws SystemException {
        UtilService utilService = getUtilService();

        List<WebmailAccountMessage> accounts = utilService.buildWebmailAccountMessages(
                userMessage.getUserMailId(),
                userMessage.getConnected()
        );

        for (WebmailAccountMessage accountMessage : accounts) {
            downloadEmails(accountMessage);
        }

        EmailSourceService emailSourceService = getEmailSourceService();
        List<Integer> incompleteEmailIdentifiers = getIncompleteEmails(userMessage, companyId);
        if (!incompleteEmailIdentifiers.isEmpty()) {
            log.debug("-> Now complete the nex emails : " + incompleteEmailIdentifiers);
            for (Integer identifier : incompleteEmailIdentifiers) {
                try {
                    emailSourceService.storeEmailSource(identifier, userMessage.getUserMailId());
                } catch (ConnectionException e) {
                    log.warn(e.getMessage());
                } catch (EmailNotFoundException e) {
                    log.warn(e.getMessage());
                }
            }
        }
    }


    private List<Integer> getIncompleteEmails(UserMessage userMessage, Integer companyId) {
        FolderHome folderHome =
                (FolderHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_FOLDER);

        List<Integer> folderIdentifiers = new ArrayList<Integer>();
        try {
            folderIdentifiers = (List<Integer>) folderHome.selectGetFolderIdentifiers(userMessage.getUserMailId(),
                    companyId);
        } catch (FinderException e) {
            log.debug("-> Read Folders for userMailId=" + userMessage.getUserMailId() + " FAIL");
        }

        MailHome mailHome =
                (MailHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_MAIL);
        List<Integer> emailIdentifiers = new ArrayList<Integer>();
        for (Integer folderId : folderIdentifiers) {
            List<Integer> mailIdentifiers = new ArrayList<Integer>();
            try {
                mailIdentifiers =
                        (List<Integer>) mailHome.selectGetIncompleteEmails(folderId, companyId);
            } catch (FinderException e) {
                //
            }
            if (!mailIdentifiers.isEmpty()) {
                emailIdentifiers.addAll(mailIdentifiers);
            }
        }

        return emailIdentifiers;
    }


    private void saveAccountErrors(Integer userMailId,
                                   MailAccount mailAccount,
                                   Integer companyId,
                                   ConnectionException exception) throws Exception {
        UtilService utilService = getUtilService();
        if (null == utilService) {
            return;
        }
        if (isConnected(userMailId)) {
            utilService.savePopErrors(userMailId, mailAccount, companyId, exception);
        }
    }

    private UtilService getUtilService() {
        UtilServiceHome utilServiceHome =
                (UtilServiceHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_UTIL_SERVICE);

        try {
            return utilServiceHome.create();
        } catch (CreateException e) {
            return null;
        }
    }

    private EmailSourceService getEmailSourceService() {
        EmailSourceServiceHome emailSourceServiceHome =
                (EmailSourceServiceHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_EMAIL_SOURCE_SERVICE);
        try {
            return emailSourceServiceHome.create();
        } catch (CreateException e) {
            return null;
        }
    }

    private Boolean isConnected(Integer userId) {
        UserSessionLogHome userSessionLogHome =
                (UserSessionLogHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USERSESSIONLOG);
        try {
            Boolean isConnected = userSessionLogHome.findByPrimaryKey(userId).getIsConnected();
            return null != isConnected && isConnected;
        } catch (FinderException e) {
            return false;
        }
    }
}
