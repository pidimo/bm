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
            var addressList = parent.$("#addressListId");
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

        if ("hidden" == parent.$("#" + hiddenId).attr("type"))
            hiddenElements[0] = parent.$("#" + hiddenId);
        else
            hiddenElements = parent.$("#" + hiddenId);

        if (null == parent.$("#" + hiddenId).attr("value")) {
            parent.$("#dynamicHiddensId").append("<input type='hidden' name='dto(" + dtoName + ")' value='" + value + "' id='" + hiddenId + "'>");
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
                parent.$("#dynamicHiddensId").append("<input type='hidden' name='dto(" + dtoName + ")' value='" + value + "' id='" + hiddenId + "'>");
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

    /**
     @deprecated use instead processSelectedContactMail
     */
    function getSelectContactMail(closeSearchWindow) {
        var checkboxes = new Array();
        var checkBoxElement = document.getElementById('listMailForm').checkBoxId;
        if (null != checkBoxElement && "checkbox" == checkBoxElement.type)
            checkboxes[0] = checkBoxElement;
        else
            checkboxes = document.getElementById('listMailForm').checkBoxId;

        var recipients =  $(window.parent.document).find('#'+'${param.nameStyle}').attr("value");

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
            parent.selectField("${param.idStyle}", "unused", "${param.nameStyle}", recipients);
        } else {
            parent.addAddressField("${param.idStyle}", "unused", "${param.nameStyle}", recipients);
            cleanSelectedCheckBoxes(checkboxes);
            $("#searchInputTextId").attr("value", "");
            $("#selectAndNewId").attr("value", "true");
            document.forms[0].submit();
        }
    }

    function processSelectedContactMail(closeSearchWindow) {
        var checkboxes = new Array();
        var checkBoxElement = document.getElementById('listMailForm').checkBoxId;
        if (null != checkBoxElement && "checkbox" == checkBoxElement.type)
            checkboxes[0] = checkBoxElement;
        else
            checkboxes = document.getElementById('listMailForm').checkBoxId;

        if (null != checkboxes && checkboxes.length > 0) {
            for (var i = 0; i < checkboxes.length; i++) {
                var checkBox = checkboxes[i];
                var isChecked = $(checkBox).attr("checked");
                if (isChecked) {
                    var checkBoxValue = $(checkBox).attr("value");

                    addSelectedAsTokenField(checkBoxValue);
                }
            }
        }

        if (closeSearchWindow) {
            parent.hideBootstrapPopup();
        } else {
            cleanSelectedCheckBoxes(checkboxes);
            $("#searchInputTextId").attr("value", "");
            $("#selectAndNewId").attr("value", "true");
            document.forms[0].submit();
        }
    }

    function addSelectedAsTokenField(indexId) {
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
            var addressList = parent.$("#addressListId");
            var valueList = $(addressList).attr("value");
            valueList += useAddressId + ",";
            $(addressList).attr("value", valueList);
        }

        var name = "";
        if (null != document.getElementById("nameId_" + indexId)) {
            name = document.getElementById("nameId_" + indexId).value;
        }

        //add as token field in parent
        parent.addTokenField("${param.nameStyle}", encodeURI(email), encodeURI(name), addressId, contactPersonId);
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

<html:form action="/${url}"
           focus="parameter(contactSearchName@_contactPersonOfSearchName)" styleClass="form-horizontal">
    <html:hidden property="parameter(selectAndNewButton)" value="false" styleId="selectAndNewId"/>
    <div class="${app2:getSearchWrapperClasses()}">
        <label class="${app2:getFormLabelOneSearchInput()} label-left">
            <fmt:message key="Common.for"/>
        </label>

        <div class="${app2:getFormOneSearchInput()}">
            <div class="input-group">
                <html:text
                        property="parameter(contactSearchName@_contactPersonOfSearchName)"
                        styleClass="${app2:getFormInputClasses()} largeText" styleId="searchInputTextId"/>
                <div class="input-group-btn">
                    <html:submit styleClass="${app2:getFormButtonClasses()}">
                        <fmt:message key="Common.go"/>
                    </html:submit>
                </div>
            </div>
        </div>
    </div>

    <div class="${app2:getAlphabetWrapperClasses()}">
        <fanta:alphabet action="${url}" parameterName="name1A1" mode="bootstrap"/>
    </div>

</html:form>


<html:form action="/${url}" styleId="listMailForm" onsubmit="return false;">
    <div class="${app2:getFormGroupClasses()}">
        <html:button property="" styleClass="${app2:getFormButtonClasses()} marginButton"
                     onclick="javascript:processSelectedContactMail(true);">
            <fmt:message key="Common.select"/>
        </html:button>
        <html:button property="" styleClass="${app2:getFormButtonClasses()} marginButton"
                     onclick="javascript:processSelectedContactMail(false);">
            <fmt:message key="Common.selectAndNew"/>
        </html:button>
    </div>
    <div class="table-responsive">
        <fanta:table mode="bootstrap" list="allAddressList"
                     styleClass="${app2:getFantabulousTableClases()}"
                     width="100%"
                     id="contact"
                     action="${url}"
                     imgPath="${baselayout}"
                     align="center"
                     withCheckBox="true"
                     withContext="false">

            <c:if test="${list_size >0}">
                <fanta:checkBoxColumn styleClass="listItemCenter"
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
                        <c:set var="personPrefixImageName" value="${app2:getClassGlyphPerson()}"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="personPrefixImageName" value="${app2:getClassGlyphPrivatePerson()}"/>
                    </c:otherwise>
                </c:choose>

                <span class="${contact.addressType == personType? personPrefixImageName : app2:getClassGlyphOrganization()}"></span>

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
                                           styleClass="${app2:getFormSelectClasses()}" optionStyleClass="list"
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
                                           addressId="${contact.addressId}" styleClass="${app2:getFormSelectClasses()}"
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
    </div>
    <div class="${app2:getFormGroupClasses()}">
        <html:button property="" styleClass="${app2:getFormButtonClasses()} marginButton"
                     onclick="javascript:processSelectedContactMail(true);">
            <fmt:message key="Common.select"/>
        </html:button>
        <html:button property="" styleClass="${app2:getFormButtonClasses()} marginButton"
                     onclick="javascript:processSelectedContactMail(false);">
            <fmt:message key="Common.selectAndNew"/>
        </html:button>
    </div>

</html:form>


<script language="JavaScript">
    <!--
    //disable checkbox
    disabledCheckbox();
    //-->
</script>
