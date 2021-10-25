<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>

<script>
    <!--
    function lib_getObj(id, d) {
        var i, x;
        if (!d) d = document;
        if (!(x = d[id]) && d.all) x = d.all[id];
        for (i = 0; !x && i < d.forms.length; i++) x = d.forms[i][id];
        for (i = 0; !x && d.layers && i < d.layers.length; i++) x = lib_getObj(id, d.layers[i].document);
        if (!x && document.getElementById) x = document.getElementById(id);
        return x;
    }
    ;

    function check() {
        field = document.getElementById('addressGroupFormX').selectedMails;
        guia = document.getElementById('addressGroupFormX').mail;

        var i;
        if (guia.checked) {
            for (i = 0; i < field.length; i++) {
                if (!field[i].disabled)
                    field[i].checked = true;
            }
        } else {
            for (i = 0; i < field.length; i++) {
                if (!field[i].disabled)
                    field[i].checked = false;
            }
        }
    }


    function replaceOne(text, textSearched, newString) {

        while (text.indexOf(textSearched) > -1) {
            pos = text.indexOf(textSearched);
            text = "" + (text.substring(0, pos) + newString + text.substring((pos + textSearched.length), text.length));
        }
        text = text + textSearched;
        return text;

    }


    function getSelectContactMail() {
        var checkboxes = document.getElementById('addressGroupFormX').selectedMails; //Array content the checkbox
        var dirEmails = document.getElementById('addressGroupFormX').dirAddressEmails; //Array content email dir
        var mails = '';

        //concatenate
        if (checkboxes != null) {
            if (checkboxes.length > 0) {
                for (var x = 0; x < checkboxes.length; x++) {
                    if (checkboxes[x].checked && lib_getObj('dirAddressEmails_' + checkboxes[x].value) != null) {
                        mails = mails + lib_getObj('dirAddressEmails_' + checkboxes[x].value).value + '%*%';
                    }
                }
            }
            else {
                if (checkboxes.checked && lib_getObj('dirAddressEmails_' + checkboxes.value) != null) {
                    mails = mails + lib_getObj('dirAddressEmails_' + checkboxes.value).value + '%*%';
                }
            }
        }
        document.getElementById('addressGroupFormX').selectedEmails.value = mails;
        document.getElementById('AddHidden').value = true;
        document.getElementById('addressGroupFormX').submit();
    }


    function disabledCheckbox() {
        var checkboxes = document.getElementById('addressGroupFormX').selectedMails; //Array content the checkbox
        cad = "";
        if (checkboxes != null) {
            if (checkboxes.length > 0) {
                for (var x = 0; x < checkboxes.length; x++) {
                    if (lib_getObj('dirAddressEmails_' + checkboxes[x].value) == null) {
                        checkboxes[x].disabled = true;
                    }
                }
            } else {
                if (lib_getObj('dirAddressEmails_' + checkboxes.value) == null) {
                    checkboxes.disabled = true;
                }
            }
        }
    }
    function testSubmit() {
        var ss = document.getElementById('isSubmit').value;
        var issubmit = 'true' == ss;
        return issubmit;
    }

    function setSubmit(issubmit) {
        document.getElementById('isSubmit').value = "" + issubmit;
        var ss = document.getElementById('isSubmit').value;
    }
    //-->
</script>
<%
    pageContext.setAttribute("searchTypeList", JSPHelper.getSearchTypeList(request));
%>

<%--Size of the list--%>
<%
    org.alfacentauro.fantabulous.controller.ResultList resultList = (org.alfacentauro.fantabulous.controller.ResultList) request.getAttribute("allAddressList");
    if (resultList != null) {
        pageContext.setAttribute("list_size", new Integer(resultList.getResultSize()));
    } else {
        pageContext.setAttribute("list_size", new Integer("0"));
    }
%>

<c:set var="TELECOMTYPE_EMAIL" value="<%=ContactConstants.TELECOMTYPE_EMAIL%>"/>
<c:set var="personType"><%= ContactConstants.ADDRESSTYPE_PERSON %>
</c:set>

