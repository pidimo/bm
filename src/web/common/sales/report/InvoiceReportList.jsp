<%@ page import="com.piramide.elwis.utils.FinanceConstants" %>
<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<tags:initSelectPopup/>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<c:set var="invoiceTypeList" value="${app2:getInvoiceTypes(pageContext.request)}"/>
<br>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="container">
<html:form action="/Report/InvoiceList/Execute.do" focus="parameter(contact)" styleId="invoiceFormId">
<tr>
    <td height="20" class="title" colspan="4">
        <fmt:message key="SalesProcess.Report.InvoiceList"/>
    </td>
</tr>
<tr>
    <td class="label" width="15%"><fmt:message key="Invoice.contact"/></td>
    <td class="contain" width="35%">
        <html:hidden property="parameter(addressId)" styleId="fieldAddressId_id"/>
        <app:text property="parameter(contact)" styleId="fieldAddressName_id" styleClass="middleText"
                  maxlength="40"
                  tabindex="1" readonly="true"/>
        <tags:selectPopup url="/contacts/SearchAddress.do" name="searchAddress" titleKey="Common.search"
                          tabindex="2"/>
        <tags:clearSelectPopup keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id"
                               titleKey="Common.clear" tabindex="3"/>
    </td>

    <td class="label" width="12%"><fmt:message key="Invoice.payCondition"/></td>
    <td class="contain" width="38%">
        <fanta:select property="parameter(payConditionId)"
                      listName="payConditionList"
                      labelProperty="name"
                      valueProperty="id"
                      styleClass="middleSelect"
                      firstEmpty="true"
                      module="/catalogs"
                      tabIndex="12">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
</tr>
<tr>
    <td class="label"><fmt:message key="Invoice.type"/></td>
    <td class="contain">
        <html:select property="parameter(type)"
                     styleClass="middleSelect"
                     tabindex="4">
            <html:option value=""/>
            <html:options collection="invoiceTypeList" property="value" labelProperty="label"/>
        </html:select>
    </td>
    <td class="label">
        <fmt:message key="Invoice.currency"/>
    </td>
    <td class="contain">
        <fanta:select property="parameter(currencyId)"
                      listName="basicCurrencyList"
                      labelProperty="name"
                      valueProperty="id"
                      firstEmpty="true"
                      styleClass="middleSelect"
                      module="/catalogs"
                      tabIndex="13">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
</tr>
<tr>

    <td class="label"><fmt:message key="Invoice.totalAmountNet"/></td>
    <td class="contain">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:numberText property="parameter(amountNet1)" styleClass="numberText" tabindex="6" maxlength="12"
                        numberType="decimal" maxInt="10" maxFloat="2"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:numberText property="parameter(amountNet2)" styleClass="numberText" tabindex="7"
                        maxlength="12" numberType="decimal" maxInt="10" maxFloat="2"/>
    </td>
    <td class="label">
        <fmt:message key="Invoice.report.voucherNumberRule"/>
    </td>
    <td class="contain">
        <c:set var="voucherType"><%=FinanceConstants.SequenceRuleType.VOUCHER.getConstantAsString()%>
        </c:set>
        <fanta:select property="parameter(ruleFormat)"
                      listName="sequenceRuleList"
                      labelProperty="label"
                      valueProperty="format"
                      styleClass="middleSelect"
                      firstEmpty="true"
                      module="/catalogs"
                      tabIndex="14">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="type" value="${voucherType}"/>
        </fanta:select>
    </td>

</tr>
<tr>

    <td class="label"><fmt:message key="Invoice.totalAmountGross"/></td>
    <td class="contain">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:numberText property="parameter(amountGross1)" styleClass="numberText" tabindex="8" maxlength="12"
                        numberType="decimal" maxInt="10" maxFloat="2"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:numberText property="parameter(amountGross2)" styleClass="numberText" tabindex="9"
                        maxlength="12" numberType="decimal" maxInt="10" maxFloat="2"/>
    </td>
    <td class="label">
        <fmt:message key="Invoice.invoiceDate"/>
    </td>
    <td class="contain">
        <fmt:message key="datePattern" var="datePattern"/>
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:dateText property="parameter(startInvoiceDate)" maxlength="10" tabindex="15"
                      styleId="startInvoiceDate"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                      convert="true"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:dateText property="parameter(endInvoiceDate)" maxlength="10" tabindex="16" styleId="endInvoiceDate"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                      convert="true"/>
    </td>
