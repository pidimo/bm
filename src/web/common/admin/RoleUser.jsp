<%@ page import="java.util.List"%>
<%@ include file="/Includes.jsp" %>



<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" >
<tr>
    <td>
<html:form action="/RoleUser/Delete.do">
    <table border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">

    <html:hidden property="dto(op)" value="${op}"/>
    <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
    <html:hidden property="dto(roleId)"/>
    <html:hidden property="dto(userId)"/>
    <tr>
        <td colspan="2" class="title">
            <c:out value="${title}"/>
        </td>
    </tr>
    <tr>
        <td class="label" width="25%" nowrap><fmt:message key="Contact.Person.lastname"/></td>
        <td class="contain" width="75%" >${dto.name1}</td>
    </tr>
    <tr>
        <td class="label" width="25%" nowrap><fmt:message key="Contact.Person.firstname"/></td>
        <td class="contain" width="75%" >${dto.name2}</td>
    </tr>

    </table>

    <table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
    <tr>
        <TD class="button">
        <%--<html:submit styleClass="button">${button}</html:submit>--%>
        <app2:securitySubmit operation="UPDATE" functionality="USER" styleClass="button">${button}</app2:securitySubmit>
        <html:cancel styleClass="button" ><fmt:message   key="Common.cancel"/></html:cancel>
        </TD>
    </tr>
    </table>
</html:form>

    </td>
</tr>
</table>