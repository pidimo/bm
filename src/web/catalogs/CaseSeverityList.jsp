<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.config.support.caseSeverity.list" scope="request"/>

<c:set var="pagetitle" value="Common.configuration" scope="request"/>
<c:set var="windowTitle" value="CaseSeverity.plural" scope="request"/>
<c:set var="delete" value="Support/CaseSeverity/Forward/Delete.do" scope="request"/>
<c:set var="edit" value="Support/CaseSeverity/Forward/Update.do" scope="request"/>
<c:set var="create" value="Support/CaseSeverity/Forward/Create.do" scope="request"/>
<c:set var="action" value="Support/CaseSeverityList.do" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/CatalogTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/catalogs/CaseSeverityList.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/catalogs/CaseSeverityList.jsp" scope="request"/>
        <c:set var="tabs" value="/CatalogTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>