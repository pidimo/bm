package com.piramide.elwis.cmd.admin;

import com.piramide.elwis.domain.admin.Role;
import com.piramide.elwis.domain.admin.RoleHome;
import com.piramide.elwis.dto.admin.RoleDTO;
import com.piramide.elwis.utils.AdminConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * Jatun s.r.l.
 *
 * @author : ivan
 */
public class RoleUtilCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext sessionContext) {
        String op = getOp();
        if ("readRole".equals(op)) {
            Integer roleId = (Integer) paramDTO.get("roleId");
            readRole(roleId);
        }
    }

    private void readRole(Integer roleId) {
        RoleHome roleHome = (RoleHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_ROLE);
        RoleDTO roleDTO = null;
        try {
            Role role = roleHome.findByPrimaryKey(roleId);
            roleDTO = new RoleDTO();
            DTOFactory.i.copyToDTO(role, roleDTO);
        } catch (FinderException e) {
            log.debug("Cannot read Role roleId=" + roleId);
        }
        resultDTO.put("roleDTO", roleDTO);
    }

    public boolean isStateful() {
        return false;
    }
}
