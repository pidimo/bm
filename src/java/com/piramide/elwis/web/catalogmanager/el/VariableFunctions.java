package com.piramide.elwis.web.catalogmanager.el;

import com.piramide.elwis.cmd.utils.VariableConstants;
import com.piramide.elwis.dto.catalogmanager.CategoryDTO;
import com.piramide.elwis.dto.catalogmanager.categoryUtil.WebCategoryGroup;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.web.common.util.JSPHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.util.LabelValueBean;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.5
 */
public class VariableFunctions {

    private static Log log = LogFactory.getLog(VariableFunctions.class);

    private static List<Map> getWebDocumentContactFields(HttpServletRequest request) {
        List<Map> result = new ArrayList<Map>();

        List<LabelValueBean> generalList = new ArrayList<LabelValueBean>();
        generalList.add(new LabelValueBean(JSPHelper.getMessage(request, "plugin.fields.btnDate"), VariableConstants.ADDRESS_PERSON_FIELDS[VariableConstants.FIELD_DATE]));
        generalList.add(new LabelValueBean(JSPHelper.getMessage(request, "plugin.fields_address.btnAddress_Names"), VariableConstants.ADDRESS_PERSON_FIELDS[VariableConstants.FIELD_ADDRESS_NAMES]));
        generalList.add(new LabelValueBean(JSPHelper.getMessage(request, "plugin.fields_address.btnAddress_Complete"), VariableConstants.ADDRESS_PERSON_FIELDS[VariableConstants.FIELD_ADDRESS_COMPLETE]));
        generalList.add(new LabelValueBean(JSPHelper.getMessage(request, "plugin.fields_address.btnAddress_Street"), VariableConstants.ADDRESS_PERSON_FIELDS[VariableConstants.FIELD_ADDRESS_STREET]));
        generalList.add(new LabelValueBean(JSPHelper.getMessage(request, "plugin.fields_address.btnAddress_Zip"), VariableConstants.ADDRESS_PERSON_FIELDS[VariableConstants.FIELD_ADDRESS_ZIP]));
        generalList.add(new LabelValueBean(JSPHelper.getMessage(request, "plugin.fields_address.btnAddress_City"), VariableConstants.ADDRESS_PERSON_FIELDS[VariableConstants.FIELD_ADDRESS_CITY]));
        generalList.add(new LabelValueBean(JSPHelper.getMessage(request, "plugin.fields_address.btnAddress_CountryCode"), VariableConstants.ADDRESS_PERSON_FIELDS[VariableConstants.FIELD_ADDRESS_COUNTRYCODE]));
        generalList.add(new LabelValueBean(JSPHelper.getMessage(request, "plugin.fields_address.btnAddress_Country"), VariableConstants.ADDRESS_PERSON_FIELDS[VariableConstants.FIELD_ADDRESS_COUNTRY]));
        generalList.add(new LabelValueBean(JSPHelper.getMessage(request, "plugin.fields_address.btnAddress_customerNumber"), VariableConstants.ADDRESS_PERSON_FIELDS[VariableConstants.FIELD_ADDRESS_CUSTOMERNUMBER]));

        List<LabelValueBean> organizationList = new ArrayList<LabelValueBean>();
        organizationList.add(new LabelValueBean(JSPHelper.getMessage(request, "plugin.fields_address.btnAddress_Name"), VariableConstants.ADDRESS_PERSON_FIELDS[VariableConstants.FIELD_ADDRESS_NAME]));
        organizationList.add(new LabelValueBean(JSPHelper.getMessage(request, "plugin.fields_address.btnAddress_Name1"), VariableConstants.ADDRESS_PERSON_FIELDS[VariableConstants.FIELD_ADDRESS_NAME1]));
        organizationList.add(new LabelValueBean(JSPHelper.getMessage(request, "plugin.fields_address.btnAddress_Name2"), VariableConstants.ADDRESS_PERSON_FIELDS[VariableConstants.FIELD_ADDRESS_NAME2]));
        organizationList.add(new LabelValueBean(JSPHelper.getMessage(request, "plugin.fields_address.btnAddress_Name3"), VariableConstants.ADDRESS_PERSON_FIELDS[VariableConstants.FIELD_ADDRESS_NAME3]));

        List<LabelValueBean> personList = new ArrayList<LabelValueBean>();
        personList.add(new LabelValueBean(JSPHelper.getMessage(request, "plugin.fields_person.btnPerson_Name"), VariableConstants.ADDRESS_PERSON_FIELDS[VariableConstants.FIELD_PERSON_NAME]));
        personList.add(new LabelValueBean(JSPHelper.getMessage(request, "plugin.fields_person.btnPerson_FirstName"), VariableConstants.ADDRESS_PERSON_FIELDS[VariableConstants.FIELD_PERSON_FIRSTNAME]));
        personList.add(new LabelValueBean(JSPHelper.getMessage(request, "plugin.fields_person.btnPerson_LastName"), VariableConstants.ADDRESS_PERSON_FIELDS[VariableConstants.FIELD_PERSON_LASTNAME]));
        personList.add(new LabelValueBean(JSPHelper.getMessage(request, "plugin.fields_person.btnPerson_Title"), VariableConstants.ADDRESS_PERSON_FIELDS[VariableConstants.FIELD_PERSON_TITLE]));
        personList.add(new LabelValueBean(JSPHelper.getMessage(request, "plugin.fields_person.btnPerson_AddressSalutation"), VariableConstants.ADDRESS_PERSON_FIELDS[VariableConstants.FIELD_PERSON_ADDRESSSALUTATION]));
        personList.add(new LabelValueBean(JSPHelper.getMessage(request, "plugin.fields_person.btnPerson_AddressName"), VariableConstants.ADDRESS_PERSON_FIELDS[VariableConstants.FIELD_PERSON_ADDRESSNAME]));
        personList.add(new LabelValueBean(JSPHelper.getMessage(request, "plugin.fields_person.btnPerson_LetterSalutation"), VariableConstants.ADDRESS_PERSON_FIELDS[VariableConstants.FIELD_PERSON_LETTERSALUTATION]));
        personList.add(new LabelValueBean(JSPHelper.getMessage(request, "plugin.fields_person.btnPerson_LetterName"), VariableConstants.ADDRESS_PERSON_FIELDS[VariableConstants.FIELD_PERSON_LETTERNAME]));

        List telecomTypesList = com.piramide.elwis.web.campaignmanager.el.Functions.getTelecomTypesToHTMLElwisPlugin(request);
        for (Iterator iterator = telecomTypesList.iterator(); iterator.hasNext();) {
            LabelValueBean labelValueBean = (LabelValueBean) iterator.next();
            labelValueBean.setValue("Address_tel_" + labelValueBean.getValue());
        }

        String organizationTitle = JSPHelper.getMessage(request, "plugin.fields_address.btnaddress_organizations");
        String personTitle = JSPHelper.getMessage(request, "plugin.fields_address.btnaddress_person");
        String telecomsTitle = JSPHelper.getMessage(request, "plugin.fields_address.btnAddress_Telecoms");

        result.add(composeGroupFieldsMap(null, generalList));
        result.add(composeGroupFieldsMap(organizationTitle, organizationList));
        result.add(composeGroupFieldsMap(personTitle, personList));
        result.add(composeGroupFieldsMap(telecomsTitle, telecomTypesList));

        //add category fields
        result.addAll(composeCategoryFields(ContactConstants.ADDRESS_CATEGORY, ContactConstants.ADDRESS_CONTACTPERSON_CATEGORY, request, VariableConstants.FIELD_CATEGORY_ADDRESS_PREFIX));

        return result;
    }

