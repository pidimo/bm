<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>
<tags:jQuery/>

<script language="JavaScript" type="text/javascript">
<!--
function addHidden(indexId) {
    var email = null;
    if (null != document.getElementsByName("emailId_" + indexId)) {
        var emailSelect = document.getElementsByName("emailId_" + indexId);
        for (var i = 0; i < emailSelect.length; i++) {
            email = emailSelect[i].options[emailSelect[i].selectedIndex].value;
        }
    }

    var contactPersonId = "";
    if (null != document.getElementById("contactPersonId_" + indexId))
        contactPersonId = document.getElementById("contactPersonId_" + indexId).value;

    var addressId = "";
    if (null != document.getElementById("addressId_" + indexId))
        addressId = document.getElementById("addressId_" + indexId).value;

    var useAddressId = "";
    if (null != document.getElementById("useAddressId_" + indexId)) {
        useAddressId = document.getElementById("useAddressId_" + indexId).value;
        var addressList = window.opener.$("#addressListId");
        var valueList = $(addressList).attr("value");
        valueList += useAddressId + ",";
        $(addressList).attr("value", valueList);
    }

    var name = "";
    if (null != document.getElementById("nameId_" + indexId))
        name = document.getElementById("nameId_" + indexId).value;

    var dtoName = "addressElement_" + indexId;
    var hiddenId = "addressElementId";
    var value = escape(name) + "|" + escape(email) + "|" + addressId + "|" + contactPersonId;
    if ("" != contactPersonId)
        value = value + ",";

    var hiddenElements = new Array();

    if ("hidden" == window.opener.$("#" + hiddenId).attr("type"))
        hiddenElements[0] = window.opener.$("#" + hiddenId);
    else
        hiddenElements = window.opener.$("#" + hiddenId);

    if (null == window.opener.$("#" + hiddenId).attr("value")) {
        window.opener.$("#dynamicHiddensId").append("<input type='hidden' name='dto(" + dtoName + ")' value='" + value + "' id='" + hiddenId + "'>");
    } else {
        var isAdded = true;

        for (var j = 0; j < hiddenElements.length; j++) {
            var hiddenElement = hiddenElements[j];

            var hiddenValue = $(hiddenElement).attr("value").valueOf();

            if (hiddenValue.indexOf(escape(email)) == -1) {
                isAdded = false;
            } else {
                if (hiddenValue.indexOf("|" + addressId + "|") == -1) {
                    isAdded = false;
                } else {
                    if ("" != contactPersonId) {
                        hiddenValue += contactPersonId + ",";
                        $(hiddenElement).attr("value", hiddenValue);
                        isAdded = true;
                    }
                }
            }
        }

        if (!isAdded)
            window.opener.$("#dynamicHiddensId").append("<input type='hidden' name='dto(" + dtoName + ")' value='" + value + "' id='" + hiddenId + "'>");
    }
}

function checkAll(mainCheckBox) {
    checkElements($(mainCheckBox).attr("checked"));
}

function checkElements(option) {
    var checkboxes = new Array();
    var element = document.getElementById('listMailForm').checkBoxId;
    if ("checkbox" == element.type)
        checkboxes[0] = element;
    else
        checkboxes = document.getElementById('listMailForm').checkBoxId;

    if (null != checkboxes && checkboxes.length > 0) {
        for (var i = 0; i < checkboxes.length; i++) {
            var checkBox = checkboxes[i];
            var isDisabled = $(checkBox).attr("disabled");
            if (isDisabled) {
                continue;
            }
            if (option) {
                $(checkBox).attr("checked", true);
            } else {
                $(checkBox).attr("checked", false);
            }
        }
    }
}

function addSelectedContact() {

}

function getSelectContactMail(closeSearchWindow) {
    var checkboxes = new Array();
    var checkBoxElement = document.getElementById('listMailForm').checkBoxId;
    if (null != checkBoxElement && "checkbox" == checkBoxElement.type)
        checkboxes[0] = checkBoxElement;
    else
        checkboxes = document.getElementById('listMailForm').checkBoxId;

    var recipients = window.opener.$("#${param.nameStyle}").attr("value");
    if (null == recipients) {
        recipients = "";
    }

    if (null != checkboxes && checkboxes.length > 0) {
        for (var i = 0; i < checkboxes.length; i++) {
            var checkBox = checkboxes[i];
            var isChecked = $(checkBox).attr("checked");
            if (isChecked) {
                var checkBoxValue = $(checkBox).attr("value");

                addHidden(checkBoxValue);

                var email = null;
                if (null != document.getElementsByName("emailId_" + checkBoxValue)) {
                    var emailSelect = document.getElementsByName("emailId_" + checkBoxValue);
                    for (var j = 0; j < emailSelect.length; j++) {
                        email = emailSelect[j].options[emailSelect[j].selectedIndex].value;
                    }
                }

                var name = null;
                if (null != document.getElementById("nameId_" + checkBoxValue)) {
                    name = document.getElementById("nameId_" + checkBoxValue).value;
                }

                var element = "\"" + name + "\"" + " <" + email + ">";
                if (recipients.indexOf(element) == -1) {
                    if ("" != recipients) {
                        recipients = recipients + ", ";
                    }
                    recipients = recipients + element;
                }
            }
        }
    }

    if (closeSearchWindow) {
        opener.selectField("${param.idStyle}", "unused", "${param.nameStyle}", recipients);
    } else {
        opener.addAddressField("${param.idStyle}", "unused", "${param.nameStyle}", recipients);
        cleanSelectedCheckBoxes(checkboxes);
        $("#searchInputTextId").attr("value", "");
        $("#selectAndNewId").attr("value", "true");
        document.forms[0].submit();
    }
}

