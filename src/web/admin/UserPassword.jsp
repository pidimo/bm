<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.userSettings.password" scope="request"/>

<fmt:message var="title" key="User.user" scope="request"/>
<fmt:message var="save" key="Common.save" scope="request"/>
<c:remove var="index" scope="session"/>
<c:set var="windowTitle" value="User.user" scope="request"/>
<c:set var="pagetitle" value="Common.users" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/UserPreferencesTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/admin/UserPassword.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/UserPreferencesTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/admin/UserPassword.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>