    private static List<Map> composeCategoryFields(String table, String secondTable, HttpServletRequest request, String fieldPrefix) {
        List<Map> result = new ArrayList<Map>();

        List<Integer> categoryIdList = Functions.buildCategoriesWithoutGroups(table, secondTable, request);

        List<LabelValueBean> generalCategoryList = getCategoriesAsFields(table, categoryIdList, request, fieldPrefix);
        String categoriesTitle = JSPHelper.getMessage(request, "Webparameter.variableGroup.categories");
        result.add(composeGroupFieldsMap(categoriesTitle, generalCategoryList));

        //load group categories
        List<WebCategoryGroup> categoryGroupList = Functions.buildGroupsWithoutTabs(table, request);
        for (WebCategoryGroup webCategoryGroup : categoryGroupList) {
            List<LabelValueBean> groupCategoryFieldList = getCategoriesAsFields(table, webCategoryGroup.getCategories(), request, fieldPrefix);
            result.add(composeGroupFieldsMap(webCategoryGroup.getLabel(), groupCategoryFieldList));
        }

        return result;
    }

    private static List<LabelValueBean> getCategoriesAsFields(String table, List<Integer> categoryIdList, HttpServletRequest request, String fieldPrefix) {
        List<LabelValueBean> result = new ArrayList<LabelValueBean>();

        for (Integer categoryId : categoryIdList) {
            CategoryDTO categoryDTO = Functions.getCategory(categoryId.toString(), table, request);
            result.add(new LabelValueBean((String) categoryDTO.get("categoryName"), fieldPrefix + categoryId.toString()));
        }
        return result;
    }

