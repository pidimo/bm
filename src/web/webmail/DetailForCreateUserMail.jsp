<%@ page import="com.piramide.elwis.web.webmail.form.MailAccountForm" %>
<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.mail.account.create" scope="request"/>

<fmt:message var="title" key="User.user" scope="request"/>
<fmt:message var="button" key="Common.save" scope="request"/>

<c:remove var="index" scope="session"/>

<!--
Varible showCancel is used in MailAccount.jsp,
when user cannot has account assigned then cancel button cannot be show in MailAccount.jsp
-->
<c:set var="showCancel" value="${false}" scope="request"/>
<!--setting up checked in default account-->
<%
    MailAccountForm form = new MailAccountForm();
    if (null != request.getAttribute("mailAccountForm"))
        form = (MailAccountForm) request.getAttribute("mailAccountForm");

    form.setDto("defaultAccount", Boolean.valueOf(true));
    request.setAttribute("mailAccountForm", form);
%>
<c:set var="op" value="create" scope="request"/>
<c:set var="action" value="/WebmailUser/Create.do" scope="request"/>
<c:set var="windowTitle" value="User.user" scope="request"/>
<c:set var="pagetitle" value="Common.users" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/WebmailTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/webmail/MailAccount.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/webmail/MailAccount.jsp" scope="request"/>
        <c:set var="tabs" value="/WebmailTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>
