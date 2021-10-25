<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>


<fmt:message  var="title" key="Product.new" scope="request"/>
<fmt:message  var="button" key="Common.save" scope="request"/>
<c:set var="action" value="/Product/Create" scope="request"/>
<c:set var="op" value="create" scope="request"/>
<c:set var="windowTitle" value="Product.new" scope="request"/>
<c:set var="pagetitle" value="Product.plural" scope="request"/>

<c:set var="helpResourceKey" value="help.product.create" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/products/Product.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/products/Product.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>

