<%@ page import="com.piramide.elwis.utils.FinanceConstants" %>
<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<c:set var="INVOICE_TYPE" value="<%=FinanceConstants.InvoiceType.Invoice.getConstantAsString()%>"/>
<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>

<script>
    function myReset() {
        var form = document.incomingInvoiceAdvancedListForm;
        for (i = 0; i < form.elements.length; i++) {
            if (form.elements[i].type == "text") {
                form.elements[i].value = "";
            } else if (form.elements[i].name == "parameter(currencyId)") {
                form.elements[i].options.selectedIndex=0;
            } else if (form.elements[i].type == "select-one") {
                form.elements[i].value = "";
            }
        }
    }
</script>


<c:set var="invoiceTypeList" value="${app2:getInvoiceTypes(pageContext.request)}"/>
<br>
<table width="95%" border="0" align="center" cellpadding="3" cellspacing="0" class="searchContainer">
<html:form action="/IncomingInvoice/AdvancedSearch.do" focus="parameter(invoiceNumber)">
<tr>
    <td height="20" class="title" colspan="4">
        <fmt:message key="${pagetitle}"/>
    </td>
</tr>
<tr>
    <td class="label" width="15%"><fmt:message key="Finance.incomingInvoice.invoiceNumber"/></td>
    <td class="contain" width="35%">
        <app:text property="parameter(invoiceNumber)" styleClass="mediumText"tabindex="1" maxlength="30"/>
    </td>
    <!--*******************COLUMN DIVISION************************-->
    <td class="label" width="15%"><fmt:message key="Finance.incomingInvoice.type"/></td>
    <td class="contain" width="35%">
        <html:select property="parameter(type)"
                     styleClass="middleSelect"
                     tabindex="9">
            <html:option value=""/>
            <html:options collection="invoiceTypeList" property="value" labelProperty="label"/>
        </html:select>
    </td>
</tr>

<tr>
    <td class="label" width="15%"><fmt:message key="Finance.incomingInvoice.supplierName"/></td>
    <td class="contain" width="35%">
        <app:text property="parameter(supplierName1@_supplierName2@_supplierSearchName)" styleId="fieldAddressName_id" tabindex="2"
                 styleClass="mediumText" maxlength="50"/>
    </td>

    <!--*******************COLUMN DIVISION************************-->
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
                      tabIndex="10">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
</tr>
<tr>
    <td class="label"><fmt:message key="Finance.incomingInvoice.amountNet"/></td>
    <td class="contain">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:numberText property="parameter(amountNet1)" styleClass="numberText" tabindex="3" maxlength="12"
                        numberType="decimal" maxInt="10" maxFloat="2"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:numberText property="parameter(amountNet2)" styleClass="numberText" tabindex="4"
                        maxlength="12" numberType="decimal" maxInt="10" maxFloat="2"/>
   </td>
    <!--*******************COLUMN DIVISION************************-->
    <td class="label">
        <fmt:message key="Finance.incomingInvoice.invoiceDate"/>
    </td>
    <td class="contain">
        <fmt:message key="datePattern" var="datePattern"/>
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:dateText property="parameter(startInvoiceDate)" maxlength="10" tabindex="11" styleId="startInvoiceDate"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:dateText property="parameter(endInvoiceDate)" maxlength="10" tabindex="12" styleId="endInvoiceDate"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
    </td>
</tr>
<tr>

   <td class="label"><fmt:message key="Finance.incomingInvoice.amountGross"/></td>
    <td class="contain">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:numberText property="parameter(amountGross1)" styleClass="numberText" tabindex="5" maxlength="12"
                        numberType="decimal" maxInt="10" maxFloat="2"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:numberText property="parameter(amountGross2)" styleClass="numberText" tabindex="6"
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
        <app:dateText property="parameter(startReceiptDate)" maxlength="10" tabindex="13" styleId="startPayDate"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:dateText property="parameter(endReceiptDate)" maxlength="10" tabindex="14" styleId="endPayDate"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
    </td>
