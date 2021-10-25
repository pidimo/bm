<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.role.user.add" scope="request"/>

<c:set var="pagetitle" value="Admin.title" scope="request"/>
<c:set var="windowTitle" value="Admin.User.Title" scope="request"/>
<c:set var="action" value="/RoleUser/Forward/UserImportList.do?roleId=${param.roleId}&roleName=${param.roleName}"
       scope="request"/>
<c:set var="actionReload" value="/RoleUser/Forward/UserImportList.do?roleId=${param.roleId}&roleName=${param.roleName}"
       scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/RoleTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/admin/UserImportList.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/RoleTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/admin/UserImportList.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>