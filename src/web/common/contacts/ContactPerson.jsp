<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>

<calendar:initialize/>
<c:set var="personType"><%= ContactConstants.ADDRESSTYPE_PERSON %>
</c:set>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>
<tags:jscript language="JavaScript" src="/js/contacts/address.js"/>

<c:set var="newAddress" value="${contactPersonForm.dtoMap['newAddress']}"/>
<c:set var="importAddress" value="${contactPersonForm.dtoMap['importAddress']}"/>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
<tr>
<td>
<c:choose>
    <c:when test="${op == 'update'}">
        <c:set var="toFocus" value="dto(name1)"/>
    </c:when>
    <c:when test="${importAddress != null && op == 'create'}">
        <c:set var="toFocus" value="dto(departmentId)"/>
    </c:when>
    <c:otherwise>
        <c:set var="toFocus" value="dto(name1)"/>
    </c:otherwise>
</c:choose>
<html:form action="${action}" focus="${contactPersonForm.dtoMap['addTelecom'] != null ? 'dto(telecomTypeId)' : toFocus}"
           enctype="multipart/form-data">

<table cellSpacing=0 cellPadding=4 width="800" border=0 align="center">
    <tr>
        <td class="button">
            <app2:securitySubmit operation="${op}" functionality="CONTACTPERSON" property="dto(save)"
                                 styleClass="button"
                                 onclick="javascript:fillMultipleSelectValues();">
                <c:out value="${button}"/>
            </app2:securitySubmit>
            <html:cancel styleClass="button">
                <fmt:message key="Common.cancel"/>
            </html:cancel>

            <%--top links--%>
            &nbsp;
            <c:if test="${op == 'update'}">
                <%-- Edit personal info link--%>
                <app2:checkAccessRight functionality="CONTACT" permission="UPDATE">
                    <html:link
                            page="/Person/Forward/Update.do?dto(onlyViewDetail)=true&index=0&contactId=${contactPersonForm.dtoMap['contactPersonId']}&dto(addressId)=${contactPersonForm.dtoMap['contactPersonId']}&dto(sourceAddressId)=${param.contactId}"
                            styleClass="folderTabLink">
                        <img src="<c:out value="${sessionScope.baselayout}"/>/img/edit.gif"
                             alt="<fmt:message    key="ContactPerson.personalInfo"/>" border="0"/>
                        <fmt:message key="ContactPerson.personalInfo"/>
                    </html:link>
                </app2:checkAccessRight>

                <app2:checkAccessRight functionality="CATEGORYTAB" permission="VIEW">
                    <c:set var="contactPersonId"
                           value="${contactPersonForm.dtoMap['contactPersonId']}"
                           scope="request"/>
                    <app2:categoryTabLink id="contactPersonCategoryLinkId"
                                          action="/contacts/ContactPerson/CategoryTab/Forward/Update.do?index=${param.index}&contactId=${param.contactId}&dto(addressId)=${param.contactId}&dto(contactPersonId)=${contactPersonForm.dtoMap['contactPersonId']}&contactPersonId=${contactPersonForm.dtoMap['contactPersonId']}&tabKey=Contacts.Tab.contactPersons"
                                          categoryConstant="2"
                                          finderName="findValueByContactPersonId"
                                          showStartSeparator="${true}"
                                          styleClass="folderTabLink">
                        <app2:categoryFinderValue forId="contactPersonCategoryLinkId" value="${param.contactId}"/>
                        <app2:categoryFinderValue forId="contactPersonCategoryLinkId"
                                                  value="${contactPersonId}"/>
                    </app2:categoryTabLink>
                </app2:checkAccessRight>
            </c:if>
        </td>
    </tr>
</table>
<table id="ContactPerson.jsp" border="0" cellpadding="0" cellspacing="0" width="800" align="center" class="container">

<html:hidden property="dto(op)" value="${op}"/>
<html:hidden property="dto(addressType)" value="${personType}"/>
<html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>