    private static Map composeGroupFieldsMap(String title, List<LabelValueBean> fields) {
        Map map = new HashMap();
        map.put("variableFieldList", fields);

        if (title != null) {
            map.put("groupTitle", title);
        }
        return map;
    }

    private static List<Map> getWebDocumentCompanyFields(HttpServletRequest request) {
        List<Map> result = new ArrayList<Map>();

        List<LabelValueBean> generalList = new ArrayList<LabelValueBean>();
        generalList.add(new LabelValueBean(JSPHelper.getMessage(request, "plugin.fields_company.btnCompany_Name"), VariableConstants.COMPANY_EMPLOYEE_FIELDS[VariableConstants.FIELD_COMPANY_NAME]));
        generalList.add(new LabelValueBean(JSPHelper.getMessage(request, "plugin.fields_company.btnCompany_Name1"), VariableConstants.COMPANY_EMPLOYEE_FIELDS[VariableConstants.FIELD_COMPANY_NAME1]));
        generalList.add(new LabelValueBean(JSPHelper.getMessage(request, "plugin.fields_company.btnCompany_Name2"), VariableConstants.COMPANY_EMPLOYEE_FIELDS[VariableConstants.FIELD_COMPANY_NAME2]));
        generalList.add(new LabelValueBean(JSPHelper.getMessage(request, "plugin.fields_company.btnCompany_Name3"), VariableConstants.COMPANY_EMPLOYEE_FIELDS[VariableConstants.FIELD_COMPANY_NAME3]));
        generalList.add(new LabelValueBean(JSPHelper.getMessage(request, "plugin.fields_company.btnCompany_Address"), VariableConstants.COMPANY_EMPLOYEE_FIELDS[VariableConstants.FIELD_COMPANY_ADDRESS]));
        generalList.add(new LabelValueBean(JSPHelper.getMessage(request, "plugin.fields_company.btnCompany_Street"), VariableConstants.COMPANY_EMPLOYEE_FIELDS[VariableConstants.FIELD_COMPANY_STREET]));
        generalList.add(new LabelValueBean(JSPHelper.getMessage(request, "plugin.fields_company.btnCompany_Zip"), VariableConstants.COMPANY_EMPLOYEE_FIELDS[VariableConstants.FIELD_COMPANY_ZIP]));
        generalList.add(new LabelValueBean(JSPHelper.getMessage(request, "plugin.fields_company.btnCompany_City"), VariableConstants.COMPANY_EMPLOYEE_FIELDS[VariableConstants.FIELD_COMPANY_CITY]));
        generalList.add(new LabelValueBean(JSPHelper.getMessage(request, "plugin.fields_company.btnCompany_CountryCode"), VariableConstants.COMPANY_EMPLOYEE_FIELDS[VariableConstants.FIELD_COMPANY_COUNTRYCODE]));
        generalList.add(new LabelValueBean(JSPHelper.getMessage(request, "plugin.fields_company.btnCompany_Country"), VariableConstants.COMPANY_EMPLOYEE_FIELDS[VariableConstants.FIELD_COMPANY_COUNTRY]));

        List telecomTypesList = com.piramide.elwis.web.campaignmanager.el.Functions.getTelecomTypesToHTMLElwisPlugin(request);
        for (Iterator iterator = telecomTypesList.iterator(); iterator.hasNext();) {
            LabelValueBean labelValueBean = (LabelValueBean) iterator.next();
            labelValueBean.setValue("company_tel_" + labelValueBean.getValue());
        }

        String telecomsTitle = JSPHelper.getMessage(request, "plugin.fields_company.btnCompany_Telecoms");

        result.add(composeGroupFieldsMap(null, generalList));
        result.add(composeGroupFieldsMap(telecomsTitle, telecomTypesList));

        return result;
    }

