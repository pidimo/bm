<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ page import="com.piramide.elwis.utils.FinanceConstants" %>
<%@ include file="/Includes.jsp" %>
<tags:initBootstrapDatepicker/>
<tags:initBootstrapSelectPopup/>

<app2:jScriptUrl url="/finance/Download/Invoice/Document.do?dto(invoiceId)=${invoiceForm.dtoMap['invoiceId']}"
                 var="jsDownloadUrl">
    <app2:jScriptUrlParam param="dto(freeTextId)" value="id_freeText"/>
</app2:jScriptUrl>

<%--url to send invoice via email--%>
<app2:jScriptUrl
        url="${urlSendViaEmail}?invoiceId=${invoiceForm.dtoMap['invoiceId']}"
        var="jsSendViaEmailUrl">
    <app2:jScriptUrlParam param="telecomId" value="telecomId"/>
</app2:jScriptUrl>

<script language="JavaScript" type="text/javascript">
    <!--
    function downloadInvoice(id_freeText) {
        location.href = ${jsDownloadUrl};
    }

    function copyOfButtonPressed() {
        document.getElementById('copyOfButtonPressed').value = 'true';
    }

    function onSubmit(obj) {
        if (obj.options[obj.selectedIndex].value != "") {
            document.getElementById('changeInvoiceType').value = 'true';
            document.getElementById('isUpdateCustomerInfo').value = 'false';
            document.forms[0].submit();
        }
    }

    function sendViaEmail(emailBoxId) {
        var telecomId = document.getElementById(emailBoxId).value;
        location.href = ${jsSendViaEmailUrl};
    }

    function reSubmit() {
        document.getElementById('isUpdateCustomerInfo').value = 'false';
        document.forms[0].submit();
    }

    //-->
</script>

<fmt:message var="datePattern" key="datePattern" scope="request"/>
<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces" scope="request"/>

<c:set var="creditNoteType" value="<%=FinanceConstants.InvoiceType.CreditNote.getConstantAsString()%>" scope="request"/>
<c:set var="invoiceType" value="<%=FinanceConstants.InvoiceType.Invoice.getConstantAsString()%>" scope="request"/>
<c:set var="voucherType" value="<%=FinanceConstants.SequenceRuleType.VOUCHER.getConstantAsString()%>" scope="request"/>

<c:set var="invoiceTypeList" value="${app2:getInvoiceTypes(pageContext.request)}" scope="request"/>

<c:set var="TELECOMTYPE_EMAIL" value="<%=ContactConstants.TELECOMTYPE_EMAIL%>"/>

