<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.config.support.workLevel.list" scope="request"/>

<c:set var="pagetitle" value="Common.configuration" scope="request"/>
<c:set var="windowTitle" value="WorkingLevel.plural" scope="request"/>
<c:set var="delete" value="Support/WorkLevel/Forward/Delete.do" scope="request"/>
<c:set var="edit" value="Support/WorkLevel/Forward/Update.do" scope="request"/>
<c:set var="create" value="Support/WorkLevel/Forward/Create.do" scope="request"/>
<c:set var="action" value="Support/WorkLevelList.do" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/catalogs/WorkLevelList.jsp" scope="request"/>
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/CatalogTabs.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/catalogs/WorkLevelList.jsp" scope="request"/>
        <c:set var="tabs" value="/CatalogTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>

