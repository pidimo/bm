package com.piramide.elwis.cmd.admin;

import com.piramide.elwis.domain.admin.*;
import com.piramide.elwis.dto.admin.RoleDTO;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.PermissionUtil;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.Collection;
import java.util.Iterator;

/**
 * Esta clase permite grabar los AccessRigths segun el modulo en el que se encuentran
 *
 * @author Ivan
 * @version $Id: AccessRightSaveCmd.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class AccessRightSaveCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("**************************************************");
        log.debug("Executing AccessRightSaveCmd..." + paramDTO);
        log.debug("**************************************************");

        CompanyModuleHome companyModuleHome = (CompanyModuleHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_COMPANYMODULE);


        Integer companyId = Integer.valueOf((String) paramDTO.get("companyId"));
        Integer roleId = Integer.valueOf((String) paramDTO.get("roleId"));
        Integer version = Integer.valueOf((String) paramDTO.get("version"));

        RoleCmd roleCmd = new RoleCmd();
        RoleDTO dto = new RoleDTO();
        dto.put("roleId", roleId);
        dto.put("companyId", companyId);

        Role role = (Role) roleCmd.execute(ctx, dto);

        if (role.getVersion().equals(version)) {
            //update version of role
            role.setVersion(Integer.valueOf(String.valueOf(version.intValue() + 1)));

            int numberOfModules = Integer.valueOf((String) paramDTO.get("numberOfModules")).intValue();
            int moduleCounter = 1;

            //iterates over all modules
            while (moduleCounter <= numberOfModules) {

                Integer companyModuleId = Integer.valueOf((String) paramDTO.get("companyModuleId" + moduleCounter));
                CompanyModulePK companyModulePK = new CompanyModulePK();
                companyModulePK.companyId = companyId;
                companyModulePK.moduleId = companyModuleId;

                try {
                    CompanyModule companyModule = companyModuleHome.findByPrimaryKey(companyModulePK);
                    Collection functions = companyModule.getSystemModule().getFunctions();

                    for (Iterator functionIterator = functions.iterator(); functionIterator.hasNext();) {
                        SystemFunction systemFunction = (SystemFunction) functionIterator.next();


                        String optionView = (String) paramDTO.get("checkBox_" + companyModule.getModuleId() + "_" + systemFunction.getFunctionId() + "_" + PermissionUtil.VIEW);
                        String optionCreate = (String) paramDTO.get("checkBox_" + companyModule.getModuleId() + "_" + systemFunction.getFunctionId() + "_" + PermissionUtil.CREATE);
                        String optionUpdate = (String) paramDTO.get("checkBox_" + companyModule.getModuleId() + "_" + systemFunction.getFunctionId() + "_" + PermissionUtil.UPDATE);
                        String optionDelete = (String) paramDTO.get("checkBox_" + companyModule.getModuleId() + "_" + systemFunction.getFunctionId() + "_" + PermissionUtil.DELETE);
                        String optionExecute = (String) paramDTO.get("checkBox_" + companyModule.getModuleId() + "_" + systemFunction.getFunctionId() + "_" + PermissionUtil.EXECUTE);
                        int sum = 0;


                        if (optionView != null) {
                            sum += PermissionUtil.VIEW;
                        }
                        if (optionCreate != null) {
                            sum += PermissionUtil.CREATE;
                        }
                        if (optionUpdate != null) {
                            sum += PermissionUtil.UPDATE;
                        }
                        if (optionDelete != null) {
                            sum += PermissionUtil.DELETE;
                        }
                        if (optionExecute != null) {
                            sum += PermissionUtil.EXECUTE;
                        }
                        // save accessRigth if the sum have value
                        if (sum > 0) {
                            saveAccesRigth(roleId, systemFunction.getFunctionId(), sum, companyModule.getModuleId(), companyModule.getCompanyId());
                        } else if (sum == 0) {
                            deleteAccesRigth(roleId, systemFunction.getFunctionId());
                        }
                    }
                } catch (FinderException e) {
                    log.error("Cannot Find companyModule...");
                }
                moduleCounter++;
            }
        } else {
            resultDTO.addResultMessage("msg.Updated", role.getRoleName());
            resultDTO.setForward("Retry");
        }
    }

    public boolean isStateful() {
        return false;
    }

    private void saveAccesRigth(Integer roleId, Integer functionId, int permission, Integer moduleId, Integer companyId) {
        log.debug("Executing method saveAccesRigth...");
        Byte result = Byte.valueOf(String.valueOf(permission));
        AccessRightsPK pk = new AccessRightsPK();
        pk.functionId = functionId;
        pk.roleId = roleId;
        AccessRightsHome accessRightsHome = (AccessRightsHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_ACCESSRIGHTS);
        try {
            AccessRights accessRights = accessRightsHome.findByPrimaryKey(pk);
            accessRights.setPermission(result);
        } catch (FinderException ef) {
            log.debug("Cannot find AccessRight...");
            try {
                AccessRights accessRights = accessRightsHome.create(functionId, roleId, moduleId, companyId);
                accessRights.setPermission(result);
                accessRights.setActive(new Boolean(true));
            } catch (CreateException ec) {
                log.error("Cannot Create AccessRight...");
            }
        }
    }

    private void deleteAccesRigth(Integer roleId, Integer functionId) {
        log.debug("Executing method deleteAccesRigth...");
        AccessRightsPK pk = new AccessRightsPK();
        pk.functionId = functionId;
        pk.roleId = roleId;
        AccessRightsHome accessRightsHome = (AccessRightsHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_ACCESSRIGHTS);
        try {
            AccessRights accessRights = accessRightsHome.findByPrimaryKey(pk);
            accessRights.remove();
        } catch (FinderException ef) {
            log.debug("Cannot find AccessRight...");
        } catch (RemoveException e) {
            log.error("Cannot remove AccessRight...", e);
        }
    }
}
