package com.piramide.elwis.cmd.webmailmanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.common.strutsejb.BeanTransactionEJBCommand;
import com.piramide.elwis.domain.contactmanager.Contact;
import com.piramide.elwis.domain.contactmanager.ContactHome;
import com.piramide.elwis.domain.supportmanager.SupportContact;
import com.piramide.elwis.domain.supportmanager.SupportContactHome;
import com.piramide.elwis.domain.webmailmanager.Folder;
import com.piramide.elwis.domain.webmailmanager.Mail;
import com.piramide.elwis.domain.webmailmanager.MailContact;
import com.piramide.elwis.domain.webmailmanager.MailHome;
import com.piramide.elwis.dto.webmailmanager.FolderDTO;
import com.piramide.elwis.dto.webmailmanager.MailContactDTO;
import com.piramide.elwis.dto.webmailmanager.MailDTO;
import com.piramide.elwis.service.webmail.EmailService;
import com.piramide.elwis.service.webmail.EmailServiceHome;
import com.piramide.elwis.service.webmail.SaveEmailService;
import com.piramide.elwis.service.webmail.SaveEmailServiceHome;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.SupportConstants;
import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.utils.webmail.MailStateUtil;
import com.piramide.elwis.utils.webmail.jms.DeleteEmailMessage;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * AlfaCentauro Team
 * This class perform operation for the view of the Sent Tray
 *
 * @author Alvaro
 * @version $Id: MailTrayCmd.java 12691 2017-06-09 21:36:10Z miguel $
 */
