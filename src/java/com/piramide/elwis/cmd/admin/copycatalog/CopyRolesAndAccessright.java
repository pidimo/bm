package com.piramide.elwis.cmd.admin.copycatalog;

import com.piramide.elwis.domain.admin.*;
import com.piramide.elwis.dto.admin.RoleDTO;
import com.piramide.elwis.utils.AdminConstants;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * Jatun s.r.l.
 *
 * @author : ivan
 */
public class CopyRolesAndAccessright implements CopyCatalog {
    private Log log = LogFactory.getLog(this.getClass());

    public void copyCatalog(Company source,
                            Company target, SessionContext sessionContext) {
        log.debug("Execute " + this.getClass().getName() + " ... ");
        copyRoles(source.getCompanyId(), target.getCompanyId());
    }

    private void copyRoles(Integer sourceCompanyId, Integer targetCompanyId) {
        RoleHome roleHome = (RoleHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_ROLE);
        Collection sourceElements = null;
        try {
            sourceElements = roleHome.findByCompanyId(sourceCompanyId);
        } catch (FinderException e) {
            log.debug("Cannot read source Roles for company = " + sourceCompanyId);
        }

        if (null != sourceElements) {
            for (Object object : sourceElements) {
                Role sourceElement = (Role) object;
                if (sourceElement.getIsDefault()) {
                    continue;
                }


                Integer targetFreeTextId = null;
                AdminFreeText sourceFreetext = sourceElement.getDescriptionText();
                try {
                    AdminFreeText targetFreeText = createFreeText(sourceFreetext.getValue(), targetCompanyId, sourceFreetext.getType());
                    targetFreeTextId = targetFreeText.getFreeTextId();
                } catch (CreateException e) {
                    log.debug("Cannot create targetFreetext company = " + targetCompanyId);
                }

                RoleDTO targetElementDTO = new RoleDTO();
                DTOFactory.i.copyToDTO(sourceElement, targetElementDTO);
                targetElementDTO.put("companyId", targetCompanyId);
                targetElementDTO.put("descriptionId", targetFreeTextId);

                try {
                    Role targetElement = roleHome.create(targetElementDTO);
                    List<Map<String, Object>> accessRights = readAccessRigths(sourceElement.getRoleId(), sourceCompanyId);
                    createAccesRights(targetElement.getRoleId(), targetCompanyId, accessRights);
                } catch (CreateException e) {
                    log.debug("Cannot create role for company = " + targetCompanyId);
                }
            }
        }
    }

    private void createAccesRights(Integer roleId,
                                   Integer companyId,
                                   List<Map<String, Object>> accessRights) {
        AccessRightsHome accessRightsHome =
                (AccessRightsHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_ACCESSRIGHTS);

        for (Map<String, Object> accessRight : accessRights) {
            Integer functionId = (Integer) accessRight.get("accessRights.functionId");
            Integer moduleId = (Integer) accessRight.get("accessRights.moduleId");
            Byte permission = (Byte) accessRight.get("accessRights.permission");

            try {
                AccessRights newAccessRight = accessRightsHome.create(functionId, roleId, moduleId, companyId);
                newAccessRight.setPermission(permission);
            } catch (CreateException e) {
                log.debug("Cannot create accessRight for role = " + roleId + " company = " + companyId);
            }
        }
    }

    private List<Map<String, Object>> readAccessRigths(Integer roleId, Integer companyId) {
        AccessRightsHome accessRightsHome =
                (AccessRightsHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_ACCESSRIGHTS);
        Collection elements = null;
        try {
            elements = accessRightsHome.findAccessRightsByRole(roleId, companyId);
        } catch (FinderException e) {
            log.debug("Cannot read acces rights for role = " + roleId + " and company = " + companyId);
        }
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        if (null != elements) {
            for (Object object : elements) {
                Map<String, Object> accessRightMap = new HashMap<String, Object>();
                AccessRights accessRights = (AccessRights) object;
                Boolean active = accessRights.getActive();
                Integer functionId = accessRights.getFunctionId();
                Integer moduleId = accessRights.getModuleId();
                Byte permission = accessRights.getPermission();
                accessRightMap.put("accessRights.active", active);
                accessRightMap.put("accessRights.functionId", functionId);
                accessRightMap.put("accessRights.moduleId", moduleId);
                accessRightMap.put("accessRights.permission", permission);
                result.add(accessRightMap);
            }
        }
        return result;
    }

    private AdminFreeText createFreeText(byte[] file, Integer companyId, Integer type) throws CreateException {
        AdminFreeTextHome freeTextHome = (AdminFreeTextHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_ADMIN_FREETEXT);
        return freeTextHome.create(file, companyId, type);
    }
}