<c:if test="${'create' == op}">
    <html:hidden property="dto(recordUserId)" value="${sessionScope.user.valueMap['userId']}"/>
    <c:choose>
        <c:when test="${newAddress != null}">
            <html:hidden property="dto(newAddress)" value="true"/>
            <html:hidden property="dto(addressId)" value="${param.contactId}"/>
            <%--<html:hidden property="dto(recordUserId)" value="${sessionScope.user.valueMap['userId']}"/>--%>
        </c:when>
        <c:when test="${importAddress != null}">
            <html:hidden property="dto(importAddress)" value="true"/>
            <html:hidden property="dto(addressId)" value="${param.contactId}"/>
            <html:hidden property="dto(addressIdToImport)" value="${contactPersonForm.dtoMap['addressIdToImport']}"/>
        </c:when>
    </c:choose>
</c:if>
    <%--<c:if test="${'create' != op}">--%>
<html:hidden property="dto(contactPersonId)"/>
<html:hidden property="dto(addressId)" value="${param.contactId}"/>
<html:hidden property="dto(version)"/>

    <%--related to private address of the contact person--%>
<html:hidden property="dto(lastModificationUserId)" value="${sessionScope.user.valueMap['userId']}"/>
<html:hidden property="dto(privateVersion)"/>
<jsp:useBean id="now" class="java.util.Date"/>
<html:hidden property="dto(lastUpdateDate)" value="${now}"/>
    <%--</c:if>--%>
<TR>
    <TD colspan="4" class="title">
        <c:out value="${title}"/>
    </TD>
</TR>
<tr>
    <TD class="label" width="15%">
        <fmt:message key="Contact.Person.salutation"/>
        /
        <fmt:message key="Contact.Person.title"/>
    </TD>
    <TD class="contain" width="35%">
        <fanta:select property="dto(salutationId)" listName="salutationBaseList" firstEmpty="true" labelProperty="label"
                      valueProperty="id" module="/catalogs" styleClass="halfMiddleTextSelect"
                      readOnly="${(importAddress != null && op == 'create') || op== 'delete'}" tabIndex="1">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
        <c:if test="${op == 'delete'}">&nbsp;</c:if>
        <fanta:select property="dto(titleId)" listName="titleList" firstEmpty="true" labelProperty="name"
                      valueProperty="id" module="/catalogs" styleClass="halfMiddleTextSelect"
                      readOnly="${(importAddress != null && op == 'create') || op== 'delete'}" tabIndex="2">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </TD>
    <TD class="label" width="15%">
        <fmt:message key="Contact.Person.searchName"/>
    </TD>
    <TD class="contain" width="35%">
        <app:text property="dto(searchName)" styleClass="mediumText" maxlength="60" tabindex="8"
                  view="${(importAddress != null && op=='create') || op == 'delete'}"/>
    </TD>
</tr>
<tr>
    <TD class="label">
        <fmt:message key="Contact.Person.lastname"/>
    </TD>
    <TD class="contain">
        <app:text property="dto(name1)" styleClass="middleText" maxlength="60" tabindex="3"
                  view="${(importAddress != null && op=='create') || op == 'delete'}"/>
    </TD>
    <TD class="label">
        <fmt:message key="Contact.keywords"/>
    </TD>
    <TD class="contain">
        <app:text property="dto(keywords)" styleClass="mediumText" maxlength="50" tabindex="9"
                  view="${(importAddress != null && op=='create') || op == 'delete'}"/>
    </TD>
</tr>
<tr>
    <TD class="label">
        <fmt:message key="Contact.Person.firstname"/>
    </TD>
    <TD class="contain">
        <app:text property="dto(name2)" styleClass="middleText" maxlength="60" tabindex="4"
                  view="${(importAddress != null && op=='create') || op == 'delete'}"/>
    </TD>
    <TD class="label">
        <fmt:message key="Contact.Person.education"/>
    </TD>
    <TD class="contain">
        <app:text property="dto(education)" styleClass="mediumText" maxlength="40" tabindex="10"
                  view="${(importAddress != null && op == 'create') || op== 'delete'}"/>
    </TD>
