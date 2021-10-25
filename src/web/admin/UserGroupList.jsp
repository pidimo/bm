<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<c:set var="helpResourceKey" value="help.userGroup.list" scope="request"/>

<c:set var="windowTitle" value="UserGroup.userGroupList" scope="request"/>
<c:set var="pagetitle" value="UserGroup.userGroupList" scope="request"/>

<%--<c:set var="tabs" value="/ContactTabs.jsp" scope="request"/>--%>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/admin/UserGroupList.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/admin/UserGroupList.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>