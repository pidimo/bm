<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<tags:initSelectPopup/>

<script type="text/javascript">
    function myReset() {
        var form = document.incomingInvoiceReportForm;
        for (i = 0; i < form.elements.length; i++) {
            if (form.elements[i].type == "text") {
                form.elements[i].value = "";
            } else if (form.elements[i].name == "parameter(currencyId)") {
                form.elements[i].options.selectedIndex = 0;
            } else if (form.elements[i].type == "select-one") {
                form.elements[i].value = "";
            }
        }
        if (document.getElementById("fieldAddressId_id") != null && document.getElementById("fieldAddressId_id") != "") {
            document.getElementById("fieldAddressId_id").value = "";
        }
    }
</script>
<c:set var="invoiceTypeList" value="${app2:getInvoiceTypes(pageContext.request)}"/>
<br>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="container">
<html:form action="/Report/IncomingInvoiceList/Execute.do" focus="parameter(supplierName_FIELD)"
           styleId="invoiceFormId">
<tr>
    <td height="20" class="title" colspan="4">
        <fmt:message key="${pagetitle}"/>
    </td>
</tr>
<tr>
    <td class="label" width="15%"><fmt:message key="Finance.incomingInvoice.supplierName"/></td>
    <td class="contain" width="35%">
        <html:hidden property="parameter(supplierId)" styleId="fieldAddressId_id"/>
        <app:text property="parameter(supplierName_FIELD)" styleId="fieldAddressName_id" styleClass="mediumText"
                  readonly="true" tabindex="1"/>


        <tags:selectPopup url="/contacts/SearchAddress.do" name="SearchSupplier"
                          titleKey="Common.search" tabindex="2"/>

        <tags:clearSelectPopup keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id"
                               titleKey="Common.clear" tabindex="3"/>
    </td>
    <!--*******************COLUMN DIVISION************************-->
    <td class="label" width="15%"><fmt:message key="Finance.incomingInvoice.type"/></td>
    <td class="contain" width="35%">
        <html:select property="parameter(type)"
                     styleClass="middleSelect"
                     tabindex="11">
            <html:option value=""/>
            <html:options collection="invoiceTypeList" property="value" labelProperty="label"/>
        </html:select>
    </td>
</tr>

<tr>
    <td class="label">
        <fmt:message key="Finance.incomingInvoice.currency"/>
    </td>
    <td class="contain">
        <fanta:select property="parameter(currencyId)"
                      listName="basicCurrencyList"
                      labelProperty="name"
                      valueProperty="id"
                      firstEmpty="true"
                      styleClass="middleSelect"
                      module="/catalogs"
                      tabIndex="4">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
    <!--*******************COLUMN DIVISION************************-->
    <td class="label">
        <fmt:message key="Finance.incomingInvoice.invoiceDate"/>
    </td>
    <td class="contain">
        <fmt:message key="datePattern" var="datePattern"/>
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:dateText property="parameter(startInvoiceDate)" maxlength="10" tabindex="12"
                      styleId="startInvoiceDate"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                      convert="true"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:dateText property="parameter(endInvoiceDate)" maxlength="10" tabindex="13" styleId="endInvoiceDate"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                      convert="true"/>
    </td>
</tr>
<tr>
    <td class="label"><fmt:message key="Finance.incomingInvoice.amountNet"/></td>
    <td class="contain">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:numberText property="parameter(amountNet1)" styleClass="numberText" tabindex="5" maxlength="12"
                        numberType="decimal" maxInt="10" maxFloat="2"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:numberText property="parameter(amountNet2)" styleClass="numberText" tabindex="6"
                        maxlength="12" numberType="decimal" maxInt="10" maxFloat="2"/>
    </td>
    <!--*******************COLUMN DIVISION************************-->
    <td class="label">
        <fmt:message key="Finance.incomingInvoice.receiptDate"/>
    </td>
    <td class="contain">
        <fmt:message key="datePattern" var="datePattern"/>
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:dateText property="parameter(startReceiptDate)" maxlength="10" tabindex="14" styleId="startPayDate"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                      convert="true"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:dateText property="parameter(endReceiptDate)" maxlength="10" tabindex="15" styleId="endPayDate"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                      convert="true"/>
    </td>
</tr>
<tr>
    <td class="label"><fmt:message key="Finance.incomingInvoice.amountGross"/></td>
    <td class="contain">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:numberText property="parameter(amountGross1)" styleClass="numberText" tabindex="7" maxlength="12"
                        numberType="decimal" maxInt="10" maxFloat="2"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:numberText property="parameter(amountGross2)" styleClass="numberText" tabindex="8"
                        maxlength="12" numberType="decimal" maxInt="10" maxFloat="2"/>
    </td>
    <!--*******************COLUMN DIVISION************************-->
    <td class="label">
        <fmt:message key="Finance.incomingInvoice.paidUntil"/>
    </td>
    <td class="contain">
        <fmt:message key="datePattern" var="datePattern"/>
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:dateText property="parameter(startPaidUntilDate)" maxlength="10" tabindex="16"
                      styleId="startPaidUntilDate"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                      convert="true"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:dateText property="parameter(endPaidUntilDate)" maxlength="10" tabindex="17"
                      styleId="endPaidUntilDate"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                      convert="true"/>
    </td>
</tr>
<tr>
    <td class="label"><fmt:message key="Finance.incomingInvoice.openAmount"/></td>
    <td class="contain">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:numberText property="parameter(openAmount1)" styleClass="numberText" tabindex="9" maxlength="12"
                        numberType="decimal" maxInt="10" maxFloat="2"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:numberText property="parameter(openAmount2)" styleClass="numberText" tabindex="10"
                        maxlength="12" numberType="decimal" maxInt="10" maxFloat="2"/>
    </td>
    <td class="contain" colspan="2">&nbsp;</td>
