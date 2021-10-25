<%@ include file="/Includes.jsp" %>

<%
    Object mailFilter=request.getParameter("mailFilter");
    request.setAttribute("mailFilter",(mailFilter!=null?mailFilter.toString():"0"));

    String locale = request.getParameter("locale");
	Locale defaultLocale = request.getLocale();
	if (locale != null) {
		defaultLocale = new Locale(locale);
    }
%>

<c:url var="urlCancel" value="/webmail/Mail/MailTray.do">
    <c:param name="folderId" value="${folderView}"/>
    <c:param name="index" value="${0}"/>
    <c:param name="mailFilter" value="${mailFilter}"/>
    <c:param name="returning" value="true"/>
</c:url>
<html:form action="${action}">
    <html:hidden property="dto(op)" value="moveAllEmailsToTrash"   styleId="aaaa"/>
    <html:hidden property="dto(folderId)" value="${mailTrayForm.dto['folderId']}" styleId="bbb"/>
<table border="0" cellpadding="0" cellspacing="0" width="50%" align="center" class="container">
    <tr>
        <td colspan="4" class="title">${title}</td>
    </tr>
    <tr>
        <td class="contain">
            <c:set var="folderName" value="${mailTrayForm.dto['folderName']}"/>
            <c:set var="numberOfEmails" value="${mailTrayForm.dto['folderSize']}"/>
            <fmt:message key="Webmail.email.moveToTrash">
                <fmt:param value="${numberOfEmails}"/>
                <fmt:param value="${folderName}"/>
            </fmt:message>
        </td>
    </tr>
    <tr>
        <TD class="button">
            <app2:securitySubmit operation="delete" property="dto(delete)" functionality="MAIL" styleClass="button" tabindex="3"><fmt:message   key="Common.delete"/></app2:securitySubmit>&nbsp;
            <html:button property="" styleClass="button" onclick="location.href='${urlCancel}'" >
                <fmt:message   key="Common.cancel"/>
            </html:button>
        </TD>
    </tr>
</table>
</html:form>
