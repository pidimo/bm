<%@ include file="/Includes.jsp" %>
<tr>
    <td width="15%" class="label">
        <fmt:message key="Invoice.number"/>
    </td>
    <td width="35%" class="contain" ${numberColspan}>
        <app:text property="dto(number)"
                  styleClass="mediumText"
                  view="true"/>
    </td>
    <td width="15%" class="label">
        <fmt:message key="Invoice.invoiceDate"/>
    </td>
    <td width="35%" class="contain">
        <app:dateText property="dto(invoiceDate)"
                      styleId="startDate"
                      calendarPicker="${op != 'delete'}"
                      datePatternKey="${datePattern}"
                      styleClass="text"
                      maxlength="10"
                      currentDate="true" view="${'delete' == op || existsReminders}"
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
                     readonly="${'delete' == op || (null != invoiceForm.dtoMap['creditNoteOfId'] && 'true' == invoiceForm.dtoMap['hasRelation'])}"
                     onchange="javascript:onSubmit(this);"
                     tabindex="3">
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
    <td width="15%" class="label">
        <fmt:message key="Invoice.creditNoteOf"/>
    </td>
    <td width="35%" class="contain">
        <html:hidden property="dto(hasRelation)" styleId="hasRelationId"/>
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
                          tabindex="1"/>

                <tags:selectPopup
                        url="/finance/Invoice/Search.do?chekIfButtonPressed=true&parameter(type)=${invoiceType}"
                        name="invoiceSearchList"
                        titleKey="Common.search"
                        width="820"
                        submitOnSelect="false"
                        hide="${'delete' == op}"
                        tabindex="2"/>
                <tags:clearSelectPopup keyFieldId="fieldInvoiceId_id"
                                       nameFieldId="fieldInvoiceNumber_id"
                                       titleKey="Common.clear"
                                       hide="${'delete' == op}"
                                       tabindex="3"/>
            </c:when>
            <c:otherwise>
                <html:hidden property="dto(creditNoteOfId)"/>
                <html:hidden property="dto(relatedInvoiceNumber)"/>

                <c:out value="${invoiceForm.dtoMap['relatedInvoiceNumber']}"/>

                <app:link contextRelative="true"
                          action="contacts/Invoice/Forward/Update.do?invoiceId=${invoiceForm.dtoMap['creditNoteOfId']}&dto(invoiceId)=${invoiceForm.dtoMap['creditNoteOfId']}&dto(number)=${app2:encode(invoiceForm.dtoMap['relatedInvoiceNumber'])}">
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
                      readOnly="${'delete' == op || existsReminders}"
                      tabIndex="7">
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
    <td class="contain" colspan="3">
        <c:set var="netGrossOptions" value="${app2:getNetGrossOptions(pageContext.request)}"/>
        <html:select property="dto(netGross)"
                     styleClass="middleSelect"
                     readonly="${'delete' == op || 'true' == invoiceForm.dtoMap['hasRelation']}"
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
