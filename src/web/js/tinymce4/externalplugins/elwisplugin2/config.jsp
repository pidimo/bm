    //<%@ page language="java" contentType="text/javascript; charset=UTF-8"%>
    //<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
    //<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
    //<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>

    //to get telecom types, the user must be registered
        <c:set var="telecomTypes" value="${app2:getTelecomTypesToHTMLElwisPlugin(pageContext.request)}"/>

    // Object that will encapsulate fields of elwis data base
        MenuElwisConfig.buttonType = "button";
        MenuElwisConfig.menuType = "menu";
        MenuElwisConfig.itemType = "item";
        MenuElwisConfig.subMenuType = "subMenu";


        function MenuElwisConfig() {
            this.name = "Elwis menu";
        };

    /*compose telecom variables*/
        MenuElwisConfig.getTelecomsVariable = function(ownerTelecom) {
            var list = new Array();

        <c:forEach items="${telecomTypes}" var="telecom">
            var labelR = "${telecom.label}";

        <c:set var="translations" value="${app2:getTelecomTypeAllTranslation(telecom.value, pageContext.request)}"/>
            var translationList = new Array();
        <c:forEach items="${translations}" var="varX">
            var translationLabel = "${varX}";
            translationList.push(MenuElwisConfig.composeTelecomVarResource(ownerTelecom, translationLabel));
        </c:forEach>

            var obj = {
                type: MenuElwisConfig.itemType,
                varId: ownerTelecom + "_tel_" + "${telecom.value}",
                varResource: MenuElwisConfig.composeTelecomVarResource(ownerTelecom, labelR),
                varTranslations: translationList,
                resource: "${telecom.label}"
            };
            list.push(obj);
        </c:forEach>

            return list;
        };

    /*compose telecom variable resource*/
        MenuElwisConfig.composeTelecomVarResource = function(ownerTelecom, telecomName) {
            return ownerTelecom + "_" + telecomName.replace(/\s/g, "_");  //replace blank space
        };

    /*Elwis fields configuration*/
        MenuElwisConfig.ElwisMenu = [
            {
                type:MenuElwisConfig.buttonType,
                resource: "<fmt:message key="plugin.fields.btnDate"/>",
                tooltip: "<fmt:message key="plugin.fields.btnDate"/>",
                varId: "date",
                varResource: "<fmt:message key="template.variable.date"/>",
                ${app2:writeResourceTranslationAsJSArray("template.variable.date")}
            },{
                //contact fields
                type:MenuElwisConfig.menuType,
                resource:"<fmt:message key="plugin.templatebar.btnContactFields"/>",
                //tooltip:"Contact Fields",
                items:[ {type:MenuElwisConfig.itemType, varId:"address_names", varResource:"<fmt:message key="template.variable.address_names"/>",${app2:writeResourceTranslationAsJSArray("template.variable.address_names")}, resource:"<fmt:message key="plugin.fields_address.btnAddress_Names"/>"},
{
    type:MenuElwisConfig.itemType,varId
:
    "address_complete",varResource
:
    "<fmt:message key="template.variable.address_complete"/>",${app2:writeResourceTranslationAsJSArray("template.variable.address_complete")},resource
:
    "<fmt:message key="plugin.fields_address.btnAddress_Complete"/>"
},
{
    type:MenuElwisConfig.itemType,varId
:
    "address_street",varResource
:
    "<fmt:message key="template.variable.address_street"/>",${app2:writeResourceTranslationAsJSArray("template.variable.address_street")},resource
:
    "<fmt:message key="plugin.fields_address.btnAddress_Street"/>"
},
{
    type:MenuElwisConfig.itemType,varId
:
    "address_zip",varResource
:
    "<fmt:message key="template.variable.address_zip"/>",${app2:writeResourceTranslationAsJSArray("template.variable.address_zip")},resource
:
    "<fmt:message key="plugin.fields_address.btnAddress_Zip"/>"
},
{
    type:MenuElwisConfig.itemType,varId
:
    "address_city",varResource
:
    "<fmt:message key="template.variable.address_city"/>",${app2:writeResourceTranslationAsJSArray("template.variable.address_city")},resource
:
    "<fmt:message key="plugin.fields_address.btnAddress_City"/>"
},
{
    type:MenuElwisConfig.itemType,varId
:
    "address_countrycode",varResource
:
    "<fmt:message key="template.variable.address_countrycode"/>",${app2:writeResourceTranslationAsJSArray("template.variable.address_countrycode")},resource
:
    "<fmt:message key="plugin.fields_address.btnAddress_CountryCode"/>"
},
{
    type:MenuElwisConfig.itemType,varId
:
    "address_country",varResource
:
    "<fmt:message key="template.variable.address_country"/>",${app2:writeResourceTranslationAsJSArray("template.variable.address_country")},resource
:
    "<fmt:message key="plugin.fields_address.btnAddress_Country"/>"
},
{
    type:MenuElwisConfig.itemType,varId
:
    "customer_number",varResource
:
    "<fmt:message key="template.variable.customer_number"/>",${app2:writeResourceTranslationAsJSArray("template.variable.customer_number")},resource
:
    "<fmt:message key="plugin.fields_address.btnAddress_customerNumber"/>"
},

{
    type:MenuElwisConfig.subMenuType,resource
:
    "<fmt:message key="plugin.fields_address.btnaddress_organizations"/>",
            items
:
    [{type:MenuElwisConfig.itemType, varId:"address_name", varResource:"<fmt:message key="template.variable.address_name"/>",${app2:writeResourceTranslationAsJSArray("template.variable.address_name")}, resource
:
    "<fmt:message key="plugin.fields_address.btnAddress_Name"/>"
},
{
    type:MenuElwisConfig.itemType,varId
:
    "address_name1",varResource
:
    "<fmt:message key="template.variable.address_name1"/>",${app2:writeResourceTranslationAsJSArray("template.variable.address_name1")},resource
:
    "<fmt:message key="plugin.fields_address.btnAddress_Name1"/>"
},
{
    type:MenuElwisConfig.itemType,varId
:
    "address_name2",varResource
:
    "<fmt:message key="template.variable.address_name2"/>",${app2:writeResourceTranslationAsJSArray("template.variable.address_name2")},resource
:
    "<fmt:message key="plugin.fields_address.btnAddress_Name2"/>"
},
{
    type:MenuElwisConfig.itemType,varId
:
    "address_name3",varResource
:
    "<fmt:message key="template.variable.address_name3"/>",${app2:writeResourceTranslationAsJSArray("template.variable.address_name3")},resource
:
    "<fmt:message key="plugin.fields_address.btnAddress_Name3"/>"
}]
        },
{
    type:MenuElwisConfig.subMenuType,resource
:
    "<fmt:message key="plugin.fields_address.btnaddress_person"/>",
            items
:
    [{type:MenuElwisConfig.itemType, varId:"person_name", varResource:"<fmt:message key="template.variable.person_name"/>",${app2:writeResourceTranslationAsJSArray("template.variable.person_name")}, resource
:
    "<fmt:message key="plugin.fields_person.btnPerson_Name"/>"
},
{
    type:MenuElwisConfig.itemType,varId
:
    "person_firstname",varResource
:
    "<fmt:message key="template.variable.person_firstname"/>",${app2:writeResourceTranslationAsJSArray("template.variable.person_firstname")},resource
:
    "<fmt:message key="plugin.fields_person.btnPerson_FirstName"/>"
},
{
    type:MenuElwisConfig.itemType,varId
:
    "person_lastname",varResource
:
    "<fmt:message key="template.variable.person_lastname"/>",${app2:writeResourceTranslationAsJSArray("template.variable.person_lastname")},resource
:
    "<fmt:message key="plugin.fields_person.btnPerson_LastName"/>"
},
{
    type:MenuElwisConfig.itemType,varId
:
    "person_title",varResource
:
    "<fmt:message key="template.variable.person_title"/>",${app2:writeResourceTranslationAsJSArray("template.variable.person_title")},resource
:
    "<fmt:message key="plugin.fields_person.btnPerson_Title"/>"
},
{
    type:MenuElwisConfig.itemType,varId
:
    "person_addresssalutation",varResource
:
    "<fmt:message key="template.variable.person_addresssalutation"/>",${app2:writeResourceTranslationAsJSArray("template.variable.person_addresssalutation")},resource
:
    "<fmt:message key="plugin.fields_person.btnPerson_AddressSalutation"/>"
},
{
    type:MenuElwisConfig.itemType,varId
:
    "person_addressname",varResource
:
    "<fmt:message key="template.variable.person_addressname"/>",${app2:writeResourceTranslationAsJSArray("template.variable.person_addressname")},resource
:
    "<fmt:message key="plugin.fields_person.btnPerson_AddressName"/>"
},
{
    type:MenuElwisConfig.itemType,varId
:
    "person_lettersalutation",varResource
:
    "<fmt:message key="template.variable.person_lettersalutation"/>",${app2:writeResourceTranslationAsJSArray("template.variable.person_lettersalutation")},resource
:
    "<fmt:message key="plugin.fields_person.btnPerson_LetterSalutation"/>"
},
{
    type:MenuElwisConfig.itemType,varId
:
    "person_lettername",varResource
:
    "<fmt:message key="template.variable.person_lettername"/>",${app2:writeResourceTranslationAsJSArray("template.variable.person_lettername")},resource
:
    "<fmt:message key="plugin.fields_person.btnPerson_LetterName"/>"
}]
        },
{
    type:MenuElwisConfig.subMenuType,resource
:
    "<fmt:message key="plugin.fields_address.btnAddress_Telecoms"/>",
        //items: []///telecomsssss
            items
:
    MenuElwisConfig.getTelecomsVariable("Address")
}]
        },{
    //company fields
    type:MenuElwisConfig.menuType,
            resource
:
    "<fmt:message key="plugin.templatebar.btnCompanyFields"/>",
            items
:
    [ {type:MenuElwisConfig.itemType, varId:"company_name", varResource:"<fmt:message key="template.variable.company_name"/>",${app2:writeResourceTranslationAsJSArray("template.variable.company_name")}, resource
:
    "<fmt:message key="plugin.fields_company.btnCompany_Name"/>"
},{
    type:MenuElwisConfig.itemType,varId
:
    "company_name1",varResource
:
    "<fmt:message key="template.variable.company_name1"/>",${app2:writeResourceTranslationAsJSArray("template.variable.company_name1")},resource
:
    "<fmt:message key="plugin.fields_company.btnCompany_Name1"/>"
},{
    type:MenuElwisConfig.itemType,varId
:
    "company_name2",varResource
:
    "<fmt:message key="template.variable.company_name2"/>",${app2:writeResourceTranslationAsJSArray("template.variable.company_name2")},resource
:
    "<fmt:message key="plugin.fields_company.btnCompany_Name2"/>"
},{
    type:MenuElwisConfig.itemType,varId
:
    "company_name3",varResource
:
    "<fmt:message key="template.variable.company_name3"/>",${app2:writeResourceTranslationAsJSArray("template.variable.company_name3")},resource
:
    "<fmt:message key="plugin.fields_company.btnCompany_Name3"/>"
},{
    type:MenuElwisConfig.itemType,varId
:
    "company_address",varResource
:
    "<fmt:message key="template.variable.company_address"/>",${app2:writeResourceTranslationAsJSArray("template.variable.company_address")},resource
:
    "<fmt:message key="plugin.fields_company.btnCompany_Address"/>"
},{
    type:MenuElwisConfig.itemType,varId
:
    "company_street",varResource
:
    "<fmt:message key="template.variable.company_street"/>",${app2:writeResourceTranslationAsJSArray("template.variable.company_street")},resource
:
    "<fmt:message key="plugin.fields_company.btnCompany_Street"/>"
},{
    type:MenuElwisConfig.itemType,varId
:
    "company_zip",varResource
:
    "<fmt:message key="template.variable.company_zip"/>",${app2:writeResourceTranslationAsJSArray("template.variable.company_zip")},resource
:
    "<fmt:message key="plugin.fields_company.btnCompany_Zip"/>"
},{
    type:MenuElwisConfig.itemType,varId
:
    "company_city",varResource
:
    "<fmt:message key="template.variable.company_city"/>",${app2:writeResourceTranslationAsJSArray("template.variable.company_city")},resource
:
    "<fmt:message key="plugin.fields_company.btnCompany_City"/>"
},{
    type:MenuElwisConfig.itemType,varId
:
    "company_countrycode",varResource
:
    "<fmt:message key="template.variable.company_countrycode"/>",${app2:writeResourceTranslationAsJSArray("template.variable.company_countrycode")},resource
:
    "<fmt:message key="plugin.fields_company.btnCompany_CountryCode"/>"
},{
    type:MenuElwisConfig.itemType,varId
:
    "company_country",varResource
:
    "<fmt:message key="template.variable.company_country"/>",${app2:writeResourceTranslationAsJSArray("template.variable.company_country")},resource
:
    "<fmt:message key="plugin.fields_company.btnCompany_Country"/>"
},{
    type:MenuElwisConfig.subMenuType,resource
:
    "<fmt:message key="plugin.fields_company.btnCompany_Telecoms"/>",
            items
:
    MenuElwisConfig.getTelecomsVariable("company")
}]
        },{
    //employee fields
    type:MenuElwisConfig.menuType,
            resource
:
    "<fmt:message key="plugin.templatebar.btnEmployeeFields"/>",
            items
:
    [ {type:MenuElwisConfig.itemType, varId:"employee_name", varResource:"<fmt:message key="template.variable.employee_name"/>",${app2:writeResourceTranslationAsJSArray("template.variable.employee_name")}, resource
:
    "<fmt:message key="plugin.fields_employee.btnEmployee_Name"/>"
},{
    type:MenuElwisConfig.itemType,varId
:
    "employee_firstname",varResource
:
    "<fmt:message key="template.variable.employee_firstname"/>",${app2:writeResourceTranslationAsJSArray("template.variable.employee_firstname")},resource
:
    "<fmt:message key="plugin.fields_employee.btnEmployee_FirstName"/>"
},{
    type:MenuElwisConfig.itemType,varId
:
    "employee_lastname",varResource
:
    "<fmt:message key="template.variable.employee_lastname"/>",${app2:writeResourceTranslationAsJSArray("template.variable.employee_lastname")},resource
:
    "<fmt:message key="plugin.fields_employee.btnEmployee_LastName"/>"
},{
    type:MenuElwisConfig.itemType,varId
:
    "employee_title",varResource
:
    "<fmt:message key="template.variable.employee_title"/>",${app2:writeResourceTranslationAsJSArray("template.variable.employee_title")},resource
:
    "<fmt:message key="plugin.fields_employee.btnEmployee_Title"/>"
},{
    type:MenuElwisConfig.itemType,varId
:
    "employee_initials",varResource
:
    "<fmt:message key="template.variable.employee_initials"/>",${app2:writeResourceTranslationAsJSArray("template.variable.employee_initials")},resource
:
    "<fmt:message key="plugin.fields_employee.btnEmployee_Initials"/>"
},{
    type:MenuElwisConfig.itemType,varId
:
    "employee_function",varResource
:
    "<fmt:message key="template.variable.employee_function"/>",${app2:writeResourceTranslationAsJSArray("template.variable.employee_function")},resource
:
    "<fmt:message key="plugin.fields_employee.btnEmployee_Function"/>"
},{
    type:MenuElwisConfig.itemType,varId
:
    "employee_department",varResource
:
    "<fmt:message key="template.variable.employee_department"/>",${app2:writeResourceTranslationAsJSArray("template.variable.employee_department")},resource
:
    "<fmt:message key="plugin.fields_employee.btnEmployee_Department"/>"
},{
    type:MenuElwisConfig.subMenuType,resource
:
    "<fmt:message key="plugin.fields_employee.btnEmployee_Telecoms"/>",
            items
:
    MenuElwisConfig.getTelecomsVariable("employee")
}]
        }
        ];


