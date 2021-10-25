<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>

<c:set var="windowTitle" value="SalePosition.Title.list" scope="request" />

<c:set var="helpResourceKey" value="help.product.salePosition.list" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ProductTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/products/SalePositionList.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/ProductTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/products/SalePositionList.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>