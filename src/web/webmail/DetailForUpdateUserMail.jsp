<%@ include file="/Includes.jsp" %>

<fmt:message var="title" key="User.user" scope="request"/>
<fmt:message var="save" key="Common.save" scope="request"/>
<c:remove var="index" scope="session"/>

<c:set var="op" value="update" scope="request"/>
<c:set var="action" value="/WebmailUser/Update.do" scope="request"/>
<c:set var="windowTitle" value="User.user" scope="request"/>
<c:set var="pagetitle" value="Common.users" scope="request"/>
<c:set var="body" value="/common/webmail/NewUser.jsp" scope="request"/>
<c:set var="tabs" value="/WebmailTabs.jsp" scope="request"/>
<c:import url="${sessionScope.layout}/main.jsp"/>
