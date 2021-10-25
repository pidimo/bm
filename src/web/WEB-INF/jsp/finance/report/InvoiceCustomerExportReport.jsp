<%@ include file="/Includes.jsp" %>

<tags:initBootstrapDatepicker/>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<fmt:message var="datePattern" key="datePattern"/>
<fmt:message var="decimalPattern" key="numberFormatFixed.2DecimalPlaces"/>

<div class="${app2:getFormClassesLarge()}">
    <html:form action="${action}" focus="parameter(startInvoiceDate)" styleId="exportFormId"
               styleClass="form-horizontal">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <c:out value="${title}"/>
                </legend>

                <div class="row">
                    <div class="col-xs-11 col-sm-11 col-md-8">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Invoice.invoiceDate"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="row">
                                <div class="col-xs-12 col-sm-6 wrapperButton">
                                    <fmt:message key="datePattern" var="datePattern"/>
                                    <fmt:message key="Common.from" var="from"/>
                                    <div class="input-group date">
                                        <app:dateText property="parameter(startInvoiceDate)" maxlength="10" tabindex="2"
                                                      styleId="startInvoiceDate"
                                                      calendarPicker="true"
                                                      placeHolder="${from}"
                                                      datePatternKey="${datePattern}"
                                                      styleClass="${app2:getFormInputClasses()} dateText"
                                                      convert="true" mode="bootstrap"/>
                                    </div>
                                </div>
                                <div class="col-xs-12 col-sm-6 wrapperButton">
                                    <fmt:message key="Common.to" var="to"/>
                                    <div class="input-group date">
                                        <app:dateText property="parameter(endInvoiceDate)" maxlength="10" tabindex="3"
                                                      styleId="endInvoiceDate"
                                                      placeHolder="${to}"
                                                      calendarPicker="true" datePatternKey="${datePattern}"
                                                      styleClass="${app2:getFormInputClasses()} dateText"
                                                      convert="true" mode="bootstrap"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-xs-11 col-sm-11 col-md-3">
                        <label class="control-label col-xs-11 col-sm-4 col-md-8">
                            <fmt:message key="Invoice.markAsExported"/>
                        </label>

                        <div class="parentElementInputSearch col-xs-11 col-sm-7 col-md-3">
                            <div class="checkbox checkbox-default">
                                <html:checkbox property="parameter(exported)" styleId="exported_id" tabindex="5"
                                               value="true"/>
                                <label for="exported_id"></label>
                            </div>
                        </div>
                    </div>
                </div>
            </fieldset>
        </div>
        <div class="${app2:getFormGroupClasses()}">
            <div class="col-xs-12">
                <html:submit styleClass="${app2:getFormButtonClasses()}" property="parameter(generate)" tabindex="10">
                    <fmt:message key="Report.generate"/>
                </html:submit>
                <html:button property="reset1" tabindex="11" styleClass="${app2:getFormButtonClasses()}"
                             onclick="formReset('exportFormId')">
                    <fmt:message key="Common.clear"/>
                </html:button>
            </div>
        </div>

        <titus:reportInitializeConstantsTag/>
        <titus:reportTag id="invoiceCustomerExportList" title="Finance.report.invoiceCustomerExport"
                         pageOrientation="${PAGE_ORIENTATION_LANDSCAPE}"
                         locale="${sessionScope.user.valueMap['locale']}"
                         timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>

        <titus:reportFieldTag name="invoiceDate" resourceKey="InvoiceExport.voucherDate"
                              type="${FIELD_TYPE_DATEINT}" patternKey="datePattern" width="1"
                              fieldPosition="1"/>

        <titus:reportFieldTag name="bookingDate" resourceKey="InvoiceExport.bookingDate"
                              type="${FIELD_TYPE_DATEINT}" patternKey="datePattern" width="1"
                              fieldPosition="2"/>

        <titus:reportFieldTag name="bookingPeriod" resourceKey="InvoiceExport.bookingPeriod"
                              type="${FIELD_TYPE_STRING}" width="1"
                              conditionMethod="com.piramide.elwis.web.financemanager.util.InvoiceCSVExportReportUtil.getMonthOfDate invoiceDate"
                              fieldPosition="3"/>

        <titus:reportFieldTag name="emptyValue1" resourceKey="InvoiceExport.voucherNumberSeries"
                              type="${FIELD_TYPE_STRING}" width="1"
                              conditionMethod="com.piramide.elwis.web.financemanager.util.InvoiceCSVExportReportUtil.getEmptyValue []"
                              fieldPosition="4"/>

        <titus:reportFieldTag name="invoiceNumber" resourceKey="InvoiceExport.voucherNumber"
                              type="${FIELD_TYPE_STRING}" width="1"
                              fieldPosition="5"/>

        <titus:reportFieldTag name="addressName" resourceKey="InvoiceExport.accountingText"
                              type="${FIELD_TYPE_STRING}" width="1"
                              conditionMethod="com.piramide.elwis.web.financemanager.util.InvoiceCSVExportReportUtil.getAddressName addressName debitorName {EXPORTDATATYPE_PARAM}"
                              fieldPosition="6"/>

        <titus:reportFieldTag name="totalPriceGross" resourceKey="InvoiceExport.accountingAmount"
                              type="${FIELD_TYPE_STRING}" width="1"
                              conditionMethod="com.piramide.elwis.web.financemanager.util.InvoiceCSVExportReportUtil.calculatePositionAmount totalPrice totalPriceGross netGross invoiceType vatRate [${sessionScope.user.valueMap['locale']}] [${decimalPattern}]"
                              fieldPosition="7"/>

        <titus:reportFieldTag name="customerNumber" resourceKey="InvoiceExport.debitAccount"
                              type="${FIELD_TYPE_STRING}" width="1"
                              conditionMethod="com.piramide.elwis.web.financemanager.util.InvoiceCSVExportReportUtil.getCustomerNumber customerNumber debitorNumber {EXPORTDATATYPE_PARAM}"
                              fieldPosition="8"/>

        <titus:reportFieldTag name="accountNumber" resourceKey="InvoiceExport.creditAccount"
                              type="${FIELD_TYPE_STRING}" width="1"
                              fieldPosition="9"/>

        <titus:reportFieldTag name="taxKey" resourceKey="InvoiceExport.taxKey"
                              type="${FIELD_TYPE_INTEGER}" width="1"
                              fieldPosition="10"/>

        <titus:reportFieldTag name="emptyValue2" resourceKey="InvoiceExport.costCenter1"
                              type="${FIELD_TYPE_STRING}" width="1"
                              conditionMethod="com.piramide.elwis.web.financemanager.util.InvoiceCSVExportReportUtil.getEmptyValue []"
                              fieldPosition="11"/>

        <titus:reportFieldTag name="emptyValue3" resourceKey="InvoiceExport.costObject"
                              type="${FIELD_TYPE_STRING}" width="1"
                              conditionMethod="com.piramide.elwis.web.financemanager.util.InvoiceCSVExportReportUtil.getEmptyValue []"
                              fieldPosition="12"/>

        <titus:reportFieldTag name="emptyValue4" resourceKey="InvoiceExport.accountingAmountEuro"
                              type="${FIELD_TYPE_STRING}" width="1"
                              conditionMethod="com.piramide.elwis.web.financemanager.util.InvoiceCSVExportReportUtil.getEmptyValue []"
                              fieldPosition="13"/>

        <titus:reportFieldTag name="currencyLabel" resourceKey="InvoiceExport.currency"
                              type="${FIELD_TYPE_STRING}" width="1"
                              fieldPosition="14"/>
    </html:form>
</div>