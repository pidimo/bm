<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:remove var="index" scope="session"/>
<c:set var="windowTitle" value="Mail.folder.inbox" scope="request"/>
<c:set var="pagetitle" value="Common.webmail" scope="request"/>
<c:set var="body" value="/common/webmail/Messages.jsp" scope="request"/>
<c:import url="${sessionScope.layout}/main.jsp"/>



