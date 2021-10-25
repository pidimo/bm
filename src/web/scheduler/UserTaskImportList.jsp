<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.task.participant.addUser" scope="request"/>

<c:set var="windowTitle" value="Admin.User.Title.search" scope="request"/>
<c:set var="pagetitle" value="Admin.User.Title.search" scope="request"/>
<c:set var="actionReload"
       value="TaskParticipant/Forward/Create.do?dto(title)=${app2:encode(param['dto(title)'])}&dto(taskId)=${param.taskId}"
       scope="request"/>
<c:set var="actionCancel" value="/Task/ParticipantTaskList.do?index=${param.index}" scope="request"/>
<c:set var="action"
       value="TaskParticipant/Create.do?dto(companyId)=${sessionScope.user.valueMap['companyId']}&dto(taskId)=${param.taskId}&dto(type)=user"
       scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/TaskTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/admin/UserGroupImportList.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/TaskTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/admin/UserGroupImportList.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>
