<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.project.assigneed.newPerson" scope="request"/>

<fmt:message var="title" key="ProjectAssignee.Title.create" scope="request"/>
<fmt:message var="button" key="Common.save" scope="request"/>

<c:set var="action" value="/ProjectUser/CreatePerson.do" scope="request"/>
<c:set var="op" value="create" scope="request"/>
<c:set var="windowTitle" value="ProjectAssignee.Title.create" scope="request"/>


<c:set var="workOnPerson" value="true" scope="request"/>



<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ProjectTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/projects/ProjectUser.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/ProjectTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/projects/ProjectUser.jsp" scope="request"/>
        <c:import url="/layout/ui/main.jsp"/>
    </c:otherwise>
</c:choose>