<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.mail.tray.trash.deleteConfirmation" scope="request"/>

<fmt:message  var="title" key="Webmail.deleteConfirmation" scope="request"/>
<fmt:message  var="button" key="Common.save" scope="request"/>

<c:set var="action" value="/Mail/DeleteConfirmation" scope="request"/>
<c:set var="op" value="deleteMails" scope="request"/>
<c:set var="windowTitle" value="Webmail.deleteConfirmation" scope="request"/>
<c:set var="pagetitle" value="Webmail.deleteConfirmation" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/webmail/DeleteConfirmation.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/webmail/DeleteConfirmation.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>