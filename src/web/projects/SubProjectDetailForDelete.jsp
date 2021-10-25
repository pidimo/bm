<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.project.subProject.delete" scope="request"/>

<fmt:message var="title" key="SubProject.Title.delete" scope="request"/>
<fmt:message var="button" key="Common.delete" scope="request"/>

<c:set var="action" value="/SubProject/Delete" scope="request"/>
<c:set var="op" value="delete" scope="request"/>
<c:set var="windowTitle" value="SubProject.Title.delete" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ProjectTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/projects/SubProject.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/ProjectTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/projects/SubProject.jsp" scope="request"/>
        <c:import url="/layout/ui/main.jsp"/>
    </c:otherwise>
</c:choose>