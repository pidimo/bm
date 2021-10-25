<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.mail.tray.readEmail" scope="request"/>

<c:remove var="index" scope="session"/>

<c:set var="windowTitle" value="Webmail.readMail" scope="request"/>
<c:set var="pagetitle" value="Common.webmail" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="webTabs" value="/WEB-INF/jsp/layout/tiles/WebmailTabs.jsp" scope="request"/>
        <c:set var="mailBody" value="/WEB-INF/jsp/webmail/ReadEmail.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/webmail/WebMail.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="webTabs" value="/WebmailTabs.jsp" scope="request"/>
        <c:set var="mailBody" value="/common/webmail/ReadEmail.jsp" scope="request"/>
        <c:set var="body" value="/common/webmail/WebMail.jsp" scope="request"/>
        <c:import url="/layout/ui/main.jsp"/>
    </c:otherwise>
</c:choose>