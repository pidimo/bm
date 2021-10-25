package com.piramide.elwis.web.contactmanager.el;

import com.piramide.elwis.cmd.admin.CompanyReadCmd;
import com.piramide.elwis.cmd.admin.UserGroupReadCmd;
import com.piramide.elwis.cmd.campaignmanager.GenerationCommunicationTextReadCmd;
import com.piramide.elwis.cmd.catalogmanager.TelecomTypeSelectCmd;
import com.piramide.elwis.cmd.contactmanager.*;
import com.piramide.elwis.dto.admin.UserGroupDTO;
import com.piramide.elwis.dto.catalogmanager.TelecomTypeDTO;
import com.piramide.elwis.dto.contactmanager.*;
import com.piramide.elwis.utils.*;
import com.piramide.elwis.utils.deduplication.DeduplicationItemWrapper;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.FantabulousUtil;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import com.piramide.elwis.web.contactmanager.delegate.dataimport.DataImportDelegate;
import com.piramide.elwis.web.contactmanager.el.deduplication.DeduplicationHelper;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.alfacentauro.fantabulous.web.action.FantabulousAction;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.util.LabelValueBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;
import java.util.*;

/**
 * Contact manager jstl functions
 *
 * @author Fernando Montaño
 * @version $Id: Functions.java 12586 2016-09-07 20:54:40Z miguel $
 */

public class Functions {

    private static Log log = LogFactory.getLog(Functions.class);

    public static List getTelecomTypes(javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        TelecomTypeSelectCmd cmd = new TelecomTypeSelectCmd();
        cmd.putParam(TelecomTypeSelectCmd.SELECT_TYPE, TelecomTypeSelectCmd.TYPE_LIST);
        cmd.putParam("companyId", user.getValue("companyId"));
        cmd.putParam(TelecomTypeSelectCmd.ISO_LANGUAGE, user.getValue("locale"));
        try {
            ResultDTO resultDto = BusinessDelegate.i.execute(cmd, request);
            List result = (LinkedList) resultDto.get(TelecomTypeSelectCmd.RESULT);
            ArrayList selectList = new ArrayList();
            for (Iterator iterator = result.iterator(); iterator.hasNext();) {
                TelecomTypeDTO dto = (TelecomTypeDTO) iterator.next();
                selectList.add(new LabelValueBean(String.valueOf(dto.get("telecomTypeName")),
                        String.valueOf(dto.get("telecomTypeId"))));
            }
            return selectList;

        } catch (AppLevelException e) {
            log.error("Error executing", e);
        }
        return new ArrayList();

    }

    public static void sortTelecomsByPosition(Map telecoms) {
        if (telecoms != null) {
            TelecomWrapperDTO.sortTelecomMapByPosition(telecoms);
        }

    }

    public static TelecomDTO findDefaultEmailTelecom(Integer addressId, Integer contactPersonId) {
        TelecomDTO telecomDTO = null;

        TelecomUtilCmd telecomUtilCmd = new TelecomUtilCmd();
        telecomUtilCmd.setOp("defaultEmailTelecom");
        telecomUtilCmd.putParam("addressId", addressId);
        if (contactPersonId != null) {
            telecomUtilCmd.putParam("contactPersonId", contactPersonId);
        }

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(telecomUtilCmd, null);
            if (resultDTO.containsKey("defaultTelecomDTO")) {
                telecomDTO = (TelecomDTO) resultDTO.get("defaultTelecomDTO");
            }
        } catch (AppLevelException e) {
            log.error("Error executing TelecomUtilCmd cmd", e);
        }

