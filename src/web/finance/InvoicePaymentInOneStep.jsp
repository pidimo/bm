<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.invoicePayment.oneStep" scope="request"/>

<fmt:message var="title" key="InvoicePayment.Title.create" scope="request"/>
<fmt:message var="button" key="Common.save" scope="request"/>

<c:set var="action" value="/InvoicePayment/CreateInOneStep.do" scope="request"/>
<c:set var="op" value="create" scope="request"/>
<c:set var="windowTitle" value="InvoicePayment.Title.create" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/finance/InvoicePaymentInOneStep.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/finance/InvoicePaymentInOneStep.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>