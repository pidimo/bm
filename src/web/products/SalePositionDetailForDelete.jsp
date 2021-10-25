<%@ include file="/Includes.jsp" %>

<fmt:message var="title" key="SalePosition.Title.delete" scope="request"/>
<fmt:message var="button" key="Common.delete" scope="request"/>

<c:set var="action" value="/SalePosition/Delete" scope="request"/>
<c:set var="op" value="delete" scope="request"/>

<c:set var="windowTitle" value="SalePosition.Title.delete" scope="request"/>

<c:set var="helpResourceKey" value="help.sale.salePosition.delete" scope="request"/>

<!--Enable Sale link-->
<c:set var="showSaleLink" value="${true}" scope="request"/>


<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ProductTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/products/SalePosition.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/ProductTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/products/SalePosition.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>