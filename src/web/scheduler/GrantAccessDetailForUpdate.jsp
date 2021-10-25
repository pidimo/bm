<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.calendar.grantAccess.edit" scope="request"/>

<fmt:message var="title" key="Scheduler.grantAccess.update" scope="request"/>
<fmt:message var="button" key="Common.save" scope="request"/>
<c:set var="action" value="/GrantAccess/Update" scope="request"/>
<c:set var="op" value="update" scope="request"/>
<c:set var="windowTitle" value="Scheduler.grantAccess.update" scope="request"/>

<c:set var="pagetitle" value="Scheduler.grantAccess.plural" scope="request"/>


<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/scheduler/GrantAccess.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/scheduler/GrantAccess.jsp"  scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>