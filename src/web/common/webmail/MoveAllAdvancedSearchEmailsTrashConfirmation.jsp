<%@ include file="/Includes.jsp"%>

<c:url var="urlCancel" value="/webmail/Mail/MailAdvancedSearch.do?mailSearch=true&mailAdvancedSearch=true"/>

<html:form action="${action}?mailSearch=true&mailAdvancedSearch=true">
    <html:hidden property="dto(op)" value="moveToTrash"/>
    <c:forEach var="key" items="${emailIdentifiersAdvancedList}">
        <html:hidden property="dto(mailIds)" value="${key}" styleId="dto(mailIds)"/>
    </c:forEach>
    <table border="0" cellpadding="0" cellspacing="0" width="50%" align="center" class="container">
        <tr>
            <td colspan="4" class="title">${title}</td>
        </tr>
        <tr>
            <td class="contain">
                <c:set var="numberOfEmails" value="${emailIdentifiersAdvancedSize}"/>
                <fmt:message key="Webmail.email.moveSearchEmailsToTrash">
                    <fmt:param value="${numberOfEmails}"/>
                </fmt:message>
            </td>
        </tr>
        <tr>
            <TD class="button">
                <app2:securitySubmit operation="delete" property="dto(delete)" functionality="MAIL" styleClass="button"
                                     tabindex="3"><fmt:message key="Common.delete"/></app2:securitySubmit>&nbsp;
                <html:button property="" styleClass="button" onclick="location.href='${urlCancel}'">
                    <fmt:message key="Common.cancel"/>
                </html:button>
            </TD>
        </tr>
    </table>
</html:form>