package com.piramide.elwis.cmd.admin;

import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.utils.AdminConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * Cmd util to manage WVApp members
 *
 * @author Miguel A. Rojas Cardenas
 * @version 6.0.2
 */
public class UserMemberCmd extends EJBCommand {
    private static Log log = LogFactory.getLog(ReadUserCmd.class);

    public void executeInStateless(SessionContext sessionContext) {
        log.debug("Executing UserMemberCmd..." + paramDTO);

        if ("makeAsFavorite".equals(getOp())) {
            defineUserAsFavoriteMember();
        }
    }

    private void defineUserAsFavoriteMember() {
        Integer companyId = new Integer(paramDTO.get("companyId").toString());
        Integer addressId = new Integer(paramDTO.get("addressId").toString());
        Boolean isFavoriteWVApp = (Boolean) paramDTO.get("isFavoriteWVApp");

        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);

        try {
            User user = userHome.findByAddressId(companyId, addressId);
            if (user != null) {
                user.setIsFavoriteWVApp(isFavoriteWVApp);
            }
        } catch (FinderException e) {
            log.error("Error in make member as favorite, not found user by addressId..." + addressId + "  " + e);
        }
    }

    public boolean isStateful() {
        return false;
    }
}
