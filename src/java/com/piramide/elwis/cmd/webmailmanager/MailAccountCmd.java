package com.piramide.elwis.cmd.webmailmanager;

import com.jatun.commons.email.connection.Account;
import com.jatun.commons.email.connection.ConnectionFactory;
import com.jatun.commons.email.connection.exeception.AuthenticationException;
import com.jatun.commons.email.connection.exeception.ConnectionException;
import com.jatun.commons.email.connection.exeception.ProviderException;
import com.jatun.commons.email.connection.pop.POPConnection;
import com.jatun.commons.email.connection.smtp.SMTPConnection;
import com.piramide.elwis.cmd.webmailmanager.SMTPFactory;
import com.piramide.elwis.cmd.webmailmanager.POPFactory;
import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.domain.catalogmanager.FreeText;
import com.piramide.elwis.domain.catalogmanager.FreeTextHome;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.AddressHome;
import com.piramide.elwis.domain.webmailmanager.*;
import com.piramide.elwis.dto.webmailmanager.MailAccountDTO;
import com.piramide.elwis.utils.*;
import com.piramide.elwis.utils.webmail.WebmailAccountUtil;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Ivan Alban
 * @version 4.2.2
 */
public class MailAccountCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext sessionContext) {
        log.debug("MailAccountCmd execute..........................." + paramDTO);

        String op = getOp();

        if ("create".equals(op)) {
            MailAccountDTO mailAccountDTO = getMailAccountDTO();
            create(mailAccountDTO);
        }
        if ("update".equals(op)) {
            MailAccountDTO mailAccountDTO = getMailAccountDTO();
            update(mailAccountDTO);
        }
        if ("delete".equals(op)) {
            delete(sessionContext);
        }
        if ("read".equals(op)) {
            read();
        }

        if ("isDuplicatedPOPAccont".equals(op)) {
            Integer mailAccountId = (Integer) paramDTO.get("mailAccountId");
            String popUser = (String) paramDTO.get("popUser");
            String popServer = (String) paramDTO.get("popServer");
            Integer userMailId = (Integer) paramDTO.get("userMailId");
            Integer companyId = (Integer) paramDTO.get("companyId");
            String formOperation = (String) paramDTO.get("formOperation");
            isDuplicatedPOPAccont(popUser, popServer, formOperation, mailAccountId, userMailId, companyId);
        }

        if ("isDuplicatedSMTPAccount".equals(op)) {
            Integer mailAccountId = (Integer) paramDTO.get("mailAccountId");
            String smtpUser = (String) paramDTO.get("smtpUser");
            String smtpServer = (String) paramDTO.get("smtpServer");
            Integer userMailId = (Integer) paramDTO.get("userMailId");
            Integer companyId = (Integer) paramDTO.get("companyId");
            String formOperation = (String) paramDTO.get("formOperation");
            isDuplicatedSMTPAccount(smtpUser, smtpServer, formOperation, mailAccountId, userMailId, companyId);
        }

        if ("checkUserMailAcountAndServer".equals(op)) {
            String login = (String) paramDTO.get("login");
            String serverName = (String) paramDTO.get("serverName");
            Integer userMailId = (Integer) paramDTO.get("userMailId");
            Integer companyId = (Integer) paramDTO.get("companyId");
            checkUserMailAcountAndServer(login, serverName, userMailId, companyId);
        }
        if ("isAvailableSMTPAccount".equals(op)) {
            String smtpUser = (String) paramDTO.get("smtpUser");
            String smtpServer = (String) paramDTO.get("smtpServer");
            Integer userMailId = (Integer) paramDTO.get("userMailId");
            Integer companyId = (Integer) paramDTO.get("companyId");
            isAvailableSMTPAccount(smtpUser, smtpServer, userMailId, companyId);
        }

        if ("checkEmailAccount".equals(op)) {
            String login = (String) paramDTO.get("login");
            String password = (String) paramDTO.get("password");
            String serverName = (String) paramDTO.get("serverName");
            String port = (String) paramDTO.get("serverPort");
            Integer userMailId = (Integer) paramDTO.get("userMailId");
            Integer userSSLConnection = (Integer) paramDTO.get("userSSLConnection");
            String validationOperation = (String) paramDTO.get("validationOperation");
            Integer mailAccountId = (Integer) paramDTO.get("mailAccountId");
            checkEmailAccount(login,
                    password,
                    serverName,
                    port,
                    userSSLConnection,
                    validationOperation, userMailId, mailAccountId);
        }

        if ("searchAccountByEmail".equals(op)) {
            String email = (String) paramDTO.get("email");
            Integer userMailId = (Integer) paramDTO.get("userMailId");
            Integer companyId = (Integer) paramDTO.get("companyId");
            String operation = (String) paramDTO.get("operation");
            if (null == operation) {
                searchAccountByEmail(email, userMailId, true);
            } else {
                searchAccountByEmail(email, userMailId, companyId, operation);
            }
        }

        if ("searchDefaultAccount".equals(op)) {
            Integer userMailId = (Integer) paramDTO.get("userMailId");
            Integer companyId = (Integer) paramDTO.get("companyId");
            searchDefaultAccount(userMailId, companyId, true);
        }

        if ("getUserAccounts".equals(op)) {
            Integer emailUserId = (Integer) paramDTO.get("emailUserId");
            Integer companyId = (Integer) paramDTO.get("companyId");
            getUserAccounts(emailUserId, companyId);
        }

