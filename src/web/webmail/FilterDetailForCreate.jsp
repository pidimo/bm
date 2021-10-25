<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.mail.filter.create" scope="request"/>

<fmt:message  var="title" key="Webmail.filter.new" scope="request"/>
<fmt:message  var="button" key="Common.save" scope="request"/>

<c:set var="action" value="/Mail/CreateFilter" scope="request"/>
<c:set var="op" value="create" scope="request"/>
<c:set var="windowTitle" value="Webmail.filter.new" scope="request"/>
<c:set var="pagetitle" value="Webmail.filter.plural" scope="request"/>


<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/WebmailTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/webmail/Filter.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/webmail/Filter.jsp" scope="request"/>
        <c:set var="tabs" value="/WebmailTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>
