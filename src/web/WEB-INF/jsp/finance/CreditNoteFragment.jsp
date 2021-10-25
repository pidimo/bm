<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ include file="/Includes.jsp" %>
<div class="row">
    <div class="${app2:getFormGroupClassesTwoColumns()}">
        <label class="${app2:getFormLabelClassesTwoColumns()}" for="number_id">
            <fmt:message key="Invoice.number"/>
        </label>

        <div class="${app2:getFormContainClassesTwoColumns(true)}">
            <app:text property="dto(number)"
                      styleId="number_id"
                      styleClass="mediumText ${app2:getFormInputClasses()}"
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

        <div class="${app2:getFormContainClassesTwoColumns('delete' == op || (null != invoiceForm.dtoMap['creditNoteOfId'] && 'true' == invoiceForm.dtoMap['hasRelation']))}">
            <html:hidden property="dto(changeInvoiceType)" styleId="changeInvoiceType" value="false"/>
            <html:select property="dto(type)"
                         styleId="type_id"
                         styleClass="middleSelect ${app2:getFormSelectClasses()}"
                         readonly="${'delete' == op || (null != invoiceForm.dtoMap['creditNoteOfId'] && 'true' == invoiceForm.dtoMap['hasRelation'])}"
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
        <label class="${app2:getFormLabelClassesTwoColumns()}" for="fieldInvoiceNumber_id">
            <fmt:message key="Invoice.creditNoteOf"/>
        </label>

        <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
            <c:choose>
                <c:when test="${null == invoiceForm.dtoMap['creditNoteOfId'] || 'false' == invoiceForm.dtoMap['hasRelation']}">

                    <div class="input-group">
                        <app:text property="dto(relatedInvoiceNumber)"
                                  styleClass="middleText ${app2:getFormInputClasses()}"
                                  maxlength="40"
                                  readonly="true"
                                  styleId="fieldInvoiceNumber_id"
                                  view="${'delete' == op}"
                                  tabindex="3"/>
                        <html:hidden property="dto(creditNoteOfId)" styleId="fieldInvoiceId_id"/>
                        <html:hidden property="dto(copyOfButtonPressed)" value="false" styleId="copyOfButtonPressed"/>
                        <span class="input-group-btn">
                            <tags:bootstrapSelectPopup
                                    styleId="invoiceSearchList_id"
                                    url="/finance/Invoice/Search.do?chekIfButtonPressed=true&parameter(type)=${invoiceType}"
                                    name="invoiceSearchList"
                                    titleKey="Common.search"
                                    modalTitleKey="Invoice.singleSearch"
                                    isLargeModal="true"
                                    submitOnSelect="false"
                                    hide="${'delete' == op}"
                                    tabindex="4"/>
                            <%--modalTitleKey="Contact.Title.search"--%>
                            <tags:clearBootstrapSelectPopup
                                    keyFieldId="fieldInvoiceId_id"
                                    nameFieldId="fieldInvoiceNumber_id"
                                    titleKey="Common.clear"
                                    hide="${'delete' == op}"
                                    tabindex="5"/>
                        </span>
                    </div>
                    <span class=" glyphicon form-control-feedback iconValidation"></span>
                </c:when>
                <c:otherwise>
                    <html:hidden property="dto(creditNoteOfId)"/>
                    <html:hidden property="dto(relatedInvoiceNumber)"/>
                    <div class="form-control-static">
                        <c:out value="${invoiceForm.dtoMap['relatedInvoiceNumber']}"/>
                        <span class="pull-right">
                             <app:link contextRelative="true"
                                       titleKey="Common.edit"
                                       action="finance/Invoice/Forward/Update.do?invoiceId=${invoiceForm.dtoMap['creditNoteOfId']}&dto(invoiceId)=${invoiceForm.dtoMap['creditNoteOfId']}&dto(number)=${app2:encode(invoiceForm.dtoMap['relatedInvoiceNumber'])}&index=0">
                                 <span class="glyphicon glyphicon-edit"></span>
                             </app:link>
                        </span>
                    </div>
                    <fmt:message key="InvoicePayment.text.creditNoteDeleteMessage" var="deleteCreditNoteMessage">
                        <fmt:param value="${invoiceForm.dtoMap['number']}"/>
                    </fmt:message>
                    <html:hidden property="dto(invoicePaymentMessage)" value="${deleteCreditNoteMessage}"
                                 styleId="invoicePaymentMessageId"/>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
