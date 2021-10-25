<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>
<tags:initSelectPopup/>
<calendar:initialize/>

<br/>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces" scope="request"/>
<fmt:message var="datePattern" key="datePattern"/>

<html:form action="${action}" focus="dto(name)">
<html:hidden property="dto(op)" value="${op}"/>

<c:if test="${'update' == op || op == 'delete'}">
    <html:hidden property="dto(projectId)"/>

</c:if>

<c:set var="canEditPage" value="${true}"/>
<c:if test="${'update' == op || 'delete' == op}">
    <c:set var="canEditPage"
           value="${app2:hasProjectUserPermission(projectForm.dtoMap['projectId'], 'ADMIN', pageContext.request)}"/>
    <html:hidden property="dto(version)"/>
</c:if>

<c:if test="${'delete' == op}">
    <html:hidden property="dto(withReferences)" value="true"/>
</c:if>


<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
<tr>
<td>
<table cellSpacing="0" cellPadding="4" width="85%" border="0" align="center">
    <TR>
        <TD class="button">
            <c:choose>
                <c:when test="${'update' == op || 'delete' == op}">
                    <c:if test="${canEditPage}">
                        <app2:securitySubmit operation="${op}" functionality="PROJECT" property="saveButton"
                                             styleClass="button"
                                             styleId="saveButtonId">
                            <c:out value="${button}"/>
                        </app2:securitySubmit>
                    </c:if>
                </c:when>
                <c:otherwise>
                    <app2:securitySubmit operation="${op}" functionality="PROJECT" property="saveButton"
                                         styleClass="button"
                                         styleId="saveButtonId">
                        <c:out value="${button}"/>
                    </app2:securitySubmit>
                </c:otherwise>
            </c:choose>
            <html:cancel styleClass="button">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </TD>
    </TR>
</table>

<table border="0" cellpadding="0" cellspacing="0" width="85%" align="center" class="container">
<TR>
    <TD colspan="4" class="title">
        <c:out value="${title}"/>
    </TD>
</TR>
<TR>
    <TD class="label" width="15%"><fmt:message key="Project.name"/></TD>
    <TD class="contain" width="35%">
        <app:text property="dto(name)" styleClass="middleText" maxlength="80" tabindex="1"
                  view="${op == 'delete' || !canEditPage}"/>
    </TD>
    <TD class="label" width="15%"><fmt:message key="Project.toBeInvoiced"/></TD>
    <TD class="contain" width="35%">
        <c:set var="toBeInvoicedOptions" value="${app2:getToBeInvoicedTypes(pageContext.request)}"/>
        <html:select property="dto(toBeInvoiced)" styleClass="select"
                     readonly="${op == 'delete' || !canEditPage}"
                     tabindex="8">
            <html:option value="">&nbsp;</html:option>
            <html:options collection="toBeInvoicedOptions" property="value" labelProperty="label"/>
        </html:select>
    </TD>
</TR>
<TR>
    <TD class="label"><fmt:message key="Project.responsible"/></TD>
    <TD class="contain">
        <fanta:select property="dto(responsibleId)" listName="userBaseList"
                      labelProperty="name" valueProperty="id" styleClass="middleSelect"
                      module="/admin" firstEmpty="true" tabIndex="2"
                      readOnly="${op == 'delete' || !canEditPage}"
                      value="${sessionScope.user.valueMap['userId']}">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>

    </TD>
    <TD class="topLabel"><fmt:message key="Project.hasTimeLimit"/></TD>
    <TD class="contain">
        <html:checkbox property="dto(hasTimeLimit)"
                       disabled="${op == 'delete' || !canEditPage}" tabindex="9"
                       styleClass="radio" value="true"/>
    </TD>
</TR>
<TR>
    <TD class="label"><fmt:message key="Project.startDate"/></TD>
    <TD class="contain">
        <app:dateText property="dto(startDate)" styleId="startDate"
                      calendarPicker="${op != 'delete' && canEditPage}"
                      datePatternKey="${datePattern}" styleClass="dateText"
                      view="${op == 'delete' || !canEditPage}"
                      maxlength="10" tabindex="3" currentDate="true"/>
    </TD>
    <TD class="label"><fmt:message key="Project.account"/></TD>
    <TD class="contain">
        <fanta:select property="dto(accountId)"
                      listName="accountList" labelProperty="name" valueProperty="accountId"
                      styleClass="mediumSelect" module="/catalogs" firstEmpty="true"
                      readOnly="${'delete' == op || !canEditPage}" tabIndex="10">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </TD>
</TR>

