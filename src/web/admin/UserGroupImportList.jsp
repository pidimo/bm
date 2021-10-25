<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.userGroup.member.add" scope="request"/>

<c:set var="pagetitle" value="Admin.User.Title.search" scope="request"/>
<c:set var="windowTitle" value="Admin.User.Title.search" scope="request"/>

<c:set var="action" value="/User/UserGroupImportList.do?parameter(userGroupId)=${param.userGroupId}" scope="request"/>
<c:set var="actionReload" value="/User/UserGroupImportList/SearchUser.do?userGroupId=${param.userGroupId}"
       scope="request"/>
<c:set var="actionCancel" value="/User/Forward/UserGroupImportList.do?parameter(userGroupId)=${param.userGroupId}"
       scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/UserGroupTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/admin/UserGroupImportList.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/UserGroupTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/admin/UserGroupImportList.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>