<div class="row">
    <div class="${app2:getFormGroupClassesTwoColumns()}">
        <label class="${app2:getFormLabelClassesTwoColumns()}" for="fieldAddressName_id">
            <fmt:message key="Invoice.contact"/>
        </label>

        <div class="${app2:getFormContainClassesTwoColumns('delete' == op || 'true' == invoiceForm.dtoMap['hasRelation'])}">

            <c:if test="${not empty invoiceForm.dtoMap['addressId'] and 'update' == op}">
                <app2:checkAccessRight functionality="CONTACT" permission="VIEW">
                    <c:set var="personType"><%= ContactConstants.ADDRESSTYPE_PERSON %>
                    </c:set>
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
            <c:set var="addressViewOnly" value="${'delete' == op || 'true' == invoiceForm.dtoMap['hasRelation']}"/>
            <div class="${addressViewOnly ? "" : "input-group"}">
                <app:text property="dto(addressName)"
                          styleClass="middleText ${app2:getFormInputClasses()}"
                          maxlength="40"
                          readonly="true"
                          styleId="fieldAddressName_id"
                          view="${'delete' == op || 'true' == invoiceForm.dtoMap['hasRelation']}"
                          tabindex="6"/>
                <html:hidden property="dto(addressId)" styleId="fieldAddressId_id"/>
                <span class="${addressViewOnly ? "pull-right" : "input-group-btn"}">
                    <c:if test="${not empty addressEditLink}">
                        <app:link action="${addressEditLink}" contextRelative="true" tabindex="7"
                                  styleClass="${addressViewOnly ? '' : 'btn btn-default'}" titleKey="Common.edit">
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
                            hide="${'delete' == op || 'true' == invoiceForm.dtoMap['hasRelation']}"
                            tabindex="8"/>
                    <tags:clearBootstrapSelectPopup
                            keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id"
                            titleKey="Common.clear"
                            submitOnClear="true"
                            hide="${'delete' == op || 'true' == invoiceForm.dtoMap['hasRelation']}"
                            tabindex="9"/>
                </span>
            </div>
            <span class=" glyphicon form-control-feedback iconValidation"></span>
            <html:hidden property="dto(isUpdateCustomerInfo)" value="true" styleId="isUpdateCustomerInfo"/>
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
                              calendarPicker="${op != 'delete'}"
                              datePatternKey="${datePattern}"
                              styleClass="text ${app2:getFormInputClasses()}"
                              mode="bootstrap"
                              maxlength="10"
                              currentDate="true"
                              view="${'delete' == op || existsReminders}"
                              tabindex="10"/>
            </div>
            <span class=" glyphicon form-control-feedback iconValidation"></span>
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
                          tabIndex="11">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                <fanta:parameter field="addressId"
                                 value="${not empty invoiceForm.dtoMap['addressId']?invoiceForm.dtoMap['addressId']:0}"/>
            </fanta:select>
            <span class=" glyphicon form-control-feedback iconValidation"></span>
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
                          styleClass="text ${app2:getFormInputClasses()}"
                          maxlength="10"
                          view="true"/>
            <span class=" glyphicon form-control-feedback iconValidation"></span>
        </div>
    </div>
