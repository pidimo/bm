<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>

<html:hidden property="dto(canUpdateInvoiceType)"/>
<div class="row">
    <div class="${app2:getFormGroupClassesTwoColumns()}">
        <label class="${app2:getFormLabelClassesTwoColumns()}">
            <fmt:message key="Invoice.number"/>
        </label>
        <div class="${app2:getFormContainClassesTwoColumns(true)}">
            <app:text property="dto(number)"
                      styleClass="mediumText"
                      view="true"/>
            <span class=" glyphicon form-control-feedback iconValidation"></span>
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
                         styleClass="middleSelect ${app2:getFormSelectClasses()}"
                         readonly="${'delete' == op || (null != invoiceForm.dtoMap['canUpdateInvoiceType'] && 'false' == invoiceForm.dtoMap['canUpdateInvoiceType'])}"
                         onchange="javascript:onSubmit(this);" tabindex="2">
                <html:option value=""/>
                <html:options collection="invoiceTypeList"
                              property="value"
                              labelProperty="label"/>
            </html:select>
            <span class=" glyphicon form-control-feedback iconValidation"></span>
        </div>
    </div>

    <div class="${app2:getFormGroupClassesTwoColumns()}">
        <label class="${app2:getFormLabelClassesTwoColumns()}" for="startDate">
            <fmt:message key="Invoice.invoiceDate"/>
        </label>
        <div class="${app2:getFormContainClassesTwoColumns('delete' == op || existsReminders)}">
            <c:if test="${'delete' != op || !existsReminders}">
            <div class="input-group date">
                </c:if>
                <app:dateText property="dto(invoiceDate)"
                              styleId="startDate"
                              calendarPicker="${op != 'delete'}"
                              datePatternKey="${datePattern}"
                              styleClass="text ${app2:getFormInputClasses()}"
                              mode="bootstrap"
                              maxlength="10"
                              currentDate="true"
                              view="${'delete' == op || existsReminders}"
                              tabindex="3"/>
                <c:if test="${'delete' != op || !existsReminders}">
            </div>
            </c:if>
            <span class=" glyphicon form-control-feedback iconValidation"></span>
        </div>
    </div>
</div>
<div class="row">
    <div class="${app2:getFormGroupClassesTwoColumns()}">
        <label class="${app2:getFormLabelClassesTwoColumns()}" for="fieldAddressName_id">
            <fmt:message key="Invoice.contact"/>
        </label>
        <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
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

            <div class="input-group">
                <app:text property="dto(addressName)"
                          styleClass="middleText ${app2:getFormInputClasses()}"
                          maxlength="40"
                          readonly="true"
                          styleId="fieldAddressName_id"
                          view="${'delete' == op}"
                          tabindex="4"/>
                <html:hidden property="dto(addressId)" styleId="fieldAddressId_id"/>

                <span class="input-group-btn">
                    <c:if test="${not empty addressEditLink}">
                        <app:link action="${addressEditLink}" contextRelative="true" titleKey="Common.edit" tabindex="5" styleClass="${app2:getFormButtonClasses()}">
                            <span class="glyphicon glyphicon-edit"></span>
                        </app:link>
                    </c:if>

                    <tags:bootstrapSelectPopup
                            styleId="searchAddress_id"
                            url="/contacts/SearchAddress.do"
                            name="searchAddress"
                            titleKey="Common.search"
                            modalTitleKey="Contact.Title.search"
                            isLargeModal="true"
                            submitOnSelect="true"
                            tabindex="6" hide="${'delete' == op}"/>
                    <tags:clearBootstrapSelectPopup
                            keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id"
                            titleKey="Common.clear"
                            submitOnClear="true"
                            tabindex="7" hide="${'delete' == op}"/>
                </span>
            </div>
            <span class=" glyphicon form-control-feedback iconValidation"></span>
            <html:hidden property="dto(isUpdateCustomerInfo)" value="true" styleId="isUpdateCustomerInfo"/>
        </div>
    </div>
    <div class="${app2:getFormGroupClassesTwoColumns()}">
        <label class="${app2:getFormLabelClassesTwoColumns()}">
            <fmt:message key="Invoice.paymentDate"/>
        </label>
        <div class="${app2:getFormContainClassesTwoColumns(true)}">
            <app:dateText property="dto(paymentDate)"
                          styleId="startDate"
                          calendarPicker="false"
                          datePatternKey="${datePattern}"
                          styleClass="text"
                          maxlength="8"
                          view="true"/>
        </div>
    </div>
