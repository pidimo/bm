package com.piramide.elwis.web.contactmanager.form;

import com.piramide.elwis.cmd.contactmanager.AddressDuplicatedCmd;
import com.piramide.elwis.cmd.contactmanager.LightlyAddressCmd;
import com.piramide.elwis.cmd.contactmanager.ZipCityCmd;
import com.piramide.elwis.dto.catalogmanager.CityDTO;
import com.piramide.elwis.dto.contactmanager.CustomerDTO;
import com.piramide.elwis.dto.contactmanager.SupplierDTO;
import com.piramide.elwis.dto.contactmanager.TelecomWrapperDTO;
import com.piramide.elwis.utils.*;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.catalogmanager.form.CategoryFieldValueForm;
import com.piramide.elwis.web.catalogmanager.form.CategoryFormUtil;
import com.piramide.elwis.web.common.el.Functions;
import com.piramide.elwis.web.common.util.JSPHelper;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.upload.FormFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Address Form handler. Manages forms to Person and Organization address Types.
 *
 * @author Ernesto
 * @version $Id: AddressForm.java 12635 2017-01-31 21:28:46Z miguel $
 */
public class AddressForm extends DefaultForm {
    // the logger
    private Log log = LogFactory.getLog(AddressForm.class);
    private Map telecomMap;


    private String countryId;
    private String cityId;
    private String zip;
    private String city;
    private String beforeZip;

    private String confirmDuplicatedCreation;
    private FormFile imageFile;

    public AddressForm() {
        super();
        telecomMap = new LinkedHashMap();
    }


    public FormFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(FormFile imageFile) {
        this.imageFile = imageFile;
    }

    public TelecomWrapperDTO getTelecom(String key) {
        if (telecomMap.containsKey(key)) {
            return (TelecomWrapperDTO) telecomMap.get(key);
        } else {
            telecomMap.put(key, new TelecomWrapperDTO());
        }
        return (TelecomWrapperDTO) telecomMap.get(key);

    }

    public void setTelecom(String key, TelecomWrapperDTO value) {
        telecomMap.put(key, value);
    }


    public Map getTelecomMap() {
        return telecomMap;
    }

    public void setTelecomMap(Map telecomMap) {
        this.telecomMap = telecomMap;
    }

    public boolean getInitTelecoms() {
        Object telecomMap = super.getDto("telecomMap");
        if (telecomMap != null) {
            this.telecomMap = (Map) telecomMap;
        }
        TelecomWrapperDTO.sortTelecomMapByPosition(this.telecomMap);
        return true;
    }

    public String getCountryId() {
        if (getDto("countryId") != null) {
            return getDto("countryId").toString();
        } else {
            return "";
        }

    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
        super.setDto("countryId", this.countryId);
    }

    public String getCityId() {
        if (getDto("cityId") != null) {
            return getDto("cityId").toString();
        } else {
            return "";
        }
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
        super.setDto("cityId", this.cityId);
    }

    public String getZip() {
        if (getDto("zip") != null) {
            return getDto("zip").toString();
        } else {
            return "";
        }
    }

    public void setZip(String zip) {
        this.zip = zip;
        super.setDto("zip", this.zip);
    }

    public String getBeforeZip() {
        if (getDto("beforeZip") != null) {
            return getDto("beforeZip").toString();
        } else {
            return "";
        }
    }

    public void setBeforeZip(String beforeZip) {
        this.beforeZip = beforeZip;
        super.setDto("beforeZip", this.beforeZip);
    }

    public String getCity() {
        if (getDto("city") != null) {
            return getDto("city").toString();
        } else {
            return "";
        }
    }

    public String getConfirmDuplicatedCreation() {
        return confirmDuplicatedCreation;
    }

    public void setConfirmDuplicatedCreation(String confirmDuplicatedCreation) {
        this.confirmDuplicatedCreation = confirmDuplicatedCreation;
    }

    public void setCity(String city) {
        this.city = city;
        super.setDto("city", this.city);
    }


