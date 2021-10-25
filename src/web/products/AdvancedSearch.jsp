<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:remove var="index" scope="session"/>
<c:set var="windowTitle" value="Product.Title.AdvancedSearch" scope="request"/>
<c:set var="pagetitle" value="Product.plural" scope="request"/>

<c:set var="helpResourceKey" value="help.product.advancedList" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/products/AdvancedSearch.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/products/AdvancedSearch.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>