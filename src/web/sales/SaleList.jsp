<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.sale.list" scope="request"/>

<c:set var="windowTitle" value="Sale.Title.singleSearch" scope="request"/>


<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/sales/SaleList.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/sales/SaleList.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>