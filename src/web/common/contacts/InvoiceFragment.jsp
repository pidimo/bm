<%@ include file="/Includes.jsp" %>
<tr>
    <td width="15%" class="label">
        <fmt:message key="Invoice.number"/>
    </td>
    <td width="35%" class="contain" ${numberColspan}>
        <app:text property="dto(number)"
                  styleClass="mediumText"
                  view="true" tabindex="1"/>
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
                      currentDate="true"
                      view="${'delete' == op || existsReminders}"
                      tabindex="5"/>
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
                     onchange="javascript:onSubmit(this);"
                     tabindex="2">
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
        <fmt:message key="Invoice.contactPerson"/>
    </td>
    <td class="contain">
        <fanta:select property="dto(contactPersonId)" listName="searchContactPersonList"
                      module="/contacts" firstEmpty="true"
                      labelProperty="contactPersonName"
                      valueProperty="contactPersonId"
                      styleClass="middleSelect"
                      readOnly="${'delete' == op}"
                      tabIndex="3">
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
                      module="/contacts" tabIndex="4" firstEmpty="true">
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
                      tabIndex="4"
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
                      tabIndex="6"
                      readOnly="${'delete' == op || existsReminders}">
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
                      firstEmpty="true" styleClass="middleSelect" readOnly="${'delete' == op}" tabIndex="4">
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
                      tabIndex="7">
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
                     readonly="${'delete' == op}"
                     tabindex="8">
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
                      tabIndex="9">
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
        <fmt:formatNumber var="openAmount" value="${invoiceForm.dtoMap['openAmount']}" type="number"
                          pattern="${numberFormat}"/>
        ${openAmount}
    </td>
</tr>
