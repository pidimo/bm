package com.piramide.elwis.cmd.schedulermanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.utils.AdminConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.HashMap;
import java.util.Map;

/**
 * Cmd to read user email info for send notification
 * @author Miguel A. Rojas Cardenas
 * @version 3.4
 */
public class AppointmentNotificationUserEmailInfoCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());


    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing AppointmentNotificationUserEmailInfoCmd...." + paramDTO);

        Integer userId = new Integer(paramDTO.get("userId").toString());
        Integer userSessionId = new Integer(paramDTO.get("userSessionId").toString());

        //user notification parameters
        resultDTO.put("userEmailInfoMap", getUserEmailInfo(userId, userSessionId));
    }

    private Map getUserEmailInfo(Integer toUserId, Integer fromUserId) {
        Map userEmailMap = new HashMap();
        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);

        try {
            User toUser = userHome.findByPrimaryKey(toUserId);
            if (!toUser.getAppointmentNotificationEmails().isEmpty()) {
                User fromUser = userHome.findByPrimaryKey(fromUserId);
                String mailFrom = null;
                if (!fromUser.getAppointmentNotificationEmails().isEmpty()) {
                    mailFrom = (String) fromUser.getAppointmentNotificationEmails().get(0);//get the first one as sender
                }
                //read from personal
                String fromPersonal = null;
                Address fromAddress = (Address) ExtendedCRUDDirector.i.read(new AddressDTO(fromUser.getAddressId()), new ResultDTO(), false);
                if (fromAddress != null) {
                    fromPersonal = fromAddress.getName();
                }

                userEmailMap.put("userId", toUserId);
                userEmailMap.put("emailFrom", mailFrom);
                userEmailMap.put("fromPersonal", fromPersonal);
                userEmailMap.put("emailTo", toUser.getNotificationAppointmentEmail());
            }
        } catch (FinderException e) {
            log.debug("Can't find user...");
        }
        return userEmailMap;
    }

    public boolean isStateful() {
        return false;
    }
}

