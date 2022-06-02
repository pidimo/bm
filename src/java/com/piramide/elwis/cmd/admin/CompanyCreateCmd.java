package com.piramide.elwis.cmd.admin;

import com.piramide.elwis.domain.admin.*;
import com.piramide.elwis.domain.catalogmanager.LangText;
import com.piramide.elwis.domain.catalogmanager.LangTextHome;
import com.piramide.elwis.domain.catalogmanager.Language;
import com.piramide.elwis.domain.catalogmanager.LanguageHome;
import com.piramide.elwis.domain.contactmanager.*;
import com.piramide.elwis.domain.contactmanager.TelecomType;
import com.piramide.elwis.dto.admin.RoleDTO;
import com.piramide.elwis.dto.admin.UserDTO;
import com.piramide.elwis.dto.admin.UserRoleDTO;
import com.piramide.elwis.dto.catalogmanager.LangTextDTO;
import com.piramide.elwis.dto.catalogmanager.LanguageDTO;
import com.piramide.elwis.dto.catalogmanager.TelecomTypeDTO;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.dto.contactmanager.CompanyDTO;
import com.piramide.elwis.dto.contactmanager.ContactPersonDTO;
import com.piramide.elwis.dto.contactmanager.EmployeeDTO;
import com.piramide.elwis.exception.ServiceUnavailableException;
import com.piramide.elwis.utils.*;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author Tayes
 * @version $Id: CompanyCreateCmd.java 12654 2017-03-24 23:46:39Z miguel $
 */

public class CompanyCreateCmd extends EJBCommand {

    private Log log = LogFactory.getLog(CompanyCreateCmd.class);

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing CompanyCreateCmd............" + paramDTO);

