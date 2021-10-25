<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.mail.preferences" scope="request"/>

<fmt:message var="title" key="Webmail.preferences" scope="request"/>
<fmt:message var="save" key="Common.save" scope="request"/>
<c:remove var="index" scope="session"/>

<c:set var="op" value="update" scope="request"/>
<c:set var="windowTitle" value="Webmail.preferences" scope="request"/>
<c:set var="pagetitle" value="Webmail.preferences" scope="request"/>


<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/WebmailTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/webmail/Preferences.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/webmail/Preferences.jsp" scope="request"/>
        <c:set var="tabs" value="/WebmailTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>