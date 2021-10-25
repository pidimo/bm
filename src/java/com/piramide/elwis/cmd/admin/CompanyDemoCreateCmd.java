package com.piramide.elwis.cmd.admin;

import com.piramide.elwis.domain.admin.*;
import com.piramide.elwis.domain.contactmanager.TelecomType;
import com.piramide.elwis.domain.contactmanager.TelecomTypeHome;
import com.piramide.elwis.dto.admin.DemoAccountDTO;
import com.piramide.elwis.dto.catalogmanager.TelecomTypeDTO;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.utils.configuration.ConfigurationFactory;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 6.5
 */
public class CompanyDemoCreateCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing CompanyDemoCreateCmd................" + paramDTO);

        createDemoCompany(ctx);
    }

    public boolean isStateful() {
        return false;
    }

    private void createDemoCompany(SessionContext ctx) {
        Integer sourceCompanyId = new Integer(paramDTO.get("sourceTemplateCompanyId").toString());
        Integer demoAccountId = new Integer(paramDTO.get("demoAccountId").toString());

        Company sourceCompany = findCompany(sourceCompanyId);
        DemoAccount demoAccount = findDemoAccount(demoAccountId);

        if (sourceCompany != null && demoAccount != null && !demoAccount.getIsAlreadyCreated()) {

            Integer startLicenseDate = DateUtils.dateToInteger(new Date());

            CompanyCreateCmd createCmd = new CompanyCreateCmd();
            createCmd.putParam("name1", demoAccount.getCompanyName());
            createCmd.putParam("name2", "");
            createCmd.putParam("name3", "");
            createCmd.putParam("companyCreateLogin", demoAccount.getCompanyLogin());
            createCmd.putParam("startLicenseDate", startLicenseDate);
            createCmd.putParam("finishLicenseDate", calculateFinishLicenceDate(startLicenseDate));
            createCmd.putParam("usersAllowed", sourceCompany.getUsersAllowed());
            createCmd.putParam("companyType", AdminConstants.CompanyType.DEMO.getConstant());
            //createCmd.putParam("copyTemplate", );
            createCmd.putParam("favoriteLanguage", paramDTO.get("favoriteLanguage"));
            createCmd.putParam("maxMaxAttachSize", sourceCompany.getMaxMaxAttachSize());
            createCmd.putParam("timeZone", sourceCompany.getTimeZone());

            createCmd.putParam("mobileActive", sourceCompany.getMobileActive());
            createCmd.putParam("mobileUserAllowed", sourceCompany.getMobileUserAllowed());
            //createCmd.putParam("mobileStartLicense", sourceCompany.getMobileStartLicense());
            //createCmd.putParam("mobileEndLicense", sourceCompany.getMobileEndLicense());

            //root user
            createCmd.putParam("rootName1", demoAccount.getLastName());
            createCmd.putParam("rootName2", demoAccount.getFirstName());
            createCmd.putParam("userName", demoAccount.getUserLogin());
            createCmd.putParam("userPassword", demoAccount.getPassword());
            createCmd.putParam("email", demoAccount.getEmail());

            createCmd.putParam("languages", paramDTO.get("languages"));
            createCmd.putParam("defaultTelecomTypes", readTelecomTypesByCompany(sourceCompany.getCompanyId()));

            //define company modules
            Map modulesInfoMap = readCompanyModules(sourceCompany);
            createCmd.putParam("modules", modulesInfoMap.get("modulesId"));
            createCmd.putParam((Map) modulesInfoMap.get("modulesLimitMap"));

            //to copy catalogs of source company
            createCmd.putParam("templateCampaignId", sourceCompany.getCompanyId());

            createCmd.executeInStateless(ctx);
            ResultDTO companyResultDTO = createCmd.getResultDTO();

            //check if company has been created
            if (companyResultDTO.get("companyId") != null) {
                Integer newCompanyId = (Integer) companyResultDTO.get("companyId");
                Integer userId = (Integer) companyResultDTO.get("rootUserId");

                Company newCompany = findCompany(newCompanyId);
                User user = findUser(userId);
                enableUserAccessToBmApp(user, newCompany);

                //update fields in demo account
                demoAccount.setCreationDate(DateUtils.dateToInteger(new Date()));
                demoAccount.setIsAlreadyCreated(Boolean.TRUE);

                //add in result dto demo account info
                DemoAccountDTO demoAccountDTO = new DemoAccountDTO();
                DTOFactory.i.copyToDTO(demoAccount, demoAccountDTO);
                resultDTO.put("infoDemoAccountDTO", demoAccountDTO);
            }

        } else {
            resultDTO.setResultAsFailure();
        }
    }

    private void enableUserAccessToBmApp(User user, Company company) {
        if (user != null && company != null) {
            if (company.getMobileActive() != null && company.getMobileActive()) {
                user.setMobileActive(Boolean.TRUE);
            }
        }
    }

    private Integer calculateFinishLicenceDate(Integer startDate) {
        Integer durationDays = Integer.valueOf(ConfigurationFactory.getConfigurationManager().getValue("elwis.demoAccount.duration.days"));

        DateTime startDateTime = DateUtils.integerToDateTime(startDate);
        DateTime endDateTime = startDateTime.plusDays(durationDays);
        return DateUtils.dateToInteger(endDateTime);
    }

    private List<TelecomTypeDTO> readTelecomTypesByCompany(Integer companyId) {
        List<TelecomTypeDTO> telecomTypeList = new ArrayList<TelecomTypeDTO>();

        TelecomTypeHome telecomTypeHome = (TelecomTypeHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_TELECOMTYPE);
        try {
            Collection telecomTypes = telecomTypeHome.findByCompanyId(companyId);
            for (Object object : telecomTypes) {
                TelecomType telecomType = (TelecomType) object;

                TelecomTypeDTO dto = new TelecomTypeDTO();
                dto.put("telecomTypeName", telecomType.getTelecomTypeName());
                dto.put("type", telecomType.getType());
                dto.put("position", telecomType.getPosition());

                telecomTypeList.add(dto);
            }
        } catch (FinderException e) {
            log.debug("-> The company has no registered TelecomTypes");
        }
        return telecomTypeList;
    }

    private Map<String, Object> readCompanyModules(Company company) {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        List<String> modulesIdList = new ArrayList<String>();
        Map<String, Integer> modulesLimitMap = new HashMap<String, Integer>();

        for (Iterator iterator = company.getCompanyModules().iterator(); iterator.hasNext();) {
            CompanyModule companyModule = (CompanyModule) iterator.next();

            if (companyModule.getActive() != null && companyModule.getActive()) {
                modulesIdList.add(companyModule.getModuleId().toString());
                modulesLimitMap.put(companyModule.getSystemModule().getNameKey().replace('.', '_') + "_" + companyModule.getModuleId(), companyModule.getMainTableRecordsLimit());
            }
        }

        resultMap.put("modulesId", modulesIdList);
        resultMap.put("modulesLimitMap", modulesLimitMap);

        return resultMap;
    }

    private Company findCompany(Integer companyId) {
        CompanyHome companyHome = (CompanyHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_COMPANY);
        Company company = null;
        if (companyId != null) {
            try {
                company = companyHome.findByPrimaryKey(companyId);
            } catch (FinderException e) {
                log.debug("Not foun company.." + e);
            }
        }
        return company;
    }

    private DemoAccount findDemoAccount(Integer demoAccountId) {
        DemoAccountHome demoAccountHome = (DemoAccountHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_DEMOACCOUNT);
        DemoAccount demoAccount = null;
        if (demoAccountId != null) {
            try {
                demoAccount = demoAccountHome.findByPrimaryKey(demoAccountId);
            } catch (FinderException e) {
                log.debug("Not foun DemoAccount.." + e);
            }
        }
        return demoAccount;
    }

    private User findUser(Integer userId) {
        User user = null;
        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
        if (userId != null) {
            try {
                user = userHome.findByPrimaryKey(userId);
            } catch (FinderException e) {
                log.debug("Not found user.... " + userId);
            }
        }
        return user;
    }
}
