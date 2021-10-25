<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.incomingInvoice.advancedList" scope="request"/>

<c:set var="windowTitle" value="Finance.IncomingInvoice.Title.advancedSearch" scope="request"/>
<c:set var="pagetitle" value="Finance.IncomingInvoice.Title.advancedSearch" scope="request"/>
<c:set var="listTableWidth" value="99%" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/finance/IncomingInvoiceAdvancedSearch.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/finance/IncomingInvoiceAdvancedSearch.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>