<%@ include file="/Includes.jsp" %>

<c:set var="windowTitle" value="Webmail.userMailRequired" scope="request"/>
<c:set var="pagetitle" value="Common.webmail" scope="request"/>
<c:set var="body" value="/common/webmail/NoUserMail.jsp" scope="request"/>
<c:import url="${sessionScope.layout}/main.jsp"/>