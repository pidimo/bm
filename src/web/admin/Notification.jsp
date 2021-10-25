<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.userSettings.notification" scope="request"/>

<fmt:message var="title" key="Admin.Notification" scope="request"/>
<fmt:message var="save" key="Common.save" scope="request"/>
<c:remove var="index" scope="session"/>
<c:set var="windowTitle" value="Admin.Notification" scope="request"/>
<c:set var="pagetitle" value="Admin.Notification" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/UserPreferencesTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/admin/Notification.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/admin/Notification.jsp" scope="request"/>
        <c:set var="tabs" value="/UserPreferencesTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>