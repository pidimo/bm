package com.piramide.elwis.cmd.admin;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.dto.admin.UserDTO;
import com.piramide.elwis.exception.ServiceUnavailableException;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.EncryptUtil;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.5.3
 */
public class UserPasswordChangeUpdateCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing UserPasswordChangeUpdateCmd................" + paramDTO);
        Integer userId = new Integer(paramDTO.get("userId").toString());
        Integer passwordChangeId = new Integer(paramDTO.get("passwordChangeId").toString());

        String prevPassword = null;
        String newPassword = null;
        try {
            prevPassword = EncryptUtil.i.encryt((String) paramDTO.get("previousPassword"));
            newPassword = EncryptUtil.i.encryt((String) paramDTO.get("password2"));
        } catch (ServiceUnavailableException e) {
            resultDTO.addResultMessage("Common.password.encryptError");
            resultDTO.setForward("Fail");
            return;
        }

        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
        User user = null;
        try {
            user = userHome.findByPrimaryKey(userId);
        } catch (FinderException e) {
            resultDTO.addResultMessage("User.notFound");
            resultDTO.setForward("Fail");
            return;
        }

        if (user != null) {
            if (isValidPasswords(prevPassword, newPassword, user)) {
                //update the new password
                UserDTO userDTO = new UserDTO(paramDTO);
                userDTO.put("userPassword", newPassword);

                User updatedUser = (User) ExtendedCRUDDirector.i.update(userDTO, resultDTO, false, false, false, "Fail");
                if (updatedUser != null) {
                    //remove el user password change
                    PasswordChangeCmdUtil passwordChangeCmdUtil = new PasswordChangeCmdUtil(ctx);
                    passwordChangeCmdUtil.deleteUserPasswordChange(userId, passwordChangeId);

                    resultDTO.addResultMessage("User.passwordUpdate.OK");
                }
            } else {
                resultDTO.setResultAsFailure();

                resultDTO.put("userId", userId);
                resultDTO.put("passwordChangeId", passwordChangeId);
            }
        }
    }

    private boolean isValidPasswords(String previousPassword, String newPassword, User user) {
        boolean isValid = true;
        //password validation
        if (user.getUserPassword().equals(previousPassword)) {
            if (user.getUserPassword().equals(newPassword)) {
                isValid = false;
                resultDTO.addResultMessage("User.newPasswordInvalid.bothAreEqual");
            }
        } else {
            isValid = false;
            resultDTO.addResultMessage("User.passInvalid");
        }
        return isValid;
    }

    public boolean isStateful() {
        return false;
    }
}
