package com.piramide.elwis.cmd.admin;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.common.config.SystemConstantCmd;
import com.piramide.elwis.domain.admin.*;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.AddressHome;
import com.piramide.elwis.domain.contactmanager.Telecom;
import com.piramide.elwis.domain.contactmanager.TelecomHome;
import com.piramide.elwis.dto.common.config.SystemConstantDTO;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.SystemLanguage;
import com.piramide.elwis.utils.TelecomType;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * @author Tayes
 * @version $Id: CompanyReadCmd.java 12654 2017-03-24 23:46:39Z miguel $
 */
public class CompanyReadCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        if ("checkTrialCompany".equals(this.getOp())) {
            checkTrialCompany(ctx);
        } else if ("getSystemModules".equals(this.getOp())) {
            getSystemModules();
        } else if ("getCompanyTimeZone".equals(this.getOp())) {
            Integer companyId = EJBCommandUtil.i.getValueAsInteger(this, "companyId");
            getCompanyTimeZone(companyId);
        } else if ("getCompanyByLogin".equals(this.getOp())) {
            getCompanyByLogin();
        } else if ("countUsersWithMobileAccessEnabled".equals(this.getOp())) {
            getUsersWithMobileAccessEnabled();
        } else if ("countUsersWithWVAppAccessEnabled".equals(this.getOp())) {
            getUsersWithWVAppAccessEnabled();
        } else {
            try {
                read();
            } catch (FinderException e) {
                String companyName = (String) paramDTO.get("companyName");
                Integer companyId = new Integer(paramDTO.getAsString("companyId"));
                resultDTO.setForward("Fail");
                resultDTO.addResultMessage("error.SelectedNotFound", companyName);
                log.debug("-> Read Company for companyId=" + companyId +
                        " FAIL, Setting up Forward=" + resultDTO.getForward());
            }
        }
    }

    private void checkTrialCompany(SessionContext ctx) {
        String language = (String) paramDTO.get("language");
        CompanyHome companyHome = (CompanyHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_COMPANY);
        AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);

        Company trialCompany = null;
        try {
            trialCompany = companyHome.findByCopyTemplateLanguage(
                    AdminConstants.CompanyTemplateTypes.trialTemplate.getConstant(), language);

            Address addressCompany = addressHome.findByPrimaryKey(trialCompany.getCompanyId());
            resultDTO.put("name1", addressCompany.getName1());
            resultDTO.put("name2", addressCompany.getName2());
            resultDTO.put("name3", addressCompany.getName3());
            resultDTO.put("trialCompanyLogin", trialCompany.getLogin());
            SystemConstantCmd systemConstantCmd = new SystemConstantCmd();
            systemConstantCmd.putParam("type", SystemLanguage.SYSTEM_CONSTANT_KEY);
            systemConstantCmd.executeInStateless(ctx);
            ResultDTO constsResultDTO = systemConstantCmd.getResultDTO();

            List languageConstants = (List) constsResultDTO.get(SystemLanguage.SYSTEM_CONSTANT_KEY);
            for (Object object : languageConstants) {
                SystemConstantDTO dto = (SystemConstantDTO) object;
                if (dto.get("value").equals(trialCompany.getLanguage())) {
                    resultDTO.put("languageResource", dto.get("resourceKey"));
                    break;
                }
            }
        } catch (FinderException e) {
            log.debug("Cannot find companyTrial. ");
        }
        if (null != trialCompany) {
            resultDTO.put("companyTrialId", trialCompany.getCompanyId());
        }
    }

    private void read() throws FinderException {
        log.debug("Read Address Company");

        CompanyHome companyHome = (CompanyHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_COMPANY);
        Company company = companyHome.findByPrimaryKey(new Integer(paramDTO.getAsString("companyId")));

        AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
        Address addressCompany = addressHome.findByPrimaryKey(new Integer(paramDTO.getAsString("companyId")));


        resultDTO.put("companyId", addressCompany.getCompanyId());
        resultDTO.put("name1", addressCompany.getName1());
        resultDTO.put("name2", addressCompany.getName2());
        resultDTO.put("name3", addressCompany.getName3());
        log.debug("ADDRESS-COMPANY:" + resultDTO);
        log.debug("Read company");


        resultDTO.put("companyCreateLogin", company.getLogin());
        resultDTO.put("startLicenseDate", company.getStartLicenseDate());
        resultDTO.put("finishLicenseDate", company.getFinishLicenseDate());
        resultDTO.put("usersAllowed", company.getUsersAllowed());
        resultDTO.put("maxMaxAttachSize", company.getMaxMaxAttachSize());
        resultDTO.put("active", company.getActive());
        resultDTO.put("version", company.getVersion());
        resultDTO.put("companyType", company.getCompanyType());
        resultDTO.put("creation_Date", addressCompany.getRecordDate());
        resultDTO.put("creation_", addressCompany.getRecordDate());
        resultDTO.put("copyTemplate", company.getCopyTemplate());
        resultDTO.put("favoriteLanguage", company.getLanguage());
        resultDTO.put("timeZone", company.getTimeZone());
        resultDTO.put("mobileActive", company.getMobileActive());
        resultDTO.put("mobileUserAllowed", company.getMobileUserAllowed());
        resultDTO.put("mobileStartLicense", company.getMobileStartLicense());
        resultDTO.put("mobileEndLicense", company.getMobileEndLicense());

        //DateUtils.integerToDate(addressCompany.getRecordDate()));
        log.debug("dataCOMPANY:" + resultDTO);

        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
        log.debug("Read USER!!!!");
        User user = userHome.findRootUserByCompany(company.getCompanyId());
        Address addressUser = addressHome.findByPrimaryKey(user.getAddressId());
        TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);
        Collection telecoms = telecomHome.findContactPersonTelecomsByTelecomTypeType(addressCompany.getAddressId(), addressUser.getAddressId(), TelecomType.EMAIL_TYPE);
        String defaultEmail = "";
        int i = 0;

        for (Iterator iterator = telecoms.iterator(); iterator.hasNext();) {
            Telecom telecom = (Telecom) iterator.next();
            if (i == 0) {
                defaultEmail = telecom.getData();
            }
            if (telecom.getPredetermined().booleanValue()) {
                resultDTO.put("email", telecom.getData());
                break;
            }
            i++;
        }
        if (resultDTO.get("email") == null) {
            resultDTO.put("email", defaultEmail);
        }

        resultDTO.put("rootName1", addressUser.getName1());
        resultDTO.put("rootName2", addressUser.getName2());
        resultDTO.put("userName", user.getUserLogin());

        log.debug("READ!!!! MODULE!!!" + company.getCompanyModules().size());
        List modules = new ArrayList(company.getCompanyModules().size());
        for (Iterator iterator = company.getCompanyModules().iterator(); iterator.hasNext();) {
            CompanyModule companyModule = (CompanyModule) iterator.next();
            log.debug("companymodule:" + companyModule);
            if (companyModule.getActive().booleanValue()) {
                modules.add(companyModule.getModuleId());
                resultDTO.put(companyModule.getSystemModule().getNameKey().replace('.', '_') + "_" + companyModule.getModuleId(), companyModule.getMainTableRecordsLimit());
            }
        }
        resultDTO.put("modules", modules);
    }

    private List<Map> getSystemModules() {
        SystemModuleHome moduleHome =
                (SystemModuleHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_SYSTEMMODULE);

        List<Map> result = new ArrayList<Map>();

        try {
            List systemModules = (List) moduleHome.findAll();
            for (int i = 0; i < systemModules.size(); i++) {
                SystemModule systemModule = (SystemModule) systemModules.get(i);
                Map module = new HashMap(2);
                module.put("nameKey", systemModule.getNameKey());
                module.put("moduleId", systemModule.getModuleId());
                result.add(module);
            }
        } catch (FinderException e) {
            log.error("Cannot read the SystemModules", e);
        }

        resultDTO.put("getSystemModules", result);

        return result;
    }

    private String getCompanyTimeZone(Integer companyId) {
        CompanyHome companyHome =
                (CompanyHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_COMPANY);
        String timeZone = null;
        try {
            Company company = companyHome.findByPrimaryKey(companyId);
            timeZone = company.getTimeZone();
        } catch (FinderException e) {
            log.debug("Cannot read Company companyId: " + companyId);
        }

        resultDTO.put("getCompanyTimeZone", timeZone);
        return timeZone;
    }

    private void getCompanyByLogin() {
        CompanyHome companyHome = (CompanyHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_COMPANY);
        String companyLogin = (String) paramDTO.get("login");

        try {
            Company company = companyHome.findByName(companyLogin);
            if (company != null) {
                DTOFactory.i.copyToDTO(company, resultDTO);
            }
        } catch (FinderException e) {
            resultDTO.setResultAsFailure();
            log.debug("Error in find by company login.." + companyLogin);
        }
    }

    private void getUsersWithMobileAccessEnabled() {
        Integer companyId = new Integer(paramDTO.get("companyId").toString());
        Integer userId = null;
        if (paramDTO.get("userId") != null) {
            userId = new Integer(paramDTO.get("userId").toString());
        }

        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);

        Integer count = null;
        try {
            if (userId != null) {
                count = userHome.selectCountCompanyUsersMobileAccessEnabledWithoutMe(companyId, userId);
            } else {
                count = userHome.selectCountCompanyUsersMobileAccessEnabled(companyId);
            }

        } catch (FinderException e) {
            log.debug("Error in count users with mobile access enabled..", e);
        }

        resultDTO.put("usersWithMobileAccess", count);
    }

    private void getUsersWithWVAppAccessEnabled() {
        Integer companyId = new Integer(paramDTO.get("companyId").toString());
        Integer userId = null;
        if (paramDTO.get("userId") != null) {
            userId = new Integer(paramDTO.get("userId").toString());
        }

        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);

        Integer count = null;
        try {
            if (userId != null) {
                count = userHome.selectCountCompanyUsersWVAppAccessEnabledWithoutMe(companyId, userId);
            } else {
                count = userHome.selectCountCompanyUsersWVAppAccessEnabled(companyId);
            }

        } catch (FinderException e) {
            log.debug("Error in count users with mobile access enabled..", e);
        }

        resultDTO.put("usersWithWVAppAccess", count);
    }


    public boolean isStateful() {
        return false;
    }
}
