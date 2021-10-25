<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.salesProcess.advancedList" scope="request"/>

<c:set var="windowTitle" value="SalesProcess.Title.simpleSearch" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/sales/SalesProcessAdvancedList.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/sales/SalesProcessAdvancedList.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>
