<%@ include file="/Includes.jsp" %>
<tags:initBootstrapDatepicker/>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<div class="${app2:getFormClassesLarge()}">
<html:form action="${action}" focus="parameter(startInvoiceDate)" styleId="exportFormId" styleClass="form-horizontal">
<div class="${app2:getFormPanelClasses()}">
    <fieldset>
        <legend class="title">
            <c:out value="${title}"/>
        </legend>

        <div class="${app2:getFormGroupClasses()}">
            <label class="${app2:getFormLabelClasses()}">
                <fmt:message key="Invoice.invoiceDate"/>
            </label>

            <div class="${app2:getFormContainClasses(null)}">
                <div class="row">
                    <div class="col-xs-12 col-sm-6 wrapperButton">
                        <fmt:message key="datePattern" var="datePattern"/>
                        <fmt:message key="Common.from" var="from"/>

                        <div class="input-group date">
                            <app:dateText property="parameter(startInvoiceDate)"
                                          maxlength="10"
                                          tabindex="15"
                                          styleId="startInvoiceDate"
                                          calendarPicker="true"
                                          datePatternKey="${datePattern}"
                                          styleClass="${app2:getFormInputClasses()} dateText"
                                          mode="bootstrap"
                                          convert="true"/>
                        </div>
                    </div>
                    <div class="col-xs-12 col-sm-6">
                        <fmt:message key="Common.to" var="to"/>

                        <div class="input-group date">
                            <app:dateText property="parameter(endInvoiceDate)"
                                          maxlength="10"
                                          tabindex="16"
                                          styleId="endInvoiceDate"
                                          calendarPicker="true"
                                          datePatternKey="${datePattern}"
                                          styleClass="${app2:getFormInputClasses()} dateText"
                                          mode="bootstrap"
                                          convert="true"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </fieldset>
</div>

<div class="${app2:getFormGroupClasses()}">
    <div class="col-xs-12">
        <html:submit styleClass="${app2:getFormButtonClasses()}" property="parameter(generate)" tabindex="57">
            <fmt:message key="Report.generate"/>
        </html:submit>
        <html:button property="reset1" tabindex="58" styleClass="${app2:getFormButtonClasses()}"
                     onclick="formReset('exportFormId')">
            <fmt:message key="Common.clear"/>
        </html:button>
    </div>
</div>

