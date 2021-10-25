<%@ page import="com.piramide.elwis.utils.SalesConstants" %>
<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<tags:initSelectPopup/>
<tags:initSelectPopupAdvanced fields="field_key, field_name, field_versionNumber, field_price, field_unitId"/>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<br>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="container">
<html:form action="/Report/ContractsOverviewList/Execute.do" focus="parameter(contact)" styleId="formId">
<tr>
    <td height="20" class="title" colspan="4">
        <fmt:message key="SalesProcess.Report.ContractsOverviewList"/>
    </td>
</tr>
<tr>
    <td class="label" width="15%"><fmt:message key="Contract.contact"/></td>
    <td class="contain" width="35%">
        <html:hidden property="parameter(addressId)" styleId="fieldAddressId_id"/>
        <app:text property="parameter(contact)" styleId="fieldAddressName_id" styleClass="middleText" maxlength="40"
                  tabindex="1" readonly="true"/>
        <tags:selectPopup url="/contacts/SearchAddress.do" name="searchAddress" titleKey="Common.search" tabindex="2"/>
        <tags:clearSelectPopup keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id"
                               titleKey="Common.clear" tabindex="2"/>
    </td>

    <td class="label" width="12%"><fmt:message key="ProductContract.seller"/></td>
    <td class="contain" width="38%">
        <fanta:select property="parameter(sellerId)"
                      listName="employeeBaseList"
                      labelProperty="employeeName"
                      valueProperty="employeeId"
                      styleClass="middleSelect"
                      firstEmpty="true"
                      module="/contacts"
                      tabIndex="6">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Contract.product"/>
    </td>
    <td class="contain">
        <html:hidden property="parameter(productId)" styleId="field_key"/>
        <html:hidden property="parameter(1)" styleId="field_versionNumber"/>
        <html:hidden property="parameter(2)" styleId="field_unitId"/>
        <html:hidden property="parameter(3)" styleId="field_price"/>

        <app:text property="parameter(productName)" styleId="field_name" styleClass="middleText" maxlength="40"
                  readonly="true" tabindex="2"/>
        <tags:selectPopup url="/products/SearchProduct.do" name="SearchProduct" titleKey="Common.search" tabindex="3"/>
        <tags:clearSelectPopup keyFieldId="field_key" nameFieldId="field_name" titleKey="Common.clear" tabindex="3"/>
    </td>
    <td class="label"><fmt:message key="Contract.contractType"/></td>
    <td class="contain">

        <fmt:message var="allToBeInvoicedLabel" key="ContractToInvoice.report.allToBeInvoiced"/>
        <c:set var="allToBeInvoiced" value="<%=SalesConstants.CONTRACTS_TO_BE_INVOICED%>"/>
        <fmt:message var="allNotToBeInvoicedLabel" key="ContractToInvoice.report.allNotToBeInvoiced"/>
        <c:set var="allNotToBeInvoiced" value="<%=SalesConstants.CONTRACTS_NOT_TO_BE_INVOICED%>"/>

        <fmt:message var="groupToInvoiced" key="ContractToInvoice.report.contractToBeInvoiced"/>
        <fmt:message var="groupNotInvoiced" key="ContractToInvoice.report.contractNotToBeInvoiced"/>

        <fanta:select property="parameter(contractTypeParam)"
                      listName="contractTypeList"
                      firstEmpty="true"
                      labelProperty="name"
                      valueProperty="contractTypeId"
                      module="/catalogs"
                      styleClass="middleSelect"
                      tabIndex="8"
                      withGroups="true" styleId="contractTypeParamField">
            <fanta:addItem key="${allToBeInvoiced}" value="${allToBeInvoicedLabel}"/>
            <fanta:addItem key="${allNotToBeInvoiced}" value="${allNotToBeInvoicedLabel}"/>

            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:group groupName="${groupToInvoiced}" columnDiscriminator="tobeInvoiced" valueDiscriminator="1"/>
            <fanta:group groupName="${groupNotInvoiced}" columnDiscriminator="tobeInvoiced" valueDiscriminator="0"/>
        </fanta:select>

    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Product.group"/>
    </td>
    <td class="contain">
        <fanta:select property="parameter(productGroupId)" listName="productGroupList" firstEmpty="true"
                      labelProperty="name" valueProperty="id" module="/catalogs" styleClass="middleSelect"
                      tabIndex="4">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:tree columnId="id" columnParentId="parentProductGroupId" separator="&nbsp;&nbsp;&nbsp;&nbsp;"/>
        </fanta:select>
    </td>
    <td class="label">
        <fmt:message key="ContractsOverview.report.date"/>
    </td>
    <td class="contain">
        <fmt:message key="datePattern" var="datePattern"/>
        <app:dateText property="parameter(inputDate)" maxlength="10" tabindex="9" styleId="dateId" currentDate="true"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Product.type"/>
    </td>
    <td class="contain" colspan="3">
        <fanta:select property="parameter(productTypeId)" listName="productTypeList" firstEmpty="true"
                      labelProperty="name" valueProperty="id" module="/catalogs" styleClass="middleSelect"
                      preProcessor="com.piramide.elwis.web.productmanager.el.ProductTypeSelectPreProcessor"
                      tabIndex="5">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
