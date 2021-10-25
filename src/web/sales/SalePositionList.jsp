<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.sale.salePosition.list" scope="request"/>

<c:set var="windowTitle" value="SalePosition.Title.list" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/SaleTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/sales/SalePositionList.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/SaleTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/sales/SalePositionList.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>