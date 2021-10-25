<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>

<html:hidden property="dto(canUpdateInvoiceType)"/>
<tr>
    <td width="15%" class="label">
        <fmt:message key="Invoice.number"/>
    </td>
    <td width="35%" class="contain">
        <app:text property="dto(number)"
                  styleClass="mediumText"
                  view="true"/>
    </td>
    <td class="label" width="15%">
        <fmt:message key="Invoice.invoiceDate"/>
    </td>
    <td class="contain" width="35%">
        <app:dateText property="dto(invoiceDate)"
                      styleId="startDate"
                      calendarPicker="${op != 'delete'}"
                      datePatternKey="${datePattern}"
                      styleClass="text"
                      maxlength="10"
                      currentDate="true"
                      view="${'delete' == op || existsReminders}"
                      tabindex="6"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Invoice.type"/>
    </td>
    <td class="contain">
        <html:hidden property="dto(changeInvoiceType)" styleId="changeInvoiceType" value="false"/>
        <html:select property="dto(type)"
                     styleClass="middleSelect"
                     readonly="${'delete' == op || (null != invoiceForm.dtoMap['canUpdateInvoiceType'] && 'false' == invoiceForm.dtoMap['canUpdateInvoiceType'])}"
                     onchange="javascript:onSubmit(this);" tabindex="1">
            <html:option value=""/>
            <html:options collection="invoiceTypeList"
                          property="value"
                          labelProperty="label"/>
        </html:select>
    </td>
    <td class="label">
        <fmt:message key="Invoice.paymentDate"/>
    </td>
    <td class="contain">
        <app:dateText property="dto(paymentDate)"
                      styleId="startDate"
                      calendarPicker="false"
                      datePatternKey="${datePattern}"
                      styleClass="text"
                      maxlength="10"
                      view="true"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Invoice.contact"/>
    </td>
    <td class="contain">
        <c:if test="${not empty invoiceForm.dtoMap['addressId'] and 'update' == op}">
            <app2:checkAccessRight functionality="CONTACT" permission="VIEW">
                <c:set var="personType"><%= ContactConstants.ADDRESSTYPE_PERSON %></c:set>
                <c:set var="addressMap" value="${app2:getAddressMap(invoiceForm.dtoMap['addressId'])}"/>
                <c:choose>
                    <c:when test="${personType == addressMap['addressType']}">
                        <c:set var="addressEditLink"
                               value="/contacts/Person/Forward/Update.do?contactId=${invoiceForm.dtoMap['addressId']}&dto(addressId)=${invoiceForm.dtoMap['addressId']}&dto(addressType)=${addressMap['addressType']}&dto(name1)=${app2:encode(addressMap['name1'])}&dto(name2)=${app2:encode(addressMap['name2'])}&dto(name3)=${app2:encode(addressMap['name3'])}&index=0"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="addressEditLink"
                               value="/contacts/Organization/Forward/Update.do?contactId=${invoiceForm.dtoMap['addressId']}&dto(addressId)=${invoiceForm.dtoMap['addressId']}&dto(addressType)=${addressMap['addressType']}&dto(name1)=${app2:encode(addressMap['name1'])}&dto(name2)=${app2:encode(addressMap['name2'])}&dto(name3)=${app2:encode(addressMap['name3'])}&index=0"/>
                    </c:otherwise>
                </c:choose>
            </app2:checkAccessRight>
        </c:if>

        <html:hidden property="dto(addressId)" styleId="fieldAddressId_id"/>
        <app:text property="dto(addressName)"
                  styleClass="middleText"
                  maxlength="40"
                  readonly="true"
                  styleId="fieldAddressName_id"
                  view="${'delete' == op}"
                  tabindex="1"/>

        <c:if test="${not empty addressEditLink}">
            <app:link action="${addressEditLink}" contextRelative="true" tabindex="2">
                <html:img src="${baselayout}/img/edit.gif" titleKey="Common.edit" border="0" align="middle"/>
            </app:link>
        </c:if>

        <tags:selectPopup url="/contacts/SearchAddress.do" name="searchAddress"
                          titleKey="Common.search"
                          submitOnSelect="true"
                          tabindex="2" hide="${'delete' == op}"/>
        <tags:clearSelectPopup keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id"
                               titleKey="Common.clear"
                               submitOnClear="true"
                               tabindex="3" hide="${'delete' == op}"/>

        <html:hidden property="dto(isUpdateCustomerInfo)" value="true" styleId="isUpdateCustomerInfo"/>
    </td>
    <td class="label">
        <fmt:message key="Invoice.reminderDate"/>
    </td>
    <td class="contain">
        <app:dateText property="dto(reminderDate)"
                      styleId="startDate"
                      calendarPicker="false"
                      datePatternKey="${datePattern}"
                      styleClass="text"
                      maxlength="10"
                      view="true"
                      readonly="${'delete' == op}"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Invoice.contactPerson"/>
    </td>
    <td class="contain">
        <fanta:select property="dto(contactPersonId)" listName="searchContactPersonList"
                      module="/contacts" firstEmpty="true"
                      labelProperty="contactPersonName"
                      valueProperty="contactPersonId"
                      styleClass="middleSelect"
                      readOnly="${'delete' == op}"
                      tabIndex="4">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="addressId"
                             value="${not empty invoiceForm.dtoMap['addressId']?invoiceForm.dtoMap['addressId']:0}"/>
        </fanta:select>
    </td>
    <td class="label">
        <fmt:message key="Invoice.reminderLevel"/>
    </td>
    <td class="contain">
        <c:out value="${app2:getReminderLevelName(invoiceForm.dtoMap['reminderLevel'],pageContext.request)}"/>
        <html:hidden property="dto(reminderLevel)"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Invoice.sentAddress"/>
    </td>
    <td class="contain">
        <fanta:select property="dto(sentAddressId)" listName="invoiceAddressRelationList"
                      labelProperty="relatedAddressName" valueProperty="relatedAddressId" styleClass="middleSelect"
                      readOnly="${op == 'delete'}"
                      onChange="reSubmit()"
                      module="/contacts" tabIndex="5" firstEmpty="true">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="addressId" value="${not empty invoiceForm.dtoMap['addressId'] ? invoiceForm.dtoMap['addressId'] : 0}"/>
        </fanta:select>
    </td>
    <td class="label">
        <fmt:message key="Invoice.payCondition"/>
    </td>
    <td class="contain">
        <fanta:select property="dto(payConditionId)"
                      listName="payConditionList"
                      labelProperty="name"
                      valueProperty="id"
                      styleClass="middleSelect"
                      firstEmpty="true"
                      module="/catalogs"
                      tabIndex="7"
                      readOnly="${'delete' == op || existsReminders}">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Invoice.sentContactPerson"/>
    </td>
    <td class="contain">
        <fanta:select property="dto(sentContactPersonId)" listName="searchContactPersonList"
                      module="/contacts" firstEmpty="true"
                      labelProperty="contactPersonName"
                      valueProperty="contactPersonId"
                      styleClass="middleSelect"
                      tabIndex="5"
                      readOnly="${'delete' == op}">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="addressId"
                         value="${not empty invoiceForm.dtoMap['sentAddressId'] ? invoiceForm.dtoMap['sentAddressId'] : 0}"/>
        </fanta:select>
    </td>
    <td class="label">
        <fmt:message key="Invoice.template"/>
    </td>
    <td class="contain">
        <fanta:select property="dto(templateId)"
                      listName="invoiceTemplateList"
                      labelProperty="title"
                      valueProperty="templateId"
                      styleClass="middleSelect"
                      module="/catalogs"
                      firstEmpty="true"
                      readOnly="${'delete' == op}"
                      tabIndex="8">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Invoice.additionalAddress"/>
    </td>
    <td class="contain">
        <fanta:select property="dto(additionalAddressId)" listName="additionalAddressSelectList"
                      labelProperty="name" valueProperty="additionalAddressId" module="/contacts"
                      preProcessor="com.piramide.elwis.web.contactmanager.el.AdditionalAddressSelectPreProcessor"
                      firstEmpty="true" styleClass="middleSelect" readOnly="${'delete' == op}" tabIndex="5">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="addressId" value="${not empty invoiceForm.dtoMap['addressId'] ? invoiceForm.dtoMap['addressId'] : 0}"/>
        </fanta:select>
    </td>

    <td class="label">
        <fmt:message key="Invoice.netGross"/>
    </td>
    <td class="contain">
        <c:set var="netGrossOptions" value="${app2:getNetGrossOptions(pageContext.request)}"/>
        <html:select property="dto(netGross)"
                     styleClass="middleSelect"
                     readonly="${'delete' == op}"
                     tabindex="9">
            <html:option value=""/>
            <html:options collection="netGrossOptions"
                          property="value"
                          labelProperty="label"/>
        </html:select>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Invoice.totalAmountNet"/>
    </td>
    <td class="contain">
        <html:hidden property="dto(totalAmountNet)"/>
        <fmt:formatNumber var="totalAmountNet"
                          value="${invoiceForm.dtoMap['totalAmountNet']}"
                          type="number"
                          pattern="${numberFormat}"/>
        ${totalAmountNet}
    </td>

    <td class="label">
        <fmt:message key="Invoice.currency"/>
    </td>
    <td class="contain">
        <fanta:select property="dto(currencyId)"
                      listName="basicCurrencyList"
                      labelProperty="name"
                      valueProperty="id"
                      firstEmpty="true"
                      styleClass="middleSelect"
                      module="/catalogs"
                      readOnly="${'delete' == op}"
                      tabIndex="10">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Invoice.totalAmountGross"/>
    </td>
    <td class="contain" colspan="3">
        <html:hidden property="dto(totalAmountGross)"/>
        <fmt:formatNumber var="totalAmountGross"
                          value="${invoiceForm.dtoMap['totalAmountGross']}"
                          type="number"
                          pattern="${numberFormat}"/>
        ${totalAmountGross}
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Invoice.openAmount"/>
    </td>
    <td class="contain" colspan="3">
        <html:hidden property="dto(openAmount)"/>
        <fmt:formatNumber var="openAmount"
                          value="${invoiceForm.dtoMap['openAmount']}"
                          type="number"
                          pattern="${numberFormat}"/>
        ${openAmount}
    </td>
</tr>