<app:url var="urlCreateAddress" value="/Mail/Forward/CreateAddress.do?dto(mailGroupAddrId)=${mailGroupAddrId}"/>

<app:url var="urlCancel" value="/Mail/AddressGroupList.do?dto(mailGroupAddrId)=${mailGroupAddrId}"/>

<div class="${app2:getListWrapperClasses()}">

    <label class="optionTitle">
        <fmt:message key="Webmail.addressGroup.addToGroup"/>: ${groupName}
    </label>

    <html:form action="/Mail/Forward/AddAddressGroup.do?dto(mailGroupAddrId)=${mailGroupAddrId}"
               focus="parameter(contactSearchName@_contactPersonOfSearchName)" styleClass="form-horizontal">
        <fieldset>
            <legend class="title">
                <fmt:message key="Webmail.Contact.searchContactsOrContactPerson"/>
            </legend>
            <div class="${app2:getSearchWrapperClasses()}">
                <label class="${app2:getFormLabelOneSearchInput()} label-left">
                    <fmt:message key="Common.for"/>
                </label>

                <div class="${app2:getFormOneSearchInput()}">
                    <div class="input-group">
                        <html:text property="parameter(contactSearchName@_contactPersonOfSearchName)"
                                   styleClass="${app2:getFormInputClasses()} largeText"/>
                        <div class="input-group-btn">
                            <html:submit styleClass="${app2:getFormButtonClasses()}">
                                <fmt:message key="Common.go"/>
                            </html:submit>
                        </div>
                    </div>
                </div>
            </div>
        </fieldset>
    </html:form>

    <div class="${app2:getAlphabetWrapperClasses()}">
        <fanta:alphabet action="Mail/Forward/AddAddressGroup.do?dto(mailGroupAddrId)=${mailGroupAddrId}"
                        parameterName="name1A1" mode="bootstrap"/>
    </div>

    <html:form action="/Mail/Forward/AddAddressGroup.do?dto(mailGroupAddrId)=${mailGroupAddrId}"
               styleId="addressGroupFormX"
               onsubmit="return testSubmit()">
        <input type="hidden" name="isSubmit" value="false" id="isSubmit">

        <div class="row">
            <div class="col-xs-12">
                <html:button property="Add" styleClass="${app2:getFormButtonClasses()} marginButton"
                             onclick="getSelectContactMail();"><c:out
                        value="${button}"/></html:button>

                <app2:checkAccessRight functionality="CONTACT" permission="CREATE">
                    <html:button property="" styleClass="${app2:getFormButtonClasses()} marginButton"
                                 onclick="location.href='${urlCreateAddress}'">
                        <fmt:message key="Webmail.addressGroup.newContact"/>
                    </html:button>
                </app2:checkAccessRight>

                <html:button property="Cancel" styleClass="${app2:getFormButtonCancelClasses()} marginButton"
                             onclick="location.href='${urlCancel}'"><fmt:message
                        key="Common.cancel"/></html:button>
            </div>
        </div>

        <html:hidden property="selectedEmails" styleId="selectedEmails_Id"/>
        <html:hidden property="Add" value="false" styleId="AddHidden"/>
        <div class="table-responsive">
            <fanta:table mode="bootstrap" styleClass="${app2:getFantabulousTableClases()}"
                         list="allAddressList"
                         width="100%" id="contact"
                         action="Mail/Forward/AddAddressGroup.do?dto(mailGroupAddrId)=${mailGroupAddrId}"
                         imgPath="${baselayout}" align="center" withCheckBox="true" withContext="false">
                <c:if test="${list_size >0}">
                    <fanta:checkBoxColumn styleClass="listItemCenter" name="mail" id="selectedMails"
                                          onClick="javascript:check();" property="checkBoxIdentifier"
                                          headerStyle="listHeader" width="2%"/>
                </c:if>
                <fanta:dataColumn name="" label="" styleClass="listItem" headerStyle="listHeader" width="5%"
                                  renderData="false">

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
                <fanta:dataColumn name="addressName2" styleClass="listItem" title="ContactPerson.contactName"
                                  headerStyle="listHeader" width="27%" orderable="true"/>
                <fanta:dataColumn name="countryCode" styleClass="listItem" title="Contact.countryCode"
                                  headerStyle="listHeader" width="8%" orderable="true"/>
                <fanta:dataColumn name="zip" styleClass="listItem" title="Contact.zip" headerStyle="listHeader"
                                  width="7%" orderable="true"/>
                <fanta:dataColumn name="cityName" styleClass="listItem" title="Contact.city"
                                  headerStyle="listHeader"
                                  width="10%" orderable="true"/>
                <fanta:dataColumn name="addressId" styleClass="listItem2" title="Webmail.mailContact.email"
                                  headerStyle="listHeader" width="18%" renderData="false">
                    <c:choose>
                        <c:when test="${contact.contactPersonAddressId!='' && contact.contactPersonAddressId!=null}">
                            <fanta:list listName="telecomsForAnContactPerson">
                                <fanta:parameter field="telecomAddressId"
                                                 value="${not empty contact.contactPersonAddressId?contact.contactPersonAddressId:0}"/>
                                <fanta:parameter field="telecomContactPersonId"
                                                 value="${not empty contact.addressId?contact.addressId:0}"/>
                                <fanta:parameter field="type" value="${TELECOMTYPE_EMAIL}"/>
                            </fanta:list>
                        </c:when>
                        <c:otherwise>
                            <fanta:list listName="telecomsForAnAddress">
                                <fanta:parameter field="addressId"
                                                 value="${not empty contact.addressId?contact.addressId:0}"/>
                                <fanta:parameter field="type" value="${TELECOMTYPE_EMAIL}"/>
                            </fanta:list>
                        </c:otherwise>
                    </c:choose>
                    <c:set var="contactPersonAddressId" value="${contact.contactPersonAddressId}" scope="request"/>
                    <c:set var="telecomsCollection"
                           value="${app2:getTelecomsDataCollection(pageContext.request, contact.addressId, contactPersonAddressId)}"/>
                    <html:select property="dto(selectedMails)"
                                 styleId="dirAddressEmails_${contact.checkBoxIdentifier}"
                                 styleClass="${app2:getFormSelectClasses()}" readonly="${withoutEmails}">
                        <c:forEach var="telecomsData" items="${telecomsCollection}">
                            <option value="${telecomsData.TELECOMID}"
                                    class="list">${telecomsData.TELECOMNUMBER}</option>
                        </c:forEach>
                    </html:select>
                    <%--cleaning the vars--%>
                    <%
                        request.setAttribute("withoutEmails", null);
                        request.setAttribute("telecomsForAnAddress", null);
                        request.setAttribute("telecomsForAnContactPerson", null);
                    %>
                </fanta:dataColumn>
            </fanta:table>
        </div>
        <div class="row">
            <div class="col-xs-12">
                <html:button property="Add" styleClass="${app2:getFormButtonClasses()} marginButton"
                             onclick="getSelectContactMail();">
                    <c:out value="${button}"/>
                </html:button>

                <app2:checkAccessRight functionality="CONTACT" permission="CREATE">
                    <html:button property="" styleClass="${app2:getFormButtonClasses()} marginButton"
                                 onclick="location.href='${urlCreateAddress}'">
                        <fmt:message key="Webmail.addressGroup.newContact"/>
                    </html:button>
                </app2:checkAccessRight>

                <html:button property="Cancel" styleClass="${app2:getFormButtonCancelClasses()} marginButton"
                             onclick="location.href='${urlCancel}'"><fmt:message
                        key="Common.cancel"/></html:button>
            </div>
        </div>
    </html:form>

    <script language="JavaScript">
        <!--
        //disable checkbox
        disabledCheckbox();
        //-->
    </script>
</div>