function cleanSelectedCheckBoxes(checkBoxes) {
    if (null != checkBoxes && checkBoxes.length > 0) {
        for (var i = 0; i < checkBoxes.length; i++) {
            var checkBox = checkBoxes[i];
            var isChecked = $(checkBox).attr("checked");
            if (isChecked) {
                $(checkBox).attr("checked", false);
            }
        }
    }
}

function disabledCheckbox() {
    var checkboxes = new Array();
    var element = document.getElementById('listMailForm').checkBoxId;
    if (null != element && "checkbox" == element.type)
        checkboxes[0] = element;
    else
        checkboxes = document.getElementById('listMailForm').checkBoxId;

    if (null != checkboxes && checkboxes.length > 0) {
        for (var i = 0; i < checkboxes.length; i++) {
            var checkBox = checkboxes[i];
            var checkBoxValue = $(checkBox).attr("value");
            var emailSelectCounter = document.getElementsByName("emailId_" + checkBoxValue).length;
            if (0 == emailSelectCounter) {
                $(checkBox).attr("disabled", true);
            }
        }
    }
}

//fanta table paginator require this function
function setSubmit(issubmit) {
    return issubmit;
}
//-->
</script>

<%--Size of the list--%>
<%
    org.alfacentauro.fantabulous.controller.ResultList resultList = (org.alfacentauro.fantabulous.controller.ResultList) request.getAttribute("allAddressList");
    if (resultList != null) {
        pageContext.setAttribute("list_size", new Integer(resultList.getResultSize()));
    } else {
        pageContext.setAttribute("list_size", new Integer("0"));
    }
%>
<c:set var="url" value="Mail/ImportAddress.do?idStyle=${param.idStyle}&nameStyle=${param.nameStyle}"/>

<c:set var="TELECOMTYPE_EMAIL" value="<%=ContactConstants.TELECOMTYPE_EMAIL%>"/>
<c:set var="personType"><%= ContactConstants.ADDRESSTYPE_PERSON %>
</c:set>

