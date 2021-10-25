<%@ include file="/Includes.jsp" %>

<div class="row">
    <div class="${app2:getFormGroupClassesTwoColumns()}">
        <label class="${app2:getFormLabelClassesTwoColumns()}" for="number_id">
            <fmt:message key="Invoice.number"/>
        </label>

        <div class="${app2:getFormContainClassesTwoColumns(true)}" ${numberColspan}>
            <app:text property="dto(number)"
                      styleId="number_id"
                      styleClass="${app2:getFormInputClasses()} mediumText"
                      view="true"/>
            <span class="glyphicon form-control-feedback iconValidation"></span>
        </div>

    </div>
    <div class="${app2:getFormGroupClassesTwoColumns()}">
        <label class="${app2:getFormLabelClassesTwoColumns()}" for="title_id">
            <fmt:message key="Invoice.title"/>
        </label>

        <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
            <app:text property="dto(title)" styleId="title_id"
                      styleClass="${app2:getFormInputClasses()} middleText"
                      maxlength="100"
                      view="${'delete' == op}" tabindex="1"/>
            <span class="glyphicon form-control-feedback iconValidation"></span>
        </div>
    </div>
</div>
<div class="row">
    <div class="${app2:getFormGroupClassesTwoColumns()}">
        <label class="${app2:getFormLabelClassesTwoColumns()}" for="type_id">
            <fmt:message key="Invoice.type"/>
        </label>

        <div class="${app2:getFormContainClassesTwoColumns('delete' == op || (null != invoiceForm.dtoMap['canUpdateInvoiceType'] && 'false' == invoiceForm.dtoMap['canUpdateInvoiceType']))}">
            <html:hidden property="dto(changeInvoiceType)" styleId="changeInvoiceType" value="false"/>
            <html:select property="dto(type)"
                         styleId="type_id"
                         styleClass="${app2:getFormSelectClasses()} middleSelect"
                         readonly="${'delete' == op || (null != invoiceForm.dtoMap['canUpdateInvoiceType'] && 'false' == invoiceForm.dtoMap['canUpdateInvoiceType'])}"
                         onchange="javascript:onSubmit(this);"
                         tabindex="2">
                <html:option value=""/>
                <html:options collection="invoiceTypeList"
                              property="value"
                              labelProperty="label"/>
            </html:select>
            <span class="glyphicon form-control-feedback iconValidation"></span>
        </div>
    </div>
    <div class="${app2:getFormGroupClassesTwoColumns()}">
        <label class="${app2:getFormLabelClassesTwoColumns()}" for="startDate">
            <fmt:message key="Invoice.invoiceDate"/>
        </label>

        <div class="${app2:getFormContainClassesTwoColumns('delete' == op || existsReminders)}">
            <div class="input-group date">
                <app:dateText property="dto(invoiceDate)"
                              styleId="startDate"
                              mode="bootstrap"
                              calendarPicker="${op != 'delete'}"
                              datePatternKey="${datePattern}"
                              styleClass="${app2:getFormInputClasses()} datepicker text"
                              maxlength="10"
                              currentDate="true"
                              view="${'delete' == op || existsReminders}"
                              tabindex="3"/>
            </div>
            <span class="glyphicon form-control-feedback iconValidation"></span>
        </div>
    </div>
</div>
<div class="row">
    <div class="${app2:getFormGroupClassesTwoColumns()}">
        <label class="${app2:getFormLabelClassesTwoColumns()}" for="contactPersonId_id">
            <fmt:message key="Invoice.contactPerson"/>
        </label>

        <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
            <fanta:select property="dto(contactPersonId)" styleId="contactPersonId_id"
                          listName="searchContactPersonList"
                          module="/contacts" firstEmpty="true"
                          labelProperty="contactPersonName"
                          valueProperty="contactPersonId"
                          styleClass="${app2:getFormInputClasses()} middleSelect"
                          readOnly="${'delete' == op}"
                          tabIndex="4">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                <fanta:parameter field="addressId"
                                 value="${not empty invoiceForm.dtoMap['addressId']?invoiceForm.dtoMap['addressId']:0}"/>
            </fanta:select>
            <span class="glyphicon form-control-feedback iconValidation"></span>
        </div>
    </div>
    <div class="${app2:getFormGroupClassesTwoColumns()}">
        <label class="${app2:getFormLabelClassesTwoColumns()}" for="startDate">
            <fmt:message key="Invoice.paymentDate"/>
        </label>

        <div class="${app2:getFormContainClassesTwoColumns(true)}">
            <app:dateText property="dto(paymentDate)"
                          styleId="startDate"
                          calendarPicker="false"
                          datePatternKey="${datePattern}"
                          styleClass="text"
                          view="true"/>
        </div>
    </div>