<div>
    <titus:reportInitializeConstantsTag/>
    <titus:reportTag id="invoicePartnerDataExportList" title="Finance.report.invoiceCustomerDataExport"
                     pageOrientation="${PAGE_ORIENTATION_LANDSCAPE}"
                     locale="${sessionScope.user.valueMap['locale']}"
                     timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>

    <titus:reportFieldTag name="debitorNumber" resourceKey="InvoiceExport.customerData.accountNumber"
                          type="${FIELD_TYPE_STRING}" width="1"
                          fieldPosition="1"/>

    <titus:reportFieldTag name="debitorName" resourceKey="InvoiceExport.customerData.accountName"
                          type="${FIELD_TYPE_STRING}" width="1"
                          fieldPosition="2"/>

    <titus:reportFieldTag name="debitorNumber2" resourceKey="InvoiceExport.customerData.customerNumber"
                          type="${FIELD_TYPE_STRING}" width="1"
                          fieldPosition="3"/>

    <titus:reportFieldTag name="emptyValue1" resourceKey="InvoiceExport.customerData.salutation"
                          type="${FIELD_TYPE_STRING}" width="1"
                          conditionMethod="com.piramide.elwis.web.financemanager.util.InvoiceCSVExportReportUtil.getEmptyValue []"
                          fieldPosition="4"/>

    <titus:reportFieldTag name="companyName" resourceKey="InvoiceExport.customerData.company"
                          type="${FIELD_TYPE_STRING}" width="1"
                          conditionMethod="com.piramide.elwis.web.financemanager.util.InvoiceCSVExportReportUtil.getColumnValue debitorName"
                          fieldPosition="5"/>

    <titus:reportFieldTag name="emptyValue2" resourceKey="InvoiceExport.customerData.lastName"
                          type="${FIELD_TYPE_STRING}" width="1"
                          conditionMethod="com.piramide.elwis.web.financemanager.util.InvoiceCSVExportReportUtil.getEmptyValue []"
                          fieldPosition="6"/>

    <titus:reportFieldTag name="emptyValue3" resourceKey="InvoiceExport.customerData.firstName"
                          type="${FIELD_TYPE_STRING}" width="1"
                          conditionMethod="com.piramide.elwis.web.financemanager.util.InvoiceCSVExportReportUtil.getEmptyValue []"
                          fieldPosition="7"/>

    <titus:reportFieldTag name="emptyValue4" resourceKey="InvoiceExport.customerData.additon"
                          type="${FIELD_TYPE_STRING}" width="1"
                          conditionMethod="com.piramide.elwis.web.financemanager.util.InvoiceCSVExportReportUtil.getEmptyValue []"
                          fieldPosition="8"/>

    <titus:reportFieldTag name="countryCode" resourceKey="InvoiceExport.customerData.country"
                          type="${FIELD_TYPE_STRING}" width="1"
                          fieldPosition="9"/>

    <titus:reportFieldTag name="street" resourceKey="InvoiceExport.customerData.street"
                          type="${FIELD_TYPE_STRING}" width="1"
                          fieldPosition="10"/>

    <titus:reportFieldTag name="houseNumber" resourceKey="InvoiceExport.customerData.houseNumber"
                          type="${FIELD_TYPE_STRING}" width="1"
                          fieldPosition="11"/>

    <titus:reportFieldTag name="cityZip" resourceKey="InvoiceExport.customerData.zipCode"
                          type="${FIELD_TYPE_STRING}" width="1"
                          fieldPosition="12"/>

    <titus:reportFieldTag name="cityName" resourceKey="InvoiceExport.customerData.city"
                          type="${FIELD_TYPE_STRING}" width="1"
                          fieldPosition="13"/>

    <titus:reportFieldTag name="emptyValue5" resourceKey="InvoiceExport.customerData.contactPerson"
                          type="${FIELD_TYPE_STRING}" width="1"
                          conditionMethod="com.piramide.elwis.web.financemanager.util.InvoiceCSVExportReportUtil.getEmptyValue []"
                          fieldPosition="14"/>

    <titus:reportFieldTag name="emptyValue6" resourceKey="InvoiceExport.customerData.phone1"
                          type="${FIELD_TYPE_STRING}" width="1"
                          conditionMethod="com.piramide.elwis.web.financemanager.util.InvoiceCSVExportReportUtil.getEmptyValue []"
                          fieldPosition="15"/>

    <titus:reportFieldTag name="emptyValue7" resourceKey="InvoiceExport.customerData.phone2"
                          type="${FIELD_TYPE_STRING}" width="1"
                          conditionMethod="com.piramide.elwis.web.financemanager.util.InvoiceCSVExportReportUtil.getEmptyValue []"
                          fieldPosition="16"/>

    <titus:reportFieldTag name="emptyValue8" resourceKey="InvoiceExport.customerData.fax"
                          type="${FIELD_TYPE_STRING}" width="1"
                          conditionMethod="com.piramide.elwis.web.financemanager.util.InvoiceCSVExportReportUtil.getEmptyValue []"
                          fieldPosition="17"/>

    <titus:reportFieldTag name="emptyValue9" resourceKey="InvoiceExport.customerData.mail"
                          type="${FIELD_TYPE_STRING}" width="1"
                          conditionMethod="com.piramide.elwis.web.financemanager.util.InvoiceCSVExportReportUtil.getEmptyValue []"
                          fieldPosition="18"/>

    <titus:reportFieldTag name="emptyValue10" resourceKey="InvoiceExport.customerData.bankCode"
                          type="${FIELD_TYPE_STRING}" width="1"
                          conditionMethod="com.piramide.elwis.web.financemanager.util.InvoiceCSVExportReportUtil.getEmptyValue []"
                          fieldPosition="19"/>

    <titus:reportFieldTag name="emptyValue11" resourceKey="InvoiceExport.customerData.bankAccount"
                          type="${FIELD_TYPE_STRING}" width="1"
                          conditionMethod="com.piramide.elwis.web.financemanager.util.InvoiceCSVExportReportUtil.getEmptyValue []"
                          fieldPosition="20"/>

    <titus:reportFieldTag name="emptyValue12" resourceKey="InvoiceExport.customerData.bankName"
                          type="${FIELD_TYPE_STRING}" width="1"
                          conditionMethod="com.piramide.elwis.web.financemanager.util.InvoiceCSVExportReportUtil.getEmptyValue []"
                          fieldPosition="21"/>

    <titus:reportFieldTag name="emptyValue13" resourceKey="InvoiceExport.customerData.BIC"
                          type="${FIELD_TYPE_STRING}" width="1"
                          conditionMethod="com.piramide.elwis.web.financemanager.util.InvoiceCSVExportReportUtil.getEmptyValue []"
                          fieldPosition="22"/>

    <titus:reportFieldTag name="emptyValue14" resourceKey="InvoiceExport.customerData.IBAN"
                          type="${FIELD_TYPE_STRING}" width="1"
                          conditionMethod="com.piramide.elwis.web.financemanager.util.InvoiceCSVExportReportUtil.getEmptyValue []"
                          fieldPosition="23"/>

    <titus:reportFieldTag name="payDays" resourceKey="InvoiceExport.customerData.periodOfPayment"
                          type="${FIELD_TYPE_INTEGER}" width="1"
                          fieldPosition="24"/>

    <titus:reportFieldTag name="emptyValue15" resourceKey="InvoiceExport.customerData.discount"
                          type="${FIELD_TYPE_STRING}" width="1"
                          conditionMethod="com.piramide.elwis.web.financemanager.util.InvoiceCSVExportReportUtil.getEmptyValue []"
                          fieldPosition="25"/>

    <titus:reportFieldTag name="emptyValue16" resourceKey="InvoiceExport.customerData.discountPeriod"
                          type="${FIELD_TYPE_STRING}" width="1"
                          conditionMethod="com.piramide.elwis.web.financemanager.util.InvoiceCSVExportReportUtil.getEmptyValue []"
                          fieldPosition="26"/>

    <titus:reportFieldTag name="emptyValue17" resourceKey="InvoiceExport.customerData.discount2"
                          type="${FIELD_TYPE_STRING}" width="1"
                          conditionMethod="com.piramide.elwis.web.financemanager.util.InvoiceCSVExportReportUtil.getEmptyValue []"
                          fieldPosition="27"/>

    <titus:reportFieldTag name="emptyValue18" resourceKey="InvoiceExport.customerData.discount2period"
                          type="${FIELD_TYPE_STRING}" width="1"
                          conditionMethod="com.piramide.elwis.web.financemanager.util.InvoiceCSVExportReportUtil.getEmptyValue []"
                          fieldPosition="28"/>

    <titus:reportFieldTag name="emptyValue19" resourceKey="InvoiceExport.customerData.directDebitAuthorization"
                          type="${FIELD_TYPE_STRING}" width="1"
                          conditionMethod="com.piramide.elwis.web.financemanager.util.InvoiceCSVExportReportUtil.getEmptyValue []"
                          fieldPosition="29"/>

    <titus:reportFieldTag name="emptyValue20" resourceKey="InvoiceExport.customerData.UStIDNr"
                          type="${FIELD_TYPE_STRING}" width="1"
                          conditionMethod="com.piramide.elwis.web.financemanager.util.InvoiceCSVExportReportUtil.getEmptyValue []"
                          fieldPosition="30"/>
</div>
</html:form>
</div>