<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.config.sales.status.edit" scope="request"/>

<fmt:message var="title" key="Status.Title.update" scope="request"/>
<fmt:message var="button" key="Common.save" scope="request"/>

<c:set var="action" value="/Status/Update" scope="request"/>
<c:set var="op" value="update" scope="request"/>
<c:set var="windowTitle" value="Status.Title.update" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/CatalogTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/catalogs/Status.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/catalogs/Status.jsp" scope="request"/>
        <c:set var="tabs" value="/CatalogTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>