    private static List<Map> getWebDocumentEmployeeFields(HttpServletRequest request) {
        List<Map> result = new ArrayList<Map>();

        List<LabelValueBean> generalList = new ArrayList<LabelValueBean>();
        generalList.add(new LabelValueBean(JSPHelper.getMessage(request, "plugin.fields_employee.btnEmployee_Name"), VariableConstants.COMPANY_EMPLOYEE_FIELDS[VariableConstants.FIELD_EMPLOYEE_NAME]));
        generalList.add(new LabelValueBean(JSPHelper.getMessage(request, "plugin.fields_employee.btnEmployee_FirstName"), VariableConstants.COMPANY_EMPLOYEE_FIELDS[VariableConstants.FIELD_EMPLOYEE_FIRSTNAME]));
        generalList.add(new LabelValueBean(JSPHelper.getMessage(request, "plugin.fields_employee.btnEmployee_LastName"), VariableConstants.COMPANY_EMPLOYEE_FIELDS[VariableConstants.FIELD_EMPLOYEE_LASTNAME]));
        generalList.add(new LabelValueBean(JSPHelper.getMessage(request, "plugin.fields_employee.btnEmployee_Title"), VariableConstants.COMPANY_EMPLOYEE_FIELDS[VariableConstants.FIELD_EMPLOYEE_TITLE]));
        generalList.add(new LabelValueBean(JSPHelper.getMessage(request, "plugin.fields_employee.btnEmployee_Initials"), VariableConstants.COMPANY_EMPLOYEE_FIELDS[VariableConstants.FIELD_EMPLOYEE_INITIALS]));
        generalList.add(new LabelValueBean(JSPHelper.getMessage(request, "plugin.fields_employee.btnEmployee_Function"), VariableConstants.COMPANY_EMPLOYEE_FIELDS[VariableConstants.FIELD_EMPLOYEE_FUNCTION]));
        generalList.add(new LabelValueBean(JSPHelper.getMessage(request, "plugin.fields_employee.btnEmployee_Department"), VariableConstants.COMPANY_EMPLOYEE_FIELDS[VariableConstants.FIELD_EMPLOYEE_DEPARTMENT]));

        List telecomTypesList = com.piramide.elwis.web.campaignmanager.el.Functions.getTelecomTypesToHTMLElwisPlugin(request);
        for (Iterator iterator = telecomTypesList.iterator(); iterator.hasNext();) {
            LabelValueBean labelValueBean = (LabelValueBean) iterator.next();
            labelValueBean.setValue("employee_tel_" + labelValueBean.getValue());
        }

        String telecomsTitle = JSPHelper.getMessage(request, "plugin.fields_employee.btnEmployee_Telecoms");

        result.add(composeGroupFieldsMap(null, generalList));
        result.add(composeGroupFieldsMap(telecomsTitle, telecomTypesList));

        return result;
    }

    public static List<Map> getWebDocumentFieldsByVariableType(String variableTypeConstant, HttpServletRequest request) {
        List<Map> result = new ArrayList<Map>();

        VariableConstants.VariableType variableType = VariableConstants.VariableType.findVariableType(variableTypeConstant);
        if (variableType != null) {
            if (VariableConstants.VariableType.CONTACT.equals(variableType)) {
                result = getWebDocumentContactFields(request);
            } else if (VariableConstants.VariableType.COMPANY.equals(variableType)) {
                result = getWebDocumentCompanyFields(request);
            } else if (VariableConstants.VariableType.EMPLOYEE.equals(variableType)) {
                result = getWebDocumentEmployeeFields(request);
            }
        }

        return result;
    }

}
