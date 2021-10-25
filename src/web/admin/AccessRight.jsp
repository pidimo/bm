<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.role.accessRight" scope="request"/>


<fmt:message var="save" key="Common.save" scope="request"/>
<c:remove var="index" scope="session"/>

<c:set var="windowTitle" value="Admin.AccessRights.Title" scope="request"/>
<c:set var="pagetitle" value="Common.company" scope="request"/>


<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/RoleTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/admin/AccessRight.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/admin/AccessRight.jsp" scope="request"/>
        <c:set var="tabs" value="/RoleTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>
