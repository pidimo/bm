package com.piramide.elwis.cmd.admin;

import com.piramide.elwis.domain.admin.Role;
import com.piramide.elwis.domain.admin.RoleHome;
import com.piramide.elwis.dto.admin.RoleDTO;
import com.piramide.elwis.utils.AdminConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author : ivan
 * @version $Id: LightlyRoleCmd.java 7936 2007-10-27 16:08:39Z fernando ${time}
 */
public class LightlyRoleCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing LightlyRoleCmd...");

        Integer companyId = Integer.valueOf(paramDTO.get("companyId").toString());

        Collection rolesByCompanyDTOs = new ArrayList();
        RoleHome home = (RoleHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_ROLE);
        Collection rolesByCompany = new ArrayList();
        try {
            rolesByCompany = home.findByCompanyKey(companyId);
            for (Iterator iterator = rolesByCompany.iterator(); iterator.hasNext();) {
                Role role = (Role) iterator.next();
                RoleDTO dto = new RoleDTO();
                dto.put("roleId", role.getRoleId());
                dto.put("roleName", role.getRoleName());
                dto.put("isDefault", role.getIsDefault());

                rolesByCompanyDTOs.add(dto);
            }


        } catch (FinderException e) {
            log.debug("---------------------------------------------------------------");
            log.error("Cannot find roles for the company with companyId = " + companyId);
            log.debug("---------------------------------------------------------------");
        }


        resultDTO.put("rolesByCompany", rolesByCompanyDTOs);
    }

    public boolean isStateful() {
        return false;
    }
}
