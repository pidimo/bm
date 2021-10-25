<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.projectTimes.list" scope="request"/>

<c:set var="windowTitle" value="Project.Title.search" scope="request"/>


<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/projects/ProjectTimeRegistrationList.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/projects/ProjectTimeRegistrationList.jsp" scope="request"/>
        <c:import url="/layout/ui/main.jsp"/>
    </c:otherwise>
</c:choose>