</div>
<div class="row">
   <div class="${app2:getFormGroupClassesTwoColumns()}">
       <label class="${app2:getFormLabelClassesTwoColumns()}" for="contactPersonId_id">
           <fmt:message key="Invoice.contactPerson"/>
       </label>
       <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
           <fanta:select property="dto(contactPersonId)"
                         styleId="contactPersonId_id"
                         listName="searchContactPersonList"
                         module="/contacts" firstEmpty="true"
                         labelProperty="contactPersonName"
                         valueProperty="contactPersonId"
                         styleClass="middleSelect ${app2:getFormSelectClasses()}"
                         readOnly="${'delete' == op}"
                         tabIndex="9">
               <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
               <fanta:parameter field="addressId"
                                value="${not empty invoiceForm.dtoMap['addressId']?invoiceForm.dtoMap['addressId']:0}"/>
           </fanta:select>
           <span class=" glyphicon form-control-feedback iconValidation"></span>
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
                          maxlength="10"
                          view="true"
                          readonly="${'delete' == op}"/>
            <span class=" glyphicon form-control-feedback iconValidation"></span>
        </div>
    </div>
</div>
<div class="row">
    <div class="${app2:getFormGroupClassesTwoColumns()}">
        <label class="${app2:getFormLabelClassesTwoColumns()}" for="sentAddressId_id">
            <fmt:message key="Invoice.sentAddress"/>
        </label>
        <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
            <fanta:select property="dto(sentAddressId)"
                          styleId="sentAddressId_id"
                          listName="invoiceAddressRelationList"
                          labelProperty="relatedAddressName"
                          valueProperty="relatedAddressId"
                          styleClass="middleSelect ${app2:getFormSelectClasses()}"
                          readOnly="${op == 'delete'}"
                          onChange="reSubmit()"
                          module="/contacts" tabIndex="11" firstEmpty="true">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                <fanta:parameter field="addressId" value="${not empty invoiceForm.dtoMap['addressId'] ? invoiceForm.dtoMap['addressId'] : 0}"/>
            </fanta:select>
            <span class=" glyphicon form-control-feedback iconValidation"></span>
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
        <label class="${app2:getFormLabelClassesTwoColumns()}" for="sentContactPersonId_id">
            <fmt:message key="Invoice.sentContactPerson"/>
        </label>
        <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
            <fanta:select property="dto(sentContactPersonId)" listName="searchContactPersonList"
                          module="/contacts" firstEmpty="true"
                          labelProperty="contactPersonName"
                          valueProperty="contactPersonId"
                          styleId="sentContactPersonId_id"
                          styleClass="middleSelect ${app2:getFormSelectClasses()}"
                          tabIndex="12"
                          readOnly="${'delete' == op}">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                <fanta:parameter field="addressId"
                                 value="${not empty invoiceForm.dtoMap['sentAddressId'] ? invoiceForm.dtoMap['sentAddressId'] : 0}"/>
            </fanta:select>
            <span class=" glyphicon form-control-feedback iconValidation"></span>
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
                          styleClass="middleSelect ${app2:getFormSelectClasses()}"
                          firstEmpty="true"
                          module="/catalogs"
                          tabIndex="13"
                          readOnly="${'delete' == op || existsReminders}">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            </fanta:select>
            <span class=" glyphicon form-control-feedback iconValidation"></span>
        </div>
    </div>
