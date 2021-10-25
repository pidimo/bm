package com.piramide.elwis.cmd.admin;

import com.piramide.elwis.cmd.contactmanager.RecentCmd;
import com.piramide.elwis.cmd.webmailmanager.FolderUtilCmd;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.dto.admin.UserDTO;
import com.piramide.elwis.exception.ServiceUnavailableException;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.EncryptUtil;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * @author Yumi
 * @version $Id: UserCmd.java 10240 2012-06-15 20:24:50Z miguel $
 */

public class UserCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        if ("preferences".equals(paramDTO.get("opPreference"))) {
            paramDTO.remove("opPreference");
            updatePreferences(ctx);
        } else if ("upPassword".equals(paramDTO.get("opPreference"))) {
            paramDTO.remove("opPreference");
            updatePassword();
        } else {
            UserDTO dto = new UserDTO(paramDTO);
            CRUDDirector.i.doCRUD(getOp(), dto, resultDTO);
        }
    }

    private void updatePreferences(SessionContext ctx) {

        if (CRUDDirector.OP_UPDATE.equals(getOp())) {

            UserDTO dto = new UserDTO(paramDTO);
            CRUDDirector.i.doCRUD(getOp(), dto, resultDTO);
            resultDTO.addResultMessage("Common.changesOK");

            UserDTO userDTO = new UserDTO(paramDTO);
            User user = (User) CRUDDirector.i.read(userDTO, resultDTO);

            log.debug("calling RecentCmd.............");
            //update recent max size list limit. If maxRecentList is changed here, then delete the exceeded user recents
            RecentCmd recentCmd = new RecentCmd();
            recentCmd.putParam(paramDTO);
            recentCmd.setOp(CRUDDirector.OP_DELETE);
            recentCmd.executeInStateless(ctx);

            //update webmail systemfolders if user have webmail account
            FolderUtilCmd folderUtilCmd = new FolderUtilCmd();
            folderUtilCmd.setOp("updateSystemFolders");
            folderUtilCmd.putParam("emailUserId", user.getUserId());
            folderUtilCmd.putParam("companyId", user.getCompanyId());
            folderUtilCmd.putParam("uiFolderNames", paramDTO.get("uiFolderNames"));
            folderUtilCmd.executeInStateless(ctx);
        }
    }

    private void updatePassword() {

        if (CRUDDirector.OP_UPDATE.equals(getOp())) {
            log.debug("** UPDATE PASSWORD EXECUTE **");
            try {
                UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
                User user = null;
                try {
                    user = userHome.findByPrimaryKey(new Integer((String) paramDTO.get("userId")));
                    String pass2 = user.getUserPassword();
                    if (user != null) {
                        String pass1 = EncryptUtil.i.encryt((String) paramDTO.get("userPassword"));
                        if (pass1.equals(pass2)) {
                            String pass = EncryptUtil.i.encryt((String) paramDTO.get("password2"));
                            paramDTO.put("userPassword", pass);

                            //update
                            UserDTO dto = new UserDTO(paramDTO);
                            CRUDDirector.i.doCRUD(getOp(), dto, resultDTO);
                            resultDTO.addResultMessage("Common.changesOK");

                            log.info("[" + user.getUserLogin() + "/" + user.getCompany().getLogin() + "] has updated its personal password successfully");
                        } else {
                            paramDTO.put("userPassword", pass2);
                            resultDTO.addResultMessage("User.passInvalid");
                            resultDTO.setForward("fail");
                            return;
                        }
                    }
                } catch (FinderException e) {
                }
            } catch (ServiceUnavailableException e) {
            }
        }
    }

    public boolean isStateful() {
        return false;
    }
}