</tr>
<tr>
   <td class="label"><fmt:message key="Finance.incomingInvoice.openAmount"/></td>
   <td class="contain">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:numberText property="parameter(openAmount1)" styleClass="numberText" tabindex="7" maxlength="12"
                        numberType="decimal" maxInt="10" maxFloat="2"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:numberText property="parameter(openAmount2)" styleClass="numberText" tabindex="8"
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
        <app:dateText property="parameter(startPaidUntilDate)" maxlength="10" tabindex="15" styleId="startPaidUntilDate"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:dateText property="parameter(endPaidUntilDate)" maxlength="10" tabindex="16" styleId="endPaidUntilDate"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
    </td>

</tr>
<TR>
    <td colspan="4" align="center" class="alpha">
        <fanta:alphabet action="IncomingInvoice/AdvancedSearch.do" parameterName="invoiceNumber"/>
    </td>
</TR>
<tr>
    <td colspan="4" class="button">
        <html:submit styleClass="button" tabindex="31"><fmt:message key="Common.go"/></html:submit>
        <html:button property="reset1" tabindex="32" styleClass="button" onclick="myReset()">
            <fmt:message key="Common.clear"/>
        </html:button>
    </td>
</tr>

</html:form>
</table>

<%--****************************FANTABULOS LIST TABLE***********************--%>

