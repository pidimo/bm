<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="helpResourceKey" value="help.userGroup.member.list" scope="request"/>

<c:set var="pagetitle" value="Admin.User.Title.search" scope="request"/>
<c:set var="windowTitle" value="Admin.User.Title.search" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/UserGroupTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/admin/UserUserGroupList.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/UserGroupTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/admin/UserUserGroupList.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>