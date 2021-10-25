<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>

<fmt:message var="title" key="Product.Title.edit" scope="request"/>
<fmt:message var="button" key="Common.save" scope="request"/>
<c:set var="action" value="/Supplier/Search" scope="request"/>
<c:set var="op" value="list" scope="request"/>
<c:set var="windowTitle" value="ProductSupplier.Title" scope="request"/>
<c:set var="pagetitle" value="Product.plural" scope="request"/>

<c:set var="helpResourceKey" value="help.product.supplier.list" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/products/SupplierList.jsp" scope="request"/>
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ProductTabs.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/ProductTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/products/SupplierList.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>