    /**
     * Validate the input fields and set defaults values to dtoMap.
     */

    public ActionErrors validate(org.apache.struts.action.ActionMapping mapping,
                                 javax.servlet.http.HttpServletRequest request) {
        log.debug("AddressForm validation execution...");
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        super.setDto("locale", user.getValue("locale"));

        String isSupplier = (String) super.getDto("isSupplier");
        String isCustomer = (String) super.getDto("isCustomer");
        String isCompany = (String) super.getDto("isCompany");

        byte code = CodeUtil.default_;
        if (isSupplier != null) {
            code += CodeUtil.supplier;
        }
        if (isCustomer != null) {
            code += CodeUtil.customer;
        }

        if (isCompany != null) //only if is logged company
        {
            code += CodeUtil.company;
        }

        super.setDto("code", new Byte(code).toString());

        if (super.getDto("isActive") != null) {
            super.setDto("active", new Boolean(true));
        } else {
            super.setDto("active", new Boolean(false));
        }

        //way description
        String wayDescription = (String) super.getDto("wayDescription");
        if (GenericValidator.isBlankOrNull(wayDescription)) {
            super.setDto("wayDescription", null);
        }


        if (getDto("lastUpdateDate") != null) { // if is in update mode send It.
            super.setDto("lastUpdateDate", new Date());
        }

        ActionErrors errors = new ActionErrors();
        //validating if save has been pressed
        if (getDto("save") != null) {

            errors = super.validate(mapping, request);// validating with super class

            // validating name1 (is required)
            if (ContactConstants.ADDRESSTYPE_PERSON.equals(getDto("addressType"))) {
                if (getDto("name1") == null || "".equals(getDto("name1").toString().trim())) {
                    errors.add("name1", new ActionError("errors.required",
                            JSPHelper.getMessage(request, "Contact.Person.lastname")));
                }

            } else if (ContactConstants.ADDRESSTYPE_ORGANIZATION.equals(getDto("addressType"))) {
                if (getDto("name1") == null || "".equals(getDto("name1").toString().trim())) {
                    errors.add("name1", new ActionError("errors.required", JSPHelper.getMessage(request,
                            "Contact.Organization.name")));
                }
            }

            // validating the Date
            Date aDate = null;
            String datePattern = JSPHelper.getMessage(request, "datePattern");
            String withoutYearPattern = JSPHelper.getMessage(request, "withoutYearPattern");
            String dateString = (String) getDto("birthday");
            boolean isWithOutYear = false;
            if (!GenericValidator.isBlankOrNull(dateString)) {
                try {
                    if (dateString.length() < datePattern.trim().length()) {
                        aDate = DateUtils.formatDate(dateString, withoutYearPattern.trim(), true);
                        isWithOutYear = true;
                    } else {
                        aDate = DateUtils.formatDate(dateString, datePattern.trim(), false);
                    }


                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }

                if (aDate == null) {
                    if (ContactConstants.ADDRESSTYPE_PERSON.equals(getDto("addressType"))) {
                        errors.add("dateText", new ActionError("Address.error.Person.birthday", datePattern,
                                withoutYearPattern));
                    } else if (ContactConstants.ADDRESSTYPE_ORGANIZATION.equals(getDto("addressType"))) {
                        errors.add("dateText", new ActionError("Address.error.Organization.foundation", datePattern,
                                withoutYearPattern));
                    }

                } else {
                    if (isWithOutYear) {
                        super.setDto("birthday", DateUtils.dateToIntegerWithoutYear(aDate));
                        super.setDto("dateWithoutYear", "true"); //define as without year for when a jsp be invoked again.
                    } else {
                        super.setDto("birthday", DateUtils.dateToInteger(aDate));
                    }

                }

            }

            // ZIP/CITY MANAGEMENT
            // if zip is not empty or city is not empty, but country is not defined.
            if ((!GenericValidator.isBlankOrNull(this.getZip()) || !GenericValidator.isBlankOrNull(this.getCity())) &&
                    GenericValidator.isBlankOrNull(this.getCountryId())) {
                log.debug("Cannot define a city without country...");
                errors.add("city", new ActionError("Address.error.cityWithoutCountry"));
            }

            // Search the city with only country and city name, if found show it, else open window to create it.
            if (!GenericValidator.isBlankOrNull(this.getCountryId()) && !GenericValidator.isBlankOrNull(this.getCity())
                    && GenericValidator.isBlankOrNull(this.getZip())) {
                log.debug("Only define the city name without zip code ");
                try {
                    ZipCityCmd zipCmd = new ZipCityCmd(); // the command to execute
                    zipCmd.putParam("countryId", this.getCountryId());
                    zipCmd.putParam("city", this.getCity());
                    zipCmd.putParam("companyId", getDto("companyId"));
                    zipCmd.putParam("withoutZip", new Boolean(true));
                    ResultDTO resultDTO = BusinessDelegate.i.execute(zipCmd, request);
                    if (resultDTO.get("cityId") != null) { //city by country and name has been found
                        this.setCityId(resultDTO.get("cityId").toString());
                        this.setCity(resultDTO.get("city").toString());
                    } else { //city by country and name not found, open window  to create it.
                        StringBuffer openUrl = new StringBuffer();
                        openUrl.append("onload=\"")
                                .append("openCitySearch('countryId', 'cityId', 'zipId', 'cityNameId', 'beforeZipId', 'notFound', 'iframeId', 'false')\"");

                        request.setAttribute("jsLoad", new String(openUrl));
                        if (errors.isEmpty()) {

                            errors.add("citySearch", new ActionError("Address.error.enteredCityNotFound"));
                        }

                    }

                } catch (AppLevelException e) {
                    log.error("Error searching the city name= " + getDto("city"), e);
                }

            }

            // Search the city with zip, cityName and country, if found some show it, else open window to create it
            //  or search for another.
            if (!GenericValidator.isBlankOrNull(this.getCountryId()) && !GenericValidator.isBlankOrNull(this.getCity())
                    && !GenericValidator.isBlankOrNull(this.getZip())) {
                log.debug("Searching with country, zip and city defined");
                try {
                    ZipCityCmd zipCmd = new ZipCityCmd(); // the command to execute
                    zipCmd.putParam("countryId", this.getCountryId());
                    zipCmd.putParam("zip", getDto("zip"));
                    zipCmd.putParam("city", this.getCity());
                    zipCmd.putParam("companyId", getDto("companyId"));
                    zipCmd.putParam("withZipAndCity", new Boolean(true));
                    ResultDTO resultDTO = BusinessDelegate.i.execute(zipCmd, request);
                    if (resultDTO.get("cityId") != null) { //city found
                        this.setCityId(resultDTO.get("cityId").toString());
                        this.setZip(resultDTO.get("zip").toString());
                        this.setCity(resultDTO.get("city").toString());
                    } else { //city by country and name not found, open window  to create it.
                        StringBuffer openUrl = new StringBuffer();
                        openUrl.append("onload=\"")
                                .append("openCitySearch('countryId', 'cityId', 'zipId', 'cityNameId', 'beforeZipId', 'notFoundWithZipCity', 'iframeId', 'false')\"");

                        request.setAttribute("jsLoad", new String(openUrl));
                        if (errors.isEmpty()) {

                            if (Functions.hasAccessRight(request, "CITY", "CREATE")) {
                                errors.add("citySearch", new ActionError("Address.error.enteredCityNotFound.selectOrCreate"));
                            } else {
                                errors.add("citySearch", new ActionError("Address.error.enteredCityNotFound"));
                            }
                        }

                    }

                } catch (AppLevelException e) {
                    log.error("Error searching the city name= " + getDto("city"), e);
                }
            }

            // Search the city with given country and zip code.
            if (!GenericValidator.isBlankOrNull(this.getCountryId()) &&
                    !GenericValidator.isBlankOrNull(this.getZip()) &&
                    GenericValidator.isBlankOrNull(this.getCity())) {
                log.debug("Retrieving city or cities with only Zip code entered");
                try {
                    ZipCityCmd zipCmd = new ZipCityCmd(); // the command to execute
                    zipCmd.putParam("countryId", getDto("countryId"));
                    zipCmd.putParam("zip", getDto("zip"));
                    zipCmd.putParam("companyId", getDto("companyId"));
                    final ResultDTO resultDTO = BusinessDelegate.i.execute(zipCmd, request);
                    ArrayList result = (ArrayList) resultDTO.get("cities");

                    if (result.size() == 0) { //if no found cities for given zip code, open create window

                        StringBuffer openUrl = new StringBuffer();
                        openUrl.append("onload=\"")
                                .append("openCitySearch('countryId', 'cityId', 'zipId', 'cityNameId', 'beforeZipId', 'notFound', 'iframeId', 'false')\"");

                        request.setAttribute("jsLoad", new String(openUrl));
                        if (errors.isEmpty()) {
                            errors.add("city", new ActionError("Address.error.enteredCityNotFound"));
                        }

                    } else if (result.size() == 1) { // if found only one
                        log.debug("One city found");
                        CityDTO city = (CityDTO) result.get(0);
                        log.debug("city = " + city.get("cityName"));
                        log.debug("cityId = " + city.get("cityId"));
                        this.setCity(String.valueOf(city.get("cityName")));
                        this.setCityId(city.get("cityId").toString());

                    } else { //if found more of one cities asociated with entered zip

                        if (!getBeforeZip().equals(getZip()) || GenericValidator.isBlankOrNull(this.getCity())) {

                            log.debug("more of one cities found, load a city search page to choose one");
                            //  setCity("");
                            StringBuffer openUrl = new StringBuffer();
                            openUrl.append("onload=\"")
                                    .append("openCitySearch('countryId', 'cityId', 'zipId', 'cityNameId', 'beforeZipId', 'tooMuch', 'iframeId', 'false')\"");

                            request.setAttribute("jsLoad", new String(openUrl));
                            if (errors.isEmpty()) {
                                errors.add("toMuchCities", new ActionError("Address.error.tooMuchCitiesByZip"));
                            }
                        }

                    }

                } catch (AppLevelException e) {
                    log.error("Error reading cities for zip = " + getDto("zip"), e);
                }
            }

            //if it leaves empty city and zip he don't want a city defined
            if (GenericValidator.isBlankOrNull(this.getZip()) && GenericValidator.isBlankOrNull(this.getCity())) {
                this.setCityId(null);
            }

            // validate address duplicated only if user is in create operation and do not have any errors.
            if ("create".equals(getDto("op")) && errors.isEmpty() && (confirmDuplicatedCreation == null ||
                    "false".equals(confirmDuplicatedCreation))) {
                log.debug("Checking duplicated contacts before creation");
                AddressDuplicatedCmd duplicatedCmd = new AddressDuplicatedCmd(); // command to execute

                duplicatedCmd.putParam("name1", getDto("name1"));
                duplicatedCmd.putParam("name2", getDto("name2"));
                duplicatedCmd.putParam("name3", getDto("name3"));
                duplicatedCmd.putParam("addressType", getDto("addressType"));
                duplicatedCmd.putParam("companyId", getDto("companyId"));
                duplicatedCmd.putParam("userId", getDto("recordUserId"));
                ResultDTO resultDTO = null;
                try {
                    resultDTO = BusinessDelegate.i.execute(duplicatedCmd, request);
                    List result = (List) resultDTO.get("duplicatedAddresses");
                    if (result.size() > 0) {
                        request.setAttribute("duplicatedAddressesList", result);
                        request.setAttribute("jsLoad", "onload=\"hideAddressForm();\"");

                        String duplicateMessageResource;
                        if (getDto("isCreatedByPopup") != null) //error when creating on popup window.
                        {
                            duplicateMessageResource = "Address.duplicatedWarningFromPopup";
                        } else {
                            duplicateMessageResource = "Address.duplicatedWarning";
                        }

                        if (ContactConstants.ADDRESSTYPE_PERSON.equals(getDto("addressType"))) {
                            errors.add("duplicatedPerson", new ActionError(duplicateMessageResource,
                                    getDto("name1") + "" +
                                            ((getDto("name2") != null && !"".equals(getDto("name2"))) ? ", " +
                                                    getDto("name2") : "")));

                        } else if (ContactConstants.ADDRESSTYPE_ORGANIZATION.equals(getDto("addressType"))) {
                            errors.add("duplicatedOrganization", new ActionError(duplicateMessageResource,
                                    getDto("name1") + " " + ((getDto("name2") != null) ? getDto("name2") : "") +
                                            " " + ((getDto("name3") != null) ? getDto("name3") : "")));
                        }
                    }
                } catch (AppLevelException e) {
                    log.error("Unexpected error checking duplicated contacts", e);
                }
            }

            /* validate existence of telecomtypes related to telecoms and validate for type EMAIL a valid value.
   if user has added an telecomtype and wrote a text on telecom data textfield, it must be validated
   and if it's valid then add it to telecomMap. */
            errors = AddressContactPersonHelper.validateTelecoms(errors, request, getDtoMap(), telecomMap, user);

            /** validate the image if it is setted */
            errors = AddressContactPersonHelper.validateImageFile(errors, getDtoMap(), imageFile);

            //categoryFieldValues validator
            setDto("pageCategoryIds", CategoryFieldValueForm.getPageCategories(request));

            CategoryFormUtil utilValidator = new CategoryFormUtil(this.getDtoMap(), request);
            List<ActionError> l = utilValidator.validateCategoryFields();
            int counter = 0;
            for (ActionError r : l) {
                errors.add("catValidation_" + counter, r);
                counter++;
            }

            String addressId = (String) getDto("addressId");
            if (null != addressId && !"".equals(addressId.trim())) {

                if (null != getDto("supplierConfirmationFlag")
                        && !"".equals(getDto("supplierConfirmationFlag").toString().trim())) {
                    ActionError supplierRelationValidation = validateSupplierRelations(request, Integer.valueOf(addressId));
                    if (null != supplierRelationValidation) {
                        errors.add("supplierValidationErrorMsg", supplierRelationValidation);
                    }
                }

                if (null != getDto("customerConfirmationFlag")
                        && !"".equals(getDto("customerConfirmationFlag").toString().trim())) {
                    ActionError customerRelationsValidation = validateCustomerRelations(request, Integer.valueOf(addressId));
                    if (null != customerRelationsValidation) {
                        errors.add("customerRelationsValidationMsg", customerRelationsValidation);
                    }
                }

                List<ActionError> confirmationMessages
                        = evaluateCustomerOrSupplierChanges(Integer.valueOf(addressId), request);
                if (!confirmationMessages.isEmpty()) {
                    for (int i = 0; i < confirmationMessages.size(); i++) {
                        ActionError confirmationMsg = confirmationMessages.get(i);
                        errors.add("confirmationMsg_" + i, confirmationMsg);
                    }
                }
            }

            if (errors.isEmpty()) {
                getDtoMap().putAll(utilValidator.getDateOptionsAsInteger());
                getDtoMap().putAll(utilValidator.getAttachmentsDTOs());
                setDto("supplierConfirmationFlag", "");
                setDto("customerConfirmationFlag", "");
            } else {
                utilValidator.restoreAttachmentFields();
            }

        } else {
            //categoryFieldValues validator
            setDto("pageCategoryIds", CategoryFieldValueForm.getPageCategories(request));
            CategoryFormUtil utilValidator = new CategoryFormUtil(this.getDtoMap(), request);
            utilValidator.validateCategoryFields();
            utilValidator.restoreAttachmentFields();
        }

        return errors;
    }

