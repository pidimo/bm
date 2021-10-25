package com.piramide.elwis.cmd.admin;

import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.utils.AdminConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.4.1.6
 */
public class ReloadUserInfoCmd extends LogonCmd {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing ReloadUserInfoCmd..." + paramDTO);

        Integer userId = Integer.valueOf(paramDTO.get("userId").toString());

        User user = null;
        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
        try {
            user = userHome.findByPrimaryKey(userId);
        } catch (FinderException e) {
            log.debug("Not found user..:" + userId);
            resultDTO.setResultAsFailure();
        }

        if (user != null) {
            //read needed attributes
            readUserAndCompanyAttributes(user, resultDTO);
            processSecurityAccessRights(user, resultDTO);
        }
    }

    public boolean isStateful() {
        return false;
    }

}
