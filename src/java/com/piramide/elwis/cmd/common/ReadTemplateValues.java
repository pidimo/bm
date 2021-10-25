package com.piramide.elwis.cmd.common;

import com.piramide.elwis.domain.contactmanager.*;
import com.piramide.elwis.domain.financemanager.Invoice;
import com.piramide.elwis.domain.financemanager.InvoiceReminder;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Jatun S.R.L.
 * read all field values to word and html templates
 *
 * @author Miky
 * @version $Id: ReadTemplateValues.java 11346 2015-10-29 01:54:58Z miguel $
 */
public class ReadTemplateValues {

    private AdditionalAddress additionalAddress;
    private Integer companyId;
    private Integer userAddressId;
    private Integer employeeId;

    boolean isEmailSend;
    boolean withSalesProcess;
    boolean isCampaign;
    String requestLocale;
    CompanyInfoValues companyInfoValues;
    CalculateDocumentValues documentValues;

    boolean processNullEntities;
    String nullEntityValue;

    private Log log = LogFactory.getLog(this.getClass());

    public ReadTemplateValues(boolean processNullEntities, String nullEntityValue) {
        this.processNullEntities = processNullEntities;
        this.nullEntityValue = nullEntityValue;
    }

    public ReadTemplateValues(Integer companyId, Integer userAddressId, Integer employeeId, boolean isEmailSend, boolean withSalesProcess, String requestLocale, boolean isCampaign) throws FinderException {
        this.processNullEntities = false;

        initialize(companyId, userAddressId, employeeId, isEmailSend, withSalesProcess, requestLocale, isCampaign);
    }

    public void initialize(Integer companyId, Integer userAddressId, Integer employeeId, boolean isEmailSend, boolean withSalesProcess, String requestLocale, boolean isCampaign) throws FinderException {

        this.companyId = companyId;
        this.userAddressId = userAddressId;
        this.employeeId = employeeId;
        this.isEmailSend = isEmailSend;
        this.withSalesProcess = withSalesProcess;
        this.isCampaign = isCampaign;
        this.requestLocale = requestLocale;

        log.debug("Read values: comapnyId:" + companyId + " userAddressId:" + userAddressId + " employeeId:" + employeeId);

        Address companyAddress = findAddress(companyId);
        Employee employee = findEmployee(employeeId);


        companyInfoValues = new CompanyInfoValues();

        //to NULL ENTITY process
        if (processNullEntities) {
            companyInfoValues.setNullEntityValue(nullEntityValue);
        }

        if (this.isEmailSend) {
            companyInfoValues.setEndLineAsHTMLDocument(); //this it must be first ,is required to html documents only;
        }
        companyInfoValues.setCompanyInfoValues(companyAddress, employee);
    }

    /**
     * get all template field names
     *
     * @return String[]
     */
    public String[] getFieldNames() {
        return companyInfoValues.getFieldNames(withSalesProcess);
    }

    /**
     * get all template field names
     *
     * @param companyId
     * @param employeeId
     * @param withSalesProcess to add sales process fields
     * @return String[]
     * @throws FinderException
     */
    public static String[] getFieldNames(Integer companyId, Integer employeeId, boolean withSalesProcess) throws FinderException {
        AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
        EmployeeHome employeeHome = (EmployeeHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_EMPLOYEE);

        Address companyAsAddress = addressHome.findByPrimaryKey(companyId);
        Employee employee = employeeHome.findByPrimaryKey(employeeId);
        CompanyInfoValues companyValues = new CompanyInfoValues();
        companyValues.setCompanyInfoValues(companyAsAddress, employee);
        return companyValues.getFieldNames(withSalesProcess);
    }

    /**
     * Field names to invoice and invoice reminder documents
     *
     * @return String[]
     */
    public String[] getInvoiceFieldNames() {
        String[] commonFields = getFieldNames();
        String[] invoiceFiels = InvoiceDocumentValues.getFieldNames();
        String[] allFields = new String[commonFields.length + invoiceFiels.length];
        System.arraycopy(commonFields, 0, allFields, 0, commonFields.length);
        System.arraycopy(invoiceFiels, 0, allFields, commonFields.length, invoiceFiels.length);
        return allFields;
    }

