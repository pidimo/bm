<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.config.scheduler.taskType.list" scope="request"/>

<c:set var="pagetitle" value="Common.configuration" scope="request"/>
<c:set var="windowTitle" value="Task.taskType.plural" scope="request"/>
<c:set var="delete" value="TaskType/Forward/Delete.do" scope="request"/>
<c:set var="edit" value="TaskType/Forward/Update.do" scope="request"/>
<c:set var="create" value="TaskType/Forward/Create.do" scope="request"/>
<c:set var="action" value="TaskType/List.do" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/catalogs/TaskTypeList.jsp" scope="request"/>
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/CatalogTabs.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/catalogs/TaskTypeList.jsp" scope="request"/>
        <c:set var="tabs" value="/CatalogTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>

