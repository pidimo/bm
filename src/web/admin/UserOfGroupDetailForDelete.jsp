<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<c:set var="helpResourceKey" value="help.userGroup.member.delete" scope="request"/>

<fmt:message var="title" key="UserGroup.member.delete" scope="request"/>
<c:set var="action" value="/User/UserOfGroup/Delete" scope="request"/>
<c:set var="op" value="delete" scope="request"/>

<c:set var="pagetitle" value="User.UserGroup.delete" scope="request"/>
<c:set var="windowTitle" value="UserGroup.member.delete" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/UserGroupTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/admin/UserOfGroup.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/UserGroupTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/admin/UserOfGroup.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>