package com.piramide.elwis.cmd.webmailmanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.domain.webmailmanager.Folder;
import com.piramide.elwis.domain.webmailmanager.FolderHome;
import com.piramide.elwis.domain.webmailmanager.Mail;
import com.piramide.elwis.domain.webmailmanager.MailHome;
import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * Util to manage mails cmd
 * @author Miguel A. Rojas Cardenas
 * @version 6.1
 */
public class MailUtilCmd extends EJBCommand {
    private Log log = LogFactory.getLog(EmailUserCmd.class);

    public void executeInStateless(SessionContext sessionContext) {
        log.debug("Executing MailUtilCmd..... " + paramDTO);
        String op = getOp();

        if ("moveToFolder".equals(op)) {
            mailMoveToFolder();
        }
    }

    private void mailMoveToFolder() {
        Integer mailId = EJBCommandUtil.i.getValueAsInteger(this, "mailId");
        Integer folderId = EJBCommandUtil.i.getValueAsInteger(this, "folderId");

        Folder folder = findFolder(folderId);
        Mail mail = findMail(mailId);

        if (folder != null && mail != null) {
            mail.setFolderId(folder.getFolderId());
            resultDTO.put("isSuccessMailMove", Boolean.TRUE);
        } else {
            resultDTO.setResultAsFailure();
        }
    }

    private Folder findFolder(Integer folderId) {
        FolderHome folderHome = (FolderHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_FOLDER);
        if (folderId != null) {
            try {
                return folderHome.findByPrimaryKey(folderId);
            } catch (FinderException e) {
                log.debug("-> Read Webmail Folder folderId=" + folderId + " Fail", e);
            }
        }
        return null;
    }

    private Mail findMail(Integer mailId) {
        MailHome mailHome = (MailHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_MAIL);
        if (mailId != null) {
            try {
                return mailHome.findByPrimaryKey(mailId);
            } catch (FinderException e) {
                log.debug("-> Read mail mailId=" + mailId + " Fail", e);
            }
        }
        return null;
    }

    public boolean isStateful() {
        return false;
    }
}