</tr>
<tr>
    <TD class="label">
        <fmt:message key="ContactPerson.department"/>
    </TD>
    <TD class="contain">
        <fanta:select property="dto(departmentId)" listName="departmentBaseList" firstEmpty="true" labelProperty="name"
                      valueProperty="departmentId" styleClass="middleSelect" readOnly="${op == 'delete'}" tabIndex="5">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="addressId" value="${not empty param.contactId?param.contactId:0}"/>
        </fanta:select>
    </TD>
    <TD class="label">
        <fmt:message key="Contact.language"/>
    </TD>
    <TD class="contain">
        <fanta:select property="dto(languageId)" listName="languageBaseList" firstEmpty="true" labelProperty="name"
                      valueProperty="id" module="/catalogs" styleClass="mediumSelect"
                      readOnly="${(importAddress != null && op == 'create') || op== 'delete'}" tabIndex="11">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </TD>
</tr>
<tr>
    <TD class="label">
        <fmt:message key="ContactPerson.function"/>
    </TD>
    <TD class="contain" nowrap>
        <app:text property="dto(function)" styleClass="middleText" maxlength="40" tabindex="6"
                  view="${op == 'delete'}"/>
    </TD>
    <TD class="label">
        <fmt:message key="Contact.Person.birthday"/>
    </TD>
    <TD class="contain">

        <c:choose>
            <c:when test="${not empty contactPersonForm.dtoMap['dateWithoutYear']}">
                <app:dateText property="dto(birthday)" datePatternKey="withoutYearPattern"
                              view="${'delete' == op || not empty importAddress}" styleId="dateTextId"
                              styleClass="mediumText" maxlength="10" withOutYear="true" tabindex="12"
                              calendarPicker="true"/>
            </c:when>
            <c:otherwise>
                <app:dateText property="dto(birthday)" datePatternKey="datePattern"
                              view="${'delete' == op || not empty importAddress}"
                              styleId="dateTextId" styleClass="mediumText" maxlength="10" tabindex="12"
                              calendarPicker="true"/>
            </c:otherwise>
        </c:choose>
    </TD>
</tr>
<tr>
    <TD class="label">
        <fmt:message key="ContactPerson.personType"/>
    </TD>
    <TD class="contain">
        <fanta:select property="dto(personTypeId)" listName="personTypeList" firstEmpty="true" labelProperty="name"
                      module="/catalogs" valueProperty="id" styleClass="middleSelect" readOnly="${op == 'delete'}"
                      tabIndex="7">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </TD>
    <TD class="label">
        <fmt:message key="Contact.status"/>
    </TD>
    <TD class="contain">
        <html:checkbox property="dto(isActive)" disabled="${op == 'delete'}" tabindex="14" styleClass="radio"/>
        <fmt:message key="Contact.active"/>
    </TD>
</tr>
<tr>
    <TD class="label">
        <fmt:message key="ContactPerson.additionalAddressLine"/>
    </TD>
    <TD class="contain">
        <app:text property="dto(addAddressLine)" styleClass="middleText" maxlength="100"
                  tabindex="7" view="${op == 'delete'}"/>
    </TD>
    <td class="label">
        <fmt:message key="ContactPerson.additionalAddress"/>
    </td>
    <td class="contain">
        <fanta:select property="dto(additionalAddressId)" listName="additionalAddressToContactPersonList"
                      labelProperty="name" valueProperty="additionalAddressId" module="/contacts"
                      firstEmpty="true" styleClass="mediumSelect" readOnly="${'delete' == op}" tabIndex="14">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="addressId" value="${not empty param.contactId ? param.contactId : 0}"/>
        </fanta:select>
    </td>
</tr>

    <%--Telecoms area--%>
<c:set var="lastTabIndex" value="15" scope="request"/>
<c:set var="isType" value="contactPerson" scope="request"/>
<c:set var="contactPersonId" scope="request" value="${contactPersonForm.dtoMap['contactPersonId']}"/>

<c:set var="contactPersonName" value="${contactPersonForm.dtoMap['name1']}" scope="request"/>
<c:if test="${contactPersonForm.dtoMap['name2'] != null}">
    <c:set var="contactPersonName" value="${contactPersonName}, ${contactPersonForm.dtoMap['name2']}" scope="request"/>
</c:if>

<html:hidden property="initTelecoms"/>
<c:set var="telecomItems" value="${contactPersonForm.telecomMap}" scope="request"/>
<c:import url="/common/contacts/TelecomTile.jsp"/>
<TR>
    <TD colspan="4" class="title">
        <fmt:message key="Contact.additionalInfo"/>
    </TD>
</TR>