</div>
<div class="row">
    <div class="${app2:getFormGroupClassesTwoColumns()}">
        <label class="${app2:getFormLabelClassesTwoColumns()}" for="additionalAddressId_id">
            <fmt:message key="Invoice.additionalAddress"/>
        </label>
        <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
            <fanta:select property="dto(additionalAddressId)"
                          styleId="additionalAddressId_id"
                          listName="additionalAddressSelectList"
                          labelProperty="name" valueProperty="additionalAddressId" module="/contacts"
                          preProcessor="com.piramide.elwis.web.contactmanager.el.AdditionalAddressSelectPreProcessor"
                          firstEmpty="true" styleClass="middleSelect ${app2:getFormSelectClasses()}"
                          readOnly="${'delete' == op}" tabIndex="14">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                <fanta:parameter field="addressId" value="${not empty invoiceForm.dtoMap['addressId'] ? invoiceForm.dtoMap['addressId'] : 0}"/>
            </fanta:select>
            <span class=" glyphicon form-control-feedback iconValidation"></span>
        </div>
    </div>

    <div class="${app2:getFormGroupClassesTwoColumns()}">
        <label class="${app2:getFormLabelClassesTwoColumns()}" for="templateId_id">
            <fmt:message key="Invoice.template"/>
        </label>
        <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
            <fanta:select property="dto(templateId)"
                          listName="invoiceTemplateList"
                          labelProperty="title"
                          valueProperty="templateId"
                          styleId="templateId_id"
                          styleClass="middleSelect ${app2:getFormSelectClasses()}"
                          module="/catalogs"
                          firstEmpty="true"
                          readOnly="${'delete' == op}"
                          tabIndex="15">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            </fanta:select>
            <span class=" glyphicon form-control-feedback iconValidation"></span>
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
            <span class=" glyphicon form-control-feedback iconValidation"></span>
        </div>
    </div>

    <div class="${app2:getFormGroupClassesTwoColumns()}">
        <label class="${app2:getFormLabelClassesTwoColumns()}" for="netGross_id">
            <fmt:message key="Invoice.netGross"/>
        </label>
        <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
            <c:set var="netGrossOptions" value="${app2:getNetGrossOptions(pageContext.request)}"/>
            <html:select property="dto(netGross)"
                         styleId="netGross_id"
                         styleClass="middleSelect ${app2:getFormSelectClasses()}"
                         readonly="${'delete' == op}"
                         tabindex="16">
                <html:option value=""/>
                <html:options collection="netGrossOptions"
                              property="value"
                              labelProperty="label"/>
            </html:select>
            <span class=" glyphicon form-control-feedback iconValidation"></span>
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
            <span class=" glyphicon form-control-feedback iconValidation"></span>
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
                          styleClass="middleSelect ${app2:getFormSelectClasses()}"
                          module="/catalogs"
                          readOnly="${'delete' == op}"
                          tabIndex="17">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            </fanta:select>
            <span class=" glyphicon form-control-feedback iconValidation"></span>
        </div>
    </div>
</div>
<div class="row">
    <div class="${app2:getFormGroupClassesTwoColumns()}">
        <label class="${app2:getFormLabelClassesTwoColumns()}">
            <fmt:message key="Invoice.openAmount"/>
        </label>
        <div class="${app2:getFormContainClassesTwoColumns(true)}">
            <html:hidden property="dto(openAmount)"/>
            <fmt:formatNumber var="openAmount"
                              value="${invoiceForm.dtoMap['openAmount']}"
                              type="number"
                              pattern="${numberFormat}"/>
            ${openAmount}
            <span class=" glyphicon form-control-feedback iconValidation"></span>
        </div>
    </div>
    <div class="${app2:getFormGroupClassesTwoColumns()}">
        <label class="${app2:getFormLabelClassesTwoColumns()}" for="serviceDate_id">
            <fmt:message key="Invoice.serviceDate"/>
        </label>
        <div class="${app2:getFormContainClassesTwoColumns('delete' == op || existsReminders)}">
            <c:if test="${'delete' != op || !existsReminders}">
            <div class="input-group date">
                </c:if>
                <app:dateText property="dto(serviceDate)"
                              styleId="serviceDate_id"
                              calendarPicker="${op != 'delete'}"
                              datePatternKey="${datePattern}"
                              styleClass="text ${app2:getFormInputClasses()}"
                              mode="bootstrap"
                              maxlength="10"
                              view="${'delete' == op || existsReminders}"
                              tabindex="17"/>
                <c:if test="${'delete' != op || !existsReminders}">
            </div>
            </c:if>
            <span class=" glyphicon form-control-feedback iconValidation"></span>
        </div>
    </div>
</div>