</tr>


<c:set var="reportFormats" value="${incomingInvoiceReportForm.reportFormats}" scope="request"/>
<c:set var="pageSizes" value="${incomingInvoiceReportForm.pageSizes}" scope="request"/>
<tr>
    <td colspan="4">
        <titus:reportGroupSortTag width="100%">
            <titus:reportGroupSortColumnTag name="INVOICENUMBER"
                                            labelKey="Finance.incomingInvoice.invoiceNumber"/>
            <titus:reportGroupSortColumnTag name="SUPPLIERNAME"
                                            labelKey="Finance.incomingInvoice.supplierName"/>
            <titus:reportGroupSortColumnTag name="invoiceDate" labelKey="Finance.incomingInvoice.invoiceDate"
                                            isDate="true" orderColumnName="INVOICEDATE"/>
            <titus:reportGroupSortColumnTag name="paidUntil" labelKey="Finance.incomingInvoice.paidUntil"
                                            isDate="true" orderColumnName="PAIDUNTILDATE"/>
            <titus:reportGroupSortColumnTag name="CURRENCYLABEL" labelKey="Finance.incomingInvoice.currency"/>
            <titus:reportGroupSortColumnTag name="amountNet" labelKey="Finance.incomingInvoice.amountNet"
                                            orderColumnName="AMOUNTNET"/>
            <titus:reportGroupSortColumnTag name="amountGross" labelKey="Finance.incomingInvoice.amountGross"
                                            orderColumnName="AMOUNTGROSS"/>
            <titus:reportGroupSortColumnTag name="openAmount" labelKey="Finance.incomingInvoice.openAmount"
                                            orderColumnName="OPENAMOUNT"/>
        </titus:reportGroupSortTag>
    </td>
</tr>

<tags:reportOptionsTag/>

<tr>
    <td colspan="4" class="button">
        <html:submit styleClass="button" property="parameter(generate)" tabindex="57">
            <fmt:message key="Campaign.Report.generate"/>
        </html:submit>
        <html:button property="reset1" tabindex="58" styleClass="button" onclick="myReset()">
            <fmt:message key="Common.clear"/>
        </html:button>
    </td>
</tr>

<titus:reportInitializeConstantsTag/>
<titus:reportTag id="incomingInvoiceReportList" title="Finance.Report.incomingInvoiceList"
                 pageOrientation="${PAGE_ORIENTATION_LANDSCAPE}"
                 locale="${sessionScope.user.valueMap['locale']}"
                 timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>
<titus:reportFieldTag name="INVOICENUMBER" resourceKey="Finance.incomingInvoice.invoiceNumber"
                      type="${FIELD_TYPE_STRING}" width="10" fieldPosition="1"/>
<titus:reportFieldTag name="SUPPLIERNAME" resourceKey="Finance.incomingInvoice.supplierName"
                      type="${FIELD_TYPE_STRING}" width="20" fieldPosition="2"/>
<titus:reportFieldTag name="invoiceDate" resourceKey="Finance.incomingInvoice.invoiceDate"
                      type="${FIELD_TYPE_DATEINT}" width="10" fieldPosition="3" patternKey="datePattern"/>
<titus:reportFieldTag name="paidUntil" resourceKey="Finance.incomingInvoice.paidUntil"
                      type="${FIELD_TYPE_DATEINT}" width="10" fieldPosition="4" patternKey="datePattern"/>
<titus:reportFieldTag name="TYPE" resourceKey="Finance.incomingInvoice.type" type="${FIELD_TYPE_STRING}"
                      width="6" fieldPosition="5"
                      conditionMethod="com.piramide.elwis.web.salesmanager.el.Functions.getInvoiceTypeName TYPE [${sessionScope.user.valueMap['locale']}]"/>
<titus:reportFieldTag name="CURRENCYLABEL" resourceKey="Finance.incomingInvoice.currency"
                      type="${FIELD_TYPE_STRING}" width="8" fieldPosition="6"/>
<titus:reportFieldTag name="amountNet" resourceKey="Finance.incomingInvoice.amountNet"
                      type="${FIELD_TYPE_DECIMALNUMBER}" width="9" fieldPosition="7"
                      patternKey="numberFormat.2DecimalPlaces" align="${FIELD_ALIGN_RIGHT}"/>
<titus:reportFieldTag name="VATAMOUNT" resourceKey="Finance.incomingInvoice.vatAmount"
                      type="${FIELD_TYPE_CALCULATEDDECIMALNUMBER}" width="9" fieldPosition="8"
                      patternKey="numberFormat.2DecimalPlaces" align="${FIELD_ALIGN_RIGHT}"
                      conditionMethod="com.piramide.elwis.utils.ReportHelper.substractDecimalAmounts amountGross amountNet"/>
<titus:reportFieldTag name="amountGross" resourceKey="Finance.incomingInvoice.amountGross"
                      type="${FIELD_TYPE_DECIMALNUMBER}" width="9" fieldPosition="9"
                      patternKey="numberFormat.2DecimalPlaces" align="${FIELD_ALIGN_RIGHT}"/>
<titus:reportFieldTag name="openAmount" resourceKey="Finance.incomingInvoice.openAmount"
                      type="${FIELD_TYPE_DECIMALNUMBER}" width="9" fieldPosition="10"
                      patternKey="numberFormat.2DecimalPlaces" align="${FIELD_ALIGN_RIGHT}"/>
</html:form>
</table>