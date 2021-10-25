<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.mail.contactGroup.contact.create" scope="request"/>

<fmt:message var="title" key="WebMail.contact.new" scope="request"/>
<fmt:message var="button" key="Common.save" scope="request"/>
<c:set var="action" value="/Mail/CreateAddress" scope="request"/>
<c:set var="op" value="create" scope="request"/>
<c:set var="windowTitle" value="WebMail.contact.new" scope="request"/>
<c:set var="pagetitle" value="WebMail.contact.new" scope="request"/>

<c:set var="optionTitle" value="WebMail.contact.new" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/WebmailTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/webmail/AddressAdd.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/WebmailTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/webmail/AddressAdd.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>