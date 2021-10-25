package com.piramide.elwis.cmd.webmailmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.webmailmanager.FolderHome;
import com.piramide.elwis.domain.webmailmanager.UserMail;
import com.piramide.elwis.domain.webmailmanager.UserMailHome;
import com.piramide.elwis.dto.webmailmanager.FolderDTO;
import com.piramide.elwis.dto.webmailmanager.UserMailDTO;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Date;

/**
 * AlfaCentauro Team
 *
 * @author Ivan
 * @version $Id: WebmailUserCmd.java 10480 2014-08-14 18:52:29Z miguel $
 */
public class WebmailUserCmd extends EJBCommand {
    public static final String DEFAULT_VALUE = "DEFAULT";

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        String op = getOp();
        Integer elwisUserId = new Integer(paramDTO.get("userId").toString());
        Integer companyId = new Integer(paramDTO.get("companyId").toString());
        Date actualDate = new Date();
        if ("create".equals(op)) {
            create(companyId, actualDate, elwisUserId, ctx);
        } else {
            read(elwisUserId);
        }
    }

    /**
     * This method looks for the webmail user associated with the elwis user
     *
     * @param elwisUserId elwin user identifier
     */
    private void read(Integer elwisUserId) {
        UserMailHome userHome =
                (UserMailHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_USERMAIL);
        try {
            UserMail user = userHome.findByPrimaryKey(elwisUserId);
            resultDTO.put("userMailId", user.getUserMailId());
        } catch (FinderException e) {
            log.debug("Cannot find Web Mail user for elwis user identifier " + elwisUserId);
            resultDTO.setForward("Create");
        }
    }

    /**
     * This method creates a new webmail user and all its supplements
     * (folders system, accounts connection)
     *
     * @param companyId   elwis user company identifier
     * @param actualDate  date of creation webmail user
     * @param elwisUserId elwis user identifier
     * @param ctx         session context for execute another commands.
     */
    private void create(Integer companyId,
                        Date actualDate,
                        Integer elwisUserId,
                        SessionContext ctx) {

        UserMailDTO dto = new UserMailDTO();
        dto.put("companyId", companyId);
        dto.put("userMailDate", DateUtils.dateToInteger(actualDate));
        dto.put("userMailId", elwisUserId);
        dto.put("replyMode", false);
        dto.put("saveSendItem", false);
        dto.put("editMode", Integer.valueOf(WebMailConstants.TEXT_EDITMODE));
        dto.put("emptyTrashLogout", false);
        dto.put("showPopNotification", false);
        dto.put("backgroundDownload", false);

        //create new Web Mail user
        UserMail userMail = (UserMail) ExtendedCRUDDirector.i.create(dto, resultDTO, false);
        createDefaultFolders(userMail);

        //update webmail systemfolders if user have webmail account
        FolderUtilCmd folderUtilCmd = new FolderUtilCmd();
        folderUtilCmd.setOp("updateSystemFolders");
        folderUtilCmd.putParam("emailUserId", userMail.getUserMailId());
        folderUtilCmd.putParam("companyId", userMail.getCompanyId());
        folderUtilCmd.putParam("uiFolderNames", paramDTO.get("uiFolderNames"));
        folderUtilCmd.executeInStateless(ctx);

        //create default mail account
        MailAccountCmd accountCmd = new MailAccountCmd();
        accountCmd.putParam(paramDTO);
        accountCmd.setOp("create");
        accountCmd.putParam("email", paramDTO.get("email"));
        accountCmd.putParam("login", paramDTO.get("login"));
        accountCmd.putParam("password", paramDTO.get("password"));
        accountCmd.putParam("serverName", paramDTO.get("serverName"));
        accountCmd.putParam("serverPort", paramDTO.get("serverPort"));
        accountCmd.putParam("useSSLConnection", paramDTO.get("useSSLConnection"));
        accountCmd.putParam("defaultAccount", paramDTO.get("defaultAccount"));
        accountCmd.putParam("companyId", userMail.getCompanyId());
        accountCmd.putParam("userMailId", userMail.getUserMailId());
        accountCmd.putParam("smtpServer", paramDTO.get("smtpServer"));
        accountCmd.putParam("smtpPort", paramDTO.get("smtpPort"));
        accountCmd.putParam("smtpAuthentication", paramDTO.get("smtpAuthentication"));
        accountCmd.putParam("smtpSSL", paramDTO.get("smtpSSL"));
        accountCmd.executeInStateless(ctx);

        resultDTO.addResultMessage("Common.changesOK");
    }


    public boolean isStateful() {
        return false;
    }


    private void createDefaultFolders(UserMail userMail) {
        FolderHome folderHome = (FolderHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_FOLDER);
        for (int i = 0; i < WebMailConstants.SYSTEM_FOLDER_KEYS.length; i++) {
            FolderDTO folderDTO = new FolderDTO();
            folderDTO.put("companyId", userMail.getCompanyId());
            folderDTO.put("folderDate", userMail.getUserMailDate());
            folderDTO.put("folderName", WebMailConstants.SYSTEM_FOLDER_NAME);
            folderDTO.put("folderType", WebMailConstants.SYSTEM_FOLDER_KEYS[i]);
            folderDTO.put("userMailId", userMail.getUserMailId());
            folderDTO.put("columnToShow", WebMailConstants.SYSTEM_FOLDER_COLUMNTOSHOW_MAP.get(WebMailConstants.SYSTEM_FOLDER_KEYS[i]).getConstant());
            folderDTO.put("isOpen", false);
            try {
                folderHome.create(folderDTO);
            } catch (CreateException e) {
                log.error("Cannot Create folder for this user..." + userMail.getUserMailId());
            }
        }
    }
}