<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="container">
    <tr>
        <td>
            <app2:checkAccessRight functionality="INCOMINGINVOICE" permission="CREATE">
                <table width="100%" border="0" align="center" cellpadding="2" cellspacing="0">
                    <tr>
                        <html:form action="/IncomingInvoice/Forward/Create.do">
                            <td class="button">
                                <html:submit styleClass="button">
                                    <fmt:message key="Common.new"/>
                                </html:submit>
                            </td>
                        </html:form>
                    </tr>
                </table>
            </app2:checkAccessRight>
        </td>
    </tr>
    <tr>
        <td>
            <fanta:table list="incomingInvoiceAdvancedList" width="100%" id="incomingInvoice" action="IncomingInvoice/AdvancedSearch.do"
                         imgPath="${baselayout}" align="center">
                <app:url value="/IncomingInvoice/Forward/Update.do?dto(incomingInvoiceId)=${incomingInvoice.INCOMINGINVOICEID}&dto(op)=read&incomingInvoiceId=${incomingInvoice.INCOMINGINVOICEID}&tabKey=Finance.IncomingInvoice.detail"
                         var="urlUpdate" enableEncodeURL="false"/>
                <app:url value="/IncomingInvoice/Forward/Delete.do?dto(incomingInvoiceId)=${incomingInvoice.INCOMINGINVOICEID}&dto(op)=read&incomingInvoiceId=${incomingInvoice.INCOMINGINVOICEID}&tabKey=Finance.IncomingInvoice.detail"
                         var="urlDelete" enableEncodeURL="false"/>

                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <app2:checkAccessRight functionality="INCOMINGINVOICE" permission="VIEW">
                        <fanta:actionColumn name="edit" title="Common.update" action="${urlUpdate}"
                                            styleClass="listItem" headerStyle="listHeader"
                                            image="${baselayout}/img/edit.gif"/>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="INCOMINGINVOICE" permission="DELETE">
                        <fanta:actionColumn name="delete" title="Common.delete" action="${urlDelete}"
                                            styleClass="listItem" headerStyle="listHeader"
                                            image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>
                </fanta:columnGroup>
                <fanta:dataColumn name="INVOICENUMBER" styleClass="listItem"
                                  action="${urlUpdate}"
                                  title="Finance.incomingInvoice.invoiceNumber" headerStyle="listHeader" width="14%"
                                  orderable="true" maxLength="40"/>

                <fanta:dataColumn name="TYPE" styleClass="listItem"
                                  title="Finance.incomingInvoice.type" headerStyle="listHeader" width="5%"
                                  orderable="true"  renderData="false">
                   <c:choose>

                       <c:when test="${incomingInvoice.TYPE==INVOICE_TYPE}">
                           <fmt:message key="Invoice.type.invoice.abbr"/>
                       </c:when>
                       <c:otherwise>
                           <fmt:message key="Invoice.type.creditNote.abbr"/>
                       </c:otherwise>
                   </c:choose>
                </fanta:dataColumn>

                <fanta:dataColumn name="INVOICEDATE" styleClass="listItem"
                                  action="${urlUpdate}"
                                  title="Finance.incomingInvoice.invoiceDate" headerStyle="listHeader" width="10%" orderable="true"
                                  renderData="false">
                   <fmt:formatDate var="invoiceDateString" value="${app2:intToDate(incomingInvoice.INVOICEDATE)}"
                                    pattern="${datePattern}"/>
                    ${invoiceDateString}
                </fanta:dataColumn>

                <fanta:dataColumn name="RECEIPTDATE" styleClass="listItem"
                                  action="${urlUpdate}"
                                  title="Finance.incomingInvoice.receiptDate" headerStyle="listHeader" width="11%" orderable="true"
                                  renderData="false">
                   <fmt:formatDate var="receiptDateString" value="${app2:intToDate(incomingInvoice.RECEIPTDATE)}"
                                    pattern="${datePattern}"/>
                    ${receiptDateString}
                </fanta:dataColumn>

                <fanta:dataColumn name="SUPPLIERNAME" styleClass="listItem"
                                  title="Finance.incomingInvoice.supplierName" headerStyle="listHeader" width="15%"
                                  orderable="true" />

                <fanta:dataColumn name="CURRENCYLABEL" styleClass="listItem" title="Finance.incomingInvoice.currency"
                                  headerStyle="listHeader"
                                  width="10%"
                                  orderable="true"/>

                <fanta:dataColumn name="AMOUNTNET" styleClass="listItemRight"
                                  title="Finance.incomingInvoice.amountNet" headerStyle="listHeader" width="10%"
                                  orderable="true" renderData="false">
                   <fmt:formatNumber var="amountNetNumber" value="${incomingInvoice.AMOUNTNET}" type="number" pattern="${numberFormat}"/>
                   ${amountNetNumber}
                </fanta:dataColumn>

                <fanta:dataColumn name="AMOUNTGROSS" styleClass="listItemRight"
                                  title="Finance.incomingInvoice.amountGross" headerStyle="listHeader" width="10%"
                                  orderable="true" renderData="false">
                   <fmt:formatNumber var="amountGrossNumber" value="${incomingInvoice.AMOUNTGROSS}" type="number" pattern="${numberFormat}"/>
                   ${amountGrossNumber}
                </fanta:dataColumn>

                <fanta:dataColumn name="OPENAMOUNT" styleClass="listItem2Right"
                                  title="Finance.incomingInvoice.openAmount" headerStyle="listHeader" width="10%"
                                  orderable="true" renderData="false">
                   <fmt:formatNumber var="openAmountNumber" value="${incomingInvoice.OPENAMOUNT}" type="number" pattern="${numberFormat}"/>
                   ${openAmountNumber}
                </fanta:dataColumn>
            </fanta:table>
        </td>
    </tr>
    <tr>
        <td>
            <app2:checkAccessRight functionality="INCOMINGINVOICE" permission="CREATE">
                <table width="100%" border="0" align="center" cellpadding="2" cellspacing="0">
                    <tr>
                        <html:form action="/IncomingInvoice/Forward/Create.do">
                            <td class="button">
                                <html:submit styleClass="button"><fmt:message key="Common.new"/></html:submit>
                            </td>
                        </html:form>
                    </tr>
                </table>
            </app2:checkAccessRight>
        </td>
    </tr>
</table>