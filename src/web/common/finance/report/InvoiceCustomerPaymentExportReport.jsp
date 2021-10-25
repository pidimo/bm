<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<fmt:message var="datePattern" key="datePattern"/>
<fmt:message var="decimalPattern" key="numberFormatFixed.2DecimalPlaces"/>

<br>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="container">
    <html:form action="${action}" focus="parameter(startInvoiceDate)" styleId="exportFormId">
        <tr>
            <td height="20" class="title" colspan="4">
                <c:out value="${title}"/>
            </td>
        </tr>
        <tr>
            <td class="label" width="15%">
                <fmt:message key="InvoicePayment.payDate"/>
            </td>
            <td class="contain" width="35%">
                <fmt:message key="datePattern" var="datePattern"/>
                <fmt:message key="Common.from"/>
                &nbsp;
                <app:dateText property="parameter(startInvoiceDate)" maxlength="10" tabindex="2"
                              styleId="startInvoiceDate"
                              calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                              convert="true"/>
                &nbsp;
                <fmt:message key="Common.to"/>
                &nbsp;
                <app:dateText property="parameter(endInvoiceDate)" maxlength="10" tabindex="3" styleId="endInvoiceDate"
                              calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                              convert="true"/>
            </td>
            <td class="label" width="15%"><fmt:message key="InvoicePayment.markAsExported"/></td>
            <td class="contain" width="35%">
                <html:checkbox property="parameter(exported)" tabindex="5" styleClass="radio" value="true"/>
            </td>
        </tr>
        <tr>
            <td colspan="4" class="button">
                <html:submit styleClass="button" property="parameter(generate)" tabindex="10">
                    <fmt:message key="Report.generate"/>
                </html:submit>
                <html:button property="reset1" tabindex="11" styleClass="button" onclick="formReset('exportFormId')">
                    <fmt:message key="Common.clear"/>
                </html:button>
            </td>
        </tr>

        <titus:reportInitializeConstantsTag/>
        <titus:reportTag id="invoiceCustomerPaymentExportList" title="Finance.report.invoiceCustomerPaymentExport"
                         pageOrientation="${PAGE_ORIENTATION_LANDSCAPE}"
                         locale="${sessionScope.user.valueMap['locale']}"
                         timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>

        <titus:reportFieldTag name="payDate" resourceKey="InvoiceExport.voucherDate"
                              type="${FIELD_TYPE_DATEINT}" patternKey="datePattern" width="1"
                              fieldPosition="1"/>

        <titus:reportFieldTag name="bookingDate" resourceKey="InvoiceExport.bookingDate"
                              type="${FIELD_TYPE_DATEINT}" patternKey="datePattern" width="1"
                              fieldPosition="2"/>

        <titus:reportFieldTag name="bookingPeriod" resourceKey="InvoiceExport.bookingPeriod"
                              type="${FIELD_TYPE_STRING}" width="1"
                              conditionMethod="com.piramide.elwis.web.financemanager.util.InvoiceCSVExportReportUtil.getMonthOfDate payDate"
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

        <titus:reportFieldTag name="amount" resourceKey="InvoiceExport.accountingAmount"
                              type="${FIELD_TYPE_STRING}" width="1"
                              conditionMethod="com.piramide.elwis.web.financemanager.util.InvoiceCSVExportReportUtil.calculatePaymentAmount amount invoiceType [${sessionScope.user.valueMap['locale']}] [${decimalPattern}]"
                              fieldPosition="7"/>

        <titus:reportFieldTag name="bookkeepingAccount" resourceKey="InvoiceExport.debitAccount"
                              type="${FIELD_TYPE_STRING}" width="1"
                              fieldPosition="8"/>

        <titus:reportFieldTag name="customerNumber" resourceKey="InvoiceExport.creditAccount"
                              type="${FIELD_TYPE_STRING}" width="1"
                              conditionMethod="com.piramide.elwis.web.financemanager.util.InvoiceCSVExportReportUtil.getCustomerNumber customerNumber debitorNumber {EXPORTDATATYPE_PARAM}"
                              fieldPosition="9"/>

        <titus:reportFieldTag name="emptyValue2" resourceKey="InvoiceExport.taxKey"
                              type="${FIELD_TYPE_STRING}" width="1"
                              conditionMethod="com.piramide.elwis.web.financemanager.util.InvoiceCSVExportReportUtil.getEmptyValue []"
                              fieldPosition="10"/>

        <titus:reportFieldTag name="emptyValue3" resourceKey="InvoiceExport.costCenter1"
                              type="${FIELD_TYPE_STRING}" width="1"
                              conditionMethod="com.piramide.elwis.web.financemanager.util.InvoiceCSVExportReportUtil.getEmptyValue []"
                              fieldPosition="11"/>

        <titus:reportFieldTag name="emptyValue4" resourceKey="InvoiceExport.costObject"
                              type="${FIELD_TYPE_STRING}" width="1"
                              conditionMethod="com.piramide.elwis.web.financemanager.util.InvoiceCSVExportReportUtil.getEmptyValue []"
                              fieldPosition="12"/>

        <titus:reportFieldTag name="emptyValue5" resourceKey="InvoiceExport.accountingAmountEuro"
                              type="${FIELD_TYPE_STRING}" width="1"
                              conditionMethod="com.piramide.elwis.web.financemanager.util.InvoiceCSVExportReportUtil.getEmptyValue []"
                              fieldPosition="13"/>

        <titus:reportFieldTag name="currencyLabel" resourceKey="InvoiceExport.currency"
                              type="${FIELD_TYPE_STRING}" width="1"
                              fieldPosition="14"/>
    </html:form>
</table>