<div class="${app2:getFormWrapperTwoColumns()}">
    <html:form action="${action}" focus="${'create' == op ? 'dto(type)' : 'dto(title)'}"
               styleClass="form-horizontal">
        <html:hidden property="dto(op)" value="${op}"/>
        <html:hidden property="dto(hasRelation)" styleId="hasRelationId"/>

        <c:if test="${'update'== op || 'delete'== op}">
            <html:hidden property="dto(invoiceId)"/>
        </c:if>

        <c:if test="${'update' == op}">
            <c:set var="documentId" value="${invoiceForm.dtoMap['documentId']}" scope="request"/>
            <c:if test="${not empty msgConfirmation}">
                <html:hidden property="dto(reGenerate)" value="true"/>
            </c:if>

            <html:hidden property="dto(version)"/>
            <html:hidden property="dto(documentId)"/>
            <html:hidden property="dto(ruleFormat)"/>
            <html:hidden property="dto(ruleNumber)"/>
            <html:hidden property="dto(customerNumber)"/>
            <html:hidden property="dto(entityInvoiceDate)"/>
            <html:hidden property="dto(confirmationMsg)" value="${confirmationMsg}" styleId="confirmationMsgId"/>
        </c:if>

        <c:if test="${'delete' == op}">
            <html:hidden property="dto(withReferences)" value="true"/>
        </c:if>

        <div class="row">
            <div class="col-xs-12">
                <app2:securitySubmit operation="${op}"
                                     functionality="INVOICE"
                                     styleClass="${app2:getFormButtonInlineClasses()}"
                                     property="save"
                                     tabindex="24">
                    ${button}
                </app2:securitySubmit>

                <c:if test="${'update' == op}">
                    <app2:securitySubmit operation="${op}"
                                         functionality="INVOICE"
                                         styleClass="${app2:getFormButtonInlineClasses()}"
                                         property="generate"
                                         tabindex="25">
                        <fmt:message key="Invoice.generate"/>
                    </app2:securitySubmit>
                    <c:if test="${not empty documentId}">
                        <app:url var="urlDownload"
                                 value="/Download/Invoice/Document.do?dto(freeTextId)=${documentId}&dto(invoiceId)=${invoiceForm.dtoMap['invoiceId']}"/>
                        <html:button property=""
                                     styleClass="${app2:getFormButtonInlineClasses()}"
                                     onclick="location.href='${urlDownload}'"
                                     tabindex="26">
                            <fmt:message key="Invoice.open"/>
                        </html:button>
                    </c:if>

                    <c:set var="isSendViaEmail"
                           value="${not empty urlSendViaEmail and not empty documentId and app2:hasDefaultMailAccount(pageContext.request)}"/>
                    <c:if test="${isSendViaEmail and not empty invoiceForm.dtoMap['addressId']}">
                        <app2:checkAccessRight functionality="MAIL" permission="EXECUTE">
                            <html:button property=""
                                         styleClass="${app2:getFormButtonInlineClasses()}"
                                         onclick="sendViaEmail('upToEmail_id')"
                                         tabindex="27">
                                <fmt:message key="Invoice.sendViaEmail"/>
                            </html:button>

                            <div class="form-group col-xs-12 col-sm-3">
                                <app:telecomSelect property="toEmail"
                                                   styleId="upToEmail_id"
                                                   tabindex="28"
                                                   telecomIdColumn="telecomid"
                                                   numberColumn="telecomnumber"
                                                   telecomType="${TELECOMTYPE_EMAIL}"
                                                   addressId="${invoiceForm.dtoMap['addressId']}"
                                                   contactPersonId="${invoiceForm.dtoMap['contactPersonId']}"
                                                   showOwner="true"
                                                   styleClass="${app2:getFormSelectClasses()}"
                                                   optionStyleClass="list" showDescription="false"
                                                   selectPredetermined="true"/>

                            </div>

                        </app2:checkAccessRight>
                    </c:if>
                </c:if>

                <html:cancel
                        styleClass="${app2:getFormButtonCancelInlineClasses()} marginLeft"
                        tabindex="29">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </div>
        </div>


        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>
                <c:if test="${'create' == op}">
                    <div class="row">
                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}" for="type_id">
                                <fmt:message key="Invoice.type"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                <html:hidden property="dto(changeInvoiceType)" styleId="changeInvoiceType"
                                             value="false"/>
                                <html:select property="dto(type)"
                                             styleId="type_id"
                                             styleClass="${app2:getFormSelectClasses()} middleSelect"
                                             readonly="false"
                                             tabindex="1"
                                             onchange="javascript:onSubmit(this);">
                                    <html:option value=""/>
                                    <html:options collection="invoiceTypeList"
                                                  property="value"
                                                  labelProperty="label"/>
                                </html:select>
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
                                          view="${'delete' == op}" tabindex="2"/>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <c:choose>
                            <c:when test="${creditNoteType == invoiceForm.dtoMap['type']}">
                                <div class="${app2:getFormGroupClassesTwoColumns()}">
                                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="fieldInvoiceNumber_id">
                                        <fmt:message key="Invoice.creditNoteOf"/>
                                    </label>

                                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                        <div class="input-group">
                                            <app:text property="dto(relatedInvoiceNumber)"
                                                      styleClass="${app2:getFormInputClasses()} middleText"
                                                      maxlength="40"
                                                      readonly="true"
                                                      styleId="fieldInvoiceNumber_id"
                                                      tabindex="3"/>
                                            <html:hidden property="dto(creditNoteOfId)" styleId="fieldInvoiceId_id"/>
                                            <html:hidden property="dto(copyOfButtonPressed)" value="false"
                                                         styleId="copyOfButtonPressed"/>
                                            <span class="input-group-btn">
                                                <tags:bootstrapSelectPopup
                                                        styleId="invoiceSearchList_id"
                                                        url="/finance/Invoice/Search.do?chekIfButtonPressed=true&parameter(type)=${invoiceType}"
                                                        name="invoiceSearchList"
                                                        titleKey="Common.search"
                                                        modalTitleKey="Invoice.singleSearch"
                                                        submitOnSelect="true"
                                                        tabindex="4"
                                                        isLargeModal="true"/>
                                                <tags:clearBootstrapSelectPopup keyFieldId="fieldInvoiceId_id"
                                                                                styleClass="${app2:getFormButtonClasses()}"
                                                                                nameFieldId="fieldInvoiceNumber_id"
                                                                                titleKey="Common.clear"
                                                                                tabindex="5"
                                                                                glyphiconClass="glyphicon-erase"/>
                                            </span>
                                        </div>
                                        <span class="glyphicon form-control-feedback iconValidation"></span>
                                    </div>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="${app2:getFormGroupClassesTwoColumns()}">
                                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="fieldInvoiceNumber_id">
                                        <fmt:message key="Invoice.copyOf"/>
                                    </label>

                                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                        <div class="input-group">
                                            <app:text property="dto(number)"
                                                      styleClass="${app2:getFormInputClasses()} middleText"
                                                      maxlength="40" readonly="true"
                                                      styleId="fieldInvoiceNumber_id"
                                                      tabindex="3"/>
                                            <html:hidden property="dto(copyOfInvoiceId)" styleId="fieldInvoiceId_id"/>
                                            <html:hidden property="dto(copyOfButtonPressed)" value="false"
                                                         styleId="copyOfButtonPressed"/>
                                            <span class="input-group-btn">
                                                <tags:bootstrapSelectPopup styleId="invoiceSearchList_id"
                                                                           url="/finance/Invoice/Search.do?chekIfButtonPressed=true&parameter(type)=${invoiceType}"
                                                                           name="invoiceSearchList"
                                                                           titleKey="Common.search"
                                                                           modalTitleKey="Invoice.singleSearch"
                                                                           submitOnSelect="true"
                                                                           tabindex="4"
                                                                           isLargeModal="true"/>
                                                <tags:clearBootstrapSelectPopup keyFieldId="fieldInvoiceId_id"
                                                                                nameFieldId="fieldInvoiceNumber_id"
                                                                                titleKey="Common.clear"
                                                                                tabindex="5"
                                                                                styleClass="${app2:getFormButtonClasses()}"
                                                                                glyphiconClass="glyphicon-erase"/>
                                            </span>
                                        </div>
                                        <span class="glyphicon form-control-feedback iconValidation"></span>
                                    </div>
                                </div>
                            </c:otherwise>
                        </c:choose>

                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}" for="currencyId_id">
                                <fmt:message key="Invoice.currency"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                <fanta:select property="dto(currencyId)"
                                              styleId="currencyId_id"
                                              listName="basicCurrencyList"
                                              labelProperty="name"
                                              valueProperty="id"
                                              firstEmpty="true"
                                              styleClass="${app2:getFormSelectClasses()} middleSelect"
                                              module="/catalogs"
                                              tabIndex="6" readOnly="false">
                                    <fanta:parameter field="companyId"
                                                     value="${sessionScope.user.valueMap['companyId']}"/>
                                </fanta:select>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}" for="sequenceRuleId_id">
                                <fmt:message key="Invoice.sequenceRule"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                <fanta:select property="dto(sequenceRuleId)"
                                              styleId="sequenceRuleId_id"
                                              listName="sequenceRuleList"
                                              labelProperty="label"
                                              valueProperty="numberId"
                                              styleClass="${app2:getFormSelectClasses()} middleSelect"
                                              firstEmpty="true"
                                              module="/catalogs"
                                              tabIndex="7">
                                    <fanta:parameter field="companyId"
                                                     value="${sessionScope.user.valueMap['companyId']}"/>
                                    <fanta:parameter field="type" value="${voucherType}"/>
                                </fanta:select>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>

                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}" for="startDate">
                                <fmt:message key="Invoice.invoiceDate"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                <div class="input-group date">
                                    <app:dateText property="dto(invoiceDate)"
                                                  mode="bootstrap"
                                                  styleId="startDate"
                                                  calendarPicker="true"
                                                  datePatternKey="${datePattern}"
                                                  styleClass="${app2:getFormInputClasses()} text"
                                                  maxlength="10"
                                                  tabindex="8"
                                                  currentDate="true"/>
                                </div>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}" for="fieldAddressName_id">
                                <fmt:message key="Invoice.contact"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns('delete' == op || 'true' == invoiceForm.dtoMap['hasRelation'])}">
                                <div class="input-group">
                                    <app:text property="dto(addressName)"
                                              styleClass="${app2:getFormInputClasses()} middleText" maxlength="40"
                                              readonly="true"
                                              styleId="fieldAddressName_id"
                                              view="${'delete' == op || 'true' == invoiceForm.dtoMap['hasRelation']}"
                                              tabindex="9"/>
                                    <html:hidden property="dto(addressId)" styleId="fieldAddressId_id"/>
                                    <span class="input-group-btn">
                                        <tags:bootstrapSelectPopup styleId="searchAddress_id"
                                                                   url="/contacts/SearchAddress.do" name="searchAddress"
                                                                   titleKey="Common.search"
                                                                   modalTitleKey="Contact.Title.search"
                                                                   submitOnSelect="true"
                                                                   tabindex="10"
                                                                   isLargeModal="true"
                                                                   hide="${'delete' == op || 'true' == invoiceForm.dtoMap['hasRelation']}"/>
                                        <tags:clearBootstrapSelectPopup keyFieldId="fieldAddressId_id"
                                                                        nameFieldId="fieldAddressName_id"
                                                                        styleClass="${app2:getFormButtonClasses()}"
                                                                        titleKey="Common.clear"
                                                                        submitOnClear="true"
                                                                        tabindex="11"
                                                                        hide="${'delete' == op || 'true' == invoiceForm.dtoMap['hasRelation']}"
                                                                        glyphiconClass="glyphicon-erase"/>
                                    </span>
                                </div>
                                <html:hidden property="dto(isUpdateCustomerInfo)" value="true"
                                             styleId="isUpdateCustomerInfo"/>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>

                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}" for="payConditionId_id">
                                <fmt:message key="Invoice.payCondition"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                <fanta:select property="dto(payConditionId)"
                                              styleId="payConditionId_id"
                                              listName="payConditionList"
                                              labelProperty="name"
                                              valueProperty="id"
                                              styleClass="${app2:getFormSelectClasses()} middleSelect"
                                              firstEmpty="true"
                                              module="/catalogs"
                                              tabIndex="12">
                                    <fanta:parameter field="companyId"
                                                     value="${sessionScope.user.valueMap['companyId']}"/>
                                </fanta:select>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}" for="contactPersonId_id">
                                <fmt:message key="Invoice.contactPerson"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                <fanta:select property="dto(contactPersonId)" styleId="contactPersonId_id"
                                              listName="searchContactPersonList"
                                              module="/contacts" firstEmpty="true"
                                              labelProperty="contactPersonName"
                                              valueProperty="contactPersonId"
                                              styleClass="${app2:getFormSelectClasses()} middleSelect" tabIndex="13"
                                              readOnly="false">
                                    <fanta:parameter field="companyId"
                                                     value="${sessionScope.user.valueMap['companyId']}"/>
                                    <fanta:parameter field="addressId"
                                                     value="${not empty invoiceForm.dtoMap['addressId']?invoiceForm.dtoMap['addressId']:0}"/>
                                </fanta:select>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>

                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}" for="templateId_id">
                                <fmt:message key="Invoice.template"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                <fanta:select property="dto(templateId)"
                                              styleId="templateId_id"
                                              listName="invoiceTemplateList"
                                              labelProperty="title"
                                              valueProperty="templateId"
                                              styleClass="${app2:getFormSelectClasses()} middleSelect"
                                              module="/catalogs"
                                              firstEmpty="true"
                                              tabIndex="14"
                                              readOnly="false">
                                    <fanta:parameter field="companyId"
                                                     value="${sessionScope.user.valueMap['companyId']}"/>
                                </fanta:select>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}" for="sentAddressId_id">
                                <fmt:message key="Invoice.sentAddress"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                                <fanta:select property="dto(sentAddressId)" styleId="sentAddressId_id"
                                              listName="invoiceAddressRelationList"
                                              labelProperty="relatedAddressName" valueProperty="relatedAddressId"
                                              styleClass="${app2:getFormSelectClasses()} middleSelect"
                                              readOnly="${op == 'delete'}"
                                              onChange="reSubmit()"
                                              module="/contacts" tabIndex="15" firstEmpty="true">
                                    <fanta:parameter field="companyId"
                                                     value="${sessionScope.user.valueMap['companyId']}"/>
                                    <fanta:parameter field="addressId"
                                                     value="${not empty invoiceForm.dtoMap['addressId'] ? invoiceForm.dtoMap['addressId'] : 0}"/>
                                </fanta:select>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>

                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}" for="netGross_id">
                                <fmt:message key="Invoice.netGross"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns('true' == invoiceForm.dtoMap['hasRelation'])}">
                                <c:set var="netGrossOptions" value="${app2:getNetGrossOptions(pageContext.request)}"/>
                                <html:select property="dto(netGross)"
                                             styleId="netGross_id"
                                             styleClass="${app2:getFormSelectClasses()} middleSelect"
                                             readonly="${'true' == invoiceForm.dtoMap['hasRelation']}"
                                             tabindex="15">
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
                            <label class="${app2:getFormLabelClassesTwoColumns()}" for="sentContactPersonId_id">
                                <fmt:message key="Invoice.sentContactPerson"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                                <fanta:select property="dto(sentContactPersonId)" listName="searchContactPersonList"
                                              styleId="sentContactPersonId_id"
                                              module="/contacts" firstEmpty="true"
                                              labelProperty="contactPersonName"
                                              valueProperty="contactPersonId"
                                              styleClass="${app2:getFormSelectClasses()} middleSelect"
                                              tabIndex="16"
                                              readOnly="${'delete' == op}">
                                    <fanta:parameter field="companyId"
                                                     value="${sessionScope.user.valueMap['companyId']}"/>
                                    <fanta:parameter field="addressId"
                                                     value="${not empty invoiceForm.dtoMap['sentAddressId'] ? invoiceForm.dtoMap['sentAddressId'] : 0}"/>
                                </fanta:select>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}" for="serviceDate_id">
                                <fmt:message key="Invoice.serviceDate"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                <div class="input-group date">
                                    <app:dateText property="dto(serviceDate)"
                                                  mode="bootstrap"
                                                  styleId="serviceDate_id"
                                                  calendarPicker="true"
                                                  datePatternKey="${datePattern}"
                                                  styleClass="${app2:getFormInputClasses()} text"
                                                  maxlength="10"
                                                  tabindex="16"
                                                  currentDate="true"/>
                                </div>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="${app2:getFormGroupClassesTwoColumns()}">
                            <label class="${app2:getFormLabelClassesTwoColumns()}" for="additionalAddressId_id">
                                <fmt:message key="Invoice.additionalAddress"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns('delete' == op)}">
                                <fanta:select property="dto(additionalAddressId)" listName="additionalAddressSelectList"
                                              labelProperty="name" valueProperty="additionalAddressId"
                                              module="/contacts"
                                              styleId="additionalAddressId_id"
                                              preProcessor="com.piramide.elwis.web.contactmanager.el.AdditionalAddressSelectPreProcessor"
                                              firstEmpty="true" styleClass="${app2:getFormSelectClasses()} middleSelect"
                                              readOnly="${'delete' == op}"
                                              tabIndex="17">
                                    <fanta:parameter field="companyId"
                                                     value="${sessionScope.user.valueMap['companyId']}"/>
                                    <fanta:parameter field="addressId"
                                                     value="${not empty invoiceForm.dtoMap['addressId'] ? invoiceForm.dtoMap['addressId'] : 0}"/>
                                </fanta:select>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </div>
                </c:if>

                <c:if test="${'create' != op}">
                    <c:set var="existsReminders"
                           value="${app2:existsInvoiceReminders(invoiceForm.dtoMap['invoiceId'], pageContext.request)}"
                           scope="request"/>

                    <c:if test="${creditNoteType == invoiceForm.dtoMap['type']}">
                        <c:import url="/WEB-INF/jsp/finance/CreditNoteFragment.jsp"/>
                    </c:if>

                    <c:if test="${invoiceType == invoiceForm.dtoMap['type']}">
                        <c:import url="/WEB-INF/jsp/finance/InvoiceFragment.jsp"/>
                    </c:if>

                </c:if>
                <div class="col-xs-12 col-sm-12">
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="control-label col-xs-12 col-sm-12 label-left row" for="notes_id">
                            <fmt:message key="Invoice.notes"/>
                        </label>

                        <div class="col-xs-12 col-sm-12 row">
                            <html:textarea property="dto(notes)"
                                           styleId="notes_id"
                                           styleClass="${app2:getFormInputClasses()} mediumDetailHigh"
                                           style="height:120px;"
                                           tabindex="18"
                                           readonly="${'delete' == op}"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <c:if test="${'create' != op}">
                    <div class="row col-xs-12">
                        <c:set var="invoiceId" value="${param.invoiceId}" scope="request"/>
                        <c:import url="/WEB-INF/jsp/finance/InvoiceVatTable.jsp"/>
                    </div>
                </c:if>
            </fieldset>
        </div>

        <div class="row">
            <div class="col-xs-12">
                <app2:securitySubmit operation="${op}"
                                     functionality="INVOICE"
                                     styleClass="${app2:getFormButtonInlineClasses()}"
                                     property="save"
                                     tabindex="19">
                    ${button}
                </app2:securitySubmit>

                <c:if test="${'update' == op}">
                    <app2:securitySubmit operation="${op}"
                                         functionality="INVOICE"
                                         styleClass="${app2:getFormButtonInlineClasses()}"
                                         property="generate"
                                         tabindex="19">
                        <fmt:message key="Invoice.generate"/>
                    </app2:securitySubmit>
                    <c:if test="${not empty documentId}">
                        <html:button property=""
                                     styleClass="${app2:getFormButtonInlineClasses()}"
                                     onclick="location.href='${urlDownload}'"
                                     tabindex="20">
                            <fmt:message key="Invoice.open"/>
                        </html:button>
                    </c:if>

                    <c:if test="${isSendViaEmail and not empty invoiceForm.dtoMap['addressId']}">
                        <app2:checkAccessRight functionality="MAIL" permission="EXECUTE">
                            <html:button property=""
                                         styleClass="${app2:getFormButtonInlineClasses()}"
                                         onclick="sendViaEmail('belowToEmail_id')"
                                         tabindex="21">
                                <fmt:message key="Invoice.sendViaEmail"/>
                            </html:button>
                            <div class="form-group col-xs-12 col-sm-3">
                                <app:telecomSelect property="toEmail"
                                                   styleId="belowToEmail_id"
                                                   tabindex="22"
                                                   telecomIdColumn="telecomid"
                                                   numberColumn="telecomnumber"
                                                   telecomType="${TELECOMTYPE_EMAIL}"
                                                   addressId="${invoiceForm.dtoMap['addressId']}"
                                                   contactPersonId="${invoiceForm.dtoMap['contactPersonId']}"
                                                   showOwner="true"
                                                   styleClass="${app2:getFormSelectClasses()}"
                                                   optionStyleClass="list" showDescription="false"
                                                   selectPredetermined="true"/>
                            </div>
                        </app2:checkAccessRight>
                    </c:if>
                </c:if>

                <html:cancel
                        styleClass="${app2:getFormButtonCancelInlineClasses()} marginLeft"
                        tabindex="23">
                    <fmt:message key="Common.cancel"/>
                </html:cancel>
            </div>
        </div>

    </html:form>
</div>

<tags:jQueryValidation formName="invoiceForm"/>