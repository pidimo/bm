<%@ include file="/Includes.jsp" %>
<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<c:set var="invoiceVats" value="${app2:getInvoiceVats(invoiceId, pageContext.request)}"/>
<c:if test="${!empty invoiceVats}">
    <table cellpadding="0" cellspacing="0" border="0" width="100%">

        <tr>
            <td class="title" colspan="3">
                <fmt:message key="Invoice.vatTable"/>
            </td>
        </tr>
        <c:forEach var="invoiceVat" items="${invoiceVats}">
            <fmt:formatNumber var="percentValue"
                              value="${invoiceVat.vatRate}"
                              type="number"
                              pattern="${numberFormat}"/>
            <fmt:formatNumber var="newValue"
                              value="${invoiceVat.value}"
                              type="number"
                              pattern="${numberFormat}"/>
            <fmt:formatNumber var="amountValue"
                              value="${invoiceVat.amount}"
                              type="number"
                              pattern="${numberFormat}"/>
            <tr>
                <td class="contain" width="30%">
                    <c:out value="${invoiceVat.label}"/>
                    (
                        ${percentValue}%
                    )
                </td>
                <td class="contain" width="40%">
                        ${newValue}
                </td>
                <td class="contain" align="right" width="30%">
                        ${percentValue}%
                    <fmt:message key="Invoice.vatTable.on"/>
                        ${amountValue}
                </td>
            </tr>
        </c:forEach>
    </table>
</c:if>
