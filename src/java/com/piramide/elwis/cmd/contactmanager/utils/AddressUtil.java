package com.piramide.elwis.cmd.contactmanager.utils;

import com.piramide.elwis.cmd.contactmanager.LightlyAddressCmd;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.utils.AdminConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * @author Ivan Alban
 * @version 4.4
 */
public class AddressUtil {
    public static final AddressUtil i = new AddressUtil();

    private AddressUtil() {
    }

    public String getAddressName(Integer userId, SessionContext ctx) {

        User user = getUser(userId);

        LightlyAddressCmd addressCmd = new LightlyAddressCmd();
        addressCmd.putParam("addressId", user.getAddressId());
        addressCmd.executeInStateless(ctx);

        ResultDTO customResultDTO = addressCmd.getResultDTO();
        return (String) customResultDTO.get("addressName");
    }

    private User getUser(Integer userId) {
        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
        try {
            return userHome.findByPrimaryKey(userId);
        } catch (FinderException e) {
            //This exception never happen because the application never deletes the users.
        }

        return null;
    }
}
