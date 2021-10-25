<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.project.create" scope="request"/>


<fmt:message var="title" key="Project.new" scope="request"/>
<fmt:message var="button" key="Common.save" scope="request"/>

<c:set var="action" value="/Project/Create" scope="request"/>
<c:set var="op" value="create" scope="request"/>
<c:set var="windowTitle" value="Project.Title.new" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/projects/Project.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>

    <c:otherwise>
        <c:set var="body" value="/common/projects/Project.jsp" scope="request"/>
        <c:import url="/layout/ui/main.jsp"/>
    </c:otherwise>
</c:choose>