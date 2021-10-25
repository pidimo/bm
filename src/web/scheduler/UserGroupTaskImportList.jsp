<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="helpResourceKey" value="help.task.participant.addUserGroup" scope="request"/>

<c:set var="windowTitle" value="UserGroup.userGroupList" scope="request"/>
<c:set var="pagetitle" value="UserGroup.userGroupList" scope="request"/>

<c:set var="action" value="/TaskParticipant/Create.do" scope="request"/>
<c:set var="fantaAction" value="/TaskParticipant/Forward/Group/Create.do?dto(taskId)=${param.taskId}" scope="request"/>
<c:set var="fantaImportAction" value="TaskParticipant/Create.do?dto(companyId)=${sessionScope.user.valueMap['companyId']}&dto(taskId)=${param.taskId}&dto(type)=group" scope="request"/>



<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/TaskTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/scheduler/UserGroupImportList.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/TaskTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/scheduler/UserGroupImportList.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>
