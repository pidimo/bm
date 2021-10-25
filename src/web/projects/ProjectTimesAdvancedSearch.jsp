<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="helpResourceKey" value="help.projectTimes.advancedList" scope="request"/>

<c:set var="pagetitle" value="Project.ProjectTimesAdvancedSearch.title" scope="request"/>
<c:set var="windowTitle" value="Project.ProjectTimesAdvancedSearch.title" scope="request"/>

<c:set var="action" value="/Project/ProjectTimesAdvancedSearch.do" scope="request"/>


<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/projects/ProjectTimesAdvancedSearch.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/projects/ProjectTimesAdvancedSearch.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>