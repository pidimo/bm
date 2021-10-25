<%@ include file="/Includes.jsp" %>
<%@ page import="com.piramide.elwis.utils.WebMailConstants, java.util.List"%>
<br/>
<br/>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" >

<tr>
    <td>
    <html:form action="${action}" >
    <html:hidden property="dto(op)" value="${op}"/>
    <html:hidden property="dto(userId)" value="${sessionScope.user.valueMap['userId']}" />
    <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>

    <table cellSpacing=0 cellPadding=0 width="50%" border=0 align="center">
    <TR>
        <TD colspan="2" class="title"><fmt:message key="Webmail.userMailAcoount"/></TD>
    </TR>
    <tr>
        <td class="label"><fmt:message key="Webmail.userMail.emailAddress"/></td>
        <td class="contain"><html:text property="dto(userMailEmail)" styleClass="text" maxlength="240"/> </td>
    </tr>
    <tr>
        <td class="label"><fmt:message key="Webmail.userMail.userAccount"/></td>
        <td class="contain"><html:text property="dto(userAccount)" styleClass="text" maxlength="50"/> </td>
    </tr>
    <tr>
        <td class="label"><fmt:message key="Webmail.userMail.emailPasword"/></td>
        <td class="contain"><html:password  property="dto(userMailPassword)" styleClass="text" maxlength="50" /> </td>
    </tr>
    <tr>
        <td class="label"><fmt:message key="Webmail.userMail.pop3Server"/></td>
        <td class="contain"><html:text property="dto(pop3Server)" styleClass="text" maxlength="50"/> </td>
    </tr>
    <tr>
        <td class="label"><fmt:message key="Webmail.userMail.pop3Port"/></td>
        <td class="contain"><html:text property="dto(pop3Port)" styleClass="text" maxlength="50"/> </td>
    </tr>
    <tr>
        <td class="label"><fmt:message key="Webmail.userMail.sslConnection"/></td>
        <td class="contain"><html:checkbox  property="dto(ssl)" styleClass="adminCheckBox"/> </td>
    </tr>

   <c:if test="${webmailUser.dtoMap['userMailDate'] != null}">
    <tr>
        <td class="label">
            <fmt:message key="Webmail.userMail.date"/>
        </td>
        <td class="contain">
            <fmt:message var="datePattern" key="datePattern"/>
            <fmt:formatDate value="${app2:intToDate(webmailUser.dtoMap['userMailDate'])}"
                            pattern="${datePattern}"/>
            <html:hidden property="dto(userMailDate)" value="${webmailUser.dtoMap['userMailDate']}"/>
        </td>
    </tr>
    </c:if>
    </table>
    <table cellSpacing=0 cellPadding=4 width="50%" border=0 align="center">
    <TR>
        <TD class="button">
        <app2:securitySubmit operation="EXECUTE" functionality="MAIL" styleClass="button"><fmt:message key="Common.save" /></app2:securitySubmit>
        </TD>
    </TR>
    </table>
    </html:form>
    </td>
</tr>
</table>
