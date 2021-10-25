package com.piramide.elwis.cmd.webmailmanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.domain.webmailmanager.*;
import com.piramide.elwis.dto.webmailmanager.MailAccountDTO;
import com.piramide.elwis.dto.webmailmanager.UserMailDTO;
import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class EmailUserCmd extends EJBCommand {
    private Log log = LogFactory.getLog(EmailUserCmd.class);

    public void executeInStateless(SessionContext sessionContext) {
        String op = getOp();
        if ("isValidEmailUser".equals(op)) {
            Integer emailUserId = EJBCommandUtil.i.getValueAsInteger(this, "emailUserId");
            isValidEmailUser(emailUserId);
        }

        if ("hasEmailAccount".equals(op)) {
            Integer emailUserId = (Integer) paramDTO.get("emailUserId");
            hasEmailAccount(emailUserId);
        }

        if ("useHtmlEditor".equals(op)) {
            Integer emailUserId = (Integer) paramDTO.get("emailUserId");
            useHtmlEditor(emailUserId);
        }

        if ("readUserConfiguration".equals(op)) {
            Integer emailUserId = (Integer) paramDTO.get("emailUserId");
            readUserConfiguration(emailUserId);
        }

        if ("readFolderConfiguration".equals(op)) {
            Integer emailUserId = (Integer) paramDTO.get("emailUserId");
            Integer companyId = (Integer) paramDTO.get("companyId");
            getSystemFolders(emailUserId, companyId);
            getCustomFolders(emailUserId, sessionContext);
        }

        if ("hasWebmailModuleConfiguration".equals(getOp())) {
            Integer userId = (Integer) paramDTO.get("userId");
            hasWebmailModuleConfiguration(userId);
        }

        if ("getMailAccounts".equals(getOp())) {
            Integer userMailId = EJBCommandUtil.i.getValueAsInteger(this, "userMailId");
            Integer companyId = EJBCommandUtil.i.getValueAsInteger(this, "companyId");
            getMailAccounts(userMailId, companyId);
        }

        if ("isWebmailUser".equals(getOp())) {
            Integer userId = EJBCommandUtil.i.getValueAsInteger(this, "userId");
            isWebmailUser(userId);
        }
    }

    private void isWebmailUser(Integer userId) {
        try {
            UserMail userMail = getEmailUser(userId);
            if (null != userMail) {
                resultDTO.put("isWebmailUser", true);
            }
        } catch (FinderException e) {
            resultDTO.put("isWebmailUser", false);
        }
    }

    private void getMailAccounts(Integer userMailId, Integer companyId) {
        MailAccountHome mailAccountHome =
                (MailAccountHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_MAILACCOUNT);

        List<MailAccountDTO> result = new ArrayList<MailAccountDTO>();
        try {
            List accounts = (List) mailAccountHome.findAccountsByUserMailAndCompany(userMailId, companyId);
            for (int i = 0; i < accounts.size(); i++) {
                MailAccount mailAccount = (MailAccount) accounts.get(i);
                MailAccountDTO dto = new MailAccountDTO();
                DTOFactory.i.copyToDTO(mailAccount, dto);
                result.add(dto);
            }
        } catch (FinderException e) {
            log.debug("-> Read Email accounts for userMailId=" + userMailId + " FAIL");
        }

        resultDTO.put("getMailAccounts", result);
    }

    private void hasWebmailModuleConfiguration(Integer userId) {
        try {
            UserMail userMail = getEmailUser(userId);

            resultDTO.put(UserMailDTO.KEY_USERMAILID, userMail.getUserMailId());
        } catch (FinderException e) {
            log.debug("-> The user [userId=" + userId + "] does not have an webmail account enabled");
        }
    }

    private Map getSystemFolders(Integer emailUserId, Integer companyId) {
        Map systemFolder = new HashMap();

        UserMail emailUser = null;
        try {
            emailUser = getEmailUser(emailUserId);
        } catch (FinderException e) {
            log.debug("->Read email account for userId=" + emailUserId + " FAIL");
        }

        if (null == emailUser) {
            return systemFolder;
        }

        FolderHome folderHome = (FolderHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_FOLDER);

        try {
            Integer inboxId = folderHome.findByFolderType(emailUserId,
                    Integer.valueOf(WebMailConstants.FOLDER_INBOX), companyId).getFolderId();
            Integer sentId = folderHome.findByFolderType(emailUserId,
                    Integer.valueOf(WebMailConstants.FOLDER_SENDITEMS), companyId).getFolderId();
            Integer trashId = folderHome.findByFolderType(emailUserId,
                    Integer.valueOf(WebMailConstants.FOLDER_TRASH), companyId).getFolderId();
            Integer draftId = folderHome.findByFolderType(emailUserId,
                    Integer.valueOf(WebMailConstants.FOLDER_DRAFTITEMS), companyId).getFolderId();
            Integer outBoxId = folderHome.findByFolderType(emailUserId,
                    Integer.valueOf(WebMailConstants.FOLDER_OUTBOX), companyId).getFolderId();

            systemFolder.put("inboxSize", countEmailsByFolder(inboxId, companyId));
            systemFolder.put("sentSize", countEmailsByFolder(sentId, companyId));
            systemFolder.put("trashSize", countEmailsByFolder(trashId, companyId));
            systemFolder.put("draftSize", countEmailsByFolder(draftId, companyId));
            systemFolder.put("outboxSize", countEmailsByFolder(outBoxId, companyId));

            systemFolder.put("unreadSize", getUnreadMessagesCounter(inboxId, companyId));
            systemFolder.put("trashUnreadSize", getUnreadMessagesCounter(trashId, companyId));

            systemFolder.put("inboxId", inboxId);
            systemFolder.put("sentId", sentId);
            systemFolder.put("trashId", trashId);
            systemFolder.put("draftId", draftId);
            systemFolder.put("outboxId", outBoxId);

        } catch (FinderException e) {
            log.error("Cannot find system folders inbox, sent, trash, draft");
        }

        resultDTO.put("systemFolderCounter", systemFolder);
        return systemFolder;
    }


    private Integer countEmailsByFolder(Integer folderId, Integer companyId) {
        MailHome mailHome =
                (MailHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_MAIL);
        try {
            return mailHome.selectCountMessages(folderId, companyId);
        } catch (FinderException e) {
            log.debug("-> Exctute selectCountMessages(" + folderId + ", " + companyId + ") FAIL", e);
        }

        return 0;
    }

    private List getCustomFolders(Integer emailUserId, SessionContext ctx) {

        FolderCmd folderCmd = new FolderCmd();
        folderCmd.putParam("op", "getCustomFolders");
        folderCmd.putParam("userMailId", emailUserId);

        folderCmd.executeInStateless(ctx);
        ResultDTO resultDTOFolder = folderCmd.getResultDTO();
        List customFoldersList = (List) resultDTOFolder.get("customFoldersList");
        resultDTO.put("customFoldersList", customFoldersList);
        return customFoldersList;
    }

    private int getUnreadMessagesCounter(Integer folderId, Integer companyId) {
        MailHome mailHome = (MailHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_MAIL);

        try {
            return mailHome.selectCountUnReadMessages(folderId, companyId);
        } catch (FinderException e) {
            log.debug("Cannot count unread messages...");
        }
        return 0;
    }

    private boolean useHtmlEditor(Integer emailUserId) {
        boolean result = false;
        try {
            UserMail emailUser = getEmailUser(emailUserId);
            result = Integer.valueOf(WebMailConstants.HTML_EDITMODE).equals(emailUser.getEditMode());
        } catch (FinderException e) {
            log.debug("-> Read email account for userId=" + emailUserId + " FAIL");
        }
        resultDTO.put("useHtmlEditor", result);
        return result;
    }

    private void readUserConfiguration(Integer emailUserId) {
        try {
            UserMail user = getEmailUser(emailUserId);
            resultDTO.put("saveSendItem", user.getSaveSendItem());
            resultDTO.put("replyMode", user.getReplyMode());
            resultDTO.put("useHtmlEditor", Integer.valueOf(WebMailConstants.HTML_EDITMODE).equals(user.getEditMode()));
            resultDTO.put("emptyTrashLogout", user.getEmptyTrashLogout());
            resultDTO.put("backgroundDownload", user.getBackgroundDownload());
        } catch (FinderException e) {
            log.debug("->Read webmail user userId=" + emailUserId + " FAIL");
        }
    }


    /**
     * This method checks if the emailUserId passed as parameter belongs to a valid webmail user.
     * After puts in ResultDTO object the next Keys:
     * <p/>
     * A) isValidEmailUser  : true if the emailUserId belongs to a valid webmail user, false in another case.
     * B) hasEmailAccounts  : true if the webmail user has any account configurated.
     * C) userMailId        : this key is setting in ResultDTO only if the isValidEmailUser is true, in other case the key
     * not exists in te ResultDTO object.
     *
     * @param emailUserId Webmail user identifier
     */
    private void isValidEmailUser(Integer emailUserId) {
        UserMail user = null;
        boolean hasEmailAccounts = false;
        try {
            user = getEmailUser(emailUserId);
            hasEmailAccounts = null != user.getMailAccounts() && !user.getMailAccounts().isEmpty();
        } catch (FinderException e) {
            //
        }
        boolean isValidEmailUser = null != user;
        if (isValidEmailUser) {
            resultDTO.put("userMailId", user.getUserMailId());
        }

        resultDTO.put("isValidEmailUser", isValidEmailUser);
        resultDTO.put("hasEmailAccounts", hasEmailAccounts);

    }

    private boolean hasEmailAccount(Integer emailUserId) {
        boolean result = false;

        try {
            UserMail emailUser = getEmailUser(emailUserId);
            if (null != emailUser.getMailAccounts() &&
                    !emailUser.getMailAccounts().isEmpty()) {
                result = true;
            }

            resultDTO.put("userMailId", emailUser.getUserMailId());

        } catch (FinderException e) {
            log.debug("-> Read email account for userId=" + emailUserId + " FAIL");
        }

        resultDTO.put("hasEmailAccount", result);
        return result;
    }

    private UserMail getEmailUser(Integer emailUserId) throws FinderException {
        UserMailHome userMailHome =
                (UserMailHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_USERMAIL);
        return userMailHome.findByPrimaryKey(emailUserId);
    }

    public boolean isStateful() {
        return false;
    }
}
