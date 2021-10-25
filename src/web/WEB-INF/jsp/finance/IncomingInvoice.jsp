<%@ page import="com.piramide.elwis.utils.FinanceConstants" %>
<%@ include file="/Includes.jsp" %>

<tags:initBootstrapDatepicker/>
<tags:initBootstrapSelectPopup/>
<tags:initBootstrapFile/>

<fmt:message var="datePattern" key="datePattern"/>
<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<c:set var="creditNoteType" value="<%=FinanceConstants.InvoiceType.CreditNote.getConstantAsString()%>"/>
<c:set var="invoiceType" value="<%=FinanceConstants.InvoiceType.Invoice.getConstantAsString()%>"/>

<c:set var="invoiceTypeList" value="${app2:getInvoiceTypes(pageContext.request)}"/>

<div class="${app2:getFormWrapperTwoColumns()}">
    <html:form action="${action}" focus="dto(type)" enctype="multipart/form-data" styleClass="form-horizontal">

        <html:hidden property="dto(op)" value="${op}" styleId="dto_op"/>

        <c:if test="${'update'== op || 'delete'== op}">
            <html:hidden property="dto(incomingInvoiceId)"/>
            <html:hidden property="dto(notesId)"/>
            <html:hidden property="dto(version)"/>
            <html:hidden property="dto(companyId)"/>
            <html:hidden property="dto(documentId)"/>

            <c:set var="documentId" value="${incomingInvoiceForm.dtoMap['documentId']}" scope="request"/>
        </c:if>

        <c:if test="${'delete' == op}">
            <html:hidden property="dto(withReferences)" value="true"/>
        </c:if>
        <c:if test="${op=='create'}">
            <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
        </c:if>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}"
                                 functionality="INCOMINGINVOICE"
                                 styleClass="${app2:getFormButtonClasses()}"
                                 property="save"
                                 tabindex="16">
                ${button}
            </app2:securitySubmit>

            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}"
                                     functionality="INCOMINGINVOICE"
                                     styleClass="${app2:getFormButtonClasses()}"
                                     property="SaveAndNew"
                                     tabindex="16">
                    <fmt:message key="Common.saveAndNew"/>
                </app2:securitySubmit>
            </c:if>

            <c:if test="${'update'== op}">
                <c:if test="${not empty documentId}">
                    <app:url value="finance/Download/IncomingInvoice/Document.do?dto(freeTextId)=${documentId}&dto(incomingInvoiceId)=${incomingInvoiceForm.dtoMap['incomingInvoiceId']}"
                            var="urlDownload" addModuleName="false" contextRelative="true"/>

                    <html:button property=""
                                 styleClass="${app2:getFormButtonClasses()}"
                                 onclick="location.href='${urlDownload}'"
                                 tabindex="16">
                        <fmt:message key="Finance.incomingInvoice.open"/>
                    </html:button>
                </c:if>
            </c:if>

            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="17">
                <fmt:message key="Common.cancel"/>
            </html:cancel>

                <%--top links--%>
            <c:if test="${op=='update' && fromContacts}">
                <app:link
                        action="/finance/IncomingInvoice/Forward/Update.do?dto(incomingInvoiceId)=${incomingInvoiceForm.dtoMap.incomingInvoiceId}&dto(op)=read&incomingInvoiceId=${incomingInvoiceForm.dtoMap.incomingInvoiceId}&tabKey=Finance.IncomingInvoice.detail"
                        contextRelative="true" styleClass="btn btn-link">
                    <fmt:message key="Invoice.editDetails"/>
                </app:link>
            </c:if>
        </div>

        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="type_id">
                            <fmt:message key="Finance.incomingInvoice.type"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op=='delete')}">
                            <html:select property="dto(type)"
                                         styleId="type_id"
                                         styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                         readonly="${op=='delete'}"
                                         tabindex="1">
                                <html:option value=""/>
                                <html:options collection="invoiceTypeList"
                                              property="value"
                                              labelProperty="label"/>
                            </html:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="currencyId_id">
                            <fmt:message key="Finance.incomingInvoice.currency"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op=='delete')}">
                            <fanta:select property="dto(currencyId)"
                                          styleId="currencyId_id"
                                          listName="basicCurrencyList"
                                          labelProperty="name"
                                          valueProperty="id"
                                          firstEmpty="true"
                                          styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                          module="/catalogs"
                                          tabIndex="2" readOnly="${op=='delete'}">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="invoiceNumber_id">
                            <fmt:message key="Finance.incomingInvoice.invoiceNumber"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op=='delete')}">
                            <app:text property="dto(invoiceNumber)" styleId="invoiceNumber_id"
                                      maxlength="30" view="${op=='delete'}" tabindex="3"
                                      styleClass="${app2:getFormInputClasses()} mediumText"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="invoiceDate_id">
                            <fmt:message key="Finance.incomingInvoice.invoiceDate"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op=='delete')}">
                            <div class="input-group date">
                                <app:dateText property="dto(invoiceDate)"
                                              mode="bootstrap"
                                              maxlength="10" tabindex="4"
                                              styleId="invoiceDate_id"
                                              calendarPicker="${op!='delete'}" datePatternKey="${datePattern}"
                                              styleClass="${app2:getFormInputClasses()} dateText"
                                              convert="true"
                                              currentDate="${op=='create'}" view="${op=='delete'}"/>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="fieldAddressName_id">
                            <fmt:message key="Finance.incomingInvoice.supplierName"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete' || fromContacts)}">
                            <c:choose>
                                <c:when test="${op=='create' && fromContacts}">
                                    <html:hidden property="dto(supplierId)" styleId="fieldAddressId_id"
                                                 value="${param.contactId}"/>
                                </c:when>
                                <c:otherwise>
                                    <div class="input-group">
                                        <app:text property="dto(supplierName)" styleId="fieldAddressName_id"
                                                  styleClass="${app2:getFormInputClasses()} mediumText"
                                                  tabindex="5" readonly="true"
                                                  view="${op == 'delete' || fromContacts}"/>
                                        <html:hidden property="dto(supplierId)" styleId="fieldAddressId_id"/>
                                        <span class="input-group-btn">
                                            <tags:bootstrapSelectPopup url="/contacts/SearchAddress.do?allowCreation=2"
                                                                       styleId="SearchSupplier_id" name="SearchSupplier"
                                                                       titleKey="Common.search"
                                                                       modalTitleKey="Contact.Title.search"
                                                                       hide="${op == 'delete' || fromContacts}"
                                                                       submitOnSelect="false" tabindex="6"
                                                                       isLargeModal="true"/>

                                            <tags:clearBootstrapSelectPopup keyFieldId="fieldAddressId_id"
                                                                            nameFieldId="fieldAddressName_id"
                                                                            styleClass="${app2:getFormButtonClasses()}"
                                                                            titleKey="Common.clear"
                                                                            hide="${op == 'delete' || fromContacts}"
                                                                            submitOnClear="false" tabindex="7"
                                                                            glyphiconClass="glyphicon-erase"/>
                                        </span>
                                    </div>
                                    <span class="glyphicon form-control-feedback iconValidation"></span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="receiptDate_id">
                            <fmt:message key="Finance.incomingInvoice.receiptDate"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op=='delete')}">
                            <div class="input-group date">
                                <app:dateText property="dto(receiptDate)" maxlength="10" tabindex="8"
                                              styleId="receiptDate_id"
                                              mode="bootstrap"
                                              calendarPicker="${op!='delete'}" datePatternKey="${datePattern}"
                                              styleClass="${app2:getFormInputClasses()} dateText"
                                              convert="true"
                                              currentDate="${op=='create'}" view="${op=='delete'}"/>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="amountNet_id">
                            <fmt:message key="Finance.incomingInvoice.amountNet"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op=='delete')}">
                            <app:numberText property="dto(amountNet)"
                                            styleId="amountNet_id"
                                            styleClass="${app2:getFormInputClasses()} numberText" tabindex="9"
                                            maxlength="13"
                                            view="${'delete' == op}" numberType="decimal" maxInt="10" maxFloat="2"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="toBePaidUntil_id">
                            <fmt:message key="Finance.incomingInvoice.toBePaidUntil"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op=='delete')}">
                            <div class="input-group date">
                                <app:dateText property="dto(toBePaidUntil)" maxlength="10" tabindex="10"
                                              styleId="toBePaidUntil_id"
                                              mode="bootstrap"
                                              calendarPicker="${op!='delete'}" datePatternKey="${datePattern}"
                                              styleClass="${app2:getFormInputClasses()} dateText"
                                              convert="true"
                                              view="${op=='delete'}"/>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="amountGross_id">
                            <fmt:message key="Finance.incomingInvoice.amountGross"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op=='delete')}">
                            <app:numberText property="dto(amountGross)" styleId="amountGross_id"
                                            styleClass="${app2:getFormInputClasses()} numberText" tabindex="11"
                                            maxlength="13"
                                            view="${'delete' == op}" numberType="decimal" maxInt="10" maxFloat="2"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="paidUntil_id">
                            <fmt:message key="Finance.incomingInvoice.paidUntil"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(true)}">
                            <app:dateText property="dto(paidUntil)" maxlength="10" tabindex="12"
                                          styleId="paidUntil_id"
                                          calendarPicker="false" datePatternKey="${datePattern}"
                                          styleClass="dateText"
                                          convert="true" view="true"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="openAmount_id">
                            <fmt:message key="Finance.incomingInvoice.openAmount"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(true)}">
                            <app:numberText property="dto(openAmount)" styleId="openAmount_id" styleClass="numberText"
                                            tabindex="12"
                                            maxlength="13"
                                            view="true" numberType="decimal" maxInt="10" maxFloat="2"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="invoiceFile_id">
                            <fmt:message key="Finance.incomingInvoice.document"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <c:if test="${'delete' != op}">

                                <c:if test="${not empty documentId}">
                                    <div class="row col-xs-11">
                                </c:if>

                                <tags:bootstrapFile property="invoiceFile"
                                                    glyphiconClass="glyphicon-folder-open"
                                                    tabIndex="12"
                                                    styleId="invoiceFile_id"/>

                                <c:if test="${not empty documentId}">
                                    </div>
                                </c:if>

                                <c:if test="${not empty documentId}">
                                    <span class="pull-right">
                                        <fmt:message key="Finance.incomingInvoice.download" var="downloadMsg"/>

                                        <app:link action="finance/Download/IncomingInvoice/Document.do?dto(freeTextId)=${documentId}&dto(incomingInvoiceId)=${incomingInvoiceForm.dtoMap['incomingInvoiceId']}"
                                                title="${downloadMsg}" contextRelative="true" addModuleName="false" styleClass="${app2:getFormButtonClasses()}" tabindex="12">
                                            <span class="glyphicon glyphicon-download-alt"></span>
                                        </app:link>
                                    </span>
                                </c:if>

                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </c:if>
                        </div>
                    </div>
                </div>
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
                                           tabindex="13"
                                           readonly="${'delete' == op}"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
            </fieldset>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}"
                                 functionality="INCOMINGINVOICE"
                                 styleClass="${app2:getFormButtonClasses()}"
                                 property="save"
                                 tabindex="14">
                ${button}
            </app2:securitySubmit>

            <c:if test="${op == 'create'}">
                <app2:securitySubmit operation="${op}"
                                     functionality="INCOMINGINVOICE"
                                     styleClass="${app2:getFormButtonClasses()}"
                                     property="SaveAndNew"
                                     tabindex="14">
                    <fmt:message key="Common.saveAndNew"/>
                </app2:securitySubmit>
            </c:if>

            <c:if test="${'update'== op}">
                <c:if test="${not empty documentId}">
                    <html:button property=""
                                 styleClass="${app2:getFormButtonClasses()}"
                                 onclick="location.href='${urlDownload}'"
                                 tabindex="14">
                        <fmt:message key="Finance.incomingInvoice.open"/>
                    </html:button>
                </c:if>
            </c:if>

            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="15">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </html:form>
</div>
<tags:jQueryValidation formName="incomingInvoiceForm"/>
