<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.invoice.advancedList" scope="request"/>

<c:set var="windowTitle" value="Invoice.Title.advancedSearch" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/finance/InvoiceAdvancedList.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/finance/InvoiceAdvancedList.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>