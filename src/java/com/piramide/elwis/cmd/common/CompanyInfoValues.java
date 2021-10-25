package com.piramide.elwis.cmd.common;

import com.piramide.elwis.cmd.utils.VariableConstants;
import com.piramide.elwis.domain.catalogmanager.City;
import com.piramide.elwis.domain.catalogmanager.CityHome;
import com.piramide.elwis.domain.catalogmanager.Country;
import com.piramide.elwis.domain.catalogmanager.CountryHome;
import com.piramide.elwis.domain.contactmanager.*;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Mar 30, 2005
 * Time: 11:07:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class CompanyInfoValues {
    private final static Log log = LogFactory.getLog("CalculateDocumentValues");

    private static final String SPACE = " ";
    private String END_LINE;

    private String[] company_employeeValues;

    private Map company_employeeTelecoms;
    private List telecomTypes;

    private String nullEntityValue;

    public CompanyInfoValues() {
        company_employeeTelecoms = new LinkedHashMap();
        telecomTypes = new LinkedList();
        company_employeeValues = new String[VariableConstants.COMPANY_EMPLOYEE_FIELDS.length];
        END_LINE = "\r\n";
    }

    public CompanyInfoValues(boolean isUpdate) {
        END_LINE = isUpdate ? "/" : "\r\n";
    }

    public void setCompanyInfoValues(Address company, Employee employeeBean) {

        for (int i = 0; i < VariableConstants.COMPANY_EMPLOYEE_FIELDS.length; i++) {
            company_employeeValues[i] = " ";
        }

        setCompanyValues(company);

        if (employeeBean != null) {
            setEmployeeValues(employeeBean, company);
        } else {
            // process if entity is null
            setNullEmployeeValues(company);
        }
    }

    private void setCompanyValues(Address company) {
        log.debug("Set CompanyInfoValues class ... ");
        // Company values
        StringBuffer temp = new StringBuffer();
        StringBuffer tempAddressComplete = new StringBuffer();

        Country country = company.getCountry();
        City city = company.getCityEntity();
        String street = company.getStreet();
        String houseNumber = company.getHouseNumber();
        String additionalLine = company.getAdditionalAddressLine();

        //verify if has additional address
        AdditionalAddress additionalAddress = findDefaultAdditionalAddress(company.getAddressId(), company.getCompanyId());
        if (additionalAddress != null) {
            country = findCountry(additionalAddress.getCountryId());
            city = findCity(additionalAddress.getCityId());
            street = additionalAddress.getStreet();
            houseNumber = additionalAddress.getHouseNumber();
            additionalLine = additionalAddress.getAdditionalAddressLine();
        }



        if (country != null) {
            setValue(VariableConstants.FIELD_COMPANY_COUNTRY, country.getCountryName());
            setValue(VariableConstants.FIELD_COMPANY_COUNTRYCODE, country.getCountryAreaCode());
        }

        // Define the status procces for COMPANY
        if (city != null) {
            setValue(VariableConstants.FIELD_COMPANY_CITY, city.getCityName());
            setValue(VariableConstants.FIELD_COMPANY_ZIP, city.getCityZip());
        }

        //additional line
        if (isValidValue(additionalLine)) {
            if (temp.length() > 0) {
                temp.append(END_LINE);
            }
            temp.append(additionalLine);
        }

        // Add HouseNumber to Street
        if (isValidValue(houseNumber)) {
            street = validValue(street) + SPACE + houseNumber;
        }
        setValue(VariableConstants.FIELD_COMPANY_STREET, street);

        if (isValidValue(VariableConstants.FIELD_COMPANY_STREET)) {
            if (temp.length() > 0) {
                temp.append(END_LINE);
            }
            temp.append(getValue(VariableConstants.FIELD_COMPANY_STREET));
        }

        // Add zip
        if (isValidValue(VariableConstants.FIELD_COMPANY_ZIP)) {
            tempAddressComplete.append(getValue(VariableConstants.FIELD_COMPANY_ZIP)).append(SPACE);
        }

        // Add cityName
        if (isValidValue(VariableConstants.FIELD_COMPANY_CITY)) {
            tempAddressComplete.append(getValue(VariableConstants.FIELD_COMPANY_CITY));
        }

        // Add endLine only exist (zip, city)
        if (isValidValue(new String(temp))) {
            temp.append(END_LINE);
        }

        if (isValidValue(new String(tempAddressComplete))) {
            temp.append(new String(tempAddressComplete));
            temp.append(END_LINE);
        }

        // Add country
        if (isValidValue(VariableConstants.FIELD_COMPANY_COUNTRY)) {
            temp.append(getValue(VariableConstants.FIELD_COMPANY_COUNTRY));
        }


        setValue(VariableConstants.FIELD_COMPANY_ADDRESS, new String(temp));

        setValue(VariableConstants.FIELD_COMPANY_NAME1, company.getName1());
        setValue(VariableConstants.FIELD_COMPANY_NAME2, company.getName2());
        setValue(VariableConstants.FIELD_COMPANY_NAME3, company.getName3());


        temp = new StringBuffer();
        temp.append(getValue(VariableConstants.FIELD_COMPANY_NAME1));

        if (isValidValue(VariableConstants.FIELD_COMPANY_NAME2)) {
            temp.append(END_LINE).append(getValue(VariableConstants.FIELD_COMPANY_NAME2));
        }
        if (isValidValue(VariableConstants.FIELD_COMPANY_NAME3)) {
            temp.append(END_LINE).append(getValue(VariableConstants.FIELD_COMPANY_NAME3));
        }


        setValue(VariableConstants.FIELD_COMPANY_NAME, new String(temp));


        // Set TELECOMS
        try {
            setCompanyTelecoms(company);
        } catch (FinderException e) {
            e.printStackTrace();
        }
    }

    private void setEmployeeValues(Employee employeeBean, Address company) {
        Address employee = employeeBean.getAddress();

        // Employee values
        log.debug("Set Employees");
        StringBuffer temp = new StringBuffer();
        setValue(VariableConstants.FIELD_EMPLOYEE_FIRSTNAME, employee.getName2());
        setValue(VariableConstants.FIELD_EMPLOYEE_LASTNAME, employee.getName1());

        temp = new StringBuffer();
        if (isValidValue(VariableConstants.FIELD_EMPLOYEE_FIRSTNAME)) {
            temp.append(getValue(VariableConstants.FIELD_EMPLOYEE_FIRSTNAME)).append(SPACE);
        }
        temp.append(getValue(VariableConstants.FIELD_EMPLOYEE_LASTNAME));
        setValue(VariableConstants.FIELD_EMPLOYEE_NAME, new String(temp));

        setValue(VariableConstants.FIELD_EMPLOYEE_INITIALS, employeeBean.getInitials());

        if (employee.getTitleId() != null) {
            setValue(VariableConstants.FIELD_EMPLOYEE_TITLE, employee.getTitle().getTitleName());
        }

        if (employeeBean.getFunction() != null) {
            setValue(VariableConstants.FIELD_EMPLOYEE_FUNCTION, employeeBean.getFunction());
        }

        if (employeeBean.getDepartmentId() != null) {
            setValue(VariableConstants.FIELD_EMPLOYEE_DEPARTMENT, employeeBean.getDepartment().getName());
        }


        // Set TELECOMS
        try {
            setEmployeeTelecoms(company, employee);
        } catch (FinderException e) {
            e.printStackTrace();
        }
    }

    private void setNullEmployeeValues(Address company) {
        String strValue = (nullEntityValue != null) ? nullEntityValue : "";

        // Employee values
        setValue(VariableConstants.FIELD_EMPLOYEE_FIRSTNAME, strValue);
        setValue(VariableConstants.FIELD_EMPLOYEE_LASTNAME, strValue);

        setValue(VariableConstants.FIELD_EMPLOYEE_NAME, strValue);
        setValue(VariableConstants.FIELD_EMPLOYEE_INITIALS, strValue);
        setValue(VariableConstants.FIELD_EMPLOYEE_TITLE, strValue);
        setValue(VariableConstants.FIELD_EMPLOYEE_FUNCTION, strValue);
        setValue(VariableConstants.FIELD_EMPLOYEE_DEPARTMENT, strValue);


        // Set TELECOMS as NULL
        TelecomTypeHome telecomTypeHome = (TelecomTypeHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_TELECOMTYPE);
        try {
            Collection telecomTypes = telecomTypeHome.findByCompanyIdOrderBySequence(company.getCompanyId());
            if (!telecomTypes.isEmpty()) {
                for (Iterator iterator = telecomTypes.iterator(); iterator.hasNext();) {
                    TelecomType telecomType = (TelecomType) iterator.next();
                    company_employeeTelecoms.put("employee_tel_" + telecomType.getTelecomTypeId(), strValue);
                }
            }
        } catch (FinderException e) {
            log.debug("Error in find...", e);
        }
    }

    private void setTelecoms(Address address, Address employee) throws FinderException {
        log.debug("[Func-calculateValues]:Extract Telecoms");
        TelecomTypeHome telecomTypeHome = (TelecomTypeHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_TELECOMTYPE);

        Collection telecomTypes = telecomTypeHome.findByCompanyIdOrderBySequence(address.getCompanyId());
        if (!telecomTypes.isEmpty()) {
            TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);
            Collection telecomsCompany = telecomHome.findAllAddressDefaultTelecoms(address.getAddressId());
            Collection telecomsEmployee = telecomHome.findAllContactPersonDefaultTelecoms(address.getAddressId(), employee.getAddressId());

            for (Iterator iterator = telecomTypes.iterator(); iterator.hasNext();) {
                TelecomType telecomType = (TelecomType) iterator.next();
                this.telecomTypes.add(telecomType.getTelecomTypeId());
                boolean find = false;
                for (Iterator iterator1 = telecomsCompany.iterator(); iterator1.hasNext();) {
                    Telecom telecom = (Telecom) iterator1.next();
                    if (telecom.getTelecomTypeId().equals(telecomType.getTelecomTypeId())) {
                        find = true;
                        company_employeeTelecoms.put("company_tel_" + telecom.getTelecomTypeId(), telecom.getData());
                        iterator1.remove();
                        iterator1 = telecomsCompany.iterator();
                        break;
                    }
                }
                if (!find) {
                    Collection telecoms = telecomHome.findAllAddressTelecomsByTypeId(address.getAddressId(), telecomType.getTelecomTypeId());
                    if (!telecoms.isEmpty()) {
                        Telecom telecom = (Telecom) telecoms.iterator().next();
                        company_employeeTelecoms.put("company_tel_" + telecom.getTelecomTypeId(), telecom.getData());
                    } else {
                        company_employeeTelecoms.put("company_tel_" + telecomType.getTelecomTypeId(), "");
                    }

                }

                find = false;
                for (Iterator iterator1 = telecomsEmployee.iterator(); iterator1.hasNext();) {
                    Telecom telecom = (Telecom) iterator1.next();
                    if (telecom.getTelecomTypeId().equals(telecomType.getTelecomTypeId())) {
                        find = true;
                        company_employeeTelecoms.put("employee_tel_" + telecom.getTelecomTypeId(), telecom.getData());
                        iterator1.remove();
                        iterator1 = telecomsCompany.iterator();
                        break;
                    }
                }
                if (!find) {
                    Collection telecoms = telecomHome.findAllContactPersonTelecomsByTypeId(address.getAddressId(), employee.getAddressId(), telecomType.getTelecomTypeId());
                    if (!telecoms.isEmpty()) {
                        Telecom telecom = (Telecom) telecoms.iterator().next();
                        company_employeeTelecoms.put("employee_tel_" + telecom.getTelecomTypeId(), telecom.getData());
                    } else
                    // If the employee hasn't current TelecomType, set the company telecom
                    {
                        company_employeeTelecoms.put("employee_tel_" + telecomType.getTelecomTypeId(), company_employeeTelecoms.get("company_tel_" + telecomType.getTelecomTypeId()));
                    }
                }
            }
        }
        log.debug("End Telecoms");
    }

    private void setCompanyTelecoms(Address company) throws FinderException {
        log.debug("[Func-calculateValues]:Extract Telecoms");
        TelecomTypeHome telecomTypeHome = (TelecomTypeHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_TELECOMTYPE);

        Collection telecomTypes = telecomTypeHome.findByCompanyIdOrderBySequence(company.getCompanyId());
        if (!telecomTypes.isEmpty()) {
            TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);
            Collection telecomsCompany = telecomHome.findAllAddressDefaultTelecoms(company.getAddressId());

            for (Iterator iterator = telecomTypes.iterator(); iterator.hasNext();) {
                TelecomType telecomType = (TelecomType) iterator.next();
                this.telecomTypes.add(telecomType.getTelecomTypeId()); //this to contact address telecoms
                boolean find = false;
                for (Iterator iterator1 = telecomsCompany.iterator(); iterator1.hasNext();) {
                    Telecom telecom = (Telecom) iterator1.next();
                    if (telecom.getTelecomTypeId().equals(telecomType.getTelecomTypeId())) {
                        find = true;
                        company_employeeTelecoms.put("company_tel_" + telecom.getTelecomTypeId(), telecom.getData());
                        iterator1.remove();
                        iterator1 = telecomsCompany.iterator();
                        break;
                    }
                }
                if (!find) {
                    Collection telecoms = telecomHome.findAllAddressTelecomsByTypeId(company.getAddressId(), telecomType.getTelecomTypeId());
                    if (!telecoms.isEmpty()) {
                        Telecom telecom = (Telecom) telecoms.iterator().next();
                        company_employeeTelecoms.put("company_tel_" + telecom.getTelecomTypeId(), telecom.getData());
                    } else {
                        company_employeeTelecoms.put("company_tel_" + telecomType.getTelecomTypeId(), "");
                    }
                }
            }
        }
        log.debug("End Telecoms");
    }

    private void setEmployeeTelecoms(Address company, Address employee) throws FinderException {
        log.debug("[Func-calculateValues]:Extract Telecoms");
        TelecomTypeHome telecomTypeHome = (TelecomTypeHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_TELECOMTYPE);

        Collection telecomTypes = telecomTypeHome.findByCompanyIdOrderBySequence(company.getCompanyId());
        if (!telecomTypes.isEmpty()) {
            TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);
            Collection telecomsEmployee = telecomHome.findAllContactPersonDefaultTelecoms(company.getAddressId(), employee.getAddressId());

            for (Iterator iterator = telecomTypes.iterator(); iterator.hasNext();) {
                TelecomType telecomType = (TelecomType) iterator.next();
                boolean find = false;

                for (Iterator iterator1 = telecomsEmployee.iterator(); iterator1.hasNext();) {
                    Telecom telecom = (Telecom) iterator1.next();
                    if (telecom.getTelecomTypeId().equals(telecomType.getTelecomTypeId())) {
                        find = true;
                        company_employeeTelecoms.put("employee_tel_" + telecom.getTelecomTypeId(), telecom.getData());
                        iterator1.remove();
                        iterator1 = telecomsEmployee.iterator();
                        break;
                    }
                }
                if (!find) {
                    Collection telecoms = telecomHome.findAllContactPersonTelecomsByTypeId(company.getAddressId(), employee.getAddressId(), telecomType.getTelecomTypeId());
                    if (!telecoms.isEmpty()) {
                        Telecom telecom = (Telecom) telecoms.iterator().next();
                        company_employeeTelecoms.put("employee_tel_" + telecom.getTelecomTypeId(), telecom.getData());
                    } else {
                        // If the employee hasn't current TelecomType, set the company telecom
                        company_employeeTelecoms.put("employee_tel_" + telecomType.getTelecomTypeId(), company_employeeTelecoms.get("company_tel_" + telecomType.getTelecomTypeId()));
                    }
                }
            }
        }
        log.debug("End Telecoms");
    }


    public String[] getFieldNames(boolean withSalesProcess) {
        int length = VariableConstants.ADDRESS_PERSON_FIELDS.length +
                VariableConstants.COMPANY_EMPLOYEE_FIELDS.length +
                VariableConstants.SALESPROCESS_ACTION_FIELDS.length +
                company_employeeTelecoms.size() +
                telecomTypes.size(); //Para addressTelecoms
        if (!withSalesProcess) {
            length = length + VariableConstants.SALESPROCESS_ACTIONPOSITION_FIELDS.length;
        }
        String[] names = new String[length];
        int lastPos = 0;
        // Copy address_person names
        System.arraycopy(VariableConstants.ADDRESS_PERSON_FIELDS, 0, names, 0, VariableConstants.ADDRESS_PERSON_FIELDS.length);
        lastPos += VariableConstants.ADDRESS_PERSON_FIELDS.length;

        // Copy company_employee names
        System.arraycopy(VariableConstants.COMPANY_EMPLOYEE_FIELDS, 0, names, lastPos, VariableConstants.COMPANY_EMPLOYEE_FIELDS.length);
        lastPos += VariableConstants.COMPANY_EMPLOYEE_FIELDS.length;

        // Copy salesprocess action names
        System.arraycopy(VariableConstants.SALESPROCESS_ACTION_FIELDS, 0, names, lastPos, VariableConstants.SALESPROCESS_ACTION_FIELDS.length);
        lastPos += VariableConstants.SALESPROCESS_ACTION_FIELDS.length;

        if (!withSalesProcess) {
            // Copy SalesProcess names
            System.arraycopy(VariableConstants.SALESPROCESS_ACTIONPOSITION_FIELDS, 0, names, lastPos, VariableConstants.SALESPROCESS_ACTIONPOSITION_FIELDS.length);
            lastPos += VariableConstants.SALESPROCESS_ACTIONPOSITION_FIELDS.length;
        }

        // Copy telecom names
        //System.arraycopy((String [])company_employeeTelecoms.keySet().toArray(new String[0]), 0, names, lastPos, company_employeeTelecoms.size());
        System.arraycopy(company_employeeTelecoms.keySet().toArray(), 0, names, lastPos, company_employeeTelecoms.size());
        lastPos += company_employeeTelecoms.size();

        String[] addressTelecomsNames = new String[telecomTypes.size()];
        //log.debug("length:"+length+" - TELECOMS:"+telecomTypes.size());
        for (int i = 0; i < telecomTypes.size(); i++) {
            addressTelecomsNames[i] = "Address_tel_" + telecomTypes.get(i);
        }

        System.arraycopy(addressTelecomsNames, 0, names, lastPos, telecomTypes.size());
        return names;
    }

    public Object[] getCompanyTelecoms() {
        return company_employeeTelecoms.values().toArray();
    }

    public String[] getCompany_employeeValues() {
        return company_employeeValues;
    }

    private String validValue(String value) {
        return value != null && value.trim().length() > 0 ? value : "";
    }

    private boolean isValidValue(String value) {
        return (value != null && value.length() > 0);
    }

    private boolean isValidValue(int pos) {
        String value = getValue(pos);//Address
        return (value != null && value.trim().length() > 0);
    }

    private void setValue(int field, String value) {
        company_employeeValues[field] = validValue(value);
    }

    public String getValue(int field) {
        return company_employeeValues[field];
    }

    public List getTelecomTypes() {
        return telecomTypes;
    }

    /**
     * set jump line '<br/>' to HTML values in send mails
     */
    public void setEndLineAsHTMLDocument() {
        END_LINE = "<br/>";
    }

    public void setNullEntityValue(String nullEntityValue) {
        this.nullEntityValue = nullEntityValue;
    }

    private AdditionalAddress findDefaultAdditionalAddress(Integer addressId, Integer companyId) {
        AdditionalAddress additionalAddress = null;
        AdditionalAddressHome additionalAddressHome = (AdditionalAddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDITIONALADDRESS);

        Collection additionalAddressDefaults = null;
        try {
            additionalAddressDefaults = additionalAddressHome.findByDefault(addressId, companyId);
            if (!additionalAddressDefaults.isEmpty()) {
                additionalAddress = (AdditionalAddress) additionalAddressDefaults.iterator().next();
            }
        } catch (FinderException e) {
            log.debug("error in found the default additional address..", e);
        }

        return additionalAddress;
    }

    private Country findCountry(Integer countryId) {
        Country country = null;
        if (countryId != null) {
            CountryHome countryHome = (CountryHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_COUNTRY);
            try {
                country = countryHome.findByPrimaryKey(countryId);
            } catch (FinderException e) {
                log.debug("Error in find country " + countryId, e);
            }
        }
        return country;
    }

    private City findCity(Integer cityId) {
        City city = null;

        if (cityId != null) {
            CityHome cityHome = (CityHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_CITY);
            try {
                city = cityHome.findByPrimaryKey(cityId);
            } catch (FinderException e) {
                log.debug("Error in find city " + cityId, e);
            }
        }
        return city;
    }

}