    private List<ActionError> evaluateCustomerOrSupplierChanges(Integer addressId, HttpServletRequest request) {
        List<ActionError> errors = new ArrayList<ActionError>();

        String isSupplier = (String) super.getDto("isSupplier");
        String isCustomer = (String) super.getDto("isCustomer");
        String currentAddressCode = getAddressCode(addressId, request);
        if (null != currentAddressCode) {
            String supplierConfirmationFlag = getConfirmationFlagValue("supplierConfirmationFlag");
            if (null == isSupplier && null == supplierConfirmationFlag) {
                if (isSupplier(currentAddressCode)) {

                    setDto("supplierConfirmationFlag", true);
                    errors.add(new ActionError("Address.supplier.delete.confirmation"));
                }
            }

            String customerConfirmationFlag = getConfirmationFlagValue("customerConfirmationFlag");
            if (null == isCustomer && null == customerConfirmationFlag) {
                if (isCustomer(currentAddressCode)) {
                    setDto("customerConfirmationFlag", true);
                    errors.add(new ActionError("Address.customer.delete.confirmation"));
                }
            }
        }

        return errors;
    }

    private ActionError validateSupplierRelations(HttpServletRequest request, Integer addressId) {
        String isSupplier = (String) super.getDto("isSupplier");
        if (null == isSupplier) {
            SupplierDTO supplierDTO = new SupplierDTO();
            supplierDTO.setPrimKey(addressId);
            if (hasRelationships(supplierDTO)) {
                String tableNames = processReferencesTableNames(request, (List) supplierDTO.get("referencedByTables"));

                return new ActionError("Address.supplier.referenced", tableNames);
            }
        }

        return null;
    }

