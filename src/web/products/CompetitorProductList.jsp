<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<c:set var="pagetitle" value="Product.plural" scope="request"/>
<c:set var="windowTitle" value="CompetitorProduct.list" scope="request" />

<c:set var="helpResourceKey" value="help.product.competitor.list" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ProductTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/products/CompetitorProductList.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/ProductTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/products/CompetitorProductList.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>