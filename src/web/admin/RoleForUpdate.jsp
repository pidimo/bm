<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.role.edit" scope="request"/>

<fmt:message   var="title" key="Admin.Role.update" scope="request"/>
<fmt:message   var="button" key="Common.save" scope="request"/>
<c:set var="action" value="/Role/Update" scope="request"/>
<c:set var="op" value="update" scope="request"/>

<c:set var="pagetitle" value="Admin.title" scope="request"/>
<c:set var="windowTitle" value="Admin.Role.update" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/RoleTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/admin/Role.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/admin/Role.jsp" scope="request"/>
        <c:set var="tabs" value="/RoleTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>