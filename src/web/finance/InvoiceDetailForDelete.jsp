<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.invoice.delete" scope="request"/>

<fmt:message var="title" key="Invoice.Title.delete" scope="request"/>
<fmt:message var="button" key="Common.delete" scope="request"/>

<c:set var="action" value="/Invoice/Delete" scope="request"/>
<c:set var="op" value="delete" scope="request"/>
<c:set var="windowTitle" value="Invoice.Title.delete" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/InvoiceTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/finance/Invoice.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/InvoiceTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/finance/Invoice.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>