        try {
            log.debug("Creating company...");
            create(ctx);
        } catch (Exception e) {
            ctx.setRollbackOnly();
            log.error("Error in create company:", e);
        }
    }

    private void create(SessionContext ctx) throws CreateException, ServiceUnavailableException {

        // Create address for company
        AddressDTO companyAddressDTO = new AddressDTO();

        companyAddressDTO.put("name1", paramDTO.remove("name1"));
        companyAddressDTO.put("name2", paramDTO.remove("name2"));
        companyAddressDTO.put("name3", paramDTO.remove("name3"));
        companyAddressDTO.put("searchName", paramDTO.get("companyCreateLogin"));
        companyAddressDTO.put("addressType", "0");
        companyAddressDTO.put("personal", "0");
        companyAddressDTO.put("isPublic", Boolean.TRUE);
        companyAddressDTO.put("code", new Byte((byte) (CodeUtil.default_ + CodeUtil.company)));     // Set default + company
        log.debug("Create Address Company");
        AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
        Address addressCompany = addressHome.create(companyAddressDTO);

        // Create Company with address
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.put("login", paramDTO.remove("companyCreateLogin"));
        companyDTO.put("startLicenseDate", paramDTO.remove("startLicenseDate"));
        companyDTO.put("finishLicenseDate", paramDTO.remove("finishLicenseDate"));
        companyDTO.put("usersAllowed", paramDTO.remove("usersAllowed"));
        companyDTO.put("companyType", paramDTO.get("companyType"));
        companyDTO.put("active", Boolean.TRUE);
        companyDTO.put("companyId", addressCompany.getAddressId());
        companyDTO.put("copyTemplate", paramDTO.get("copyTemplate"));
        companyDTO.put("language", paramDTO.get("favoriteLanguage"));
        companyDTO.put("invoiceDaysSend", 0);
        companyDTO.put("vatId", paramDTO.get("vatId"));
        companyDTO.put("maxMaxAttachSize", paramDTO.get("maxMaxAttachSize"));
        companyDTO.put("maxAttachSize", paramDTO.get("maxMaxAttachSize"));
        companyDTO.put("rowsPerPage", 10);
        companyDTO.put("timeout", 30);
        companyDTO.put("timeZone", paramDTO.get("timeZone"));

        companyDTO.put("mobileActive", paramDTO.get("mobileActive"));
        companyDTO.put("mobileUserAllowed", paramDTO.get("mobileUserAllowed"));
        companyDTO.put("mobileStartLicense", paramDTO.get("mobileStartLicense"));
        companyDTO.put("mobileEndLicense", paramDTO.get("mobileEndLicense"));

        log.debug("COMPANYDTO:" + companyDTO);
        CompanyHome companyHome = (CompanyHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_COMPANY);
        log.debug("Create company");
        Company company = companyHome.create(companyDTO);
        company.setIsDefault(Boolean.FALSE);
        addressCompany.setCompanyId(company.getCompanyId());

        // Create address for ContactPerson
        AddressDTO userAddressDTO = new AddressDTO();
        userAddressDTO.put("name1", paramDTO.remove("rootName1"));
        userAddressDTO.put("name2", paramDTO.remove("rootName2"));
        userAddressDTO.put("searchName", paramDTO.get("userName"));
        userAddressDTO.put("companyId", company.getCompanyId());
        userAddressDTO.put("addressType", "1");
        userAddressDTO.put("personal", "0");
        userAddressDTO.put("isPublic", Boolean.TRUE);
        userAddressDTO.put("code", new Byte((byte) (CodeUtil.default_ + CodeUtil.employee)));     // Set default + employee
        log.debug("Create Address User");
        Address addressUser = addressHome.create(userAddressDTO);

        // Create ContactPerson with addressUser
        ContactPersonDTO contactPersonDTO = new ContactPersonDTO();
        contactPersonDTO.put("addressId", addressCompany.getAddressId());
        contactPersonDTO.put("contactPersonId", addressUser.getAddressId());
        contactPersonDTO.put("companyId", company.getCompanyId());
        contactPersonDTO.put("active", Boolean.TRUE);

        ContactPersonHome contactPersonHome = (ContactPersonHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CONTACTPERSON);
        log.debug("Create Contactperson for User");
        ContactPerson contactPerson = contactPersonHome.create(contactPersonDTO);

        log.debug("Create Telecom for user");
        log.debug("CompanyId:" + company);
        log.debug("AddressCompanyId:" + addressCompany.getAddressId() + "CompanyID:" + addressCompany.getCompanyId());
        log.debug("AddressUSERId:" + addressUser.getAddressId());


        LangTextHome langTextHome = (LangTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGTEXT);
        LanguageHome languageHome = (LanguageHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGUAGE);

        // Create default languages IU --
        List languages = (List) paramDTO.get("languages");
        Integer languageId = null;
        String favoriteLanguage = paramDTO.getAsString("favoriteLanguage");

        for (Iterator iterator = languages.iterator(); iterator.hasNext();) {
            LanguageDTO languageDTO = (LanguageDTO) iterator.next();
            languageDTO.put("companyId", company.getCompanyId());
            Language language = languageHome.create(languageDTO);

            if (language.getLanguageIso().equals(favoriteLanguage)) {
                language.setIsDefault(new Boolean(true));
            } else {
                language.setIsDefault(new Boolean(false));
            }
            if (favoriteLanguage.equals(language.getLanguageIso())) {
                languageId = language.getLanguageId();
            }
        }

        /*Creating the default telecomtypes and administrator telecom email by default */
        createDefaultTelecomTypes(languageId, company, addressCompany, addressUser);

        log.debug("CompanyId:" + company.getCompanyId());
        log.debug("AddressCompanyId:" + addressCompany.getAddressId() + "CompanyID:" + addressCompany.getCompanyId());
        log.debug("AddressUSERId:" + addressUser.getAddressId() + " - CompanyID:" + addressUser.getCompanyId());

        // Create employee linked to ContactPerson
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.put("company", addressUser.getCompanyId());
        employeeDTO.put("employeeId", addressUser.getAddressId());
        EmployeeHome employeeHome = (EmployeeHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_EMPLOYEE);
        log.debug("Create Employee for User");
        employeeHome.create(employeeDTO);

        UserDTO userDTO = new UserDTO();
        userDTO.put("isDefaultUser", Boolean.TRUE);
        userDTO.put("userLogin", paramDTO.remove("userName"));
        userDTO.put("addressId", addressUser.getAddressId());
        userDTO.put("type", new Integer(1));
        userDTO.put("active", Boolean.TRUE);
        userDTO.put("companyId", company.getCompanyId());
        userDTO.put("favoriteLanguage", paramDTO.getAsString("favoriteLanguage"));
        userDTO.put("userPassword", EncryptUtil.i.encryt((String) paramDTO.get("userPassword")));
        userDTO.put("rowsPerPage", company.getRowsPerPage());
        userDTO.put("timeout", company.getTimeout());
        userDTO.put("maxRecentList", new Integer(10));
        if (null != company.getTimeZone() && !"".equals(company.getTimeZone().trim())) {
            userDTO.put("timeZone", company.getTimeZone());
        }

        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);

        log.debug("Create USER!!!!");
        log.debug("USERDTO:" + userDTO);
        User user = userHome.create(userDTO);

        /* Set the user email like notification user email */
        String userEmail = paramDTO.getAsString("email");
        user.setNotificationAppointmentEmail(userEmail);
        user.setNotificationQuestionEmail(userEmail);
        user.setNotificationSchedulerTaskEmail(userEmail);
        user.setNotificationSupportCaseEmail(userEmail);

        /**updaing recorduserid in addresses above created */
        addressCompany.setRecordUserId(user.getUserId());
        addressUser.setRecordUserId(user.getUserId());

        /**updating recorduserid for contactperson */
        contactPerson.setRecordUserId(user.getUserId());

        /**
         * Set additional values for some field of addresses
         */
        addressUser.setLastModificationDate(DateUtils.dateToInteger(new Date()));
        addressUser.setLastModificationUserId(user.getUserId());
        addressCompany.setLastModificationDate(addressUser.getLastModificationDate());
        addressCompany.setLastModificationUserId(addressUser.getLastModificationUserId());


        RoleDTO roleDTO = new RoleDTO();
        roleDTO.put("companyId", company.getCompanyId());
        roleDTO.put("roleName", "System root");
        roleDTO.put("isDefault", Boolean.TRUE);

        log.debug("Create Administrator ROLE" + roleDTO);
        RoleHome roleHome = (RoleHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_ROLE);
        Role role = roleHome.create(roleDTO);


        List modules = (List) paramDTO.get("modules");
        CompanyModuleHome companyModuleHome = (CompanyModuleHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_COMPANYMODULE);
        AccessRightsHome accessRightsHome = (AccessRightsHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_ACCESSRIGHTS);
        if (null != modules)
        // Creating companyModules for company
        {
            for (Iterator iterator = modules.iterator(); iterator.hasNext();) {
                String id = (String) iterator.next();

                for (Iterator iterator1 = paramDTO.keySet().iterator(); iterator1.hasNext();) {
                    String key = (String) iterator1.next();
                    //log.debug("COMMAND:KEY:" + key);
                    if (key.endsWith("_" + id)) {
                        Integer value = (Integer) paramDTO.get(key);
                        CompanyModule companyModule = companyModuleHome.create(company.getCompanyId(), new Integer(id), value);
                        companyModule.setActive(Boolean.TRUE);
                        for (Iterator funtions = companyModule.getSystemModule().getFunctions().iterator(); funtions.hasNext();) {
                            SystemFunction function = (SystemFunction) funtions.next();
                            if ("COMPANY".equals(function.getFunctionCode()) ||
                                    "APPLICATIONSIGNATURE".equals(function.getFunctionCode())) {
                                continue;
                            }

                            AccessRights accessRights = accessRightsHome.create(function.getFunctionId(), role.getRoleId(), companyModule.getModuleId(), companyModule.getCompanyId());
                            accessRights.setPermission(function.getPermissionsAllowed());
                        }
                    }
                }
            }
        }

        List users = new ArrayList();
        users.add(user);

        log.debug("Assign to user the Administrator ROL..");
        //role.setUsers(users);
        createUserRole(role, user.getUserId(), company.getCompanyId());

        //copy template company values
        copyCompanyTemplateValues(ctx, company.getCompanyId());

        //set values in result dto
        resultDTO.put("companyId", company.getCompanyId());
        resultDTO.put("rootUserId", user.getUserId());
    }

    private UserRole createUserRole(Role role, Integer userId, Integer companyId) {
        UserRoleHome home = (UserRoleHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USERROLE);
        UserRoleDTO dto = new UserRoleDTO();
        dto.put("roleId", role.getRoleId());
        dto.put("userId", userId);
        dto.put("companyId", companyId);
        try {
            log.debug("-> Assign roleId=" + role.getRoleId() + " to userId=" + userId + " OK");
            return home.create(dto);
        } catch (CreateException e) {
            log.debug("-> Assign roleId=" + role.getRoleId() + " to userId=" + userId + " FAIL", e);
        }
        return null;
    }

    private void copyCompanyTemplateValues(SessionContext ctx, Integer targetCompanyId) {
        log.debug("Copy catalogs configuration to new company...");
        Integer campaignTemplateId = null;
        if (null != paramDTO.get("templateCampaignId")
                && !"".equals(paramDTO.get("templateCampaignId").toString().trim())) {
            campaignTemplateId = new Integer(paramDTO.get("templateCampaignId").toString());
            CopyCatalogCmd cmd = new CopyCatalogCmd();
            cmd.putParam("sourceCompanyId", campaignTemplateId);
            cmd.putParam("targetCompanyId", targetCompanyId);
            cmd.executeInStateless(ctx);
        }
    }

    /**
     * Creating the default telecomtypes and administrator telecom email by default
     *
     * @param languageId
     * @param company
     * @param addressCompany
     * @param addressUser
     * @throws CreateException
     */
    private void createDefaultTelecomTypes(Integer languageId, Company company, Address addressCompany, Address addressUser) throws CreateException {
        LangTextHome langTextHome = (LangTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGTEXT);

        List defaultTelecomTypes = (List) paramDTO.get("defaultTelecomTypes");
        TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);
        for (Iterator iterator = defaultTelecomTypes.iterator(); iterator.hasNext();) {
            TelecomTypeDTO telecomTypeDTO = (TelecomTypeDTO) iterator.next();
            //Create langtext associated to telecomtype
            LangTextDTO newLangTextDTO = new LangTextDTO();
            newLangTextDTO.put("languageId", languageId);
            newLangTextDTO.put("companyId", company.getCompanyId());
            LangText langText = langTextHome.create(newLangTextDTO);
            langText.setText((String) telecomTypeDTO.get("telecomTypeName"));
            langText.setType(SystemLanguage.SYSTEM_TRANSLATION);
            langText.setIsDefault(Boolean.TRUE);

            telecomTypeDTO.put("companyId", company.getCompanyId());
            TelecomType telecomType = (TelecomType) EJBFactory.i.createEJB(telecomTypeDTO);
            telecomType.setLangTextId(langText.getLangTextId());

            if (com.piramide.elwis.utils.TelecomType.EMAIL_TYPE.equals(telecomType.getType())) { // Create Telecom email for address ContactPerson
                telecomHome.create(addressCompany.getAddressId(), addressUser.getAddressId(), paramDTO.getAsString("email"),
                        "", Boolean.TRUE, telecomType.getTelecomTypeId(), company.getCompanyId());
            }
        }
        //updating telecomtypestatus in Company table after creation of telecomtypes by default
        company.setTelecomTypeStatus(String.valueOf(System.currentTimeMillis()));
    }

    public boolean isStateful() {
        return false;
    }
}
