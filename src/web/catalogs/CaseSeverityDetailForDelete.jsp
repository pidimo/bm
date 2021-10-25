<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.config.support.caseSeverity.delete" scope="request"/>

<fmt:message var="title" key="CaseSeverity.Title.delete" scope="request"/>
<fmt:message var="button" key="Common.delete" scope="request"/>
<c:set var="action" value="/Support/CaseSeverity/Delete" scope="request"/>
<c:set var="op" value="delete" scope="request"/>

<c:set var="pagetitle" value="Common.configuration" scope="request"/>
<c:set var="windowTitle" value="CaseSeverity.Title.delete" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/CatalogTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/catalogs/CaseSeverity.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/catalogs/CaseSeverity.jsp" scope="request"/>
        <c:set var="tabs" value="/CatalogTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>