    private ActionError validateCustomerRelations(HttpServletRequest request, Integer addressId) {
        String isCustomer = (String) super.getDto("isCustomer");
        if (null == isCustomer) {
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setPrimKey(addressId);
            if (hasRelationships(customerDTO)) {
                String tableNames = processReferencesTableNames(request, (List) customerDTO.get("referencedByTables"));

                return new ActionError("Address.customer.referenced", tableNames);
            }
        }

        return null;
    }

    @SuppressWarnings(value = "unchecked")
    private boolean hasRelationships(ComponentDTO componentDTO) {
        ResultDTO resultDTO = new ResultDTO();
        IntegrityReferentialChecker.i.check(componentDTO, resultDTO);
        boolean isFailure = resultDTO.isFailure();
        if (isFailure) {
            componentDTO.put("referencedByTables", resultDTO.get("referencedByTables"));
        }

        return isFailure;
    }

    private String getConfirmationFlagValue(String key) {
        String confirmationFlag = (String) getDto(key);
        if (null != confirmationFlag && !"".equals(confirmationFlag.trim())) {
            return confirmationFlag;
        }

        return null;
    }

    private boolean isCustomer(String addressCode) {
        return CodeUtil.isCustomer(addressCode);
    }

    private boolean isSupplier(String addressCode) {
        return CodeUtil.isSupplier(addressCode);
    }


    private String getAddressCode(Integer addressId, HttpServletRequest request) {
        LightlyAddressCmd cmd = new LightlyAddressCmd();
        cmd.putParam("addressId", addressId);
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);
            return (String) resultDTO.get("code");
        } catch (AppLevelException e) {
            log.debug("Cannot read Address addressId: " + addressId);
            return null;
        }
    }

    private String processReferencesTableNames(HttpServletRequest request, List tableNames) {
        String result = "";
        for (int i = 0; i < tableNames.size(); i++) {
            Object element = tableNames.get(i);

            result += JSPHelper.getMessage(request, "ReferencedBy.table." + element.toString());
            if (i < tableNames.size() - 1) {
                result += ", ";
            }
        }

        return result;
    }
}
