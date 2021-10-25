<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>

<tr>
    <td width="15%" class="label">
        <fmt:message key="Invoice.number"/>
    </td>
    <td width="35%" class="contain">
        <app:text property="dto(number)"
                  styleClass="mediumText"
                  view="true"/>
    </td>
    <td width="15%" class="label">
        <fmt:message key="Invoice.creditNoteOf"/>
    </td>
    <td width="35%" class="contain">
        <c:choose>
            <c:when test="${null == invoiceForm.dtoMap['creditNoteOfId'] || 'false' == invoiceForm.dtoMap['hasRelation']}">
                <html:hidden property="dto(creditNoteOfId)" styleId="fieldInvoiceId_id"/>
                <html:hidden property="dto(copyOfButtonPressed)" value="false" styleId="copyOfButtonPressed"/>
                <app:text property="dto(relatedInvoiceNumber)"
                          styleClass="middleText"
                          maxlength="40"
                          readonly="true"
                          styleId="fieldInvoiceNumber_id"
                          view="${'delete' == op}"
                          tabindex="7"/>

                <tags:selectPopup
                        url="/finance/Invoice/Search.do?chekIfButtonPressed=true&parameter(type)=${invoiceType}"
                        name="invoiceSearchList"
                        titleKey="Common.search"
                        width="820"
                        submitOnSelect="false"
                        hide="${'delete' == op}"
                        tabindex="8"/>
                <tags:clearSelectPopup keyFieldId="fieldInvoiceId_id"
                                       nameFieldId="fieldInvoiceNumber_id"
                                       titleKey="Common.clear"
                                       hide="${'delete' == op}"
                                       tabindex="9"/>
            </c:when>
            <c:otherwise>
                <html:hidden property="dto(creditNoteOfId)"/>
                <html:hidden property="dto(relatedInvoiceNumber)"/>

                <c:out value="${invoiceForm.dtoMap['relatedInvoiceNumber']}"/>

                <app:link contextRelative="true"
                          action="finance/Invoice/Forward/Update.do?invoiceId=${invoiceForm.dtoMap['creditNoteOfId']}&dto(invoiceId)=${invoiceForm.dtoMap['creditNoteOfId']}&dto(number)=${app2:encode(invoiceForm.dtoMap['relatedInvoiceNumber'])}&index=0">
                    <html:img src="${baselayout}/img/edit.gif" titleKey="Common.edit" border="0"/>
                </app:link>

                <fmt:message key="InvoicePayment.text.creditNoteDeleteMessage" var="deleteCreditNoteMessage">
                    <fmt:param value="${invoiceForm.dtoMap['number']}"/>
                </fmt:message>
                <html:hidden property="dto(invoicePaymentMessage)" value="${deleteCreditNoteMessage}"
                             styleId="invoicePaymentMessageId"/>
            </c:otherwise>
        </c:choose>
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
                     readonly="${'delete' == op || (null != invoiceForm.dtoMap['creditNoteOfId'] && 'true' == invoiceForm.dtoMap['hasRelation'])}"
                     onchange="javascript:onSubmit(this);" tabindex="1">
            <html:option value=""/>
            <html:options collection="invoiceTypeList"
                          property="value"
                          labelProperty="label"/>
        </html:select>
    </td>
    <td class="label">
        <fmt:message key="Invoice.invoiceDate"/>
    </td>
    <td class="contain">
        <app:dateText property="dto(invoiceDate)"
                      styleId="startDate"
                      calendarPicker="${op != 'delete'}"
                      datePatternKey="${datePattern}"
                      styleClass="text"
                      maxlength="10"
                      currentDate="true"
                      view="${'delete' == op || existsReminders}"
                      tabindex="10"/>
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
                  view="${'delete' == op || 'true' == invoiceForm.dtoMap['hasRelation']}"
                  tabindex="2"/>

        <c:if test="${not empty addressEditLink}">
            <app:link action="${addressEditLink}" contextRelative="true" tabindex="2">
                <html:img src="${baselayout}/img/edit.gif" titleKey="Common.edit" border="0"/>
            </app:link>
        </c:if>

        <tags:selectPopup url="/contacts/SearchAddress.do" name="searchAddress"
                          titleKey="Common.search"
                          submitOnSelect="true"
                          hide="${'delete' == op || 'true' == invoiceForm.dtoMap['hasRelation']}"
                          tabindex="3"/>
        <tags:clearSelectPopup keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id"
                               titleKey="Common.clear"
                               submitOnClear="true"
                               hide="${'delete' == op || 'true' == invoiceForm.dtoMap['hasRelation']}"
                               tabindex="4"/>

        <html:hidden property="dto(isUpdateCustomerInfo)" value="true" styleId="isUpdateCustomerInfo"/>
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
        <fmt:message key="Invoice.contactPerson"/>
    </td>
    <td class="contain">
        <fanta:select property="dto(contactPersonId)" listName="searchContactPersonList"
                      module="/contacts" firstEmpty="true"
                      labelProperty="contactPersonName"
                      valueProperty="contactPersonId"
                      styleClass="middleSelect"
                      readOnly="${'delete' == op}"
                      tabIndex="5">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="addressId"
                             value="${not empty invoiceForm.dtoMap['addressId']?invoiceForm.dtoMap['addressId']:0}"/>
        </fanta:select>
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
        <fmt:message key="Invoice.sentAddress"/>
    </td>
    <td class="contain">
        <fanta:select property="dto(sentAddressId)" listName="invoiceAddressRelationList"
                      labelProperty="relatedAddressName" valueProperty="relatedAddressId" styleClass="middleSelect"
                      readOnly="${op == 'delete'}"
                      onChange="reSubmit()"
                      module="/contacts" tabIndex="6" firstEmpty="true">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="addressId" value="${not empty invoiceForm.dtoMap['addressId'] ? invoiceForm.dtoMap['addressId'] : 0}"/>
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
        <fmt:message key="Invoice.sentContactPerson"/>
    </td>
    <td class="contain">
        <fanta:select property="dto(sentContactPersonId)" listName="searchContactPersonList"
                      module="/contacts" firstEmpty="true"
                      labelProperty="contactPersonName"
                      valueProperty="contactPersonId"
                      styleClass="middleSelect"
                      tabIndex="7"
                      readOnly="${'delete' == op}">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="addressId"
                         value="${not empty invoiceForm.dtoMap['sentAddressId'] ? invoiceForm.dtoMap['sentAddressId'] : 0}"/>
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
                      readOnly="${'delete' == op || existsReminders}"
                      tabIndex="11">
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
                      firstEmpty="true" styleClass="middleSelect" readOnly="${'delete' == op}" tabIndex="7">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="addressId" value="${not empty invoiceForm.dtoMap['addressId'] ? invoiceForm.dtoMap['addressId'] : 0}"/>
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
                      tabIndex="12">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
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
        <fmt:message key="Invoice.netGross"/>
    </td>
    <td class="contain">
        <c:set var="netGrossOptions" value="${app2:getNetGrossOptions(pageContext.request)}"/>
        <html:select property="dto(netGross)"
                     styleClass="middleSelect"
                     readonly="${'delete' == op || 'true' == invoiceForm.dtoMap['hasRelation']}"
                     tabindex="13">
            <html:option value=""/>
            <html:options collection="netGrossOptions"
                          property="value"
                          labelProperty="label"/>
        </html:select>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Invoice.totalAmountGross"/>
    </td>
    <td class="contain">
        <html:hidden property="dto(totalAmountGross)"/>
        <fmt:formatNumber var="totalAmountGross"
                          value="${invoiceForm.dtoMap['totalAmountGross']}"
                          type="number"
                          pattern="${numberFormat}"/>
        ${totalAmountGross}
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
                      tabIndex="14">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
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