</div>
<div class="row">
    <div class="${app2:getFormGroupClassesTwoColumns()}">
        <label class="${app2:getFormLabelClassesTwoColumns()}" for="sentAddressId_id">
            <fmt:message key="Invoice.sentAddress"/>
        </label>

        <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
            <fanta:select property="dto(sentAddressId)" styleId="sentAddressId_id"
                          listName="invoiceAddressRelationList"
                          labelProperty="relatedAddressName" valueProperty="relatedAddressId"
                          styleClass="${app2:getFormSelectClasses()} middleSelect"
                          readOnly="${op == 'delete'}"
                          onChange="reSubmit()"
                          module="/contacts" tabIndex="5" firstEmpty="true">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                <fanta:parameter field="addressId"
                                 value="${not empty invoiceForm.dtoMap['addressId'] ? invoiceForm.dtoMap['addressId'] : 0}"/>
            </fanta:select>
            <span class="glyphicon form-control-feedback iconValidation"></span>
        </div>
    </div>
    <div class="${app2:getFormGroupClassesTwoColumns()}">
        <label class="${app2:getFormLabelClassesTwoColumns()}" for="startDate">
            <fmt:message key="Invoice.reminderDate"/>
        </label>

        <div class="${app2:getFormContainClassesTwoColumns(true)}">
            <app:dateText property="dto(reminderDate)"
                          styleId="startDate"
                          calendarPicker="false"
                          datePatternKey="${datePattern}"
                          styleClass="text"
                          maxlength="6"
                          view="true"
                          readonly="${'delete' == op}"/>
        </div>
    </div>
</div>
<div class="row">
    <div class="${app2:getFormGroupClassesTwoColumns()}">
        <label class="${app2:getFormLabelClassesTwoColumns()}" for="sentContactPersonId_id">
            <fmt:message key="Invoice.sentContactPerson"/>
        </label>

        <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
            <fanta:select property="dto(sentContactPersonId)" styleId="sentContactPersonId_id"
                          listName="searchContactPersonList"
                          module="/contacts" firstEmpty="true"
                          labelProperty="contactPersonName"
                          valueProperty="contactPersonId"
                          styleClass="${app2:getFormSelectClasses()} middleSelect"
                          tabIndex="6"
                          readOnly="${'delete' == op}">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                <fanta:parameter field="addressId"
                                 value="${not empty invoiceForm.dtoMap['sentAddressId'] ? invoiceForm.dtoMap['sentAddressId'] : 0}"/>
            </fanta:select>
            <span class="glyphicon form-control-feedback iconValidation"></span>
        </div>
    </div>
    <div class="${app2:getFormGroupClassesTwoColumns()}">
        <label class="${app2:getFormLabelClassesTwoColumns()}">
            <fmt:message key="Invoice.reminderLevel"/>
        </label>

        <div class="${app2:getFormContainClassesTwoColumns(true)}">
            <c:out value="${app2:getReminderLevelName(invoiceForm.dtoMap['reminderLevel'],pageContext.request)}"/>
            <html:hidden property="dto(reminderLevel)"/>
        </div>
    </div>
</div>
<div class="row">
    <div class="${app2:getFormGroupClassesTwoColumns()}">
        <label class="${app2:getFormLabelClassesTwoColumns()}" for="additionalAddressId_id">
            <fmt:message key="Invoice.additionalAddress"/>
        </label>

        <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
            <fanta:select property="dto(additionalAddressId)" styleId="additionalAddressId_id"
                          listName="additionalAddressSelectList"
                          labelProperty="name" valueProperty="additionalAddressId" module="/contacts"
                          preProcessor="com.piramide.elwis.web.contactmanager.el.AdditionalAddressSelectPreProcessor"
                          firstEmpty="true" styleClass="${app2:getFormSelectClasses()} middleSelect"
                          readOnly="${'delete' == op}" tabIndex="7">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                <fanta:parameter field="addressId"
                                 value="${not empty invoiceForm.dtoMap['addressId'] ? invoiceForm.dtoMap['addressId'] : 0}"/>
            </fanta:select>
            <span class="glyphicon form-control-feedback iconValidation"></span>
        </div>
    </div>
    <div class="${app2:getFormGroupClassesTwoColumns()}">
        <label class="${app2:getFormLabelClassesTwoColumns()}" for="payConditionId_id">
            <fmt:message key="Invoice.payCondition"/>
        </label>

        <div class="${app2:getFormContainClassesTwoColumns('delete' == op || existsReminders)}">
            <fanta:select property="dto(payConditionId)"
                          styleId="payConditionId_id"
                          listName="payConditionList"
                          labelProperty="name"
                          valueProperty="id"
                          styleClass="${app2:getFormSelectClasses()} middleSelect"
                          firstEmpty="true"
                          module="/catalogs"
                          tabIndex="8"
                          readOnly="${'delete' == op || existsReminders}">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            </fanta:select>
            <span class="glyphicon form-control-feedback iconValidation"></span>
        </div>
    </div>
