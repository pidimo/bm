<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.sale.salePosition.create" scope="request"/>

<fmt:message var="title" key="SalePosition.Title.create" scope="request"/>
<fmt:message var="button" key="Common.save" scope="request"/>

<c:set var="action" value="/SalesProcess/SalePosition/Create" scope="request"/>
<c:set var="op" value="create" scope="request"/>

<c:set var="windowTitle" value="SalePosition.Title.create" scope="request"/>
<%@ include file="/Includes.jsp" %>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/SalesProcessTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/sales/SalePositionBySalesProcess.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/SalesProcessTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/sales/SalePositionBySalesProcess.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>