        return telecomDTO;
    }

    /**
     * This function returns the formatted address complete name for a person or organization
     * if the address is not found, it returns an empty string
     *
     * @param addressId the address id to get its name
     * @return the fomatted address name
     */
    public static String getAddressName(Object addressId) {
        LightlyAddressCmd addressCmd = new LightlyAddressCmd();
        addressCmd.putParam("addressId", addressId);
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(addressCmd, null);
            return (String) resultDTO.get("addressName");
        } catch (Exception e) {
            log.warn("Address was not found");
            return "";
        }
    }

    /**
     * Get light address info as Map
     * @param addressId
     * @return Map
     */
    public static Map getAddressMap(Object addressId) {
        Map addressMap = new HashMap();
        if (addressId != null) {
            LightlyAddressCmd addressCmd = new LightlyAddressCmd();
            addressCmd.putParam("addressId", addressId);
            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(addressCmd, null);
                if (!resultDTO.isFailure()) {
                    addressMap.put("addressId", addressId);
                    addressMap.put("addressType", resultDTO.get("addressType"));
                    addressMap.put("name1", resultDTO.get("name1"));
                    addressMap.put("name2", resultDTO.get("name2"));
                    addressMap.put("name3", resultDTO.get("name3"));
                    addressMap.put("addressName", resultDTO.get("addressName"));
                    addressMap.put("recordDate", resultDTO.get("recordDate"));
                    addressMap.put("recordUserId", resultDTO.get("recordUserId"));
                    addressMap.put("lastModificationDate", resultDTO.get("lastModificationDate"));
                    addressMap.put("lastModificationUserId", resultDTO.get("lastModificationUserId"));
                    addressMap.put("titleName", resultDTO.get("titleName"));
                    addressMap.put("languageIso", resultDTO.get("languageIso"));

                    addressMap.put("countryCode", resultDTO.get("countryCode"));
                    addressMap.put("countryName", resultDTO.get("countryName"));
                    addressMap.put("zip", resultDTO.get("zip"));
                    addressMap.put("city", resultDTO.get("city"));
                    addressMap.put("street", resultDTO.get("street"));
                    addressMap.put("houseNumber", resultDTO.get("houseNumber"));
                    addressMap.put("note", resultDTO.get("note"));
                    addressMap.put("imageId", resultDTO.get("imageId"));
                }
            } catch (Exception e) {
                log.warn("Address was not found");
            }
        }
        return addressMap;
    }

    public static Map getAddressTelecomsMap(Object addressId) {
        Map telecomsMap = new HashMap();
        if (addressId != null) {
            TelecomUtilCmd telecomUtilCmd = new TelecomUtilCmd();
            telecomUtilCmd.setOp("readAddressTelecom");
            telecomUtilCmd.putParam("addressId", addressId);
            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(telecomUtilCmd, null);
                if (resultDTO.get("addressTelecomInfoMap") != null) {
                    telecomsMap = (Map) resultDTO.get("addressTelecomInfoMap");
                }
            } catch (Exception e) {
                log.warn("Error in execute cmd TelecomUtilCmd..", e);
            }
        }
        return telecomsMap;
    }

    public static TelecomDTO findDefaultTelecomByTelecomType(Integer addressId, Integer contactPersonId, Integer telecomTypeId) {
        TelecomDTO telecomDTO = null;

        if (addressId != null && telecomTypeId != null) {
            TelecomUtilCmd telecomUtilCmd = new TelecomUtilCmd();
            telecomUtilCmd.setOp("defaultTelecomByType");
            telecomUtilCmd.putParam("telecomTypeId", telecomTypeId);
            telecomUtilCmd.putParam("addressId", addressId);
            if (contactPersonId != null) {
                telecomUtilCmd.putParam("contactPersonId", contactPersonId);
            }

            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(telecomUtilCmd, null);
                if (resultDTO.containsKey("telecomDTOByType")) {
                    telecomDTO = (TelecomDTO) resultDTO.get("telecomDTOByType");
                }
            } catch (AppLevelException e) {
                log.error("Error executing TelecomUtilCmd cmd...", e);
            }
        }

        return telecomDTO;
    }

    public static CustomerDTO getCustomer(Integer addressId,
                                          javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        LightlyCustomerCmd lightlyCustomerCmd = new LightlyCustomerCmd();
        lightlyCustomerCmd.putParam("addressId", addressId);
        lightlyCustomerCmd.setOp("getCustomer");
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(lightlyCustomerCmd, request);
            CustomerDTO customerDTO = (CustomerDTO) resultDTO.get("getCustomer");
            if (null != customerDTO) {
                return customerDTO;
            }
        } catch (AppLevelException e) {
            log.error("-> Execute " + LightlyCustomerCmd.class.getName() + " FAIL", e);
        }
        return null;
    }

    public static CompanyDTO getCompanyConfiguration(javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        User user = RequestUtils.getUser(request);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

        CompanyCmd companyCmd = new CompanyCmd();
        companyCmd.putParam("addressId", companyId);
        companyCmd.setOp("read");
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(companyCmd, request);
            CompanyDTO companyDTO = new CompanyDTO();
            companyDTO.put("invoiceDays", resultDTO.get("invoiceDays"));
            companyDTO.put("sequenceRuleIdForCreditNote", resultDTO.get("sequenceRuleIdForCreditNote"));
            companyDTO.put("sequenceRuleIdForInvoice", resultDTO.get("sequenceRuleIdForInvoice"));
            companyDTO.put("netGross", resultDTO.get("netGross"));
            companyDTO.put("mediaType", resultDTO.get("mediaType"));
            return companyDTO;
        } catch (AppLevelException e) {
            log.error("-> Execute " + CompanyCmd.class.getName() + " FAIL", e);
        }
        return new CompanyDTO();
    }

    public static boolean existsAddress(Object addressId) {
        ActionErrors errors = new ActionErrors();
        errors = ForeignkeyValidator.i.validate(
                ContactConstants.TABLE_ADDRESS,
                "addressid",
                addressId,
                errors, new ActionError("Address.NotFound"));
        return errors.isEmpty();
    }

    public static List getImportTypes(javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        List<LabelValueBean> importTypes = new ArrayList<LabelValueBean>();
        importTypes.add(
                new LabelValueBean(JSPHelper.getMessage(request, "dataImport.type.Organization"),
                        ContactConstants.ImportProfileType.ORGANIZATION.getConstant().toString())
        );
        importTypes.add(
                new LabelValueBean(JSPHelper.getMessage(request, "dataImport.type.Person"),
                        ContactConstants.ImportProfileType.PERSON.getConstant().toString())
        );

        importTypes.add(
                new LabelValueBean(JSPHelper.getMessage(request, "dataImport.type.OrganizationAndContactPerson"),
                        ContactConstants.ImportProfileType.ORGANIZATION_AND_CONTACT_PERSON.getConstant().toString())
        );
        return importTypes;
    }

    public static void setDataImportValuesFromAjaxRequest(PageContext pageContext,
                                                          javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        String position = request.getParameter("position");
        String dtoKey = request.getParameter("dtoKey");
        String columnName = request.getParameter("columnName");
        String groupName = request.getParameter("groupName");

        request.setAttribute("dtoKey", dtoKey);
        request.setAttribute("columnName", columnName);
        request.setAttribute("groupName", groupName);
        request.setAttribute("position", position);

        DefaultForm defaultForm = new DefaultForm();
        defaultForm.setDto("dtoKey_" + dtoKey, position);

        pageContext.setAttribute("org.apache.struts.taglib.html.BEAN", defaultForm, 2);
    }

    /**
     * Get communication document info, like freetext id and an flag to verify if document is from campaign generation
     *
     * @param contactId id
     * @return Map with keys:{freeTextId, isfromCampaigGeneration}
     */
    public static Map getCommunicationDocumentInfo(String contactId) {
        Map documentInfoMap = new HashMap();

        if (!GenericValidator.isBlankOrNull(contactId)) {
            //firts verify if communication is from campaign generation
            boolean isCampaignGenCommunication = com.piramide.elwis.web.campaignmanager.el.Functions.isCampaignGenerationCommunication(contactId);
            try {
                if (isCampaignGenCommunication) {
                    GenerationCommunicationTextReadCmd templateTextReadCmd = new GenerationCommunicationTextReadCmd();
                    templateTextReadCmd.putParam("contactId", contactId);
                    ResultDTO resultDTO = BusinessDelegate.i.execute(templateTextReadCmd, null);
                    if (resultDTO.get("fid") != null) {
                        documentInfoMap.put("freeTextId", resultDTO.get("fid"));
                    }

                } else {
                    ContactDocumentInfoCmd documentInfoCmd = new ContactDocumentInfoCmd();
                    documentInfoCmd.putParam("contactId", contactId);
                    ResultDTO resultDTO = BusinessDelegate.i.execute(documentInfoCmd, null);
                    if (Boolean.valueOf(resultDTO.get("contactHasDocument").toString())) {
                        documentInfoMap.put("freeTextId", resultDTO.get("contactFreeTextId"));
                    }
                }
            } catch (AppLevelException e) {
                log.debug("Error in execute cmd..." + ContactDocumentInfoCmd.class);
            }

            documentInfoMap.put("isfromCampaigGeneration", isCampaignGenCommunication);
        }
        return documentInfoMap;
    }

    /**
     * Verify if communication has generated document
     *
     * @param contactId id
     * @return true or false
     */
    public static boolean communicationHasDocument(String contactId) {
        Map documentInfoMap = getCommunicationDocumentInfo(contactId);
        return documentInfoMap.get("freeTextId") != null;
    }

    public static void setDefaultCommunicationAdditionalAddress(DefaultForm defaultForm, HttpServletRequest request) {
        Object addressIdObj = defaultForm.getDto("addressId");

        if (addressIdObj != null && !GenericValidator.isBlankOrNull(addressIdObj.toString())) {
            AdditionalAddressDTO additionalAddressDTO = findDefaulaAdditionalAddressDTO(new Integer(addressIdObj.toString()), request);
            if (additionalAddressDTO != null) {
                defaultForm.setDto("additionalAddressId", additionalAddressDTO.get("additionalAddressId"));
            }
        }
    }

    private static AdditionalAddressDTO findDefaulaAdditionalAddressDTO(Integer addressId, HttpServletRequest request) {
        AdditionalAddressDTO additionalAddressDTO = null;

        if (addressId != null) {
            User user = RequestUtils.getUser(request);
            Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

            AdditionalAddressCmd additionalAddressCmd = new AdditionalAddressCmd();
            additionalAddressCmd.setOp("findDefaultAddAddress");
            additionalAddressCmd.putParam("addressId", addressId);
            additionalAddressCmd.putParam("companyId", companyId);

            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(additionalAddressCmd, request);
                if (resultDTO.get("defaultAddAddressDTO") != null) {
                    additionalAddressDTO = (AdditionalAddressDTO) resultDTO.get("defaultAddAddressDTO");
                }
            } catch (AppLevelException e) {
                log.error("-> Execute " + AdditionalAddressCmd.class.getName() + " FAIL", e);
            }
        }

        return additionalAddressDTO;
    }

    public static boolean isCustomer(String code) {
        return CodeUtil.isCustomer(code);
    }

    public static boolean isSupplier(String code) {
        return CodeUtil.isSupplier(code);
    }

    public static boolean customerHasRelations(Integer addressId) {
        CustomerDTO dto = new CustomerDTO();
        dto.setPrimKey(addressId);
        ResultDTO resultDTO = new ResultDTO();
        IntegrityReferentialChecker.i.check(dto, resultDTO);

        return resultDTO.isFailure();
    }

    public static boolean supplierHasRelations(Integer supplierId) {
        SupplierDTO dto = new SupplierDTO();
        dto.setPrimKey(supplierId);
        ResultDTO resultDTO = new ResultDTO();
        IntegrityReferentialChecker.i.check(dto, resultDTO);

        return resultDTO.isFailure();
    }

    /**
     * Execute the <code>CompanyReadCmd</code> with operation = 'getSystemModules' to read the system modules
     *
     * @param servletRequest page ServletRequest object
     * @return a <code>List</code> of <code>Map</code> object when every <code>Map</code>
     *         contains the module resource key and the module identifier.
     */
    public static List<Map> getSystemModulesMap(javax.servlet.ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        CompanyReadCmd companyReadCmd = new CompanyReadCmd();
        companyReadCmd.setOp("getSystemModules");

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(companyReadCmd, request);
            return (List<Map>) resultDTO.get("getSystemModules");
        } catch (AppLevelException e) {
            log.error("Cannot execute CompanyReadCmd. ", e);
        }
        return new ArrayList<Map>();
    }

    /**
     * Verify if is there registered only one bank account in company
     * and get the bank account id
     * @param request
     * @return
     */
    public static Integer getOnlyOneCompanyBankAccountId(HttpServletRequest request) {
        Integer bankAccountId = null;

        User user = RequestUtils.getUser(request);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

        BankAccountCmd bankAccountCmd = new BankAccountCmd();
        bankAccountCmd.setOp("isOnlyOneAccount");
        bankAccountCmd.putParam("addressId", companyId);
        bankAccountCmd.putParam("companyId", companyId);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(bankAccountCmd, request);
            if (resultDTO.get("bankAccountId") != null) {
                bankAccountId = (Integer) resultDTO.get("bankAccountId");
            }
        } catch (AppLevelException e) {
            log.error("-> Execute " + BankAccountCmd.class.getName() + " FAIL", e);
        }
        return bankAccountId;
    }

    public static void buildAddressUserGroupAccessRightValues(String userGroupIds, HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

        UserGroupReadCmd userGroupReadCmd = new UserGroupReadCmd();
        userGroupReadCmd.setOp("userGroupDataAccess");
        userGroupReadCmd.putParam("companyId", companyId);

        List<UserGroupDTO> companyUserGroupsList = new ArrayList<UserGroupDTO>();
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(userGroupReadCmd, request);
            if (resultDTO.get("companyUserGroups") != null) {
                companyUserGroupsList = (List<UserGroupDTO>) resultDTO.get("companyUserGroups");
            }
        } catch (AppLevelException e) {
            log.error("-> Execute " + UserGroupReadCmd.class.getName() + " FAIL", e);
        }

        //process the selected user groups
        String[] userGroupIdArray = new String[0];
        if (userGroupIds != null) {
            userGroupIdArray = userGroupIds.split(",");
        }

        ArrayList<LabelValueBean> selectedList = new ArrayList<LabelValueBean>();
        ArrayList<LabelValueBean> availableList = new ArrayList<LabelValueBean>();

        for (UserGroupDTO userGroupDTO : companyUserGroupsList) {
            String userGroupId = userGroupDTO.get("userGroupId").toString();
            LabelValueBean labelValueBean = new LabelValueBean(userGroupDTO.get("groupName").toString(), userGroupId);

            boolean isSelected = existItemInArray(userGroupIdArray, userGroupId);
            if (isSelected) {
                selectedList.add(labelValueBean);
            } else {
                availableList.add(labelValueBean);
            }
        }

        //add creator user item
        LabelValueBean creatorLabelValueBean = new LabelValueBean(JSPHelper.getMessage(request, "Contact.accessRight.creatorUser"), ContactConstants.CREATORUSER_ACCESSRIGHT.toString());
        boolean isCreatorSelected = existItemInArray(userGroupIdArray, ContactConstants.CREATORUSER_ACCESSRIGHT.toString());
        if (isCreatorSelected) {
            selectedList.add(creatorLabelValueBean);
        } else {
            availableList.add(creatorLabelValueBean);
        }

        request.setAttribute("selectedUserGroupList", SortUtils.orderByProperty(selectedList, "label"));
        request.setAttribute("availableUserGroupList", SortUtils.orderByProperty(availableList, "label"));
    }

    private static boolean existItemInArray(String[] array, String value) {
        if (value != null) {
            for (String arrayValue : array) {
                if (value.equals(arrayValue)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean existsDataImport(Object keyValue) {
        ActionErrors errors = new ActionErrors();
        errors = ForeignkeyValidator.i.validate(ContactConstants.TABLE_IMPORTPROFILE, "profileid", keyValue, errors, new ActionError("DataImport.NotFound"));
        return errors.isEmpty();
    }

    public static boolean existsImportRecord(Object keyValue) {
        ActionErrors errors = new ActionErrors();
        errors = ForeignkeyValidator.i.validate(ContactConstants.TABLE_IMPORTRECORD, "importrecordid", keyValue, errors, new ActionError("ImportRecord.NotFound"));
        return errors.isEmpty();
    }

    public static boolean existsDuplicateGroup(Object keyValue) {
        ActionErrors errors = new ActionErrors();
        errors = ForeignkeyValidator.i.validate(ContactConstants.TABLE_DUPLICATEGROUP, "duplicategroupid", keyValue, errors, new ActionError("DedupliContact.duplicate.notFound"));
        return errors.isEmpty();
    }

    public static ImportProfileDTO getImportProfileDTO(String profileId) {
        ImportProfileDTO importProfileDTO = new ImportProfileDTO();
        if (!GenericValidator.isBlankOrNull(profileId)) {
            importProfileDTO = DataImportDelegate.i.getImportProfileDTO(new Integer(profileId));
        }
        return importProfileDTO;
    }

    public static List<DeduplicationItemWrapper> buildHeaderProfileColumns(String profileId, HttpServletRequest request) {
        List<DeduplicationItemWrapper> result = new ArrayList<DeduplicationItemWrapper>();
        if (!GenericValidator.isBlankOrNull(profileId)) {
            DeduplicationHelper deduplicationHelper = new DeduplicationHelper();
            result = deduplicationHelper.buildHeaderProfileColumns(Integer.valueOf(profileId), request);
        }
        return result;
    }

    public static List<DeduplicationItemWrapper> buildHeaderProfileContactPersonColumns(String profileId, HttpServletRequest request) {
        List<DeduplicationItemWrapper> result = new ArrayList<DeduplicationItemWrapper>();
        if (!GenericValidator.isBlankOrNull(profileId)) {
            DeduplicationHelper deduplicationHelper = new DeduplicationHelper(true);
            result = deduplicationHelper.buildHeaderProfileColumns(Integer.valueOf(profileId), request);
        }
        return result;
    }

    public static List<Map> buildImportDeduplicationItemValues(Integer importRecordId, Integer profileId, HttpServletRequest request) {
        DeduplicationHelper deduplicationHelper = new DeduplicationHelper();
        return deduplicationHelper.buildImportDeduplicationItemValues(Integer.valueOf(importRecordId), Integer.valueOf(profileId), request);
    }

    public static List<Map> buildImportContactPersonDeduplicationItemValues(Integer importRecordId, Integer profileId, Integer organizationId, HttpServletRequest request) {
        DeduplicationHelper deduplicationHelper = new DeduplicationHelper(organizationId);
        return deduplicationHelper.buildImportDeduplicationItemValues(Integer.valueOf(importRecordId), Integer.valueOf(profileId), request);
    }

    public static List<LabelValueBean> getImportMergeValuesList(Integer importRecordId, HttpServletRequest request) {
        DeduplicationHelper deduplicationHelper = new DeduplicationHelper();
        return deduplicationHelper.getImportMergeValuesList(Integer.valueOf(importRecordId), request);
    }

    public static List<DeduplicationItemWrapper> buildHeaderContactDeduplicationColumns(HttpServletRequest request) {
        DeduplicationHelper deduplicationHelper = new DeduplicationHelper();
        return deduplicationHelper.buildHeaderContactColumns(request);
    }

    public static List<Map> buildContactDeduplicationItemValues(Integer duplicateGroupId, HttpServletRequest request) {
        DeduplicationHelper deduplicationHelper = new DeduplicationHelper();
        return deduplicationHelper.buildContactDeduplicationItemValues(duplicateGroupId, request);
    }

    public static List<LabelValueBean> getContactMergeSelectValuesList(Integer duplicateGroupId, HttpServletRequest request) {
        List<LabelValueBean> result = new ArrayList<LabelValueBean>();
        if (duplicateGroupId != null) {
            DeduplicationHelper deduplicationHelper = new DeduplicationHelper();
            result = deduplicationHelper.getContactMergeSelectValues(duplicateGroupId, request);
        }
        return result;
    }

    public static boolean isContactMergeSelectedItem(String selectValue, String uiKey) {
        DeduplicationHelper deduplicationHelper = new DeduplicationHelper();
        return deduplicationHelper.isContactMergeSelectedItem(selectValue, uiKey);
    }

    /**
     * Add fantabulous filter to contact search name. Fix the "," separator
     * @param parameterProperty parameter name
     * @param listForm form
     * @param listAction action
     */
    public static void addContactSearchNameFixedFilter(String parameterProperty, SearchForm listForm, FantabulousAction listAction) {
        Object name = listForm.getParameter(parameterProperty);
        if (name != null) {
            String namefilter = name.toString().replaceAll(",", "%");
            //add as filter and to save in persistence
            listForm.setParameter("contactSearchNameFixed",namefilter);
        }
    }

    public static boolean importRecordFixedHasDuplicateContactPerson(Integer importRecordId) {
        return DataImportDelegate.i.importRecordFixedHasDuplicateContactPerson(importRecordId);
    }

    public static Integer countCompanyAddress(HttpServletRequest request) {
        Integer resultCount = 0;

        User user = RequestUtils.getUser(request);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);
        String listName = "addressCountList";

        FantabulousUtil fantabulousUtil = new FantabulousUtil();
        fantabulousUtil.setModule("/contacts");
        fantabulousUtil.addSearchParameter("companyId", companyId.toString());

        List<Map> resultSetList = fantabulousUtil.getData(request, listName);
        for (Map resultSetMap : resultSetList) {
            String counterValue = (String) resultSetMap.get("counter");
            if (!GenericValidator.isBlankOrNull(counterValue)) {
                try {
                    resultCount = Integer.valueOf(counterValue);
                } catch (NumberFormatException ignore) {
                }
            }
        }
        log.debug("total address in company:" + resultCount);
        return resultCount;
    }

    public static void buildDashboardBirthdayEmployeeViewValues(String employeeIds, HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

        EmployeeUtilCmd employeeUtilCmd = new EmployeeUtilCmd();
        employeeUtilCmd.setOp("companyEmployee");
        employeeUtilCmd.putParam("companyId", companyId);

        List<EmployeeDTO> companyEmployeeList = new ArrayList<EmployeeDTO>();
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(employeeUtilCmd, request);
            if (resultDTO.get("companyEmployees") != null) {
                companyEmployeeList = (List<EmployeeDTO>) resultDTO.get("companyEmployees");
            }
        } catch (AppLevelException e) {
            log.error("-> Execute " + EmployeeUtilCmd.class.getName() + " FAIL", e);
        }

        //process the selected
        String[] employeeIdArray = new String[0];
        if (employeeIds != null) {
            employeeIdArray = employeeIds.split(",");
        }

        ArrayList<LabelValueBean> selectedList = new ArrayList<LabelValueBean>();
        ArrayList<LabelValueBean> availableList = new ArrayList<LabelValueBean>();

        for (EmployeeDTO employeeDTO : companyEmployeeList) {
            String employeeId = employeeDTO.get("employeeId").toString();
            LabelValueBean labelValueBean = new LabelValueBean(employeeDTO.get("name").toString(), employeeId);

            boolean isSelected = existItemInArray(employeeIdArray, employeeId);
            if (isSelected) {
                selectedList.add(labelValueBean);
            } else {
                availableList.add(labelValueBean);
            }
        }

        request.setAttribute("selectedEmployeeList", SortUtils.orderByProperty(selectedList, "label"));
        request.setAttribute("availableEmployeeList", SortUtils.orderByProperty(availableList, "label"));
    }

    public static List getCustomerInvoiceShippingList(HttpServletRequest request) {
        ArrayList list = new ArrayList();

        list.add(new LabelValueBean(JSPHelper.getMessage(request, ContactConstants.CustomerInvoiceShipping.VIA_EMAIL.getResource()), ContactConstants.CustomerInvoiceShipping.VIA_EMAIL.getConstantAsString()));
        list.add(new LabelValueBean(JSPHelper.getMessage(request, ContactConstants.CustomerInvoiceShipping.VIA_LETTER.getResource()), ContactConstants.CustomerInvoiceShipping.VIA_LETTER.getConstantAsString()));
        return list;
    }


}

