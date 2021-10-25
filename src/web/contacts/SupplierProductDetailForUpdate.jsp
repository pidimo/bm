<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<c:set var="helpResourceKey" value="help.contact.supplierProduct.edit" scope="request"/>

<fmt:message var="title" key="Product.Title.edit" scope="request"/>
<fmt:message var="button" key="Common.save" scope="request"/>
<c:set var="action" value="/SupplierProduct/Update" scope="request"/>
<c:set var="op" value="update" scope="request"/>
<c:set var="windowTitle" value="Supplier.Title.edit" scope="request"/>
<c:set var="pagetitle" value="Supplier.plural" scope="request"/>
<c:set var="isProduct" value="${false}" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ContactTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/contacts/SupplierProduct.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/contacts/SupplierProduct.jsp" scope="request"/>
        <c:set var="tabs" value="/ContactTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>

