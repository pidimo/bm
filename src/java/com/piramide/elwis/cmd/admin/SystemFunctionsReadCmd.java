package com.piramide.elwis.cmd.admin;

import com.piramide.elwis.domain.admin.*;
import com.piramide.elwis.dto.admin.AccessRightsDTO;
import com.piramide.elwis.dto.admin.SystemFunctionDTO;
import com.piramide.elwis.utils.AdminConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * This Class read an specific company, identify the modules that this have assign; search all functionalities
 * per module and setting up in the resultDTO the "companyFunctionsDTOs" that contains all systemfunctions by module,
 * "numberOfModules" that contains number of modules and "accessRightsDTOs" that contains all accessRights by role
 *
 * @author Ivan
 * @version $Id: SystemFunctionsReadCmd.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class SystemFunctionsReadCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());


    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing SystemFunctionsReadCmd...");

        //read the companyId
        Integer companyId = Integer.valueOf(paramDTO.get("companyId").toString());


        Integer roleId = Integer.valueOf(paramDTO.get("roleId").toString());
        RoleHome roleHome = (RoleHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_ROLE);
        Role role = null;
        try {
            role = roleHome.findByPrimaryKey(roleId);
            Collection accessRights = role.getAccessRights();
            Collection accessRightsDTOs = new ArrayList();
            for (Iterator accessRightsIterator = accessRights.iterator(); accessRightsIterator.hasNext();) {
                AccessRights accessRight = (AccessRights) accessRightsIterator.next();
                AccessRightsDTO dto = new AccessRightsDTO();
                dto.put("functionId", accessRight.getFunctionId());
                dto.put("permission", accessRight.getPermission());
                dto.put("roleId", accessRight.getRoleId());

                accessRightsDTOs.add(dto);
            }

            resultDTO.put("accessRightsDTOs", accessRightsDTOs);
            resultDTO.put("version", role.getVersion());
        } catch (FinderException e) {
            log.error("Cannont find user role  whit userId = " + roleId);
        }


        CompanyHome companyHome = (CompanyHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_COMPANY);
        try {
            Company company = companyHome.findByPrimaryKey(companyId);

            Collection functions = null;

            //get back all company modules that this company have assigned
            Collection companyModules = company.getCompanyModules();

            Collection companyModuleDTOs = new ArrayList();
            for (Iterator companyModulesIterator = companyModules.iterator(); companyModulesIterator.hasNext();) {
                CompanyModule companyModule = (CompanyModule) companyModulesIterator.next();

                /*log.debug(".................................... " + companyModule.getActive());*/
                if (companyModule.getActive() != null && companyModule.getActive().equals(Boolean.valueOf("true"))) {
                    Map companyModuleDTO = new HashMap();
                    companyModuleDTO.put("nameKey", companyModule.getSystemModule().getNameKey());
                    companyModuleDTO.put("moduleId", companyModule.getSystemModule().getModuleId());

                    //get back all functions that module have assigned
                    functions = companyModule.getSystemModule().getFunctions();


                    Collection systemFunctionDTOs = new ArrayList();
                    for (Iterator functionsIterator = functions.iterator(); functionsIterator.hasNext();) {
                        SystemFunction systemFunction = (SystemFunction) functionsIterator.next();
                        SystemFunctionDTO dto = new SystemFunctionDTO();
                        dto.put("description", systemFunction.getDescription());
                        dto.put("functionCode", systemFunction.getFunctionCode());
                        dto.put("functionId", systemFunction.getFunctionId());
                        dto.put("moduleId", systemFunction.getModuleId());
                        dto.put("nameKey", systemFunction.getNameKey());
                        dto.put("permissionsAllowed", systemFunction.getPermissionsAllowed());

                        if (role.getIsDefault().booleanValue()) {
                            if ("ROLE".equals(systemFunction.getFunctionCode()) ||
                                    "ACCESSRIGHT".equals(systemFunction.getFunctionCode())) {
                                dto.put("disabled", Boolean.valueOf(true));
                            }
                        } else {
                            dto.put("disabled", Boolean.valueOf(false));
                        }

                        systemFunctionDTOs.add(dto);
                    }
                    companyModuleDTO.put("systemFunctionDTOs", systemFunctionDTOs);
                    companyModuleDTOs.add(companyModuleDTO);
                }
            }

            resultDTO.put("companyModuleDTOs", companyModuleDTOs);
            resultDTO.put("numberOfModules", new Integer(companyModuleDTOs.size()));

        } catch (FinderException e) {
            log.error("Cannot find company with companyId = " + companyId);
        }


    }

    public boolean isStateful() {
        return false;
    }
}
