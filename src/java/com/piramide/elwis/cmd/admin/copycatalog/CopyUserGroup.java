package com.piramide.elwis.cmd.admin.copycatalog;

import com.piramide.elwis.domain.admin.Company;
import com.piramide.elwis.domain.admin.UserGroup;
import com.piramide.elwis.domain.admin.UserGroupHome;
import com.piramide.elwis.dto.admin.UserGroupDTO;
import com.piramide.elwis.utils.AdminConstants;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Collection;

/**
 * Jatun s.r.l.
 *
 * @author : ivan
 */
public class CopyUserGroup implements CopyCatalog {
    private Log log = LogFactory.getLog(this.getClass());

    public void copyCatalog(Company source, Company target, SessionContext sessionContext) {
        log.debug("Execute " + this.getClass().getName() + " ... ");
        UserGroupHome userGroupHome =
                (UserGroupHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USERGROUP);

        Collection sourceElements = null;
        try {
            sourceElements = userGroupHome.findByCompanyId(source.getCompanyId());
        } catch (FinderException e) {
            log.debug("Cannot read Source User groups for company =" + source.getCompanyId());
        }

        if (null != sourceElements) {
            for (Object object : sourceElements) {
                UserGroup sourceElement = (UserGroup) object;

                UserGroupDTO targetElementDTO = new UserGroupDTO();
                DTOFactory.i.copyToDTO(sourceElement, targetElementDTO);
                targetElementDTO.put("companyId", target.getCompanyId());
                targetElementDTO.remove("userGroupId");

                try {
                    userGroupHome.create(targetElementDTO);
                } catch (CreateException e) {
                    log.debug("Cannor create target usergroup for =" + target.getCompanyId() + "... ", e);
                }
            }
        }
    }
}
