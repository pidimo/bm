package com.piramide.elwis.cmd.webmailmanager;

import com.piramide.elwis.domain.webmailmanager.Folder;
import com.piramide.elwis.domain.webmailmanager.FolderHome;
import com.piramide.elwis.domain.webmailmanager.UserMail;
import com.piramide.elwis.domain.webmailmanager.UserMailHome;
import com.piramide.elwis.dto.webmailmanager.FolderDTO;
import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.List;
import java.util.Map;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class FolderUtilCmd extends EJBCommand {
    private Log log = LogFactory.getLog(FolderUtilCmd.class);

    public void executeInStateless(SessionContext ctx) {
        if ("updateSystemFolders".equals(getOp())) {
            Integer emailUserId = (Integer) paramDTO.get("emailUserId");
            Integer companyId = (Integer) paramDTO.get("companyId");

            List uiFolderNames = (List) paramDTO.get("uiFolderNames");
            updateSystemFolders(emailUserId, companyId, uiFolderNames);
        }
    }


    private void updateSystemFolders(Integer emailUserId,
                                     Integer companyId,
                                     List uiFolderNames) {
        UserMail emailUser;

        try {
            emailUser = getUserMail(emailUserId);
        } catch (FinderException e) {
            log.debug("-> The user [userId=" + emailUserId + "] does not have an webmail account enabled");
            return;
        }

        for (Object uiFolderObject : uiFolderNames) {
            Map uiFolder = (Map) uiFolderObject;
            String folderConstant = (String) uiFolder.get("type");
            String newName = (String) uiFolder.get("name");

            checkSystemFolder(emailUserId, companyId, folderConstant, newName, emailUser.getUserMailDate());
        }
    }

    private void checkSystemFolder(Integer emailUserId,
                                   Integer companyId,
                                   String folderConstant,
                                   String newName,
                                   Integer date) {
        try {
            Folder systemFolder = getFolderByType(emailUserId, companyId, folderConstant);
            systemFolder.setFolderName(newName);
            log.debug("-> Update Webmail System folder [folderType = " + folderConstant + "] OK");
        } catch (FinderException e) {
            log.debug("-> Inbox folder not registred for User [userId=" + emailUserId + "]");

            FolderDTO dto = new FolderDTO();
            dto.put("companyId", companyId);
            dto.put("folderDate", date);
            dto.put("folderName", newName);
            dto.put("folderType", folderConstant);
            dto.put("userMailId", emailUserId);
            dto.put("columnToShow", WebMailConstants.SYSTEM_FOLDER_COLUMNTOSHOW_MAP.get(folderConstant).getConstant());

            FolderHome folderHome = getFolderHome();
            try {
                folderHome.create(dto);
            } catch (CreateException e1) {
                log.error("-> Create System folder " + newName + " FAIL");
            }
        }
    }

    private Folder getFolderByType(Integer emailUserId, Integer companyId, String type) throws FinderException {
        FolderHome folderHome = getFolderHome();

        return folderHome.findByFolderType(emailUserId,
                Integer.valueOf(type),
                companyId);
    }

    private UserMail getUserMail(Integer emailUserId) throws FinderException {
        UserMailHome userMailHome =
                (UserMailHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_USERMAIL);

        return userMailHome.findByPrimaryKey(emailUserId);
    }

    private FolderHome getFolderHome() {
        return (FolderHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_FOLDER);
    }

    public boolean isStateful() {
        return false;
    }
}
