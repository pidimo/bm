<%@ include file="/Includes.jsp" %>
<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<c:set var="invoiceVats" value="${app2:getInvoiceVats(invoiceId, pageContext.request)}"/>

<c:if test="${!empty invoiceVats}">

    <fieldset>
        <legend class="title">
            <fmt:message key="Invoice.vatTable"/>
        </legend>
    </fieldset>
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
        <div class="row">
            <div class="col-xs-12 col-sm-4">
                <c:out value="${invoiceVat.label}"/>
                (
                    ${percentValue}%
                )
            </div>
            <div class="col-xs-12 col-sm-4">
                    ${newValue}
            </div>
            <div class="col-xs-12 col-sm-4">
                    ${percentValue}%
                <fmt:message key="Invoice.vatTable.on"/>
                    ${amountValue}
            </div>
        </div>
    </c:forEach>
</c:if>
