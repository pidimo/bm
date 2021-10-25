<%@ include file="/Includes.jsp" %>

<c:if test="${empty(incomingInvoiceId)}"> <%--from create IncomingInvoice--%>
    <c:set var="incomingInvoiceId" value="${incomingInvoiceForm.dtoMap.incomingInvoiceId}" scope="request"/>
</c:if>

<c:set var="tabHeaderLabel" value="Finance.incomingInvoice" scope="request"/>
<c:set var="tabHeaderValue" value="${invoiceNumber}" scope="request"/>

<c:import url="${layout}/TabHeader.jsp"/>
<jsp:useBean id="tabItems" class="java.util.LinkedHashMap" scope="request"/>
    <app2:checkAccessRight functionality="INCOMINGINVOICE" permission="VIEW">
        <c:set target="${tabItems}" property="Finance.IncomingInvoice.detail"  value="/IncomingInvoice/Forward/Update.do?dto(incomingInvoiceId)=${incomingInvoiceId}&dto(op)=read&incomingInvoiceId=${incomingInvoiceId}"/>
    </app2:checkAccessRight>
    <app2:checkAccessRight functionality="INCOMINGPAYMENT" permission="VIEW">
        <c:set target="${tabItems}" property="Finance.IncomingInvoice.incomingPayments" value="/IncomingPayment/List.do?dto(incomingInvoiceId)=${incomingInvoiceId}&incomingInvoiceId=${incomingInvoiceId}"/>
    </app2:checkAccessRight>
<c:import url="${sessionScope.layout}/submenu.jsp"/>
