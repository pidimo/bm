<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="windowTitle" value="Webmail.mailCommunications.plural" scope="request"/>
<c:set var="pagetitle" value="Webmail.mailCommunications.plural" scope="request"/>
<c:set var="body" value="/common/webmail/MailCommunication.jsp" scope="request"/>
<c:set var="op" value="create" scope="request" />  
<c:import url="${sessionScope.layout}/OnlyBody.jsp"/>