    /**
     * get values to Invoice document
     *
     * @param addressId
     * @param contactPersonId
     * @param languageId
     * @param telecomTypeId
     * @param invoice
     * @return Object[]
     * @throws FinderException
     */
    public Object[] getInvoiceFieldValues(Integer addressId, Integer contactPersonId, Integer languageId, Integer telecomTypeId, Invoice invoice) throws FinderException {
        Address companyAddress = findAddress(companyId);
        Address userAddress = findAddress(userAddressId);

        //set the additional address of invoice if exist
        setAdditionalAddress(findAdditionalAddress(invoice.getAdditionalAddressId()));

        Object[] commonValues = getFieldValues(addressId, contactPersonId, languageId, telecomTypeId);

        //calculate invoice values
        AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
        Address address = addressHome.findByPrimaryKey(addressId);
        Address contactPerson = null;
        if (contactPersonId != null) {
            contactPerson = addressHome.findByPrimaryKey(contactPersonId);
        }

        Integer defaultLanguageId = InvoiceDocumentValues.getDefaultLanguageId(address, contactPerson, companyAddress, userAddress);
        if (defaultLanguageId == null) {
            defaultLanguageId = languageId;
        }
        String defaultIsoLang = InvoiceDocumentValues.getDefaultIsoLang(address, contactPerson, companyAddress, userAddress);
        if (defaultIsoLang == null) {
            defaultIsoLang = requestLocale;
        }

        InvoiceDocumentValues invoiceDocValues = new InvoiceDocumentValues(defaultIsoLang);

        Object[] invoiceValues = invoiceDocValues.getValues(invoice, defaultLanguageId);

        Object[] allValues = new Object[commonValues.length + invoiceValues.length];
        System.arraycopy(commonValues, 0, allValues, 0, commonValues.length);
        System.arraycopy(invoiceValues, 0, allValues, commonValues.length, invoiceValues.length);
        return allValues;
    }

    /**
     * Get values to invoice reminder document
     *
     * @param addressId
     * @param contactPersonId
     * @param languageId
     * @param telecomTypeId
     * @param reminder
     * @return Object[]
     * @throws FinderException
     */
    public Object[] getInvoiceReminderFieldValues(Integer addressId, Integer contactPersonId, Integer languageId, Integer telecomTypeId, InvoiceReminder reminder) throws FinderException {
        Address companyAddress = findAddress(companyId);
        Address userAddress = findAddress(userAddressId);

        Invoice invoice = reminder.getInvoice();
        //set the additional address of invoice if exist
        setAdditionalAddress(findAdditionalAddress(invoice.getAdditionalAddressId()));

        Object[] commonValues = getFieldValues(addressId, contactPersonId, languageId, telecomTypeId);

        //calculate invoice reminder values
        AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
        Address address = addressHome.findByPrimaryKey(addressId);
        Address contactPerson = null;
        if (contactPersonId != null) {
            contactPerson = addressHome.findByPrimaryKey(contactPersonId);
        }

        Integer defaultLanguageId = InvoiceDocumentValues.getDefaultLanguageId(address, contactPerson, companyAddress, userAddress);
        if (defaultLanguageId == null) {
            defaultLanguageId = languageId;
        }
        String defaultIsoLang = InvoiceDocumentValues.getDefaultIsoLang(address, contactPerson, companyAddress, userAddress);
        if (defaultIsoLang == null) {
            defaultIsoLang = requestLocale;
        }

        InvoiceDocumentValues invoiceDocValues = new InvoiceDocumentValues(defaultIsoLang);

        Object[] invoiceValues = invoiceDocValues.getReminderValues(reminder, defaultLanguageId);

        Object[] allValues = new Object[commonValues.length + invoiceValues.length];
        System.arraycopy(commonValues, 0, allValues, 0, commonValues.length);
        System.arraycopy(invoiceValues, 0, allValues, commonValues.length, invoiceValues.length);
        return allValues;
    }

