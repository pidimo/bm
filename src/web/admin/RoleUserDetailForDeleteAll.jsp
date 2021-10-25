<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.role.user.deleteSelected" scope="request"/>

<fmt:message   var="title" key="Admin.UserRole.deleteAll" scope="request"/>
<fmt:message   var="button" key="Common.delete" scope="request"/>
<c:set var="action" value="/UserRole/Delete" scope="request"/>
<c:set var="op" value="deleteAll" scope="request"/>

<c:set var="pagetitle" value="Admin.title" scope="request"/>
<c:set var="windowTitle" value="Admin.UserRole.deleteAll" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/RoleTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/admin/RoleUserAll.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/admin/RoleUserAll.jsp" scope="request"/>
        <c:set var="tabs" value="/RoleTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>