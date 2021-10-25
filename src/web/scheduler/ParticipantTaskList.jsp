<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="helpResourceKey" value="help.task.participant.list" scope="request"/>

<c:set var="windowTitle" value="Scheduler.Task.users" scope="request"/>
<c:set var="pagetitle" value="Task.participant.taskList" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/TaskTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/scheduler/ParticipantTaskList.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/TaskTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/scheduler/ParticipantTaskList.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>