<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td class="title">
            <fmt:message key="Webmail.Contact.searchContactsOrContactPerson"/>
        </td>
    </tr>
    <tr>
        <td>
            <table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">
                <html:form action="/${url}"
                           focus="parameter(contactSearchName@_contactPersonOfSearchName)">
                    <html:hidden property="parameter(selectAndNewButton)" value="false" styleId="selectAndNewId"/>
                    <TR>
                        <TD class="label" width="10%">
                            <fmt:message key="Common.for"/>
                        </TD>
                        <td class="contain" width="90%">
                            <html:text
                                    property="parameter(contactSearchName@_contactPersonOfSearchName)"
                                    styleClass="largeText" styleId="searchInputTextId"/>
                            <html:submit styleClass="button">
                                <fmt:message key="Common.go"/>
                            </html:submit>
                        </td>
                    </TR>
                    <tr>
                        <td colspan="4" align="center" class="alpha">
                            <fanta:alphabet action="${url}" parameterName="name1A1"/>
                        </td>
                    </tr>
                </html:form>
            </table>
        </td>
    </tr>
    <tr>
        <td>
            <table width="100%" border="0" align="center" cellpadding="3" cellspacing="1" class="container">
                <html:form action="/${url}" styleId="listMailForm" onsubmit="return false;">
                    <tr>
                        <td class="button">
                            <html:button property="" styleClass="button"
                                         onclick="javascript:getSelectContactMail(true);">
                                <fmt:message key="Common.select"/>
                            </html:button>
                            <html:button property="" styleClass="button"
                                         onclick="javascript:getSelectContactMail(false);">
                                <fmt:message key="Common.selectAndNew"/>
                            </html:button>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <fanta:table list="allAddressList"
                                         width="100%"
                                         id="contact"
                                         action="${url}"
                                         imgPath="${baselayout}"
                                         align="center"
                                         withCheckBox="true"
                                         withContext="false">

                                <c:if test="${list_size >0}">
                                    <fanta:checkBoxColumn styleClass="radio listItemCenter"
                                                          name="mail"
                                                          id="checkBoxId"
                                                          onClick="javascript:checkAll(this);"
                                                          property="checkBoxIdentifier"
                                                          headerStyle="listHeader"
                                                          width="2%"/>
                                </c:if>

                                <fanta:dataColumn name="" label="" styleClass="listItem" headerStyle="listHeader"
                                                  width="3%" renderData="false">

                                    <c:choose>
                                        <c:when test="${not empty contact.contactPersonAddressId}">
                                            <c:set var="personPrefixImageName" value="person"/>
                                        </c:when>
                                        <c:otherwise>
                                            <c:set var="personPrefixImageName" value="person-private"/>
                                        </c:otherwise>
                                    </c:choose>

                                    <html:img
                                            src="${baselayout}/img/${contact.addressType == personType? personPrefixImageName : 'org'}.gif"
                                            border="0"/>

                                </fanta:dataColumn>
                                <fanta:dataColumn name="addressName1" styleClass="listItem" title="Contact.name"
                                                  headerStyle="listHeader" width="25%" orderable="true"/>
                                <fanta:dataColumn name="addressName2" styleClass="listItem"
                                                  title="ContactPerson.contactName" headerStyle="listHeader" width="27%"
                                                  orderable="true"/>
                                <fanta:dataColumn name="countryCode" styleClass="listItem" title="Contact.countryCode"
                                                  headerStyle="listHeader" width="8%" orderable="true"/>
                                <fanta:dataColumn name="zip" styleClass="listItem" title="Contact.zip"
                                                  headerStyle="listHeader" width="7%" orderable="true"/>
                                <fanta:dataColumn name="cityName" styleClass="listItem" title="Contact.city"
                                                  headerStyle="listHeader" width="10%" orderable="true"/>
                                <fanta:dataColumn name="addressId" styleClass="listItem2"
                                                  title="Webmail.mailContact.email" headerStyle="listHeader" width="18%"
                                                  renderData="false">
                                    <html:hidden property="addressId_${contact.checkBoxIdentifier}"
                                                 value="${contact.addressId}"
                                                 styleId="addressId_${contact.checkBoxIdentifier}"/>
                                    <c:choose>
                                        <c:when test="${contact.contactPersonAddressId!='' && contact.contactPersonAddressId!=null}">
                                            <app:telecomSelect property="emailId_${contact.checkBoxIdentifier}"
                                                               numberColumn="telecomnumber"
                                                               telecomType="${TELECOMTYPE_EMAIL}"
                                                               resultIsEmptyKey="Webmail.Address.withoutEmails"
                                                               addressId="${contact.contactPersonAddressId}"
                                                               contactPersonId="${contact.addressId}"
                                                               styleClass="select" optionStyleClass="list"
                                                               showDescription="false" selectPredetermined="true"/>
                                            <html:hidden property="contactPersonId_${contact.checkBoxIdentifier}"
                                                         value="${contact.contactPersonAddressId}"
                                                         styleId="contactPersonId_${contact.checkBoxIdentifier}"/>
                                        </c:when>
                                        <c:otherwise>
                                            <app:telecomSelect property="emailId_${contact.checkBoxIdentifier}"
                                                               numberColumn="telecomnumber"
                                                               telecomType="${TELECOMTYPE_EMAIL}"
                                                               resultIsEmptyKey="Webmail.Address.withoutEmails"
                                                               addressId="${contact.addressId}" styleClass="select"
                                                               optionStyleClass="list" showDescription="false"
                                                               selectPredetermined="true"/>
                                            <html:hidden property="useAddressId_${contact.checkBoxIdentifier}"
                                                         value="${contact.addressId}"
                                                         styleId="useAddressId_${contact.checkBoxIdentifier}"/>
                                        </c:otherwise>
                                    </c:choose>
                                </fanta:dataColumn>

                                <html:hidden property="nameId_${contact.checkBoxIdentifier}"
                                             value="${contact.addressName1}"
                                             styleId="nameId_${contact.checkBoxIdentifier}"/>
                            </fanta:table>
                        </td>
                    </tr>
                    <tr>
                        <td class="button">
                            <html:button property="" styleClass="button"
                                         onclick="javascript:getSelectContactMail(true);">
                                <fmt:message key="Common.select"/>
                            </html:button>
                            <html:button property="" styleClass="button"
                                         onclick="javascript:getSelectContactMail(false);">
                                <fmt:message key="Common.selectAndNew"/>
                            </html:button>
                        </td>
                    </tr>
                </html:form>
            </table>
        </td>
    </tr>
</table>

<script language="JavaScript">
    <!--
    //disable checkbox
    disabledCheckbox();
    //-->
</script>