</tr>
<tr>
    <td class="label"><fmt:message key="Invoice.openAmount"/></td>
    <td class="contain">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:numberText property="parameter(openAmount1)" styleClass="numberText" tabindex="10" maxlength="12"
                        numberType="decimal" maxInt="10" maxFloat="2"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:numberText property="parameter(openAmount2)" styleClass="numberText" tabindex="11"
                        maxlength="12" numberType="decimal" maxInt="10" maxFloat="2"/>
    </td>
    <td class="label">
        <fmt:message key="Invoice.paymentDate"/>
    </td>
    <td class="contain">
        <fmt:message key="datePattern" var="datePattern"/>
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:dateText property="parameter(startPaymentDate)" maxlength="10" tabindex="17" styleId="startPayDate"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                      convert="true"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:dateText property="parameter(endPaymentDate)" maxlength="10" tabindex="18" styleId="endPayDate"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                      convert="true"/>
    </td>
</tr>


<c:set var="reportFormats" value="${invoiceReportForm.reportFormats}" scope="request"/>
<c:set var="pageSizes" value="${invoiceReportForm.pageSizes}" scope="request"/>
<tr>
    <td colspan="4">
        <titus:reportGroupSortTag width="100%">
            <titus:reportGroupSortColumnTag name="number" labelKey="Invoice.number"/>
            <titus:reportGroupSortColumnTag name="addressName" labelKey="Invoice.contact"/>
            <titus:reportGroupSortColumnTag name="invoiceDate" labelKey="Invoice.invoiceDate" isDate="true"/>
            <titus:reportGroupSortColumnTag name="totalAmountGross" labelKey="Invoice.report.totalGross"/>
            <titus:reportGroupSortColumnTag name="currencyLabel" labelKey="Invoice.currency"/>
            <titus:reportGroupSortColumnTag name="totalAmountNet" labelKey="Invoice.report.totalNet"/>
        </titus:reportGroupSortTag>
    </td>
</tr>
<tags:reportOptionsTag/>

<tr>
    <td colspan="4" class="button">
        <html:submit styleClass="button" property="parameter(generate)" tabindex="57">
            <fmt:message key="Campaign.Report.generate"/>
        </html:submit>
        <html:button property="reset1" tabindex="58" styleClass="button" onclick="formReset('invoiceFormId')">
            <fmt:message key="Common.clear"/>
        </html:button>
    </td>
</tr>

<titus:reportInitializeConstantsTag/>
<titus:reportTag id="invoiceReportList" title="SalesProcess.Report.InvoiceList"
                 pageOrientation="${PAGE_ORIENTATION_LANDSCAPE}"
                 locale="${sessionScope.user.valueMap['locale']}"
                 timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>
<titus:reportFieldTag name="number" resourceKey="Invoice.number" type="${FIELD_TYPE_STRING}" width="18"
                      fieldPosition="1"/>
<titus:reportFieldTag name="addressName" resourceKey="Invoice.contact" type="${FIELD_TYPE_STRING}" width="20"
                      fieldPosition="2"/>
<titus:reportFieldTag name="invoiceDate" resourceKey="Invoice.invoiceDate" type="${FIELD_TYPE_DATEINT}"
                      width="10" fieldPosition="3" patternKey="datePattern"/>
<titus:reportFieldTag name="totalAmountGross" resourceKey="Invoice.report.totalGross"
                      type="${FIELD_TYPE_DECIMALNUMBER}" width="10" fieldPosition="4"
                      patternKey="numberFormat.2DecimalPlaces" align="${FIELD_ALIGN_RIGHT}"/>
<titus:reportFieldTag name="currencyLabel" resourceKey="Invoice.currency" type="${FIELD_TYPE_STRING}" width="12"
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


</html:form>
</table>