<TR>
    <TD class="label"><fmt:message key="Project.endDate"/></TD>
    <TD class="contain">
        <app:dateText property="dto(endDate)" styleId="endDate"
                      calendarPicker="${op != 'delete' && canEditPage}"
                      datePatternKey="${datePattern}" styleClass="dateText"
                      view="${op == 'delete' || !canEditPage}"
                      maxlength="10" tabindex="4"/>
    </TD>
    <TD class="label"><fmt:message key="Project.customer"/></TD>
    <TD class="contain">
        <c:if test="${not empty projectForm.dtoMap['customerId'] and 'update' == op}">
            <app2:checkAccessRight functionality="CONTACT" permission="VIEW">
                <c:set var="personType"><%= ContactConstants.ADDRESSTYPE_PERSON %></c:set>
                <c:set var="addressMap" value="${app2:getAddressMap(projectForm.dtoMap['customerId'])}"/>
                <c:choose>
                    <c:when test="${personType == addressMap['addressType']}">
                        <c:set var="addressEditLink"
                               value="/contacts/Person/Forward/Update.do?contactId=${projectForm.dtoMap['customerId']}&dto(addressId)=${projectForm.dtoMap['customerId']}&dto(addressType)=${addressMap['addressType']}&dto(name1)=${app2:encode(addressMap['name1'])}&dto(name2)=${app2:encode(addressMap['name2'])}&dto(name3)=${app2:encode(addressMap['name3'])}&index=0"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="addressEditLink"
                               value="/contacts/Organization/Forward/Update.do?contactId=${projectForm.dtoMap['customerId']}&dto(addressId)=${projectForm.dtoMap['customerId']}&dto(addressType)=${addressMap['addressType']}&dto(name1)=${app2:encode(addressMap['name1'])}&dto(name2)=${app2:encode(addressMap['name2'])}&dto(name3)=${app2:encode(addressMap['name3'])}&index=0"/>
                    </c:otherwise>
                </c:choose>
            </app2:checkAccessRight>
        </c:if>

        <html:hidden property="dto(customerId)" styleId="fieldAddressId_id"/>
        <app:text property="dto(customerName)" styleClass="mediumText" readonly="true"
                  styleId="fieldAddressName_id" view="${'delete' == op || !canEditPage}" tabindex="11"/>

        <c:if test="${not empty addressEditLink}">
            <app:link action="${addressEditLink}" contextRelative="true" tabindex="11">
                <html:img src="${baselayout}/img/edit.gif" titleKey="Common.edit" border="0" align="middle"/>
            </app:link>
        </c:if>

        <tags:selectPopup url="/contacts/SearchAddress.do" name="searchAddress"
                          titleKey="Common.search" submitOnSelect="true"
                          hide="${'delete' == op || !canEditPage}"
                          tabindex="11"/>
        <tags:clearSelectPopup keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id"
                               titleKey="Common.clear" submitOnClear="true"
                               hide="${'delete' == op || !canEditPage}"
                               tabindex="11"/>
    </TD>
</TR>

<TR>
    <TD class="label"><fmt:message key="Project.status"/></TD>
    <TD class="contain">
        <c:set var="statusesOptions" value="${app2:getProjectStatuses(pageContext.request)}"/>
        <html:select property="dto(status)" styleClass="shortSelect"
                     readonly="${op == 'delete' || !canEditPage}"
                     tabindex="5">
            <html:option value="">&nbsp;</html:option>
            <html:options collection="statusesOptions" property="value" labelProperty="label"/>
        </html:select>
    </TD>
    <TD class="label"><fmt:message key="Project.contactPerson"/></TD>
    <TD class="contain">
        <fanta:select property="dto(contactPersonId)" listName="searchContactPersonList"
                      module="/contacts" firstEmpty="true" labelProperty="contactPersonName"
                      valueProperty="contactPersonId" styleClass="mediumSelect"
                      readOnly="${'delete' == op || !canEditPage}" tabIndex="12">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="addressId"
                             value="${not empty projectForm.dtoMap['customerId']? projectForm.dtoMap['customerId']:0}"/>
        </fanta:select>
    </TD>
</TR>

<TR>
    <TD class="label"><fmt:message key="Project.plannedInvoice"/></TD>
    <TD class="contain">
        <app:numberText property="dto(plannedInvoice)" styleClass="numberText" maxlength="6"
                        numberType="decimal" maxInt="6" maxFloat="1"
                        view="${'delete' == op || !canEditPage}"
                        tabindex="6"/>
    </TD>

    <TD class="label">
        <fmt:message key="Project.totalInvoice"/>
    </TD>
    <TD class="contain">
        <app:numberText property="dto(totalInvoice)" styleClass="numberText" maxlength="6"
                        numberType="decimal" maxInt="6" maxFloat="1" view="true" tabindex="13"/>
    </TD>

</TR>
<TR>
    <TD class="label"><fmt:message key="Project.plannedNoInvoice"/></TD>
    <TD class="contain">
        <app:numberText property="dto(plannedNoInvoice)" styleClass="numberText"
                        maxlength="6" numberType="decimal" maxInt="6" maxFloat="1"
                        view="${'delete' == op || !canEditPage}" tabindex="7"/>
    </TD>

    <TD class="label">
        <fmt:message key="Project.totalNoInvoice"/>
    </TD>
    <TD class="contain">
        <app:numberText property="dto(totalNoInvoice)" styleClass="numberText" maxlength="6"
                        numberType="decimal" maxInt="6" maxFloat="1" view="true" tabindex="14"/>
    </TD>

</TR>

<TR>
    <TD class="topLabel" colspan="4">
        <html:textarea property="dto(description)" styleClass="mediumDetailHigh"
                       style="height:120px;width:99%;"
                       tabindex="15" readonly="${op == 'delete' || !canEditPage}"/>
    </TD>
</TR>
</table>

<table cellSpacing=0 cellPadding=4 width="85%" border=0 align="center">
    <TR>
        <TD class="button">
            <c:choose>
                <c:when test="${'update' == op || 'delete' == op}">
                    <c:if test="${canEditPage}">
                        <app2:securitySubmit operation="${op}" functionality="PROJECT" property="saveButton"
                                             styleClass="button" styleId="saveButtonId" tabindex="16">
                            <c:out value="${button}"/>
                        </app2:securitySubmit>
                    </c:if>
                </c:when>
                <c:otherwise>
                    <app2:securitySubmit operation="${op}" functionality="PROJECT" property="saveButton"
                                         styleClass="button" styleId="saveButtonId" tabindex="16">
                        <c:out value="${button}"/>
                    </app2:securitySubmit>
                </c:otherwise>
            </c:choose>
            <html:cancel styleClass="button" tabindex="17">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </TD>
    </TR>
</table>

</td>
</tr>
</table>
</html:form>
<br/>