    /**
     * get all template field values
     *
     * @param addressId       person or organization
     * @param contactPersonId contact person, null to read only as address
     * @param languageId
     * @param telecomTypeId   id of telecom type, null if is word template
     * @return Object[] values of template fields
     * @throws FinderException
     */
    public Object[] getFieldValues(Integer addressId, Integer contactPersonId, Integer languageId, Integer telecomTypeId) throws FinderException {

        AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
        Address address = null;
        Address contactPerson = null;
        Address companyAddress = findAddress(companyId);
        Address userAddress = findAddress(userAddressId);
        Employee employee = findEmployee(employeeId);

        if (processNullEntities) {
            if (addressId != null) {
                address = addressHome.findByPrimaryKey(addressId);
            }
        } else {
            address = addressHome.findByPrimaryKey(addressId);
        }

        boolean isPerson = (address == null) || ContactConstants.ADDRESSTYPE_PERSON.equals(address.getAddressType());
        if (!isPerson && contactPersonId != null) {
            contactPerson = addressHome.findByPrimaryKey(contactPersonId);
        }

        log.debug("Invoke calculate....:" + contactPerson + " - " + address);
        documentValues = new CalculateDocumentValues(isPerson);
        if (isEmailSend) {
            documentValues.setEndLineAsHTMLDocument(); //this it must be first,is required to html documents only;
        }

        documentValues.setIsoLang(languageId.toString()); /// add BY ME. For salutation:set ISO value of the generate language.
        documentValues.setMainAdditionalAddress(additionalAddress);

        if (isCampaign) {
            documentValues.setIsCampaign("true");
        }

        documentValues.setTelecomTypes(companyInfoValues.getTelecomTypes());
        documentValues.setEmail(isEmailSend);

        //calculate values
        if (address != null) {
            documentValues.calculateValues(address, contactPerson, companyAddress, employee, userAddress, requestLocale, new Date(), telecomTypeId);
        } else {
            documentValues.setNullEntityValue(nullEntityValue);
            documentValues.calculateValuesToNullEntity(new Date());
        }

        log.debug("IS EMAIL:" + isEmailSend);

        return documentValues.getValues(companyInfoValues.getCompany_employeeValues(), companyInfoValues.getCompanyTelecoms(), withSalesProcess);
    }

    /**
     * get all template field values
     *
     * @param addressId
     * @param languageId
     * @param telecomTypeId
     * @return Object[] values of template fields
     * @throws FinderException
     */
    public Object[] getFieldValues(Integer addressId, Integer languageId, Integer telecomTypeId) throws FinderException {
        return getFieldValues(addressId, null, languageId, telecomTypeId);
    }

    /**
     * get all template field name and field values as Map
     *
     * @param addressId
     * @param contactPersonId
     * @param languageId
     * @param telecomTypeId
     * @return Map{field_name, field_value}
     * @throws FinderException
     */
    public Map getFieldValuesAsMap(Integer addressId, Integer contactPersonId, Integer languageId, Integer telecomTypeId) throws FinderException {
        Map resultMap = new HashMap();
        Object[] fieldNames = getFieldNames();
        Object[] fieldValues = getFieldValues(addressId, contactPersonId, languageId, telecomTypeId);

        log.debug("field names length:" + fieldNames.length + " field values length:" + fieldValues.length);
        for (int i = 0; i < fieldNames.length; i++) {
            resultMap.put(fieldNames[i], (fieldValues[i] != null ? fieldValues[i] : ""));
        }
        return resultMap;
    }

    /**
     * get all template field name and field values as Map
     *
     * @param addressId
     * @param languageId
     * @param telecomTypeId
     * @return Map{field_name, field_value}
     * @throws FinderException
     */
    public Map getFieldValuesAsMap(Integer addressId, Integer languageId, Integer telecomTypeId) throws FinderException {
        return getFieldValuesAsMap(addressId, null, languageId, telecomTypeId);
    }

    /**
     * get email of address
     *
     * @return String
     */
    public String getAddressEmail() {
        return documentValues != null ? documentValues.getEmail() : null;
    }

    public Locale getLocale() {
        return documentValues != null ? documentValues.getLocale() : null;
    }

    private AdditionalAddress findAdditionalAddress(Integer additionalAddressId) {
        AdditionalAddress additionalAddress = null;
        AdditionalAddressHome additionalAddressHome = (AdditionalAddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDITIONALADDRESS);

        if (additionalAddressId != null) {
            try {
                additionalAddress = additionalAddressHome.findByPrimaryKey(additionalAddressId);
            } catch (FinderException e) {
                log.debug("Not found additional address " + additionalAddressId, e);
            }
        }
        return additionalAddress;
    }

    private Address findAddress(Integer addressId) throws FinderException {
        Address address = null;
        AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
        if (addressId != null) {
            address = addressHome.findByPrimaryKey(addressId);
        }
        return address;
    }

    private Employee findEmployee(Integer employeeId) throws FinderException {
        Employee employee = null;
        EmployeeHome employeeHome = (EmployeeHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_EMPLOYEE);

        if (processNullEntities) {
            if (employeeId != null) {
                employee = employeeHome.findByPrimaryKey(employeeId);
            }
        } else {
            employee = employeeHome.findByPrimaryKey(employeeId);
        }
        return employee;
    }

    public AdditionalAddress getAdditionalAddress() {
        return additionalAddress;
    }

    public void setAdditionalAddress(AdditionalAddress additionalAddress) {
        this.additionalAddress = additionalAddress;
    }
}
