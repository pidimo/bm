<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.task.participant.edit" scope="request"/>

<fmt:message var="title" key="Task.participant.update" scope="request"/>
<fmt:message var="button" key="Common.save" scope="request"/>
<c:set var="action" value="/TaskParticipant/Update" scope="request"/>
<c:set var="op" value="update" scope="request"/>
<c:set var="windowTitle" value="Task.participant.update" scope="request"/>

<c:set var="pagetitle" value="Scheduler.Task.users" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/TaskTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/scheduler/TaskParticipant.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/TaskTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/scheduler/TaskParticipant.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>