</div>
<div class="row">
    <div class="${app2:getFormGroupClassesTwoColumns()}">
        <label class="${app2:getFormLabelClassesTwoColumns()}" for="sentAddressId_id">
            <fmt:message key="Invoice.sentAddress"/>
        </label>

        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
            <fanta:select property="dto(sentAddressId)"
                          styleId="sentAddressId_id"
                          listName="invoiceAddressRelationList"
                          labelProperty="relatedAddressName" valueProperty="relatedAddressId"
                          styleClass="middleSelect ${app2:getFormSelectClasses()}"
                          readOnly="${op == 'delete'}"
                          onChange="reSubmit()"
                          module="/contacts" tabIndex="12" firstEmpty="true">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                <fanta:parameter field="addressId"
                                 value="${not empty invoiceForm.dtoMap['addressId'] ? invoiceForm.dtoMap['addressId'] : 0}"/>
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
        <label class="${app2:getFormLabelClassesTwoColumns()}" for="sentContactPersonId_id">
            <fmt:message key="Invoice.sentContactPerson"/>
        </label>

        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
            <fanta:select property="dto(sentContactPersonId)"
                          styleId="sentContactPersonId_id"
                          listName="searchContactPersonList"
                          module="/contacts" firstEmpty="true"
                          labelProperty="contactPersonName"
                          valueProperty="contactPersonId"
                          styleClass="middleSelect ${app2:getFormSelectClasses()}"
                          tabIndex="13"
                          readOnly="${'delete' == op}">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                <fanta:parameter field="addressId"
                                 value="${not empty invoiceForm.dtoMap['sentAddressId'] ? invoiceForm.dtoMap['sentAddressId'] : 0}"/>
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
                <fanta:parameter field="addressId"
                                 value="${not empty invoiceForm.dtoMap['addressId'] ? invoiceForm.dtoMap['addressId'] : 0}"/>
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
                          readOnly="${'delete' == op || existsReminders}"
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
        <label class="${app2:getFormLabelClassesTwoColumns()}" for="templateId_id">
            <fmt:message key="Invoice.template"/>
        </label>

        <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
            <fanta:select property="dto(templateId)"
                          styleId="templateId_id"
                          listName="invoiceTemplateList"
                          labelProperty="title"
                          valueProperty="templateId"
                          styleClass="middleSelect ${app2:getFormSelectClasses()}"
                          module="/catalogs"
                          firstEmpty="true"
                          readOnly="${'delete' == op}"
                          tabIndex="16">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            </fanta:select>
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
        <label class="${app2:getFormLabelClassesTwoColumns()}" for="netGross_id">
            <fmt:message key="Invoice.netGross"/>
        </label>

        <div class="${app2:getFormContainClassesTwoColumns('delete' == op || 'true' == invoiceForm.dtoMap['hasRelation'])}">
            <c:set var="netGrossOptions" value="${app2:getNetGrossOptions(pageContext.request)}"/>
            <html:select property="dto(netGross)"
                         styleId="netGross_id"
                         styleClass="middleSelect ${app2:getFormSelectClasses()}"
                         readonly="${'delete' == op || 'true' == invoiceForm.dtoMap['hasRelation']}"
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
    </div>
    <div class="${app2:getFormGroupClassesTwoColumns()}">
        <label class="${app2:getFormLabelClassesTwoColumns()}" for="serviceDate_id">
            <fmt:message key="Invoice.serviceDate"/>
        </label>

        <div class="${app2:getFormContainClassesTwoColumns('delete' == op || existsReminders)}">
            <div class="input-group date">
                <app:dateText property="dto(serviceDate)"
                              styleId="serviceDate_id"
                              calendarPicker="${op != 'delete'}"
                              datePatternKey="${datePattern}"
                              styleClass="text ${app2:getFormInputClasses()}"
                              mode="bootstrap"
                              maxlength="10"
                              view="${'delete' == op || existsReminders}"
                              tabindex="17"/>
            </div>
            <span class=" glyphicon form-control-feedback iconValidation"></span>
        </div>
    </div>
</div>