        if ("validateCreateSMTPAccount".equals(getOp())) {
            String email = (String) paramDTO.get("email");
            String user = (String) paramDTO.get("login");
            String password = (String) paramDTO.get("password");
            String smtpServer = (String) paramDTO.get("smtpServer");
            String smtpPort = (String) paramDTO.get("smtpPort");
            Boolean smtpAuthentication = false;
            if (null != paramDTO.get("smtpAuthentication") &&
                    "true".equals(paramDTO.get("smtpAuthentication").toString())) {
                smtpAuthentication = true;
            }

            Integer  smtpSSL = (Integer) paramDTO.get("smtpSSL");

            validateCreateSMTPAccount(email, user, password, smtpServer, smtpPort, smtpAuthentication, smtpSSL);
        }
        if ("validateUpdateSMTPAccount".equals(getOp())) {
            String email = (String) paramDTO.get("email");
            Integer mailAccountId = (Integer) paramDTO.get("mailAccountId");
            String user = (String) paramDTO.get("login");
            String password = (String) paramDTO.get("password");
            String smtpServer = (String) paramDTO.get("smtpServer");
            String smtpPort = (String) paramDTO.get("smtpPort");
            Boolean smtpAuthentication = false;
            if (null != paramDTO.get("smtpAuthentication") &&
                    "true".equals(paramDTO.get("smtpAuthentication").toString())) {
                smtpAuthentication = true;
            }

            Integer  smtpSSL = (Integer) paramDTO.get("smtpSSL");

            validateUpdateSMTPAccount(mailAccountId, email, user, password, smtpServer, smtpPort, smtpAuthentication, smtpSSL);
        }
    }


    private void validateUpdateSMTPAccount(Integer mailAccountId,
                                           String email,
                                           String user,
                                           String password,
                                           String smtpServer,
                                           String smtpPort,
                                           Boolean smtpAuthentication,
                                           Integer smtpSSL) {
        log.debug("Validating SMTP account");
        String decodedPassword = "";
        MailAccountHome mailAccountHome =
                (MailAccountHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_MAILACCOUNT);
        try {
            MailAccount mailAccount = mailAccountHome.findByPrimaryKey(mailAccountId);
            if (!mailAccount.getPassword().equals(password)) {
                decodedPassword = password;
            } else {
                decodedPassword = getDecodedPassword(mailAccount.getUserMailId(), mailAccount.getPassword());
            }
        } catch (FinderException e) {
            log.debug("-> Find mailAccount mailAccountId=" + mailAccountId + " FAIL");
            return;
        }

        MailAccountDTO mailAccountDTO = new MailAccountDTO();
        mailAccountDTO.put("email", email);
        mailAccountDTO.put("login", user);
        mailAccountDTO.put("password", decodedPassword);
        mailAccountDTO.put("smtpServer", smtpServer);
        mailAccountDTO.put("smtpPort", smtpPort);
        mailAccountDTO.put("smtpAuthentication", smtpAuthentication);
        mailAccountDTO.put("smtpSSL", smtpSSL);

        resultDTO.put("validateUpdateSMTPAccount",
                validateSMTPConfiguration(mailAccountDTO));
    }

    private void validateCreateSMTPAccount(String email,
                                           String user,
                                           String password,
                                           String smtpServer,
                                           String smtpPort,
                                           Boolean smtpAuthentication,
                                           Integer smtpSSL) {
        MailAccountDTO mailAccountDTO = new MailAccountDTO();
        mailAccountDTO.put("email", email);
        mailAccountDTO.put("login", user);
        mailAccountDTO.put("password", password);
        mailAccountDTO.put("smtpServer", smtpServer);
        mailAccountDTO.put("smtpPort", smtpPort);
        mailAccountDTO.put("smtpAuthentication", smtpAuthentication);
        mailAccountDTO.put("smtpSSL", smtpSSL);

        resultDTO.put("validateCreateSMTPAccount",
                validateSMTPConfiguration(mailAccountDTO));
    }

    private WebMailConstants.EmailAccountErrorType validateSMTPConfiguration(MailAccountDTO mailAccountDTO) {
        String email = (String) mailAccountDTO.get("email");
        String login = (String) mailAccountDTO.get("login");
        String password = (String) mailAccountDTO.get("password");
        String smtpServer = (String) mailAccountDTO.get("smtpServer");
        String smtpPort = (String) mailAccountDTO.get("smtpPort");
        Boolean smtpAuthentication = (Boolean) mailAccountDTO.get("smtpAuthentication");
        Integer smtpSSL = (Integer) mailAccountDTO.get("smtpSSL");

        Account account = new Account();
        account.setLogin(login);
        System.out.println("***************************00: " + login);
        account.setPassword(password);
        account.setHost(smtpServer);
        account.setPort(smtpPort);
        if (smtpAuthentication) {
            account.enableAuthentication();
        }

        if (WebmailAccountUtil.i.isSTARTTLSEnabled(smtpSSL)) {
            account.enableTLS();
        }
        if (WebmailAccountUtil.i.isSSLEnabled(smtpSSL)) {
            account.enableSSL();
        }

        System.out.println("***************************11");
        ConnectionFactory smtpFactory = new SMTPFactory();
        SMTPConnection smtpConnection = null;
        Exception exception = null;
        try {
            smtpConnection = (SMTPConnection) smtpFactory.getConnection(account);
            System.out.println("***************************22");
        } catch (AuthenticationException e) {
            exception = e;
        } catch (ProviderException e) {
            exception = e;
        } catch (ConnectionException e) {
            exception = e;
        } catch (Exception e) {
            exception = e;
        } finally {
            if (null != smtpConnection) {
                smtpConnection.close();
            }
//            exception.printStackTrace();
        }

        if (null != exception) {
            if (null != exception.getCause()) {
                String exceptionMessage = exception.getCause().toString();
                log.warn("It was not possible to connect with SMTP server because of the following " +
                        "exception reported by it: " + exceptionMessage);
            }

            if (exception instanceof AuthenticationException) {
                System.out.println("***************************33 "+exception.getCause().toString());
                return WebMailConstants.EmailAccountErrorType.SMTP_AUTHENTICATION;
            }

            if (exception instanceof ProviderException) {
                System.out.println("***************************44 "+exception.getCause().toString());
                return WebMailConstants.EmailAccountErrorType.SMTP_PROVIDER;
            }

            if (exception instanceof ConnectionException) {
                System.out.println("***************************55 "+exception.getCause().toString());
                return WebMailConstants.EmailAccountErrorType.SMTP_SERVICE;
            }
            System.out.println("***************************66 "+exception.getCause().toString());
            return WebMailConstants.EmailAccountErrorType.SMTP_SERVICE;
        }

        return null;
    }

    /**
     * Create a new <code>MailAccount</code> object.
     *
     * @param mailAccountDTO <code>MailAccountDTO</code> object that contain all information for create the new
     *                       <code>MailAccount</code>.
     */
    private void create(MailAccountDTO mailAccountDTO) {

        Boolean isDefaultAccount = (Boolean) mailAccountDTO.get("defaultAccount");
        if (isDefaultAccount) {
            MailAccount defaultAccount = getDefaultAccount((Integer) mailAccountDTO.get("userMailId"),
                    (Integer) mailAccountDTO.get("companyId"));
            if (null != defaultAccount) {
                defaultAccount.setDefaultAccount(false);
            }
        }

        MailAccount mailAccount = (MailAccount) ExtendedCRUDDirector.i.create(mailAccountDTO, resultDTO, false);

        //create free text reply messages
        createOrUpdateTextReplyMessage(mailAccount);
        createOrUpdateHtmlReplyMessage(mailAccount);

        //encode pop password
        String encodedPOPPassword = encodePassword(mailAccount.getUserMailId(), mailAccount.getPassword());
        mailAccount.setPassword(encodedPOPPassword);

        //encode smtp password only when the account use diferent configuration for smtp server.
        if (!mailAccount.getUsePOPConfiguration()) {
            String encodedSMTPPassword = encodePassword(mailAccount.getUserMailId(), mailAccount.getSmtpPassword());
            mailAccount.setSmtpPassword(encodedSMTPPassword);
        } else {
            mailAccount.setSmtpPassword(null);
            mailAccount.setSmtpLogin(null);
            mailAccount.setUsePOPConfiguration(true);
        }
        if(!mailAccount.getSmtpAuthentication()) {
            mailAccount.setSmtpSSL(null);
        }
    }

    /**
     * Updates the <code>MailAccount</code> object.
     *
     * @param mailAccountDTO <code>MailAccountDTO</code> object that contains all data to update.
     */
    private void update(MailAccountDTO mailAccountDTO) {
        Integer mailAccountId = (Integer) mailAccountDTO.get("mailAccountId");

        MailAccount mailAccount = getMailAccount(mailAccountId);
        if (null == mailAccount) {
            mailAccountDTO.addNotFoundMsgTo(resultDTO);
            resultDTO.setForward("Fail");
            return;
        }

        Boolean isDefaultAccount = (Boolean) mailAccountDTO.get("defaultAccount");
        if (isDefaultAccount) {
            MailAccount defaultAccount = getDefaultAccount((Integer) mailAccountDTO.get("userMailId"),
                    (Integer) mailAccountDTO.get("companyId"));
            if (null != defaultAccount) {
                defaultAccount.setDefaultAccount(false);
            }
        }

        String newPOPUser = (String) mailAccountDTO.get("login");
        if (!mailAccount.getLogin().equals(newPOPUser)) {
            deleteUidlTrack(mailAccount);
        }

        String actualPOPPassword = mailAccount.getPassword();
        String actualSMTPPassword = mailAccount.getSmtpPassword();

        MailAccount updatedAccount =
                (MailAccount) ExtendedCRUDDirector.i.update(mailAccountDTO, resultDTO, false, false, false, "Fail");

        //update free text reply messages
        createOrUpdateTextReplyMessage(updatedAccount);
        createOrUpdateHtmlReplyMessage(updatedAccount);

        //update password only it has changed
        if (!updatedAccount.getPassword().equals(actualPOPPassword)) {
            String encodedPassword = encodePassword(updatedAccount.getUserMailId(), updatedAccount.getPassword());
            updatedAccount.setPassword(encodedPassword);
        }

        if (!updatedAccount.getUsePOPConfiguration()) {
            if (!updatedAccount.getSmtpPassword().equals(actualSMTPPassword)) {
                String encodedPassword = encodePassword(updatedAccount.getUserMailId(),
                        updatedAccount.getSmtpPassword());
                updatedAccount.setSmtpPassword(encodedPassword);
            }
        } else {
            mailAccount.setSmtpPassword(null);
            mailAccount.setSmtpLogin(null);
            mailAccount.setUsePOPConfiguration(true);
        }

        if(!updatedAccount.getSmtpAuthentication()) {
            mailAccount.setSmtpSSL(null);
        }
    }

    private void delete(SessionContext sessionContext) {
        Integer mailAccountId = new Integer(paramDTO.get("mailAccountId").toString());

        MailAccountDTO dto = new MailAccountDTO();
        dto.put("mailAccountId", mailAccountId);

        MailAccount account = (MailAccount) ExtendedCRUDDirector.i.read(dto, resultDTO, false);
        if (!account.getSignatures().isEmpty()) {
            Collection<Signature> signatures = new ArrayList<Signature>(account.getSignatures());
            SignatureCmd signatureCmd;
            for (Signature signature : signatures) {
                signatureCmd = new SignatureCmd();
                signatureCmd.putParam("signatureId", signature.getSignatureId());
                signatureCmd.setOp(ExtendedCRUDDirector.OP_DELETE);
                signatureCmd.executeInStateless(sessionContext);
            }
        }

        deleteUidlTrack(account);

        Collection<EmailAccountError> emailAccountErrors = getEmailAccountError(account);
        if (emailAccountErrors != null) {
            for (EmailAccountError emailAccountError : emailAccountErrors) {
                emailAccountError.setMailAccountId(null);
            }
        }

        Integer replyMessageTextId = account.getReplyMessageTextId();
        Integer replyMessageHtmlId = account.getReplyMessageHtmlId();

        try {
            account.remove();

            //remove reply messages free texts
            deleteFreeText(replyMessageTextId);
            deleteFreeText(replyMessageHtmlId);
        } catch (RemoveException e) {
            log.debug("Cannot remove account with identifier " + mailAccountId);
        }


    }

    private Collection<EmailAccountError> getEmailAccountError(MailAccount mailAccount) {
        EmailAccountErrorHome emailAccountErrorHome =
                (EmailAccountErrorHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_EMAILACCOUNTERROR);
        try {
            return emailAccountErrorHome.findByMailAccountId(mailAccount.getUserMailId(),
                    mailAccount.getMailAccountId(),
                    mailAccount.getCompanyId());
        } catch (FinderException e) {
            return null;
        }
    }

    private void deleteUidlTrack(MailAccount account) {
        List uidlList = getUidlTrack(account.getMailAccountId(), account.getCompanyId());
        for (int i = 0; i < uidlList.size(); i++) {
            UidlTrack uidlTrack = (UidlTrack) uidlList.get(i);
            try {
                uidlTrack.remove();
            } catch (RemoveException e) {
                log.debug("-> Remove UidlTrack for mailAccountId=" + account.getMailAccountId() + " FAIL");
            }
        }
    }

    private List getUidlTrack(Integer mailAccountId, Integer companyId) {
        UidlTrackHome uidlTrackHome = (UidlTrackHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_UIDLTRACK);
        try {
            return (List) uidlTrackHome.findByMailAccountId(mailAccountId, companyId);
        } catch (FinderException e) {
            log.debug("-> No uidl tracking for mailAccountId=" + mailAccountId);
        }

        return new ArrayList();
    }

    private void read() {
        Integer mailAccountId = new Integer(paramDTO.get("mailAccountId").toString());
        MailAccountDTO dto = new MailAccountDTO();
        dto.put("mailAccountId", mailAccountId);

        MailAccount mailAccount = (MailAccount) ExtendedCRUDDirector.i.read(dto, resultDTO, false);

        if (null == mailAccount) {
            dto.addNotFoundMsgTo(resultDTO);
            resultDTO.setForward("Fail");
            return;
        }

        //read reply message free texts
        resultDTO.put("textReplyMessage", readFreeTextValue(mailAccount.getReplyMessageTextId()));
        resultDTO.put("htmlReplyMessage", readFreeTextValue(mailAccount.getReplyMessageHtmlId()));
    }


    private MailAccount getDefaultEmailAccount(Integer emailUserId, Integer companyId) {
        MailAccountHome accountHome = getMailAccountHome();
        try {
            return accountHome.findDefaultAccount(emailUserId, companyId);
        } catch (FinderException e) {
            //the user have not assigned default account
        }

        return null;
    }

    /**
     * Search  all webmail accounts associated to the <code>emailUserId</code> parameter, and put in
     * <code>ResultDTO</code> the next keys:
     * <p/>
     * 1.- defaultEmailAccount: than contain a <code>MailAccountDTO</code> object, it is the default account.
     * 2.- getUserAccounts: than contain a <code>List</code> of <code>MailAccountDTO</code> object,
     * it is the all accounts associated to the <code>emailUserId</code> parameter.
     *
     * @param emailUserId <code>Integer</code> object it is the usermail identifier.
     * @param companyId   <code>Integer</code> object, it is the company identifier.
     */
    private void getUserAccounts(Integer emailUserId, Integer companyId) {

        MailAccount defaultMailAccount = getDefaultEmailAccount(emailUserId, companyId);
        if (null != defaultMailAccount) {
            MailAccountDTO defaultAccountDTO = new MailAccountDTO();
            DTOFactory.i.copyToDTO(defaultMailAccount, defaultAccountDTO);
            resultDTO.put("defaultEmailAccount", defaultAccountDTO);
        }

        List<MailAccountDTO> result = new ArrayList<MailAccountDTO>();

        MailAccountHome accountHome = getMailAccountHome();
        try {
            List emailAccounts = (List) accountHome.findAccountsByUserMailAndCompany(emailUserId, companyId);
            for (int i = 0; i < emailAccounts.size(); i++) {
                MailAccount mailAccount = (MailAccount) emailAccounts.get(i);
                MailAccountDTO mailAccountDTO = new MailAccountDTO();
                DTOFactory.i.copyToDTO(mailAccount, mailAccountDTO);
                result.add(mailAccountDTO);
            }
        } catch (FinderException e) {
            //The user accounts was empty
        }

        resultDTO.put("getUserAccounts", result);
    }


    /**
     * This method search default mail account and puts <code>MailAccountDTO</code>
     * in <code>resultDTO</code> object under key "defaultAccount"
     * if user not defined default account retunrs null.
     *
     * @param userMailId  user mail identifier.
     * @param companyId   mail user company identifier.
     * @param publicInDTO to public default account information in <code>resultDTO</code>.
     * @return <code>MailAccount</code> object that is default account, if not exists returns null.
     * @deprecated
     */
    private MailAccount searchDefaultAccount(Integer userMailId, Integer companyId, boolean publicInDTO) {
        MailAccountHome accountHome = getMailAccountHome();
        MailAccount defaultMailAccount = null;
        try {
            defaultMailAccount = accountHome.findDefaultAccount(userMailId, companyId);
            if (publicInDTO) {
                MailAccountDTO defaultAccountDTO = new MailAccountDTO();
                DTOFactory.i.copyToDTO(defaultMailAccount, defaultAccountDTO);
                resultDTO.put("defaultAccount", defaultAccountDTO);
            }
        } catch (FinderException e) {
            log.debug("Cannot find default account for user " + userMailId + " in company " + companyId);
        }

        return defaultMailAccount;
    }

    private MailAccount getDefaultAccount(Integer userMailId, Integer companyId) {
        MailAccountHome accountHome = getMailAccountHome();
        try {
            return accountHome.findDefaultAccount(userMailId, companyId);

        } catch (FinderException e) {
            log.debug("Cannot find default account for user " + userMailId + " in company " + companyId);
        }

        return null;
    }

    /**
     * This method search an e-mail account from email, and the mail user who owns the associated company,
     * Depending on the operation to be doing this, composition or reply, puts an outcome in
     * <code>resultDTO</code> object.
     * If the operation is the composition, puts default mail account.
     * If the operation is response, it search to mail account associated with mail,
     * and if it exists, then this is the object <code>resultDTO</code>;  if is not the case,
     * then the default account is used. if there is no account by default, then put a null value
     * in <code>resultDTO</code>.
     * The account object is registered with the key "mailAccount".
     *
     * @param email      email to search account.
     * @param userMailId user mail identifier.
     * @param companyId  mail user company identifier.
     * @param operation  Mail operations compose or reply.
     */
    private void searchAccountByEmail(String email, Integer userMailId, Integer companyId, String operation) {
        log.debug("searchAccountByEmail - email:" + email + " userMailId:" +
                userMailId + " companyId:" + companyId + " operation:" + operation);
        MailAccount account;
        if (WebMailConstants.MailOperations.REPLY.getOperation().equals(operation)) {
            account = searchAccountByEmail(email, userMailId, false);
            if (null == account) {
                account = searchDefaultAccount(userMailId, companyId, false);
            }
        } else {
            account = searchDefaultAccount(userMailId, companyId, false);
        }

        if (null != account) {
            MailAccountDTO accountDTO = new MailAccountDTO();
            DTOFactory.i.copyToDTO(account, accountDTO);
            resultDTO.put("mailAccount", accountDTO);
        }
    }

    /**
     * This method search an web mail account from the email field and mail user identifier,
     * If there is more than one e-mail account associated to  email, the first account is selected
     * and putting the identifier in the subject <code>resultDTO</code> object if publicInDTO = true.
     *
     * @param email       email to search account
     * @param userMailId  user mail identifier
     * @param publicInDTO to public default account information in <code>resultDTO</code>.
     * @return <code>MailAccount</code> object that is default account, if not exists returns null.
     */
    private MailAccount searchAccountByEmail(String email, Integer userMailId, boolean publicInDTO) {
        log.debug("searchAccountByEmail - email:" + email + " userMailId :" + userMailId);
        MailAccountHome accountHome = getMailAccountHome();

        Integer mailAccountId = null;
        MailAccount account = null;
        try {
            Collection accounts = accountHome.findAccountsByEmailAndUser(email, userMailId);
            if (null != accounts && accounts.size() > 0) {
                account = (MailAccount) accounts.toArray()[0];
                mailAccountId = account.getMailAccountId();
            }
        } catch (FinderException e) {
            log.debug("User " + userMailId + " cannot assigned any account with email " + email);
        }
        if (publicInDTO) {
            resultDTO.put("mailAccountId", mailAccountId);
        }
        return account;
    }

    private void isDuplicatedSMTPAccount(String smtpUser,
                                         String smtpServer,
                                         String formOperation,
                                         Integer mailAccountId,
                                         Integer userMailId,
                                         Integer companyId) {
        MailAccountHome mailAccountHome = getMailAccountHome();

        boolean isDuplicated = false;
        try {
            MailAccount account = mailAccountHome.findSMTPAccountsByUserMail(smtpUser, smtpServer, userMailId, companyId);
            if ("create".equals(formOperation)) {
                isDuplicated = true;
            }
            if ("update".equals(formOperation)) {
                isDuplicated = !account.getMailAccountId().equals(mailAccountId);
            }
        } catch (FinderException e) {
            //
        }

        resultDTO.put("isDuplicatedSMTPAccount", isDuplicated);
    }

    private void isDuplicatedPOPAccont(String popUser,
                                       String popServer,
                                       String formOperation,
                                       Integer mailAccountId,
                                       Integer userMailId,
                                       Integer companyId) {
        boolean isDuplicated = false;
        MailAccountHome accountHome = getMailAccountHome();
        try {
            MailAccount account = accountHome.findAccountByUserMail(popUser, popServer, userMailId, companyId);
            if ("create".equals(formOperation)) {
                isDuplicated = true;
            }
            if ("update".equals(formOperation)) {
                isDuplicated = !account.getMailAccountId().equals(mailAccountId);
            }
        } catch (FinderException e) {
            //
        }

        resultDTO.put("isDuplicatedPOPAccont", isDuplicated);
    }

    private void isAvailableSMTPAccount(String smtpUser,
                                        String smtpServer,
                                        Integer userMailId,
                                        Integer companyId) {
        boolean result = false;
        MailAccountHome accountHome = getMailAccountHome();

        List othersUsersWebmail = new ArrayList();
        try {
            Collection duplicatedAccounts = accountHome.findSMTPAccountsByUserAndServerName(
                    smtpUser, smtpServer, userMailId, companyId);

            if (duplicatedAccounts.isEmpty()) {
                result = true;
            }

            if (!result) {
                for (Object object : duplicatedAccounts) {
                    MailAccount account = (MailAccount) object;
                    othersUsersWebmail.add(account.getUserMailId());
                }
                log.debug("Users also have assigned account login: " + smtpUser + ", serverName: " + smtpServer + "\n" +
                        othersUsersWebmail);
            }
        } catch (FinderException e) {
            result = true;
        }
        if (!result) {
            UserHome elwisUserHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);

            AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
            String names = "";
            for (int i = 0; i < othersUsersWebmail.size(); i++) {
                Integer elwisUserId = (Integer) othersUsersWebmail.get(i);
                User elwisUser = null;
                try {
                    elwisUser = elwisUserHome.findByPrimaryKey(elwisUserId);
                } catch (FinderException e) {
                    log.debug("Cannot find elwis user with identifier" + elwisUserId);
                }

                if (null != elwisUser) {
                    try {
                        Address address = addressHome.findByPrimaryKey(elwisUser.getAddressId());
                        names = address.getName1();
                        names += (null != address.getName2() ? ", " + address.getName2() : "; ");
                    } catch (FinderException e) {
                        log.debug("Cannot find Address with identifier" + elwisUser.getAddressId());
                    }
                }
            }

            resultDTO.addResultMessage("Webmail.smtpAccountAssigned", names);
            resultDTO.setResultAsFailure();
        }
    }

    /**
     * This method verifies that the object <code>MailAccount</code> not assigned to another user.
     * if <code>MailAccount</code> object already assigned to another user, puts in <code>ResultDTO</code> Object
     * error message "Webmail.PopAccountAssigned" that contain all names for users can use this account,
     * and setting up resultDTO as faliure.
     *
     * @param login      user login form mail account
     * @param serverName server name for mail account
     * @param userMailId mail user identifier
     * @param companyId  mail user company identifier
     */
    private void checkUserMailAcountAndServer(String login,
                                              String serverName,
                                              Integer userMailId,
                                              Integer companyId) {
        boolean result = false;

        MailAccountHome accountHome = getMailAccountHome();

        UserHome elwisUserHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);

        AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);

        List othersUsersWebmail = new ArrayList();
        try {
            Collection duplicatedAccounts = accountHome.findAccountsByLoginAndServerName(
                    login, serverName, userMailId, companyId);

            if (duplicatedAccounts.isEmpty()) {
                result = true;
            }

            if (!result) {
                for (Object object : duplicatedAccounts) {
                    MailAccount account = (MailAccount) object;
                    othersUsersWebmail.add(account.getUserMailId());
                }
                log.debug("Users also have assigned account login: " + login + ", serverName: " + serverName + "\n" +
                        othersUsersWebmail);
            }
        } catch (FinderException e) {
            result = true;
        }
        if (!result) {
            String names = "";
            for (int i = 0; i < othersUsersWebmail.size(); i++) {
                Integer elwisUserId = (Integer) othersUsersWebmail.get(i);
                User elwisUser = null;
                try {
                    elwisUser = elwisUserHome.findByPrimaryKey(elwisUserId);
                } catch (FinderException e) {
                    log.debug("Cannot find elwis user with identifier" + elwisUserId);
                }

                if (null != elwisUser) {
                    try {
                        Address address = addressHome.findByPrimaryKey(elwisUser.getAddressId());
                        names = address.getName1();
                        names += (null != address.getName2() ? ", " + address.getName2() : "; ");
                    } catch (FinderException e) {
                        log.debug("Cannot find Address with identifier" + elwisUser.getAddressId());
                    }
                }
            }

            resultDTO.addResultMessage("Webmail.PopAccountAssigned", names);
            resultDTO.setResultAsFailure();
        }
    }

    /**
     * This method verifies that the e-mail account actually exists, using the service connections and
     * makes a connection to the account of emails.
     * If cannot connect for e-mail account put in <code>ResultDTO</code> object "Webmail.cannotConnect" error message.
     * If server cannot offer POP service put in <code>ResultDTO</code> object "Webmail.notFeaturePop" error message.
     * For any error in the connection put in <code>ResultDTO</code> object "Webmail.cannotConnectPop" error message.
     * <p/>
     * For any error setting up resultDTO as faliure.
     *
     * @param login               user login form mail account
     * @param password            user e-mail accout password
     * @param server              server name for mail account
     * @param port                service port
     * @param useSSLConnection    if connection required SSL connection
     * @param validationOperation operation for validation "create" or "update"
     * @param userMailId          mail user identifier
     * @param mailAccountId       actual mailAccount identifier (for update operations)
     */
    private void checkEmailAccount(String login,
                                   String password,
                                   String server,
                                   String port,
                                   Integer useSSLConnection,
                                   String validationOperation,
                                   Integer userMailId,
                                   Integer mailAccountId) {
        log.debug("Validating POP account");
        //if update validation operation then descipher password
        if ("update".equals(validationOperation)) {
            MailAccount mailAccount = getMailAccount(mailAccountId);
            if (null == mailAccount) {
                return;
            }

            if (mailAccount.getPassword().equals(password)) {
                password = getDecodedPassword(userMailId, password);
            }
        }

        Account popAccount = new Account();
        popAccount.setLogin(login);
        System.out.println("***************************POP0:login"+ login);
        popAccount.setPassword(password);
        System.out.println("***************************POP0:password"+ password);
        popAccount.setHost(server);
        System.out.println("***************************POP0:server"+ server);
        popAccount.setPort(port);
        System.out.println("***************************POP0:port"+ port);
        popAccount.enableAuthentication();
        System.out.println("***************************POP0:enableAuthentication"+ popAccount.getAuthentication());

        if (WebmailAccountUtil.i.isSTARTTLSEnabled(useSSLConnection)) {
            System.out.println("***************************POP0:useSSLConnection:isSTARTTLSEnabled:"+ useSSLConnection);
            popAccount.enableTLS();
        }
        if (WebmailAccountUtil.i.isSSLEnabled(useSSLConnection)) {
            System.out.println("***************************POP0:useSSLConnection:isSSLEnabled:"+ useSSLConnection);
            popAccount.enableSSL();
        }

        ConnectionFactory popConnectionFactory = new POPFactory();
        POPConnection connection = null;
        Exception exception = null;
        System.out.println("***************************POP1");
        try {
            connection = (POPConnection) popConnectionFactory.getConnection(popAccount);
            System.out.println("***************************POP2");
        } catch (AuthenticationException e) {
            resultDTO.addResultMessage("Webmail.cannotConnect");
            resultDTO.setResultAsFailure();
            exception = e;
        } catch (ProviderException e) {
            resultDTO.addResultMessage("Webmail.notFeaturePop");
            resultDTO.setResultAsFailure();
            exception = e;
        } catch (ConnectionException e) {
            resultDTO.addResultMessage("Webmail.cannotConnectPop");
            resultDTO.setResultAsFailure();
            exception = e;
        } catch (Exception e) {
            resultDTO.addResultMessage("Webmail.cannotConnectPop");
            resultDTO.setResultAsFailure();
            exception = e;
        } finally {
            if (null != connection) {
                connection.close();
            }
        }

        if (null != exception && null != exception.getCause()) {
            String exceptionMessage = exception.getCause().toString();
            log.warn("MailAccountCmd wasn't validate POP Account cause:" + exceptionMessage);
            System.out.println("***************************POP3:"+ exceptionMessage);
        }
    }

    private String getDecodedPassword(Integer userMailId, String password) {
        UserMailHome userHome =
                (UserMailHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_USERMAIL);

        UserMail user = null;
        try {
            user = userHome.findByPrimaryKey(userMailId);
        } catch (FinderException e) {
            log.debug("Cannot find userMail " + userMailId);
        }

        String decodedPassword = password;
        if (null != user) {
            try {
                decodedPassword = EncryptUtil.i.desCipher(password, user.getUserMailId().toString());
            } catch (Exception e) {
                log.debug("Cannot desCipher password for " + userMailId);
            }
        }
        return decodedPassword;
    }

    /**
     * This method encode user mail account password
     *
     * @param userMailId user mail identifier
     * @param password   password to encode
     * @return encoded password
     */
    private String encodePassword(Integer userMailId, String password) {
        UserMailHome userHome =
                (UserMailHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_USERMAIL);

        UserMail user = null;
        try {
            user = userHome.findByPrimaryKey(userMailId);
        } catch (FinderException e) {
            log.debug("Cannot find userMail " + userMailId);
        }

        String encodedPassword = password;
        if (null != user) {
            try {
                encodedPassword = EncryptUtil.i.cipher(password, user.getUserMailId().toString());
            } catch (Exception e) {
                log.debug("Cannot encode password for " + userMailId);
            }
        }

        return encodedPassword;
    }


    private MailAccountDTO getMailAccountDTO() {
        MailAccountDTO mailAccountDTO = new MailAccountDTO();
        EJBCommandUtil.i.setValueAsInteger(this, mailAccountDTO, "accountType");
        EJBCommandUtil.i.setValueAsInteger(this, mailAccountDTO, "companyId");
        EJBCommandUtil.i.setValueAsInteger(this, mailAccountDTO, "mailAccountId");
        EJBCommandUtil.i.setValueAsInteger(this, mailAccountDTO, "userMailId");

        mailAccountDTO.put("email", paramDTO.get("email"));
        mailAccountDTO.put("login", paramDTO.get("login"));
        mailAccountDTO.put("password", paramDTO.get("password"));

        Boolean defaultAccount = EJBCommandUtil.i.getValueAsBoolean(this, "defaultAccount");
        if (null == defaultAccount) {
            defaultAccount = false;
        }
        mailAccountDTO.put("defaultAccount", defaultAccount);

        mailAccountDTO.put("prefix", paramDTO.get("prefix"));
        mailAccountDTO.put("serverName", paramDTO.get("serverName"));
        mailAccountDTO.put("serverPort", paramDTO.get("serverPort"));

        Boolean smtpAuthentication = EJBCommandUtil.i.getValueAsBoolean(this, "smtpAuthentication");
        if (null == smtpAuthentication) {
            smtpAuthentication = false;
        }
        mailAccountDTO.put("smtpAuthentication", smtpAuthentication);

        mailAccountDTO.put("smtpLogin", paramDTO.get("smtpLogin"));
        mailAccountDTO.put("smtpPassword", paramDTO.get("smtpPassword"));
        mailAccountDTO.put("smtpPort", paramDTO.get("smtpPort"));
        mailAccountDTO.put("smtpServer", paramDTO.get("smtpServer"));

        Integer smtpSSL = EJBCommandUtil.i.getValueAsInteger(this, "smtpSSL");
        mailAccountDTO.put("smtpSSL", smtpSSL);

        Boolean usePOPConfiguration = EJBCommandUtil.i.getValueAsBoolean(this, "usePOPConfiguration");
        if (null == usePOPConfiguration) {
            usePOPConfiguration = false;
        }
        mailAccountDTO.put("usePOPConfiguration", usePOPConfiguration);

        Integer useSSLConnection = EJBCommandUtil.i.getValueAsInteger(this, "useSSLConnection");
        mailAccountDTO.put("useSSLConnection", useSSLConnection);

        mailAccountDTO.put("keepEmailOnServer", EJBCommandUtil.i.getValueAsBoolean(this, "keepEmailOnServer"));
        mailAccountDTO.put("createInCommunication", EJBCommandUtil.i.getValueAsBoolean(this, "createInCommunication"));
        mailAccountDTO.put("createOutCommunication", EJBCommandUtil.i.getValueAsBoolean(this, "createOutCommunication"));
        mailAccountDTO.put("automaticForward", EJBCommandUtil.i.getValueAsBoolean(this, "automaticForward"));
        mailAccountDTO.put("automaticReply", EJBCommandUtil.i.getValueAsBoolean(this, "automaticReply"));
        mailAccountDTO.put("forwardEmail", paramDTO.get("forwardEmail"));
        mailAccountDTO.put("automaticReplyMessageSubject", paramDTO.get("automaticReplyMessageSubject"));
        mailAccountDTO.put("automaticReplyMessage", paramDTO.get("automaticReplyMessage"));
        mailAccountDTO.put("removeEmailOnServerAfterOf", paramDTO.get("removeEmailOnServerAfterOf"));

        return mailAccountDTO;
    }

    /**
     * This method returns <code>MailAccountHome</code> object
     *
     * @return <code>MailAccountHome</code> object;
     */
    private MailAccountHome getMailAccountHome() {
        return (MailAccountHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_MAILACCOUNT);
    }


    private MailAccount getMailAccount(Integer mailAccountId) {
        MailAccountHome mailAccountHome = getMailAccountHome();
        try {
            return mailAccountHome.findByPrimaryKey(mailAccountId);
        } catch (FinderException e) {
            return null;
        }
    }

    private void createOrUpdateTextReplyMessage(MailAccount mailAccount) {
        String textMessage = (String) paramDTO.get("textReplyMessage");

        if (mailAccount != null) {
            if (mailAccount.getReplyMessageTextId() != null) {
                updateFreeText(textMessage, mailAccount.getReplyMessageTextId());
            } else if (textMessage != null) {
                FreeText freeText = createFreeText(textMessage, mailAccount.getCompanyId());
                if (freeText != null) {
                    mailAccount.setReplyMessageTextId(freeText.getFreeTextId());
                }
            }
        }
    }

    private void createOrUpdateHtmlReplyMessage(MailAccount mailAccount) {
        String htmlMessage = (String) paramDTO.get("htmlReplyMessage");

        if (mailAccount != null) {
            if (mailAccount.getReplyMessageHtmlId() != null) {
                updateFreeText(htmlMessage, mailAccount.getReplyMessageHtmlId());
            } else if (htmlMessage != null) {
                FreeText freeText = createFreeText(htmlMessage, mailAccount.getCompanyId());
                if (freeText != null) {
                    mailAccount.setReplyMessageHtmlId(freeText.getFreeTextId());
                }
            }
        }
    }

    private FreeText findFreeText(Integer freeTextId) {
        FreeTextHome freeTextHome = (FreeTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_FREETEXT);

        if (freeTextId != null) {
            try {
                return freeTextHome.findByPrimaryKey(freeTextId);
            } catch (FinderException e) {
                log.debug("Cannot find FreeText.." + freeTextId, e);
            }
        }
        return null;
    }

    private String readFreeTextValue(Integer freeTextId) {
        String value = null;
        FreeText freeText = findFreeText(freeTextId);
        if (freeText != null) {
            value = new String(freeText.getValue());
        }
        return value;
    }

    private FreeText createFreeText(String message, Integer companyId) {
        FreeText freeText = null;

        if (message != null) {
            FreeTextHome freeTextHome = (FreeTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_FREETEXT);
            try {
                freeText = freeTextHome.create(message.getBytes(), companyId, FreeTextTypes.FREETEXT_MAILACCOUNT);
            } catch (CreateException e) {
                log.debug("Cannot create FreeText....", e);
            }
        }
        return freeText;
    }

    private FreeText updateFreeText(String message, Integer freeTextId) {
        FreeText freeText = findFreeText(freeTextId);
        if (freeText != null) {
            freeText.setValue((message != null) ? message.getBytes() : null);
        }
        return freeText;
    }

    private void deleteFreeText(Integer freeTextId) {
        FreeText freeText = findFreeText(freeTextId);
        if (freeText != null) {
            try {
                freeText.remove();
            } catch (RemoveException e) {
                log.debug("Freetext was deleted by other user.", e);
            }
        }
    }


    public boolean isStateful() {
        return false;
    }
}
