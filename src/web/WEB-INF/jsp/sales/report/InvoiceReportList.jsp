<%@ page import="com.piramide.elwis.utils.FinanceConstants" %>
<%@ include file="/Includes.jsp" %>

<tags:initBootstrapSelectPopup/>
<tags:initBootstrapDatepicker/>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<c:set var="invoiceTypeList" value="${app2:getInvoiceTypes(pageContext.request)}"/>

<div class="${app2:getFormWrapperTwoColumns()}">

    <html:form action="/Report/InvoiceList/Execute.do"
               focus="parameter(contact)"
               styleId="invoiceFormId"
               styleClass="form-horizontal">

        <div class="${app2:getFormPanelClasses()}">

            <legend class="title">
                <fmt:message key="SalesProcess.Report.InvoiceList"/>
            </legend>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="fieldAddressName_id">
                        <fmt:message key="Invoice.contact"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">

                        <div class="input-group">
                            <app:text property="parameter(contact)"
                                      styleId="fieldAddressName_id"
                                      styleClass="${app2:getFormInputClasses()} middleText"
                                      maxlength="40"
                                      tabindex="1" readonly="true"/>
                            <html:hidden property="parameter(addressId)" styleId="fieldAddressId_id"/>
                <span class="input-group-btn">
                    <tags:bootstrapSelectPopup url="/contacts/SearchAddress.do"
                                               name="searchAddress"
                                               isLargeModal="true"
                                               styleId="contactSelectPopup_id"
                                               modalTitleKey="Contact.Title.search"
                                               styleClass="${app2:getFormButtonClasses()}"
                                               titleKey="Common.search"
                                               tabindex="2"/>
                    <tags:clearBootstrapSelectPopup keyFieldId="fieldAddressId_id"
                                                    nameFieldId="fieldAddressName_id"
                                                    styleClass="${app2:getFormButtonClasses()}"
                                                    titleKey="Common.clear"
                                                    tabindex="3"/>
                </span>
                        </div>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="payConditionId_id">
                        <fmt:message key="Invoice.payCondition"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <fanta:select property="parameter(payConditionId)"
                                      listName="payConditionList"
                                      labelProperty="name"
                                      styleId="payConditionId_id"
                                      valueProperty="id"
                                      styleClass="${app2:getFormSelectClasses()} middleSelect"
                                      firstEmpty="true"
                                      module="/catalogs"
                                      tabIndex="4">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="type_id">
                        <fmt:message key="Invoice.type"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <html:select property="parameter(type)"
                                     styleId="type_id"
                                     styleClass="${app2:getFormSelectClasses()} middleSelect"
                                     tabindex="5">
                            <html:option value=""/>
                            <html:options collection="invoiceTypeList" property="value" labelProperty="label"/>
                        </html:select>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="currencyId_id">
                        <fmt:message key="Invoice.currency"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <fanta:select property="parameter(currencyId)"
                                      listName="basicCurrencyList"
                                      labelProperty="name"
                                      styleId="currencyId_id"
                                      valueProperty="id"
                                      firstEmpty="true"
                                      styleClass="${app2:getFormSelectClasses()} middleSelect"
                                      module="/catalogs"
                                      tabIndex="6">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="amountNet1_id">
                        <fmt:message key="Invoice.totalAmountNet"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 wrapperButton">
                                <label class="control-label">
                                    <fmt:message key="Common.from"/>
                                </label>

                                <app:numberText property="parameter(amountNet1)"
                                                styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                tabindex="7"
                                                styleId="amountNet1_id"
                                                maxlength="12"
                                                numberType="decimal"
                                                maxInt="10"
                                                maxFloat="2"/>
                            </div>

                            <div class="col-xs-12 col-sm-6">
                                <label class="control-label">
                                    <fmt:message key="Common.to"/>
                                </label>

                                <app:numberText property="parameter(amountNet2)"
                                                styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                tabindex="8"
                                                maxlength="12"
                                                numberType="decimal"
                                                maxInt="10"
                                                maxFloat="2"/>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="ruleFormat_id">
                        <fmt:message key="Invoice.report.voucherNumberRule"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <c:set var="voucherType"><%=FinanceConstants.SequenceRuleType.VOUCHER.getConstantAsString()%>
                        </c:set>
                        <fanta:select property="parameter(ruleFormat)"
                                      listName="sequenceRuleList"
                                      labelProperty="label"
                                      styleId="ruleFormat_id"
                                      valueProperty="format"
                                      styleClass="${app2:getFormSelectClasses()} middleSelect"
                                      firstEmpty="true"
                                      module="/catalogs"
                                      tabIndex="9">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            <fanta:parameter field="type" value="${voucherType}"/>
                        </fanta:select>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="amountGross1_id">
                        <fmt:message key="Invoice.totalAmountGross"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 wrapperButton">
                                <label class="control-label">
                                    <fmt:message key="Common.from"/>
                                </label>

                                <app:numberText property="parameter(amountGross1)"
                                                styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                tabindex="10"
                                                maxlength="12"
                                                styleId="amountGross1_id"
                                                numberType="decimal"
                                                maxInt="10"
                                                maxFloat="2"/>
                            </div>

                            <div class="col-xs-12 col-sm-6">
                                <label class="control-label">
                                    <fmt:message key="Common.to"/>
                                </label>

                                <app:numberText property="parameter(amountGross2)"
                                                styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                tabindex="11"
                                                maxlength="12"
                                                numberType="decimal"
                                                maxInt="10"
                                                maxFloat="2"/>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="startInvoiceDate">
                        <fmt:message key="Invoice.invoiceDate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 wrapperButton">
                                <fmt:message key="datePattern" var="datePattern"/>
                                <fmt:message key="Common.from" var="from"/>

                                <div class="input-group date">
                                    <app:dateText property="parameter(startInvoiceDate)"
                                                  maxlength="10"
                                                  tabindex="12"
                                                  styleId="startInvoiceDate"
                                                  calendarPicker="true"
                                                  placeHolder="${from}"
                                                  mode="bootstrap"
                                                  datePatternKey="${datePattern}"
                                                  styleClass="${app2:getFormInputClasses()} dateText"
                                                  convert="true"/>
                                </div>
                            </div>
                            <div class="col-xs-12 col-sm-6">
                                <fmt:message var="to" key="Common.to"/>

                                <div class="input-group date">
                                    <app:dateText property="parameter(endInvoiceDate)"
                                                  maxlength="10"
                                                  tabindex="13"
                                                  placeHolder="${to}"
                                                  styleId="endInvoiceDate"
                                                  calendarPicker="true"
                                                  mode="bootstrap"
                                                  datePatternKey="${datePattern}"
                                                  styleClass="${app2:getFormInputClasses()} dateText"
                                                  convert="true"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="openAmount1_id">
                        <fmt:message key="Invoice.openAmount"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 wrapperButton">
                                <label class="control-label">
                                    <fmt:message key="Common.from"/>
                                </label>

                                <app:numberText property="parameter(openAmount1)"
                                                styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                tabindex="14"
                                                styleId="openAmount1_id"
                                                maxlength="12"
                                                numberType="decimal"
                                                maxInt="10"
                                                maxFloat="2"/>
                            </div>

                            <div class="col-xs-12 col-sm-6">
                                <label class="control-label">
                                    <fmt:message key="Common.to"/>
                                </label>

                                <app:numberText property="parameter(openAmount2)"
                                                styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                tabindex="15"
                                                maxlength="12"
                                                numberType="decimal"
                                                maxInt="10"
                                                maxFloat="2"/>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="startPayDate">
                        <fmt:message key="Invoice.paymentDate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 wrapperButton">
                                <fmt:message key="datePattern" var="datePattern"/>
                                <fmt:message key="Common.from" var="from"/>

                                <div class="input-group date">
                                    <app:dateText property="parameter(startPaymentDate)"
                                                  maxlength="10"
                                                  tabindex="16"
                                                  mode="bootstrap"
                                                  placeHolder="${from}"
                                                  styleId="startPayDate"
                                                  calendarPicker="true"
                                                  datePatternKey="${datePattern}"
                                                  styleClass="${app2:getFormInputClasses()} dateText"
                                                  convert="true"/>
                                </div>
                            </div>
                            <div class="col-xs-12 col-sm-6">
                                <fmt:message var="to" key="Common.to"/>

                                <div class="input-group date">
                                    <app:dateText property="parameter(endPaymentDate)"
                                                  maxlength="10"
                                                  tabindex="17"
                                                  styleId="endPayDate"
                                                  placeHolder="${to}"
                                                  calendarPicker="true"
                                                  mode="bootstrap"
                                                  datePatternKey="${datePattern}"
                                                  styleClass="${app2:getFormInputClasses()} dateText"
                                                  convert="true"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>


            <c:set var="reportFormats" value="${invoiceReportForm.reportFormats}" scope="request"/>
            <c:set var="pageSizes" value="${invoiceReportForm.pageSizes}" scope="request"/>

            <titus:reportGroupSortTag width="100%" mode="bootstrap"
                                      tableStyleClass="${app2:getTableClasesIntoForm()}">
                <titus:reportGroupSortColumnTag name="number" labelKey="Invoice.number"/>
                <titus:reportGroupSortColumnTag name="addressName" labelKey="Invoice.contact"/>
                <titus:reportGroupSortColumnTag name="invoiceDate" labelKey="Invoice.invoiceDate" isDate="true"/>
                <titus:reportGroupSortColumnTag name="totalAmountGross" labelKey="Invoice.report.totalGross"/>
                <titus:reportGroupSortColumnTag name="currencyLabel" labelKey="Invoice.currency"/>
                <titus:reportGroupSortColumnTag name="totalAmountNet" labelKey="Invoice.report.totalNet"/>
            </titus:reportGroupSortTag>

            <tags:bootstrapReportOptionsTag/>

            <titus:reportInitializeConstantsTag/>
            <titus:reportTag id="invoiceReportList" title="SalesProcess.Report.InvoiceList"
                             pageOrientation="${PAGE_ORIENTATION_LANDSCAPE}"
                             locale="${sessionScope.user.valueMap['locale']}"
                             timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>
            <titus:reportFieldTag name="number" resourceKey="Invoice.number" type="${FIELD_TYPE_STRING}" width="18"
                                  fieldPosition="1"/>
            <titus:reportFieldTag name="addressName" resourceKey="Invoice.contact" type="${FIELD_TYPE_STRING}"
                                  width="20"
                                  fieldPosition="2"/>
            <titus:reportFieldTag name="invoiceDate" resourceKey="Invoice.invoiceDate" type="${FIELD_TYPE_DATEINT}"
                                  width="10" fieldPosition="3" patternKey="datePattern"/>
            <titus:reportFieldTag name="totalAmountGross" resourceKey="Invoice.report.totalGross"
                                  type="${FIELD_TYPE_DECIMALNUMBER}" width="10" fieldPosition="4"
                                  patternKey="numberFormat.2DecimalPlaces" align="${FIELD_ALIGN_RIGHT}"/>
            <titus:reportFieldTag name="currencyLabel" resourceKey="Invoice.currency" type="${FIELD_TYPE_STRING}"
                                  width="12"
                                  fieldPosition="5"/>
            <titus:reportFieldTag name="totalAmountNet" resourceKey="Invoice.report.totalNet"
                                  type="${FIELD_TYPE_DECIMALNUMBER}" width="10" fieldPosition="6"
                                  patternKey="numberFormat.2DecimalPlaces" align="${FIELD_ALIGN_RIGHT}"/>
            <titus:reportFieldTag name="openAmount" resourceKey="Invoice.report.openAmount"
                                  type="${FIELD_TYPE_DECIMALNUMBER}" width="10" fieldPosition="7"
                                  patternKey="numberFormat.2DecimalPlaces" align="${FIELD_ALIGN_RIGHT}"/>
            <titus:reportFieldTag name="type" resourceKey="Invoice.type" type="${FIELD_TYPE_STRING}" width="10"
                                  fieldPosition="8"
                                  conditionMethod="com.piramide.elwis.web.salesmanager.el.Functions.getInvoiceTypeName type [${sessionScope.user.valueMap['locale']}]"/>

        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <html:submit styleClass="${app2:getFormButtonClasses()}" property="parameter(generate)" tabindex="57">
                <fmt:message key="Campaign.Report.generate"/>
            </html:submit>
            <html:button property="reset1" tabindex="58" styleClass="${app2:getFormButtonClasses()}"
                         onclick="formReset('invoiceFormId')">
                <fmt:message key="Common.clear"/>
            </html:button>
        </div>

    </html:form>

</div>