<%@ page import="com.piramide.elwis.utils.FinanceConstants" %>
<%@ include file="/Includes.jsp" %>


<c:set var="creditNoteType" value="<%=FinanceConstants.InvoiceType.CreditNote.getConstantAsString()%>"/>
<c:set var="invoiceType" value="<%=FinanceConstants.InvoiceType.Invoice.getConstantAsString()%>"/>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>

<br/>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">

    <tr>
        <td>
            <fanta:table list="saleInvoicedDocumentList"
                         width="100%"
                         id="invoice"
                         action="Sale/Invoiced/List.do"
                         imgPath="${baselayout}"
                         align="center">

                <c:set var="editLink"
                       value="/finance/Invoice/Forward/Update.do?invoiceId=${invoice.invoiceId}&dto(invoiceId)=${invoice.invoiceId}&dto(number)=${app2:encode(invoice.number)}&index=0"/>
                <c:set var="downloadAction"
                       value="/finance/Download/Invoice/Document.do?dto(freeTextId)=${invoice.documentId}&dto(invoiceId)=${invoice.invoiceId}"/>

                <c:set var="deleteLink"
                       value="Sale/Invoice/Forward/Delete.do?invoiceId=${invoice.invoiceId}&dto(invoiceId)=${invoice.invoiceId}&dto(number)=${app2:encode(invoice.number)}&dto(withReferences)=true"/>


                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <app2:checkAccessRight functionality="INVOICE" permission="VIEW">
                        <fanta:actionColumn name="edit" title="Common.update"
                                            styleClass="listItem"
                                            headerStyle="listHeader" width="33%" render="false">
                            <app:link action="${editLink}" contextRelative="true">
                                <html:img src="${baselayout}/img/edit.gif" titleKey="Common.update" border="0"/>
                            </app:link>
                        </fanta:actionColumn>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="INVOICE" permission="DELETE">
                        <fanta:actionColumn name="delete" title="Common.delete"
                                            action="${deleteLink}"
                                            styleClass="listItem" headerStyle="listHeader" width="33%"
                                            image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>


                    <fanta:actionColumn name="download" title="Common.download"
                                        styleClass="listItem"
                                        headerStyle="listHeader"
                                        width="34%"
                                        render="false">
                        <c:if test="${not empty invoice.documentId}">
                            <app:link action="${downloadAction}" contextRelative="true">
                                <html:img src="${baselayout}/img/openfile.png" titleKey="Common.download" border="0"/>
                            </app:link>
                        </c:if>

                    </fanta:actionColumn>

                </fanta:columnGroup>
                <fanta:dataColumn name="number" styleClass="listItem" title="Invoice.number"
                                  headerStyle="listHeader"
                                  width="14%"
                                  orderable="true"
                                  maxLength="40" renderData="false">
                    <app:link action="${editLink}" contextRelative="true">
                        ${invoice.number}
                    </app:link>
                </fanta:dataColumn>
                <fanta:dataColumn name="type" styleClass="listItem" title="Invoice.type"
                                  headerStyle="listHeader" orderable="true" renderData="false" width="5%">
                    <c:if test="${invoice.type == invoiceType}">
                        <fmt:message key="Invoice.type.invoice.abbr"/>
                    </c:if>
                    <c:if test="${invoice.type == creditNoteType}">
                        <fmt:message key="Invoice.type.creditNote.abbr"/>
                    </c:if>
                </fanta:dataColumn>
                <fanta:dataColumn name="invoiceDate" styleClass="listItem" title="Invoice.invoiceDate"
                                  headerStyle="listHeader" width="10%" orderable="true" renderData="false">
                    <fmt:formatDate var="dateValue" value="${app2:intToDate(invoice.invoiceDate)}"
                                    pattern="${datePattern}"/>
                    ${dateValue}
                </fanta:dataColumn>
                <fanta:dataColumn name="paymentDate" styleClass="listItem" title="Invoice.paymentDate"
                                  headerStyle="listHeader" width="11%" orderable="true" renderData="false">
                    <fmt:formatDate var="paymentDateValue" value="${app2:intToDate(invoice.paymentDate)}"
                                    pattern="${datePattern}"/>
                    ${paymentDateValue}
                </fanta:dataColumn>
                <fanta:dataColumn name="addressName" styleClass="listItem" title="Invoice.contact"
                                  headerStyle="listHeader"
                                  width="15%"
                                  orderable="true"/>
                <fanta:dataColumn name="currencyName" styleClass="listItem" title="Invoice.currency"
                                  headerStyle="listHeader"
                                  width="10%"
                                  orderable="true"/>
                <fanta:dataColumn name="totalAmountNet" styleClass="listItemRight" title="Invoice.totalAmountNet"
                                  headerStyle="listHeader" width="10%" orderable="true" renderData="false">
                    <fmt:formatNumber var="totalAmountNet" value="${invoice.totalAmountNet}" type="number"
                                      pattern="${numberFormat}"/>
                    ${totalAmountNet}
                </fanta:dataColumn>
                <fanta:dataColumn name="totalAmountGross" styleClass="listItemRight" title="Invoice.totalAmountGross"
                                  headerStyle="listHeader" width="10%" orderable="true" renderData="false">
                    <fmt:formatNumber var="totalAmountGross" value="${invoice.totalAmountGross}" type="number"
                                      pattern="${numberFormat}"/>
                    ${totalAmountGross}
                </fanta:dataColumn>
                <fanta:dataColumn name="openAmount" styleClass="listItem2Right" title="Invoice.openAmount"
                                  headerStyle="listHeader" width="10%" orderable="true" renderData="false">
                    <fmt:formatNumber var="openAmount" value="${invoice.openAmount}" type="number"
                                      pattern="${numberFormat}"/>
                    ${openAmount}
                </fanta:dataColumn>
            </fanta:table>

        </td>
    </tr>
</table>