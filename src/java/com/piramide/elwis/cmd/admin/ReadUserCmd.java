package com.piramide.elwis.cmd.admin;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.utils.SchedulerUtil;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.dto.admin.UserDTO;
import com.piramide.elwis.utils.AdminConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class ReadUserCmd extends EJBCommand {
    private static Log log = LogFactory.getLog(ReadUserCmd.class);

    public void executeInStateless(SessionContext sessionContext) {
        log.debug("Executing ReadUserCmd..." + paramDTO);

        boolean defaultOperation = true;
        if ("getUserTimeZone".equals(getOp())) {
            defaultOperation = false;
            Integer userId = EJBCommandUtil.i.getValueAsInteger(this, "userId");
            getUserTimeZone(userId);
        }

        if ("userByAddressId".equals(getOp())) {
            defaultOperation = false;
            getUserByAddressId();
        }

        if (defaultOperation) {
            Integer userId = (Integer) paramDTO.get("userId");
            defaultOperation(userId);
        }
    }

    @SuppressWarnings(value = "unchecked")
    private void getUserTimeZone(Integer userId) {
        resultDTO.put("getUserTimeZone", SchedulerUtil.i.getUserDateTimeZone(userId));
    }

    @SuppressWarnings(value = "unchecked")
    private void defaultOperation(Integer userId) {
        UserHome userHome =
                (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);

        UserDTO dto = null;
        try {
            User elwisUser = userHome.findByPrimaryKey(userId);
            dto = new UserDTO();
            DTOFactory.i.copyToDTO(elwisUser, dto);
            log.debug("-> Read userId=" + userId + " OK");
        } catch (FinderException e) {
            log.debug("-> Read userId=" + userId + " FAIL");
        }

        resultDTO.put("elwisUser", dto);
    }

    private void getUserByAddressId() {
        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
        Integer addressId = EJBCommandUtil.i.getValueAsInteger(this, "addressId");
        Integer companyId = EJBCommandUtil.i.getValueAsInteger(this, "companyId");

        try {
            User user = userHome.findByAddressId(companyId, addressId);
            if (user != null) {
                DTOFactory.i.copyToDTO(user, resultDTO);
            }
        } catch (FinderException e) {
            resultDTO.setResultAsFailure();
            log.debug("Error in find user with address..." + addressId + "-" + companyId, e);
        }
    }

    public boolean isStateful() {
        return false;
    }
}
