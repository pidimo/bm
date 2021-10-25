<%@ page import="com.piramide.elwis.utils.FinanceConstants" %>
<%@ include file="/Includes.jsp" %>

<c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
<fmt:message var="datePattern" key="datePattern"/>
<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>

<c:set var="INVOICE_TYPE" value="<%=FinanceConstants.InvoiceType.Invoice.getConstantAsString()%>"/>

<table width="${listTableWidth}" border="0" align="center" cellpadding="3" cellspacing="0" class="searchContainer">
    <tr>
        <td class="title" colspan="2">
            <fmt:message key="${pagetitle}"/>
        </td>
    </tr>
    <TR>
        <html:form action="/IncomingInvoice/List.do" focus="parameter(invoiceNumber@_supplierName1@_supplierName2)">
            <td class="label"><fmt:message key="Common.search"/></td>
            <td align="left" class="contain">
                <html:text
                        property="parameter(invoiceNumber@_supplierName1@_supplierName2)"
                        styleClass="largeText" maxlength="80"/>
                &nbsp;
                <html:submit styleClass="button">
                    <fmt:message key="Common.go"/>
                </html:submit>&nbsp;
                <c:if test="${fromFinance}"><app:link
                        action="/IncomingInvoice/AdvancedSearch.do?advancedListForward=IncomingInvoiceAdvancedSearch">&nbsp;<fmt:message
                        key="Common.advancedSearch"/></app:link></c:if>
            </td>
        </html:form>
    </TR>
    <tr>
        <td colspan="2" align="center" class="alpha">
            <fanta:alphabet action="/IncomingInvoice/List.do" parameterName="invoiceNumber"/>
        </td>
    </tr>
</table>
<br/>

<table width="${listTableWidth}" border="0" align="center" cellpadding="0" cellspacing="0">
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
            <fanta:table list="incomingInvoiceList" width="100%" id="incomingInvoice" action="/IncomingInvoice/List.do"
                         imgPath="${baselayout}" align="center">
                <c:set value="/IncomingInvoice/Forward/Update.do?dto(incomingInvoiceId)=${incomingInvoice.INCOMINGINVOICEID}&dto(op)=read&incomingInvoiceId=${incomingInvoice.INCOMINGINVOICEID}"
                       var="urlUpdate"/>
                <c:set value="/IncomingInvoice/Forward/Delete.do?dto(incomingInvoiceId)=${incomingInvoice.INCOMINGINVOICEID}&dto(op)=read&incomingInvoiceId=${incomingInvoice.INCOMINGINVOICEID}"
                       var="urlDelete"/>

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
                                  orderable="true" renderData="false">
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
                                  title="Finance.incomingInvoice.invoiceDate" headerStyle="listHeader" width="10%"
                                  orderable="true"
                                  renderData="false">
                    <fmt:formatDate var="invoiceDateString" value="${app2:intToDate(incomingInvoice.INVOICEDATE)}"
                                    pattern="${datePattern}"/>
                    ${invoiceDateString}
                </fanta:dataColumn>

                <fanta:dataColumn name="RECEIPTDATE" styleClass="listItem"
                                  action="${urlUpdate}"
                                  title="Finance.incomingInvoice.receiptDate" headerStyle="listHeader" width="11%"
                                  orderable="true"
                                  renderData="false">
                    <fmt:formatDate var="receiptDateString" value="${app2:intToDate(incomingInvoice.RECEIPTDATE)}"
                                    pattern="${datePattern}"/>
                    ${receiptDateString}
                </fanta:dataColumn>

                <fanta:dataColumn name="SUPPLIERNAME" styleClass="listItem"
                                  title="Finance.incomingInvoice.supplierName" headerStyle="listHeader" width="15%"
                                  orderable="true"/>

                <fanta:dataColumn name="CURRENCYLABEL" styleClass="listItem" title="Finance.incomingInvoice.currency"
                                  headerStyle="listHeader"
                                  width="10%"
                                  orderable="true"/>

                <fanta:dataColumn name="AMOUNTNET" styleClass="listItemRight"
                                  title="Finance.incomingInvoice.amountNet" headerStyle="listHeader" width="10%"
                                  orderable="true" renderData="false">
                    <fmt:formatNumber var="amountNetNumber" value="${incomingInvoice.AMOUNTNET}" type="number"
                                      pattern="${numberFormat}"/>
                    ${amountNetNumber}
                </fanta:dataColumn>

                <fanta:dataColumn name="AMOUNTGROSS" styleClass="listItemRight"
                                  title="Finance.incomingInvoice.amountGross" headerStyle="listHeader" width="10%"
                                  orderable="true" renderData="false">
                    <fmt:formatNumber var="amountGrossNumber" value="${incomingInvoice.AMOUNTGROSS}" type="number"
                                      pattern="${numberFormat}"/>
                    ${amountGrossNumber}
                </fanta:dataColumn>

                <fanta:dataColumn name="OPENAMOUNT" styleClass="listItem2Right"
                                  title="Finance.incomingInvoice.openAmount" headerStyle="listHeader" width="10%"
                                  orderable="true" renderData="false">
                    <fmt:formatNumber var="openAmountNumber" value="${incomingInvoice.OPENAMOUNT}" type="number"
                                      pattern="${numberFormat}"/>
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