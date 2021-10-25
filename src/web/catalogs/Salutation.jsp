<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>

<c:if test="${'create' == param.op}">
<fmt:message    var="title" key="Salutation.Title.create" scope="request"/>
<fmt:message   var="button" key="Common.save" scope="request"/>
<c:set var="action" value="/Salutation/Create" scope="request"/>
<c:set var="op" value="create" scope="request"/>
</c:if>

<c:if test="${'update' ==  param.op}">
<fmt:message   var="title" key="Salutation.Title.update" scope="request"/>
<fmt:message   var="button" key="Common.save" scope="request"/>
<c:set var="action" value="/Salutation/Update" scope="request"/>
<c:set var="op" value="update" scope="request"/>
</c:if>


<c:if test="${'delete' == param.op}">
<fmt:message   var="title" key="Salutation.Title.delete" scope="request"/>
<fmt:message   var="button" key="Common.delete" scope="request"/>
<c:set var="action" value="/Salutation/Delete" scope="request"/>
<c:set var="op" value="delete" scope="request"/>
</c:if>
<c:set var="pagetitle" value="Common.configuration" scope="request"/>


<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/catalogs/Salutation.jsp" scope="request"/>
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/CatalogTabs.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/catalogs/Salutation.jsp" scope="request"/>
        <c:set var="tabs" value="/CatalogTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>

