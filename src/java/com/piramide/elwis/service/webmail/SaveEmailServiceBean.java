/**
 * @author ivan
 *
 * Jatun S.R.L. 
 */
package com.piramide.elwis.service.webmail;

import com.jatun.commons.email.model.Address;
import com.jatun.commons.email.model.Email;
import com.jatun.commons.email.model.Header;
import com.piramide.elwis.cmd.webmailmanager.CommunicationManagerCmd;
import com.piramide.elwis.domain.webmailmanager.*;
import com.piramide.elwis.dto.webmailmanager.MailDTO;
import com.piramide.elwis.dto.webmailmanager.MailRecipientDTO;
import com.piramide.elwis.dto.webmailmanager.UidlTrackDTO;
import com.piramide.elwis.utils.CharacterUtil;
import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.utils.webmail.EmailSourceUtil;
import com.piramide.elwis.utils.webmail.MailStateUtil;
import com.piramide.elwis.utils.webmail.jms.WebmailAccountMessage;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

import javax.ejb.*;
import java.io.ByteArrayOutputStream;
import java.util.*;

public class SaveEmailServiceBean implements SessionBean {

    private Log log = LogFactory.getLog(this.getClass());

    private SessionContext ctx;

    public SaveEmailServiceBean() {
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

    //businessMethods
    public Mail storeEmail(Header header,
                           WebmailAccountMessage accountMessage,
                           MailAccount mailAccount, ByteArrayOutputStream emailSource) {
        MailHome mailHome =
                (MailHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_MAIL);

        MailDTO mailDTO = new MailDTO();
        mailDTO.put("companyId", mailAccount.getCompanyId());
        mailDTO.put("folderId", applyFilters(header, accountMessage));
        mailDTO.put("haveAttachments", header.haveAttachments());
        mailDTO.put("hidden", false);
        mailDTO.put("incomingOutgoing", WebMailConstants.IN_VALUE);
        mailDTO.put("mailAccount", mailAccount.getEmail());
        mailDTO.put("mailFrom", header.getFrom().getEmail());

        String mailPersonalFrom = header.getFrom().getEmail();
        if (null != header.getFrom().getPersonal() && !"".equals(header.getFrom().getPersonal())) {
            mailPersonalFrom = header.getFrom().getPersonal();
        }
        //remove special symbols
        mailPersonalFrom = CharacterUtil.cleanMysqlUnsupportedSymbols(mailPersonalFrom);
        mailDTO.put("mailPersonalFrom", CharacterUtil.truncate(mailPersonalFrom, 250));

        mailDTO.put("mailPriority", Integer.valueOf(WebMailConstants.MAIL_PRIORITY_DEFAULT));
        if (1 == header.getPriority() || 2 == header.getPriority()) {
            mailDTO.put("mailPriority", Integer.valueOf(WebMailConstants.MAIL_PRIORITY_HIGHT));
        }

        mailDTO.put("mailSize", header.getSize());
        mailDTO.put("mailState", MailStateUtil.getDefault());
        mailDTO.put("mailSubject", CharacterUtil.cleanMysqlUnsupportedSymbols(header.getSubject()));
        mailDTO.put("messageId", header.getMessageId());
        mailDTO.put("sentDate", header.getSentDate().getTime());
        mailDTO.put("isNewEmail", true);

        /*
        set null value because the new emails contain only email source object and in the mailtray list the communication icon
        filter all messages when isCompleteEmail = 1 or isCompleteEmail is empty
        */
        mailDTO.put("isCompleteEmail", null);
        mailDTO.put("uidl", header.getUidl());
        mailDTO.put("processToSent", false);

        Mail email = null;
        try {
            email = mailHome.create(mailDTO);
            createMailRecipientsByType(email, header.getTo(), WebMailConstants.TO_TYPE_DEFAULT);
            createMailRecipientsByType(email, header.getCc(), WebMailConstants.TO_TYPE_CC);
            createUidlTrack(mailAccount, email);
            EmailSourceUtil.i.createEmailSource(email.getMailId(), email.getCompanyId(), emailSource.toByteArray());
            mailAccount.setLastDownloadTime(header.getSentDate().getTime());

            if (null != mailAccount.getCreateInCommunication() && mailAccount.getCreateInCommunication()) {
                createCommunications(email, accountMessage.getUserMailId(), this.ctx);
            }
        } catch (Exception e) {
            log.error("Cannot save the email in data base, some exception has happening: messageId= " + header.getMessageId()
                    + " uidl= " + header.getUidl() + " mailAccountId= " + mailAccount.getMailAccountId(), e);
            ctx.setRollbackOnly();
            throw new RuntimeException(e);
        }

        return email;
    }

    public void storeEmail(Email parsedEmail,
                           WebmailAccountMessage accountMessage,
                           MailAccount mailAccount, ByteArrayOutputStream emailSource) {

        Mail mail = storeEmail(parsedEmail.getHeader(), accountMessage, mailAccount, emailSource);
        if (mail != null) {
            EmailSourceUtil.i.completeEmail(parsedEmail, mail);
            mail.setIsCompleteEmail(true);
        }
    }

    public void storeEmailWithoutBody(Header header, WebmailAccountMessage accountMessage, MailAccount mailAccount, ByteArrayOutputStream emailSource) {
        Mail mail = storeEmail(header, accountMessage, mailAccount, emailSource);
        if (mail != null) {
            mail.setIsCompleteEmail(true);
        }
    }

    public void deleteEmail(Mail dataBaseEmail) throws RemoveException {


        if (null != dataBaseEmail.getBodyId()) {
            dataBaseEmail.getBody().remove();
        }

        EmailSource emailSource = EmailSourceUtil.i.getEmailSource(dataBaseEmail.getMailId(), dataBaseEmail.getCompanyId());
        if (null != emailSource) {
            emailSource.remove();
        }

        dataBaseEmail.remove();
    }

    private void createUidlTrack(MailAccount mailAccount, Mail email) throws CreateException {
        UidlTrackHome uilTrackHome = (UidlTrackHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_UIDLTRACK);
        UidlTrackDTO uidlTrackDTO = new UidlTrackDTO();
        uidlTrackDTO.put("companyId", mailAccount.getCompanyId());
        uidlTrackDTO.put("mailAccountId", mailAccount.getMailAccountId());
        uidlTrackDTO.put("uidl", email.getUidl());
        uidlTrackDTO.put("deleteFromPopAtTime", calculateDeleteFromPOPInXDaysTime(mailAccount));

        uilTrackHome.create(uidlTrackDTO);
    }

    private Long calculateDeleteFromPOPInXDaysTime(MailAccount mailAccount) {
        Long deleteMillis = null;
        if (mailAccount.getKeepEmailOnServer() != null && mailAccount.getKeepEmailOnServer() && mailAccount.getRemoveEmailOnServerAfterOf() != null) {
            Integer xDays = mailAccount.getRemoveEmailOnServerAfterOf();
            DateTime currentTime = new DateTime();
            DateTime deleteDateTime = currentTime.plusDays(xDays);

            deleteMillis = deleteDateTime.getMillis();

            log.debug("Programing email to delete on POP server in: " + xDays + " days.. " + deleteDateTime);
        }

        return deleteMillis;
    }

    private void createCommunications(Mail mail, Integer userMailId, SessionContext ctx) {
        List recipientMapList = new ArrayList();
        Map recipientMap = new HashMap(5);

        recipientMap.put("addressId", new ArrayList());
        recipientMap.put("contactPersonOf", new ArrayList());
        recipientMap.put("personalName", mail.getMailPersonalFrom());
        recipientMap.put("email", mail.getMailFrom());

        recipientMapList.add(recipientMap);

        CommunicationManagerCmd cmd = new CommunicationManagerCmd();
        cmd.setOp("createInCommunication");
        cmd.putParam("userMailId", userMailId);
        cmd.putParam("recipientMapList", recipientMapList);
        cmd.putParam("email", mail);
        cmd.putParam("inOut", "1");
        cmd.executeInStateless(ctx);
    }

    private void createMailRecipientsByType(Mail email, List<Address> recipients, String type) {
        for (Address address : recipients) {
            createMailRecipient(address, email, type);
        }
    }

    private void createMailRecipient(Address address, Mail email, String type) {
        MailRecipientHome mailRecipientHome =
                (MailRecipientHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_MAILRECIPIENT);
        MailRecipientDTO mailRecipientDTO = new MailRecipientDTO();
        mailRecipientDTO.put("companyId", email.getCompanyId());
        mailRecipientDTO.put("mailId", email.getMailId());
        mailRecipientDTO.put("email", address.getEmail());
        if (null != address.getPersonal() && !"".equals(address.getPersonal())) {
            mailRecipientDTO.put("personalName", address.getPersonal());
        }
        mailRecipientDTO.put("type", Integer.valueOf(type));
        try {
            mailRecipientHome.create(mailRecipientDTO);
        } catch (CreateException e) {
            log.error("-> Create MailRecipient Object FAIL ", e);
        }
    }

    private Integer applyFilters(Header emailHeader, WebmailAccountMessage accountMessage) {
        Integer folderId = accountMessage.getInboxFolderId();

        Collection allFilters = getFilters(accountMessage.getUserMailId(), accountMessage.getCompanyId());
        if (null == allFilters) {
            return folderId;
        }

        String fromAsString = emailHeader.getFrom().getStringRepresentation();
        String toAsString = emailHeader.getTOAsString();
        String ccAsString = emailHeader.getCCAsString();
        String subject = emailHeader.getSubject();

        for (Object object : allFilters) {
            Filter filter = (Filter) object;
            Collection conditions = filter.getConditions();
            if (null != conditions) {
                boolean applyFolder = evaluateConditions(conditions, fromAsString,
                        toAsString,
                        ccAsString,
                        subject);

                log.debug("-> Apply filter=" + filter.getFilterName() + " in '" + subject + "'=" + applyFolder);
                if (applyFolder) {
                    folderId = filter.getFolderId();
                    break;
                }
            }
        }
        return folderId;
    }

    private boolean evaluate(String param, Condition condition) {
        if (null == param) {
            return false;
        }

        param = param.trim();
        boolean result = false;
        if (condition.getConditionKey().equals(Integer.valueOf(WebMailConstants.CONDITION_CONTAIN))) {
            if (param.indexOf(condition.getConditionText()) != -1) {
                result = true;
            }
        }
        if (condition.getConditionKey().equals(Integer.valueOf(WebMailConstants.CONDITION_NOT_CONTAIN))) {
            if (param.indexOf(condition.getConditionText()) == -1) {
                result = true;
            }
        }
        if (condition.getConditionKey().equals(Integer.valueOf(WebMailConstants.CONDITION_BEGIN_WITH))) {
            if (condition.getConditionNameKey().equals(Integer.valueOf(WebMailConstants.FROM_PART)) ||
                    condition.getConditionNameKey().equals(Integer.valueOf(WebMailConstants.TO_o_CC_PART))) {
                if (param.startsWith("\"")) {
                    param = param.substring(1).trim(); //if start whit quotes, quit this to apply the filter
                }
            }
            if (param.startsWith(condition.getConditionText())) {
                result = true;
            }
        }
        if (condition.getConditionKey().equals(Integer.valueOf(WebMailConstants.CONDITION_TERMINATED_IN))) {
            if (param.endsWith(condition.getConditionText())) {
                result = true;
            }
        }

        return result;
    }

    private boolean evaluateConditions(Collection myConditions,
                                       String from,
                                       String to,
                                       String cc,
                                       String subject) {

        boolean result = true;
        for (Object myCondition : myConditions) {
            Condition condition = (Condition) myCondition;

            if (condition.getConditionNameKey().equals(Integer.valueOf(WebMailConstants.FROM_PART))) {
                result = result && evaluate(from, condition);
            }

            if (condition.getConditionNameKey().equals(Integer.valueOf(WebMailConstants.TO_o_CC_PART))) {
                result = result && (evaluate(to, condition) || evaluate(cc, condition));
            }

            if (condition.getConditionNameKey().equals(Integer.valueOf(WebMailConstants.SUBJECT_PART))) {
                result = result && evaluate(subject, condition);
            }
        }
        return result;
    }

    private Collection getFilters(Integer userMailId, Integer companyId) {
        FilterHome filterHome =
                (FilterHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_FILTER);
        try {
            return filterHome.findByUserId(userMailId, companyId);
        } catch (FinderException e) {
            return null;
        }
    }

}
