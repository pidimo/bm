package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.dto.admin.UserDTO;
import com.piramide.elwis.utils.AdminConstants;
import net.java.dev.strutsejb.EJBCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * Jatun S.R.L.
 * read user and verify if this is internal user
 *
 * @author Miky
 * @version $Id: LightlyInternalUserCmd.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class LightlyInternalUserCmd extends EJBCommand {
    private Log log = LogFactory.getLog(com.piramide.elwis.cmd.campaignmanager.LightlyCampaignCmd.class);

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing LightlyInternalUserCmd..... " + paramDTO);

        UserDTO dto = new UserDTO(paramDTO);
        User user = (User) ExtendedCRUDDirector.i.read(dto, resultDTO, false);

        boolean isInternalUser = false;
        if (user != null) {
            isInternalUser = AdminConstants.INTERNAL_USER.equals(user.getType().toString());
        }
        resultDTO.put("isInternalUser", (isInternalUser ? "true" : "false"));
    }

    public boolean isStateful() {
        return false;
    }
}
