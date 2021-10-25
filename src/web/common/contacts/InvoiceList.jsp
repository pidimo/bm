<%@ page import="com.piramide.elwis.utils.FinanceConstants" %>
<%@ include file="/Includes.jsp" %>

<c:set var="creditNoteType" value="<%=FinanceConstants.InvoiceType.CreditNote.getConstantAsString()%>"/>
<c:set var="invoiceType" value="<%=FinanceConstants.InvoiceType.Invoice.getConstantAsString()%>"/>
<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>

<table width="95%" border="0" align="center" cellpadding="3" cellspacing="0" class="searchContainer">
    <tr>
        <td class="title" colspan="2">
            <fmt:message key="Invoice.singleSearch"/>
        </td>
    </tr>
    <TR>
        <html:form action="/Invoice/SingleList.do"
                   focus="parameter(number)">
            <td class="label"><fmt:message key="Common.search"/></td>
            <td align="left" class="contain">
                <html:text
                        property="parameter(number)"
                        styleClass="largeText" maxlength="80"/>
                &nbsp;
                <html:submit styleClass="button">
                    <fmt:message key="Common.go"/>
                </html:submit>
            </td>
        </html:form>
    </TR>
    <tr>
        <td colspan="2" align="center" class="alpha">
            <fanta:alphabet action="/Invoice/SingleList.do" parameterName="numberAlpha"/>
        </td>
    </tr>
</table>
<br/>

<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td>
            <app2:checkAccessRight functionality="INVOICE" permission="CREATE">
                <table width="100%" border="0" align="center" cellpadding="2" cellspacing="0">
                    <tr>
                        <html:form action="/Invoice/Forward/Create.do">
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
            <fanta:table list="invoiceSingleForContactList"
                         width="100%"
                         id="invoice"
                         action="Invoice/SingleList.do"
                         imgPath="${baselayout}"
                         align="center">

                <c:set var="editLink"
                       value="Invoice/Forward/Update.do?invoiceId=${invoice.invoiceId}&dto(invoiceId)=${invoice.invoiceId}&dto(number)=${app2:encode(invoice.number)}"/>
                <c:set var="deleteLink"
                       value="Invoice/Forward/Delete.do?invoiceId=${invoice.invoiceId}&dto(invoiceId)=${invoice.invoiceId}&dto(number)=${app2:encode(invoice.number)}&dto(withReferences)=true"/>

                <c:set var="downloadAction"
                       value="/finance/Download/Invoice/Document.do?dto(freeTextId)=${invoice.documentId}&dto(invoiceId)=${invoice.invoiceId}"/>

                <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                    <app2:checkAccessRight functionality="INVOICE" permission="VIEW">
                        <fanta:actionColumn name="edit" title="Common.update"
                                            action="${editLink}"
                                            styleClass="listItem"
                                            headerStyle="listHeader" width="33%" image="${baselayout}/img/edit.gif"/>
                    </app2:checkAccessRight>
                    <app2:checkAccessRight functionality="INVOICE" permission="DELETE">
                        <fanta:actionColumn name="delete" title="Common.delete"
                                            action="${deleteLink}"
                                            styleClass="listItem" headerStyle="listHeader" width="33%"
                                            image="${baselayout}/img/delete.gif"/>
                    </app2:checkAccessRight>

                    <fanta:actionColumn name="download" title="Common.download"
                                        styleClass="listItem" headerStyle="listHeader" width="34%"
                                        render="false">
                        <c:choose>
                            <c:when test="${not empty invoice.documentId}">
                                <app:link action="${downloadAction}" contextRelative="true">
                                    <html:img src="${baselayout}/img/openfile.png" titleKey="Common.download"
                                              border="0"/>
                                </app:link>
                            </c:when>
                            <c:otherwise>
                                &nbsp;
                            </c:otherwise>
                        </c:choose>
                    </fanta:actionColumn>

                </fanta:columnGroup>
                <fanta:dataColumn name="number" action="${editLink}" styleClass="listItem" title="Invoice.number"
                                  headerStyle="listHeader" width="14%" orderable="true" maxLength="40"/>
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
                                  headerStyle="listHeader" width="10%" orderable="true" renderData="false">
                    <fmt:formatDate var="paymentDateValue" value="${app2:intToDate(invoice.paymentDate)}"
                                    pattern="${datePattern}"/>
                    ${paymentDateValue}
                </fanta:dataColumn>
                <fanta:dataColumn name="contactPersonName" styleClass="listItem" title="Invoice.contactPerson"
                                  headerStyle="listHeader" width="20%" orderable="true"/>
                <fanta:dataColumn name="currencyName" styleClass="listItem" title="Invoice.currency"
                                  headerStyle="listHeader"
                                  width="10%" orderable="true"/>
                <fanta:dataColumn name="totalAmountNet" styleClass="listItemRight" title="Invoice.totalAmountNet"
                                  headerStyle="listHeader" width="12%" orderable="true" renderData="false">
                    <fmt:formatNumber var="totalAmountNet" value="${invoice.totalAmountNet}" type="number"
                                      pattern="${numberFormat}"/>
                    ${totalAmountNet}
                </fanta:dataColumn>
                <fanta:dataColumn name="totalAmountGross" styleClass="listItemRight" title="Invoice.totalAmountGross"
                                  headerStyle="listHeader" width="12%" orderable="true" renderData="false">
                    <fmt:formatNumber var="totalAmountGross" value="${invoice.totalAmountGross}" type="number"
                                      pattern="${numberFormat}"/>
                    ${totalAmountGross}
                </fanta:dataColumn>
                <fanta:dataColumn name="openAmount" styleClass="listItem2Right" title="Invoice.openAmount"
                                  headerStyle="listHeader" width="12%" orderable="true" renderData="false">
                    <fmt:formatNumber var="openAmount" value="${invoice.openAmount}" type="number"
                                      pattern="${numberFormat}"/>
                    ${openAmount}
                </fanta:dataColumn>
            </fanta:table>

        </td>
    </tr>
    <tr>
        <td>
            <app2:checkAccessRight functionality="INVOICE" permission="CREATE">
                <table width="100%" border="0" align="center" cellpadding="2" cellspacing="0">
                    <tr>
                        <html:form action="/Invoice/Forward/Create.do">
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