</tr>


<c:set var="reportFormats" value="${contractsOverviewReportForm.reportFormats}" scope="request"/>
<c:set var="pageSizes" value="${contractsOverviewReportForm.pageSizes}" scope="request"/>
<tr>
    <td colspan="4">
        <titus:reportGroupSortTag width="100%">
            <titus:reportGroupSortColumnTag name="contractNumber" labelKey="ProductContract.contractNumber"/>
            <titus:reportGroupSortColumnTag name="addressName" labelKey="Contract.contact"/>
            <titus:reportGroupSortColumnTag name="customerName" labelKey="Sale.customer"/>
            <titus:reportGroupSortColumnTag name="productName" labelKey="Contract.product"/>
            <titus:reportGroupSortColumnTag name="orderDate" labelKey="Contract.orderDate" isDate="true"/>
            <titus:reportGroupSortColumnTag name="payStartDate" labelKey="ProductContract.payStartDate" isDate="true" />
            <titus:reportGroupSortColumnTag name="contractEndDate" labelKey="ContractToInvoice.report.contractEndDate" isDate="true" />
            <titus:reportGroupSortColumnTag name="invoicedUntil" labelKey="ProductContract.invoiceUntil" isDate="true"/>
            <titus:reportGroupSortColumnTag name="currencyLabel" labelKey="ProductContract.currency"/>
        </titus:reportGroupSortTag>
    </td>
</tr>
<tags:reportOptionsTag />

<tr>
    <td colspan="4" class="button">
        <html:submit styleClass="button" property="parameter(generate)" tabindex="57">
            <fmt:message key="Campaign.Report.generate"/>
        </html:submit>
        <html:button property="reset1" tabindex="58" styleClass="button" onclick="formReset('formId')">
            <fmt:message key="Common.clear"/>
        </html:button>
    </td>
</tr>

<titus:reportInitializeConstantsTag/>
<titus:reportTag id="contractsOverviewReportList" title="SalesProcess.Report.ContractsOverviewList"
                 pageOrientation="${PAGE_ORIENTATION_LANDSCAPE}"
                 locale="${sessionScope.user.valueMap['locale']}"
                 timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>

    <titus:reportFieldTag name="contractNumber" resourceKey="ProductContract.contractNumber" type="${FIELD_TYPE_STRING}" width="8"
                          fieldPosition="1"/>
    <titus:reportFieldTag name="addressName" resourceKey="Contract.contact" type="${FIELD_TYPE_STRING}" width="15"
                          fieldPosition="2"/>
    <titus:reportFieldTag name="customerName" resourceKey="Sale.customer" type="${FIELD_TYPE_STRING}" width="15"
                          fieldPosition="3"/>
    <titus:reportFieldTag name="productName" resourceKey="Contract.product" type="${FIELD_TYPE_STRING}" width="13"
                          fieldPosition="4"/>
    <titus:reportFieldTag name="orderDate" resourceKey="Contract.orderDate" type="${FIELD_TYPE_DATEINT}" width="8"
                          fieldPosition="5" patternKey="datePattern"/>
    <titus:reportFieldTag name="payStartDate" resourceKey="ProductContract.payStartDate" type="${FIELD_TYPE_DATEINT}" width="8"
                          fieldPosition="6" patternKey="datePattern"/>
    <titus:reportFieldTag name="contractEndDate" resourceKey="ContractToInvoice.report.contractEndDate" type="${FIELD_TYPE_DATEINT}" width="8"
                          fieldPosition="7" patternKey="datePattern"/>
    <titus:reportFieldTag name="invoicedUntil" resourceKey="ProductContract.invoiceUntil" type="${FIELD_TYPE_DATEINT}" width="8"
                          fieldPosition="8" patternKey="datePattern"/>
    <titus:reportFieldTag name="price" resourceKey="ContractsOverview.report.incomeCost" type="${FIELD_TYPE_CALCULATEDDECIMALNUMBER}" width="9"
                          fieldPosition="9" align="${FIELD_ALIGN_RIGHT}"
                          conditionMethod="com.piramide.elwis.utils.ReportHelper.getAsDouble (overviewIncomeVar)" patternKey="numberFormat.2DecimalPlaces"/>
    <titus:reportFieldTag name="currencyLabel" resourceKey="ProductContract.currency" type="${FIELD_TYPE_STRING}" width="8"
                          fieldPosition="10"/>
</html:form>
</table>