<TR>
    <TD class="topLabel" colspan="4" style="height: 150px">
        <html:hidden property="dto(imageId)"/>
        <html:hidden property="dto(imageRemovedFlag)" styleId="imageRemovedFlagId"/>
        <c:set var="imageId" value="${contactPersonForm.dtoMap['imageId']}"/>
        <table border="0" cellspacing="0" cellPadding="0" width="100%">
            <tr>
                <c:if test="${imageId != null && imageId != '' && contactPersonForm.dtoMap['imageRemovedFlag']!='true'}">
                    <td class="topLabel" style="border: 0px;" width="140" nowrap>
                        <div id="imageDivId">
                            &nbsp;<br>
                            <html:img page="/DownloadAddressImage.do?dto(freeTextId)=${imageId}" border="0"
                                      width="110px" vspace="0" hspace="0" styleId="imageId"/>
                            <c:if test="${!((importAddress != null && op == 'create') || op== 'delete')}">
                                &nbsp;<a href="javascript:hideImageDiv();"><img src="${sessionScope.baselayout}/img/tinydelete.gif" alt="<fmt:message
                                    key="Contact.removePhoto"/>" title="<fmt:message
                                    key="Contact.removePhoto"/>" border="0" align="top"/></a>
                            </c:if>
                        </div>
                    </td>
                </c:if>
                <td class="topLabel" style="border: 0px" width="100%">
                    <fmt:message key="Contact.notes"/>
                    <br>
                    <html:textarea property="dto(note)" styleClass="mediumDetailHigh"
                                   tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}"
                                   readonly="${(importAddress != null && op == 'create') || op== 'delete'}"
                                   style="height:110px;width:400;"/>
                </td>
            </tr>
        </table>
    </TD>
</tr>

<c:if test="${op != 'delete'}">
    <TR>
        <TD class="label">
            <fmt:message key="Contact.photo"/>
        </TD>
        <TD class="contain" colspan="3">
            <html:file property="imageFile" tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}"
                       size="50"/>
            <fmt:message key="Common.fileUpload.info">
                <fmt:param value="200 KB"/>
                <fmt:param value="gif, jpeg, png"/>
            </fmt:message>
        </TD>
    </TR>
</c:if>

<tr>
    <td colspan="4">
        <!--added by ivan-->
        <c:set var="elementCounter" value="${app2:incrementByOne(pageContext, 'lastTabIndex')}" scope="request"/>
        <c:set var="ajaxAction" value="/contacts/ContactPerson/MainPageReadSubCategories.do" scope="request"/>
        <c:set var="downloadAction"
               value="/contacts/ContactPerson/MainPage/DownloadCategoryFieldValue.do?dto(addressId)=${param.contactId}&contactId=${param.contactId}&dto(contactPersonId)=${contactPersonId}&contactPersonId=${contactPersonId}"
               scope="request"/>
        <c:set var="formName" value="contactPersonForm" scope="request"/>
        <c:set var="table" value="<%=ContactConstants.CONTACTPERSON_CATEGORY%>" scope="request"/>
        <c:set var="secondTable" value="<%=ContactConstants.ADDRESS_CONTACTPERSON_CATEGORY%>" scope="request"/>
        <c:set var="operation" value="${op}" scope="request"/>
        <c:set var="labelWidth" value="15" scope="request"/>
        <c:set var="containWidth" value="85" scope="request"/>
        <c:set var="generalWidth" value="${250}" scope="request"/>
        <c:import url="/common/catalogs/CategoryUtil.jsp"/>
        <c:set var="lastTabIndex" value="${elementCounter+1}" scope="request"/>
    </td>
</tr>

</table>

<table cellSpacing=0 cellPadding=4 width="800" border=0 align="center">
    <TR>
        <TD class="button">
            <app2:securitySubmit operation="${op}" functionality="CONTACTPERSON" property="dto(save)"
                                 styleClass="button" tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}"
                                 onclick="javascript:fillMultipleSelectValues();">
                <c:out value="${button}"/>
            </app2:securitySubmit>
            <html:cancel styleClass="button" tabindex="${app2:incrementByOne(pageContext, 'lastTabIndex')}">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </TD>
    </TR>
</table>
</html:form>
</td>
</tr>
</table>

