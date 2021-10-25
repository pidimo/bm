<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.invoice.payment.list" scope="request"/>

<c:set var="windowTitle" value="InvoicePayment.Title.list" scope="request" />

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/InvoiceTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/finance/InvoicePaymentList.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/InvoiceTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/finance/InvoicePaymentList.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>