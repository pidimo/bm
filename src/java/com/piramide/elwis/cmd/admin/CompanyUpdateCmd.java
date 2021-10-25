package com.piramide.elwis.cmd.admin;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.admin.*;
import com.piramide.elwis.dto.contactmanager.CompanyDTO;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.EncryptUtil;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author tayes
 * @version $Id: CompanyUpdateCmd.java 12655 2017-03-28 00:45:30Z miguel $
 */

public class CompanyUpdateCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        String companyName = paramDTO.get("name1") + " " + paramDTO.get("name2");
        Integer companyId = new Integer(paramDTO.getAsString("companyId"));
        boolean exists = readCompany(companyId, companyName);

        if (exists) {
            try {
                update();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean readCompany(Integer companyId, String companyName) {
        CompanyHome companyHome = (CompanyHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_COMPANY);
        try {
            companyHome.findByPrimaryKey(companyId);
        } catch (FinderException e) {

            resultDTO.setForward("Fail");
            resultDTO.addResultMessage("error.SelectedNotFound", companyName);
            log.debug("-> Read Company for companyId=" + companyId +
                    " FAIL, Setting up Forward=" + resultDTO.getForward());
            return false;
        }
        return true;
    }

    private void update() throws Exception {

        log.debug("Update company");


        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setPrimKey(paramDTO.getAsString("companyId"));
        companyDTO.put("version", paramDTO.get("version"));
        companyDTO.put("startLicenseDate", paramDTO.get("startLicenseDate"));
        companyDTO.put("finishLicenseDate", paramDTO.get("finishLicenseDate"));
        companyDTO.put("usersAllowed", paramDTO.get("usersAllowed"));
        companyDTO.put("maxMaxAttachSize", paramDTO.get("maxMaxAttachSize"));
        companyDTO.put("maxAttachSize", paramDTO.get("maxMaxAttachSize"));
        companyDTO.put("companyType", paramDTO.get("companyType"));
        companyDTO.put("active", Boolean.valueOf("true".equals(paramDTO.getAsString("active"))));
        companyDTO.put("copyTemplate", paramDTO.get("copyTemplate"));
        companyDTO.put("language", paramDTO.get("favoriteLanguage"));
        companyDTO.put("timeZone", paramDTO.get("timeZone"));

        companyDTO.put("mobileActive", paramDTO.get("mobileActive"));
        companyDTO.put("mobileUserAllowed", paramDTO.get("mobileUserAllowed"));
        companyDTO.put("mobileStartLicense", paramDTO.get("mobileStartLicense"));
        companyDTO.put("mobileEndLicense", paramDTO.get("mobileEndLicense"));

        if (paramDTO.get("companyCreateLogin") != null && paramDTO.get("companyCreateLogin").toString().length() > 0) {
            companyDTO.put("login", paramDTO.get("companyCreateLogin"));
        }

        Company company = (Company) ExtendedCRUDDirector.i.update(companyDTO, resultDTO, false, true, false, "Reload");

        company.setIsDefault(company.getIsDefault() != null && company.getIsDefault().booleanValue() ?
                Boolean.TRUE : Boolean.FALSE);


        if (company != null && !resultDTO.isFailure()) {
            log.debug("Update other fields!!!!");
            UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);

            if (paramDTO.get("userPassword") != null && paramDTO.getAsString("userPassword").length() > 0) {
                User user = userHome.findRootUserByCompany(company.getCompanyId());
                user.setUserPassword(EncryptUtil.i.encryt((String) paramDTO.get("userPassword")));
            }

            List updateModules = (List) paramDTO.get("modules");

            if (updateModules == null) {
                updateModules = new ArrayList();
            }
            AccessRightsHome accessRightsHome = (AccessRightsHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_ACCESSRIGHTS);
            RoleHome roleHome = (RoleHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_ROLE);
            List companyModules = new ArrayList();
            for (Iterator iterator = company.getCompanyModules().iterator(); iterator.hasNext();) {
                CompanyModule companyModule = (CompanyModule) iterator.next();
                log.debug("MODULE:" + companyModule.getModuleId());
                boolean isActiveModule = false;
                String id = null;
                companyModules.add(companyModule.getModuleId().toString());

                for (Iterator iterator1 = updateModules.iterator(); iterator1.hasNext();) {
                    id = (String) iterator1.next();
                    if (id.equals(companyModule.getModuleId().toString())) {
                        isActiveModule = true;
                        String moduleKey = new StringBuffer().append(companyModule.getSystemModule().getNameKey().replace('.', '_'))
                                .append("_").append(companyModule.getModuleId()).toString();
                        log.debug("TABLE LIMIT:" + moduleKey);
                        log.debug(" ... before ...." + companyModule.getMainTableRecordsLimit());
                        companyModule.setMainTableRecordsLimit((Integer) (paramDTO.get(moduleKey)));
                        log.debug(" ... after ... " + companyModule.getMainTableRecordsLimit());
                        break;
                    }
                }
                log.debug("Active:" + companyModule.getActive() + " - ISactive BOOL:" + isActiveModule);
                // For set the new  active value in the module
                if (companyModule.getActive().booleanValue() != isActiveModule) {
                    companyModule.setActive(Boolean.valueOf(isActiveModule));
                    Collection collection = accessRightsHome.findAllAccessRightsModule(companyModule.getModuleId(), companyModule.getCompanyId());
                    log.debug("AccessRights: size:" + collection.size());
                    for (Iterator iterator2 = collection.iterator(); iterator2.hasNext();) {
                        AccessRights accessRights = (AccessRights) iterator2.next();
                        accessRights.setActive(Boolean.valueOf(isActiveModule));
                    }
                }
            }
            log.debug("CompanyMODULES:" + companyModules);
            List finalMod = new ArrayList(updateModules.size());
            for (Iterator i1 = updateModules.iterator(); i1.hasNext();) {
                String s = (String) i1.next();
                finalMod.add(s);
            }

            finalMod.removeAll(companyModules);

            //Add module
            log.debug("MODULES:" + updateModules);
            for (Iterator modIte = finalMod.iterator(); modIte.hasNext();) {
                String id = (String) modIte.next();
                //log.debug("Assign new Module:"+id);
                CompanyModuleHome companyModuleHome = (CompanyModuleHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_COMPANYMODULE);
                CompanyModule companyModuleNew = companyModuleHome.create(company.getCompanyId(), new Integer(id), null);
                companyModuleNew.setActive(new Boolean(true));
                String moduleKey = new StringBuffer().append(companyModuleNew.getSystemModule().getNameKey().replace('.', '_'))
                        .append("_").append(companyModuleNew.getModuleId()).toString();
                log.debug("TABLE LIMIT:" + moduleKey);
                companyModuleNew.setMainTableRecordsLimit((Integer) (paramDTO.get(moduleKey)));
                Role role = roleHome.findDefault(companyModuleNew.getCompanyId());
                for (Iterator funtions = companyModuleNew.getSystemModule().getFunctions().iterator(); funtions.hasNext();) {
                    SystemFunction function = (SystemFunction) funtions.next();
                    AccessRights accessRights = accessRightsHome.create(function.getFunctionId(), role.getRoleId(), companyModuleNew.getModuleId(), companyModuleNew.getCompanyId());
                    accessRights.setPermission(function.getPermissionsAllowed());
                    accessRights.setActive(new Boolean(true));
                }
            }
        }
    }

    public boolean isStateful() {
        return false;
    }
}
