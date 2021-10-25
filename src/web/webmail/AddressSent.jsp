<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.mail.send.asignEmail" scope="request"/>

<fmt:message var="title" key="Webmail.mailContact.new" scope="request"/>
<c:set var="action" value="/Mail/CreateManyMailContact.do" scope="request"/>
<c:set var="op" value="createMany" scope="request"/>
<c:set var="windowTitle" value="Webmail.mailContact.new" scope="request"/>
<c:set var="pagetitle" value="Webmail.mailContact.plural" scope="request"/>
<c:set var="optionTitle" value="Webmail.mailContact.new" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="webTabs" value="/WEB-INF/jsp/layout/tiles/WebmailTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/webmail/WebMail.jsp" scope="request"/>
        <c:set var="mailBody" value="/WEB-INF/jsp/webmail/AddressSent.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="webTabs" value="/WebmailTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/webmail/WebMail.jsp" scope="request"/>
        <c:set var="mailBody" value="/common/webmail/AddressSent.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>