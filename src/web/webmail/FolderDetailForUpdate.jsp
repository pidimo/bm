<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.mail.folder.edit" scope="request"/>

<fmt:message  var="title" key="Webmail.folder.update" scope="request"/>
<fmt:message  var="button" key="Common.save" scope="request"/>

<c:set var="action" value="/Mail/UpdateFolder" scope="request"/>
<c:set var="op" value="update" scope="request"/>
<c:set var="windowTitle" value="Webmail.folder.update" scope="request"/>
<c:set var="pagetitle" value="Webamil.folder.plural" scope="request"/>


<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/WebmailTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/webmail/Folder.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/webmail/Folder.jsp" scope="request"/>
        <c:set var="tabs" value="/WebmailTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>