public class MailTrayCmd extends BeanTransactionEJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    @Override
    public int getTransactionTimeout() {
        return 10800;//three hours as limit
    }

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing MailTrayCmd.......");
        String op = null;
        try {
            op = paramDTO.get("op").toString();
            resultDTO.put("op", op);
        } catch (NullPointerException n) {
            op = "read";
        }
        String companyId = paramDTO.get("companyId").toString();

        String userMailId = paramDTO.get("userMailId").toString();
        Object mailFilter = paramDTO.get("mailFilter");
        Object folderId = paramDTO.get("folderId");

        if (folderId == null || "".equals(folderId.toString().trim())) {
            Folder f = (Folder) EJBFactory.i.callFinder(new FolderDTO(),
                    "findByFolderType", new Object[]{Integer.valueOf(userMailId),
                    Integer.valueOf(WebMailConstants.FOLDER_INBOX),
                    Integer.valueOf(companyId)});
            folderId = f.getFolderId();

        }


        if (mailFilter != null && (mailFilter != null && !mailFilter.toString().equals(WebMailConstants.MAIL_FILTERS[0]))) {
            resultDTO.put("mailFilter", mailFilter);
        }
        if (op.equals("moveTo")) {
            moveTo(Arrays.asList((Object[]) paramDTO.get("mailIds")), paramDTO.get("op_parameter").toString());
        } else if (op.equals("markAs")) {
            markAs(Arrays.asList((Object[]) paramDTO.get("mailIds")), paramDTO.get("op_parameter").toString());
        } else if (op.equals("deleteMails")) {
            deleteMails(Arrays.asList((Object[]) paramDTO.get("mailIds")), ctx);
        } else if (op.equals("emptyFolder")) {
            emptyFolder(paramDTO.get("toEmptyFolder"), new Boolean(true), ctx);
        } else if (op.equals("emptyTrashOnExit")) {
            Folder f = (Folder) EJBFactory.i.callFinder(new FolderDTO(),
                    "findByFolderType", new Object[]{Integer.valueOf(userMailId),
                    Integer.valueOf(WebMailConstants.FOLDER_TRASH),
                    Integer.valueOf(companyId)});
            emptyFolder(f.getFolderId(), true, ctx);
        } else if (op.equals("moveToTrash")) {
            moveTo(Arrays.asList((Object[]) paramDTO.get("mailIds")), getTrashId(userMailId, companyId).toString());
        } else if (op.equals("emptyFolderToTrash")) {
            readFolderDetails(userMailId, folderId, companyId);
        } else if (op.equals("readMailsToDelete")) {
            readMailsToDelete(Arrays.asList((Object[]) paramDTO.get("mailIds")), userMailId, companyId, ctx);
        } else if (op.equals("readFolderToEmpty")) {
            readToEmptyFolder(paramDTO.get("toEmptyFolder"), companyId);
        } else if ("moveAllEmailsToTrash".equals(op)) {
            moveTo(getFolderMailIds(folderId, companyId), getTrashId(userMailId, companyId).toString());
        } else if (op.equals("emptyFolderComplete")) {
            emptyFolderComplete((Integer) paramDTO.get("toEmptyFolderId"),
                    ctx);
        }

        if (op.equals("readUserFolders")) {
            readConstants(userMailId, companyId);
        } else if (!op.equals("readMailsToDelete") && !op.equals("readFolderToEmpty") && !op.equals("emptyTrashOnExit")) {
            //operations for mailtray
            readConstants(userMailId, companyId);
            readFolderDetails(userMailId, folderId, companyId);
        }
    }

    /*private void setMailsIds(List dtos) {
        resultDTO.put("mailDTOs", dtos);
    }*/

    /**
     * Read the constants necesary for the view of the tray of mails
     *
     * @param userMailId
     * @param companyId
     */
    public void readConstants(String userMailId, String companyId) {
        //Read the Constants for the moveTo
        Object params[] = {new Integer(userMailId), new Integer(companyId)};
        Collection userFolders = (Collection) EJBFactory.i.callFinder(new FolderDTO(), "findByUserMailKey", params);

        ArrayList folderConstants = new ArrayList();//User folders
        Iterator i = userFolders.iterator();
        while (i.hasNext()) {
            Folder folder = (Folder) (i.next());
            FolderDTO folderDTO = new FolderDTO();
            folderDTO.put("mailFolderId", folder.getFolderId());

            if (folder.getFolderType().toString().equals(WebMailConstants.FOLDER_DEFAULT)) {
                folderDTO.put("mailFolderName", folder.getFolderName());
                folderConstants.add(folderDTO);
            }
        }
        resultDTO.put("folderConstants", folderConstants);
    }

    /**
     * Move the collection of mails to the selected folder
     *
     * @param mailIds
     * @param folderId
     */
    public void moveTo(Collection mailIds, String folderId) {
        if (null != folderId && !"".equals(folderId.trim())) {
            Iterator i = mailIds.iterator();
            while (i.hasNext()) {
                MailDTO mailDTO = new MailDTO();
                mailDTO.put("mailId", new Integer(i.next().toString()));
                Mail mail = (Mail) ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_READ, mailDTO, new ResultDTO());
                if (null != mail) {
                    mail.setFolderId(new Integer(folderId));
                }
            }
        }
    }

    /**
     * Mark the collection of mails as the selected state
     *
     * @param mailIds
     * @param markAsConstant
     */
    public void markAs(Collection mailIds, String markAsConstant) {
        Iterator i = mailIds.iterator();
        while (i.hasNext()) {
            MailDTO mailDTO = new MailDTO();
            mailDTO.put("mailId", new Integer(i.next().toString()));
            Mail mail = (Mail) ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_READ, mailDTO, new ResultDTO());
            mail.setMailState(MailStateUtil.markAs(mail.getMailState(), markAsConstant));
        }
    }

    /**
     * Deletes a lis of mails of one folder
     *
     * @param mailIds
     */
    public void deleteMails(Collection mailIds, SessionContext ctx) {

        List mails = new ArrayList();
        for (Object object : mailIds) {
            Integer mailId = new Integer(object.toString());

            Mail mail = getMail(mailId);
            if (null != mail) {
                mails.add(mail);
            }
        }

        Map<String, List<String>> incompleteEmails = filterIncompleteEmails(mails);

        for (int i = 0; i < mails.size(); i++) {
            Mail mail = (Mail) mails.get(i);
            if (!mail.getHidden()) {
                deleteMail(mail, true, ctx);
            }
        }
        Integer userMailId = EJBCommandUtil.i.getValueAsInteger(this, "userId");
        if (null != userMailId) {
            deleteEmailMessagesFromPOPServer(incompleteEmails, userMailId);
        }

    }

    private void readEmailsToDelete(List emailIds) {
        resultDTO.put("folderSize", emailIds.size());
        resultDTO.put("emailIdentifiers", emailIds);
    }

    /**
     * Read folder detail neccesary for the view of the tray of mails
     *
     * @param userMailId
     * @param folderId
     * @param companyId
     */
    public void readFolderDetails(String userMailId, Object folderId, String companyId) {

        List emailIdentifiers = (List) paramDTO.get("emailIdentifiers");

        if (null != emailIdentifiers && !emailIdentifiers.isEmpty()) {
            readEmailsToDelete(emailIdentifiers);
            return;
        }
        Object folderName = new Object();
        Object folderType = new Object();
        Integer folderSize = new Integer("0");
        if (folderId == null) {
            Object params[] = {new Integer(userMailId), new Integer(WebMailConstants.FOLDER_INBOX), new Integer(companyId)};
            Folder folder = (Folder) EJBFactory.i.callFinder(new FolderDTO(), "findByFolderType", params);
            folderName = "SYSTEMFOLDER";
            folderType = folder.getFolderType();
            folderId = folder.getFolderId();
        } else {
            FolderDTO folderDTO = new FolderDTO();
            folderDTO.put("folderId", new Integer(folderId.toString()));
            Folder folder = (Folder) ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_READ, folderDTO, new ResultDTO());
            folderType = folder.getFolderType();
            folderSize = countEmailsByFolder(folder.getFolderId(), folder.getCompanyId());

            if (folderType.toString().equals(WebMailConstants.FOLDER_DEFAULT)) {
                folderName = folder.getFolderName();
            } else {
                folderName = folder.getFolderName();
            }
        }
        resultDTO.put("folderId", folderId);
        resultDTO.put("folderName", folderName);
        resultDTO.put("folderType", folderType);
        resultDTO.put("folderSize", folderSize);
    }

    private Integer countEmailsByFolder(Integer folderId, Integer companyId) {
        MailHome mailHome = (MailHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_MAIL);
        try {
            return mailHome.selectCountMessages(folderId, companyId);
        } catch (FinderException e) {
            log.debug("-> Exctute mail tray selectCountMessages(" + folderId + ", " + companyId + ") FAIL", e);
        }

        return 0;
    }

    /**
     * Delete the mails of one folder
     *
     * @param folderId
     */
    public void emptyFolder(Object folderId, Boolean deleteMailCommunication, SessionContext ctx) {

        Integer id = null;
        try {
            id = Integer.valueOf(folderId.toString());
        } catch (NumberFormatException e) {

        }


        if (id != null) {

            FolderDTO folderDTO = new FolderDTO();
            folderDTO.put("folderId", new Integer(folderId.toString()));
            Folder folder = (Folder) ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_READ, folderDTO, new ResultDTO());
            List mails = new ArrayList(folder.getMails());//Because the cascade-delete dontt works

            Map<String, List<String>> incompleteEmails = filterIncompleteEmails(mails);

            for (int i = 0; i < mails.size(); i++) {
                Mail mail = (Mail) mails.get(i);
                if (!mail.getHidden()) {
                    deleteMail(mail, deleteMailCommunication, ctx);
                }
            }

            deleteEmailMessagesFromPOPServer(incompleteEmails, folder.getUserMailId());
        }
    }

    private void deleteEmailMessagesFromPOPServer(Map<String, List<String>> incompleteEmails, Integer userMailId) {
        EmailServiceHome emailServiceHome =
                (EmailServiceHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_EMAIL_SERVICE);

        DeleteEmailMessage deleteEmailMessage = new DeleteEmailMessage(userMailId, incompleteEmails);

        try {
            EmailService emailService = emailServiceHome.create();
            emailService.deleteEmailsFromPOPServer(deleteEmailMessage);
        } catch (CreateException e) {
            log.error("-> Cannot Create EmailService Object ", e);
        }
    }

    public Map<String, List<String>> filterIncompleteEmails(List<Mail> emails) {
        Map<String, List<String>> result = new HashMap<String, List<String>>();

        for (int i = 0; i < emails.size(); i++) {
            Mail mail = emails.get(i);
            if (null == mail.getIsCompleteEmail() || mail.getIsCompleteEmail()) {
                continue;
            }

            List<String> messageIds = result.get(mail.getMailAccount());
            if (null == messageIds) {
                messageIds = new ArrayList<String>();
                if (null != mail.getUidl()) {
                    messageIds.add(mail.getUidl());
                } else {
                    messageIds.add(mail.getMessageId());
                }

                result.put(mail.getMailAccount(), messageIds);
            } else {
                if (null != mail.getUidl()) {
                    messageIds.add(mail.getUidl());
                } else {
                    messageIds.add(mail.getMessageId());
                }

                result.put(mail.getMailAccount(), messageIds);
            }
        }

        return result;
    }

    /**
     * Get the trash folder Id of the user
     *
     * @param userMailId
     * @param companyId
     * @return The trashId
     */
    private Integer getTrashId(String userMailId, String companyId) {
        Object params[] = {new Integer(userMailId), new Integer(WebMailConstants.FOLDER_TRASH), new Integer(companyId)};
        Integer res = ((Folder) EJBFactory.i.callFinder(new FolderDTO(), "findByFolderType", params)).getFolderId();
        return (res);
    }

    /**
     * Get the mailId of the mails in one folder
     *
     * @param folderId
     * @param companyId
     * @return Colection of mailIds(as Integers)
     */
    private Collection getFolderMailIds(Object folderId, String companyId) {
        ArrayList res = new ArrayList();
        if (folderId != null) {
            Object params[] = {new Integer(folderId.toString()), new Integer(companyId)};
            ArrayList mails = new ArrayList((Collection) EJBFactory.i.callFinder(new MailDTO(), "findByFolderId", params));
            for (int i = 0; i < mails.size(); i++) {
                res.add(((Mail) mails.get(i)).getMailId());
            }
        }
        return (res);
    }

    /**
     * Read the data of the list of mails to delete
     *
     * @param mailIds
     * @param userMailId
     * @param companyId
     * @param ctx
     */
    private void readMailsToDelete(Collection mailIds, String userMailId, String companyId,
                                   SessionContext ctx) {
        ArrayList mailsToDelete = new ArrayList();
        if (mailIds != null) {
            Iterator i = mailIds.iterator();
            while (i.hasNext()) {
                ReadMailCmd readMailCmd = new ReadMailCmd();
                DTO dto = new DTO();
                dto.put("mailId", i.next());
                dto.put("companyId", companyId);
                dto.put("userMailId", userMailId);
                dto.put("op", "readMailForDelete");
                dto.put("findByFolderType", new Boolean(false));
                readMailCmd.putParam(dto);
                readMailCmd.executeInStateless(ctx);
                ResultDTO resDTO = readMailCmd.getResultDTO();

                MailDTO mailDTO = new MailDTO();
                mailDTO.put("mailId", resDTO.get("mailId"));
                mailDTO.put("mailFrom", resDTO.get("mailFrom"));
                mailDTO.put("mailDate", resDTO.get("sentDate"));
                mailDTO.put("mailHour", resDTO.get("mailHour"));
                Collection attachs = (Collection) resDTO.get("attachs");
                mailDTO.put("hasAttachs", (attachs != null && attachs.size() > 0) ? "true" : "false");
                mailDTO.put("subject", resDTO.get("subject"));
                mailDTO.put("mailTos", resDTO.get("mailTos"));
                mailDTO.put("mailTosCC", resDTO.get("mailTosCC"));
                mailDTO.put("haveMailContacts", resDTO.get("haveMailContacts"));
                mailDTO.put("isAction", resDTO.get("isAction"));
                mailDTO.put("isSupportActivity", resDTO.get("isSupportActivity"));
                mailsToDelete.add(mailDTO);
            }
            resultDTO.put("readMails", mailsToDelete);
        }
    }

    /**
     * Reads the number of mails in the folder to delete
     *
     * @param toEmptyFolder
     * @param companyId
     */
    private void readToEmptyFolder(Object toEmptyFolder, String companyId) {
        if (toEmptyFolder != null) {
            ContactHome contactHome = (ContactHome)
                    EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CONTACT);
            SupportContactHome supportContactHome =
                    (SupportContactHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_SUPPORT_CONTACT);

            Object params[] = {new Integer(toEmptyFolder.toString()), new Integer(companyId)};

            Collection emails = (Collection) EJBFactory.i.callFinder(new MailDTO(), "findByFolderId", params);
            int communicationCounter = 0;
            int actionCounter = 0;
            int supportCounter = 0;
            for (Object object : emails) {
                Mail email = (Mail) object;
                Collection associatedCommunications = (Collection) EJBFactory.i.callFinder(new MailContactDTO(),
                        "findByMailId",
                        new Object[]{email.getMailId(), email.getCompanyId()});
                if (null != associatedCommunications &&
                        associatedCommunications.size() > 0) {
                    for (Object element : associatedCommunications) {
                        MailContact emailContact = (MailContact) element;

                        try {
                            SupportContact supportContact =
                                    supportContactHome.findByContactId(emailContact.getContactId(),
                                            emailContact.getCompanyId());
                            if (null != supportContact) {
                                supportCounter++;
                                break;
                            }
                        } catch (FinderException e) {
                            log.debug("-> Read SupportContact contactId=" + emailContact.getContactId() + " FAIL");
                        }

                        try {
                            Contact contact = contactHome.findByPrimaryKey(emailContact.getContactId());
                            if (contact.getIsAction()) {
                                actionCounter++;
                                break;
                            } else {
                                communicationCounter++;
                                break;
                            }
                        } catch (FinderException e) {
                            log.debug("-> Read Contact contactId" + emailContact.getContactId() + " FAIL");
                        }
                    }
                }
            }
            resultDTO.put("toEmptyFolder", toEmptyFolder);
            resultDTO.put("folderMails", emails.size() + "");
            resultDTO.put("folderMailContacts", String.valueOf(communicationCounter) + "");
            resultDTO.put("folderMailActions", String.valueOf(actionCounter));
            resultDTO.put("folderMailActivities", String.valueOf(supportCounter));
        }
    }

    /**
     * Delete a mail
     *
     * @param mail
     * @param ctx
     */
    private void deleteMail(Mail mail, Boolean deleteMailCommunication, SessionContext ctx) {
        deleteMailCommunication = (deleteMailCommunication != null) ? deleteMailCommunication : new Boolean(true);

        ReadMailCmd readMailCmd = new ReadMailCmd();
        readMailCmd.putParam("op", "haveMailContacts");
        readMailCmd.putParam("findByFolderType", new Boolean(false));
        readMailCmd.putParam("mailId", mail.getMailId());
        readMailCmd.executeInStateless(ctx);
        ResultDTO readResultDto = readMailCmd.getResultDTO();
        Boolean isAction = (Boolean) readResultDto.get("isAction");
        Boolean haveMailContacts = (Boolean) readResultDto.get("haveMailContacts");
        Boolean isActivity = (Boolean) readResultDto.get("isSupportActivity");
        if ((deleteMailCommunication.booleanValue())) {

            //check if mail have communications asociated
            CommunicationManagerCmd commManagerCmd = new CommunicationManagerCmd();
            commManagerCmd.putParam("op", "getMailContact");
            commManagerCmd.putParam("mailId", mail.getMailId());
            commManagerCmd.putParam("companyId", mail.getCompanyId());
            commManagerCmd.executeInStateless(ctx);

            List emailCommunications = (List) commManagerCmd.getResultDTO().get("mailContacts");
            boolean hiddeEmail = (null != emailCommunications && !emailCommunications.isEmpty()) ||
                    isActivity ||
                    isAction ||
                    haveMailContacts;
            Integer mailId = mail.getMailId();
            if (hiddeEmail) {
                mail.setHidden(true);
                log.debug("-> Now hidde email mailId=" + mailId + " OK");
            } else {

                SaveEmailService saveEmailService = getSaveEmailService();
                try {
                    saveEmailService.deleteEmail(mail);
                } catch (RemoveException e) {
                    log.error("Delete Email from DataBase Fail", e);
                }
                log.debug("-> Now delete email mailId=" + mailId + " OK");
            }
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

    private void emptyFolderComplete(Integer folderId, SessionContext ctx) {
        log.debug("Empty folder and move hidden mails.....");
        //readFolder
        FolderDTO folderDTO = new FolderDTO();
        folderDTO.put("folderId", folderId);
        Folder folder = (Folder) ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_READ, folderDTO, new ResultDTO());

        //Clean folder (remain hidden mails)
        emptyFolder(folderId, Boolean.TRUE, ctx);
        Folder trashFolder = (Folder) EJBFactory.i.callFinder(new FolderDTO(),
                "findByFolderType", new Object[]{Integer.valueOf(folder.getUserMailId()),
                Integer.valueOf(WebMailConstants.FOLDER_TRASH),
                Integer.valueOf(folder.getCompanyId())});

        //move remain mails in folder to inbox
        ArrayList mails = new ArrayList(folder.getMails());
        for (int i = 0; i < mails.size(); i++) {
            Mail mail = (Mail) mails.get(i);
            mail.setFolder(trashFolder);
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
}
