package com.piramide.elwis.cmd.schedulermanager;

import com.piramide.elwis.cmd.common.GeneralCmd;
import com.piramide.elwis.domain.schedulermanager.ScheduledUser;
import com.piramide.elwis.dto.schedulermanager.ScheduledUserDTO;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;

/**
 * AlfaCentauro Team
 * This class executes the operation of delete for a scheduledUser and a deletes users from his
 * userGroupId and his appointmentId
 *
 * @author Alvaro
 * @version $Id: ParticipantDeleteCmd.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class ParticipantDeleteCmd extends GeneralCmd {
    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing ParticipantDeleteCmd........");
        String op = paramDTO.get("op").toString();
        if (op.equals("deleteGroup")) {
            deleteGroup(paramDTO.get("userGroupId").toString(), paramDTO.get("appointmentId").toString());
        } else if (op.equals("delete")) {
            super.setOp(this.getOp());
            super.executeInStateless(ctx, paramDTO, ScheduledUserDTO.class);
        }
    }

    public void deleteGroup(String userGroupId, String appointmentId) {
        Object params[] = {new Integer(userGroupId), new Integer(appointmentId)};
        ArrayList users = new ArrayList((Collection) EJBFactory.i.callFinder(new ScheduledUserDTO(), "findByUserGroupIdAndAppId", params));
        for (int i = 0; i < users.size(); i++) {
            try {
                ((ScheduledUser) users.get(i)).remove();
            }
            catch (javax.ejb.RemoveException ex) {
            }
        }
    }
}