</div>
<div class="row">
    <div class="${app2:getFormGroupClassesTwoColumns()}">
        <label class="${app2:getFormLabelClassesTwoColumns()}">
            <fmt:message key="Invoice.totalAmountNet"/>
        </label>

        <div class="${app2:getFormContainClassesTwoColumns(true)}">
            <html:hidden property="dto(totalAmountNet)"/>
            <fmt:formatNumber var="totalAmountNet"
                              value="${invoiceForm.dtoMap['totalAmountNet']}"
                              type="number"
                              pattern="${numberFormat}"/>
            ${totalAmountNet}
            <span class="glyphicon form-control-feedback iconValidation"></span>
        </div>
    </div>
    <div class="${app2:getFormGroupClassesTwoColumns()}">
        <label class="${app2:getFormLabelClassesTwoColumns()}" for="templateId_id">
            <fmt:message key="Invoice.template"/>
        </label>

        <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
            <fanta:select property="dto(templateId)"
                          styleId="templateId_id"
                          listName="invoiceTemplateList"
                          labelProperty="title"
                          valueProperty="templateId"
                          styleClass="${app2:getFormSelectClasses()} middleSelect"
                          module="/catalogs"
                          firstEmpty="true"
                          readOnly="${'delete' == op}"
                          tabIndex="9">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            </fanta:select>
            <span class="glyphicon form-control-feedback iconValidation"></span>
        </div>
    </div>
</div>
<div class="row">
    <div class="${app2:getFormGroupClassesTwoColumns()}">
        <label class="${app2:getFormLabelClassesTwoColumns()}">
            <fmt:message key="Invoice.totalAmountGross"/>
        </label>

        <div class="${app2:getFormContainClassesTwoColumns(true)}">
            <html:hidden property="dto(totalAmountGross)"/>
            <fmt:formatNumber var="totalAmountGross"
                              value="${invoiceForm.dtoMap['totalAmountGross']}"
                              type="number"
                              pattern="${numberFormat}"/>
            ${totalAmountGross}
            <span class="glyphicon form-control-feedback iconValidation"></span>
        </div>
    </div>
    <div class="${app2:getFormGroupClassesTwoColumns()}">
        <label class="${app2:getFormLabelClassesTwoColumns()}">
            <fmt:message key="Invoice.netGross"/>
        </label>

        <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
            <c:set var="netGrossOptions" value="${app2:getNetGrossOptions(pageContext.request)}"/>
            <html:select property="dto(netGross)"
                         styleClass="${app2:getFormSelectClasses()} middleSelect"
                         readonly="${'delete' == op}"
                         tabindex="10">
                <html:option value=""/>
                <html:options collection="netGrossOptions"
                              property="value"
                              labelProperty="label"/>
            </html:select>
            <span class="glyphicon form-control-feedback iconValidation"></span>
        </div>
    </div>
</div>
<div class="row">
    <div class="${app2:getFormGroupClassesTwoColumns()}">
        <label class="${app2:getFormLabelClassesTwoColumns()}">
            <fmt:message key="Invoice.openAmount"/>
        </label>

        <div class="${app2:getFormContainClassesTwoColumns(true)}" colspan="3">
            <html:hidden property="dto(openAmount)"/>
            <fmt:formatNumber var="openAmount" value="${invoiceForm.dtoMap['openAmount']}" type="number"
                              pattern="${numberFormat}"/>
            ${openAmount}
            <span class="glyphicon form-control-feedback iconValidation"></span>
        </div>
    </div>
    <div class="${app2:getFormGroupClassesTwoColumns()}">
        <label class="${app2:getFormLabelClassesTwoColumns()}" for="currencyId_id">
            <fmt:message key="Invoice.currency"/>
        </label>

        <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
            <fanta:select property="dto(currencyId)"
                          styleId="currencyId_id"
                          listName="basicCurrencyList"
                          labelProperty="name"
                          valueProperty="id"
                          firstEmpty="true"
                          styleClass="${app2:getFormSelectClasses()} middleSelect"
                          module="/catalogs"
                          readOnly="${'delete' == op}"
                          tabIndex="11">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            </fanta:select>
            <span class="glyphicon form-control-feedback iconValidation"></span>
        </div>
    </div>
</div>
<div class="row">
    <div class="${app2:getFormGroupClassesTwoColumns()}">
    </div>
    <div class="${app2:getFormGroupClassesTwoColumns()}">
        <label class="${app2:getFormLabelClassesTwoColumns()}" for="serviceDate_id">
            <fmt:message key="Invoice.serviceDate"/>
        </label>

        <div class="${app2:getFormContainClassesTwoColumns('delete' == op || existsReminders)}">
            <div class="input-group date">
                <app:dateText property="dto(serviceDate)"
                              styleId="serviceDate_id"
                              mode="bootstrap"
                              calendarPicker="${op != 'delete'}"
                              datePatternKey="${datePattern}"
                              styleClass="${app2:getFormInputClasses()} datepicker text"
                              maxlength="10"
                              view="${'delete' == op || existsReminders}"
                              tabindex="12"/>
            </div>
            <span class="glyphicon form-control-feedback iconValidation"></span>
        </div>
    </div>
</div>

<tags:jQueryValidation formName="invoiceForm"/>