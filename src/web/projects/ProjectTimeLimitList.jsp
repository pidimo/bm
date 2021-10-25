<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.project.timeLimit.list" scope="request"/>

<c:set var="windowTitle" value="ProjectTimeLimit.title.list" scope="request" />


<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ProjectTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/projects/ProjectTimeLimitList.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/ProjectTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/projects/ProjectTimeLimitList.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>