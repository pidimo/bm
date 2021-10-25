<%@ include file="/Includes.jsp" %>

<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" >
<tr>
    <td>
<html:form action="${action}" focus="dto(statusName)" >
    <table id="Status.jsp" border="0" cellpadding="0" cellspacing="0" width="60%" align="center" class="container">

    <html:hidden property="dto(op)" value="${op}"/>
    <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
    <c:if test="${('update' == op) || ('delete' == op)}">
        <html:hidden property="dto(statusId)"/>
    </c:if>

<%--for the version control if update action--%>
    <c:if test="${'update' == op}">
        <html:hidden property="dto(version)"/>
    </c:if>
    <TR>
        <TD colspan="2" class="title">
            <c:out value="${title}"/>
        </TD>
    </TR>

    <TR>
        <TD class="label" width="25%" nowrap><fmt:message    key="Status.name"/></TD>
        <TD class="contain" width="75%">
            <app:text property="dto(statusName)" styleClass="mediumText" maxlength="80" view="${'delete' == op}"/>
        </TD>
    </TR>
   <TR>
        <TD class="label" width="25%" nowrap><fmt:message   key="Status.isFinal"/></TD>
        <TD class="contain" width="75%">
            <html:radio property="dto(isFinal)" value="true" styleClass="radio" disabled="${'delete' == op}" />
            &nbsp;<fmt:message key="Common.yes"/>&nbsp;&nbsp;
            <html:radio property="dto(isFinal)" value="false" styleClass="radio" disabled="${'delete' == op}"/>
            &nbsp;<fmt:message key="Common.no"/>
        </TD>
    </TR>
    </table>

    <table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
    <TR>
        <TD class="button">
        <app2:securitySubmit operation="${op}" functionality="STATUS" styleClass="button">${button}</app2:securitySubmit>
        <c:if test="${op == 'create'}" >
                <app2:securitySubmit operation="${op}" functionality="STATUS" styleClass="button" property="SaveAndNew"><fmt:message   key="Common.saveAndNew"/></app2:securitySubmit>
        </c:if>
        <html:cancel styleClass="button" ><fmt:message   key="Common.cancel"/></html:cancel>
        </TD>
    </TR>
    </table>

    </html:form>

    </td>
</tr>
</table>



