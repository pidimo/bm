<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.project.activity.list" scope="request"/>

<c:set var="windowTitle" value="ProjectActivity.Title.list" scope="request" />

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ProjectTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/projects/ProjectActivityList.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/ProjectTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/projects/ProjectActivityList.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>