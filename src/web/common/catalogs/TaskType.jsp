<%@ include file="/Includes.jsp" %>
<table align="center" width="60%" border="0" cellpadding="0" cellspacing="0">
<tr>
<td>
<html:form action="${action}" focus="dto(name)">
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td colspan="2" class="title" width="100%">
            <c:out value="${title}"/>
        </td>
    </tr>
    <tr>
        <td class="label" width="30%"><fmt:message key="AppointmentType.name"/></td>
        <td class="contain" width="70%"><app:text property="dto(name)" styleClass="largetext" maxlength="20" view="${'delete' == op}" tabindex="2" /></td>
    </tr>
    </table>
    <table cellSpacing=0 cellPadding=4 width="100%" border=0 align="center">
      <tr>
          <td class="button">
          <c:if test="${op == 'create' || op=='update'}">
          <app2:securitySubmit operation="${op}" functionality="TASKTYPE" styleClass="button" tabindex="3" >
            <fmt:message key="Common.save"/>
          </app2:securitySubmit>
          </c:if>
          <c:if test="${op == 'delete'}">
          <app2:securitySubmit operation="${op}" functionality="TASKTYPE" styleClass="button" tabindex="4">
            <fmt:message key="Common.delete"/>
          </app2:securitySubmit>
          </c:if>
          <c:if test="${op == 'create'}" >
          <app2:securitySubmit operation="${op}" functionality="TASKTYPE" styleClass="button" property="SaveAndNew" tabindex="5">
            <fmt:message key="Common.saveAndNew"/>
          </app2:securitySubmit>
          </c:if>
          <html:cancel styleClass="button" tabindex="6" ><fmt:message   key="Common.cancel"/></html:cancel>
          </TD>
      </TR>
   </table>
    <html:hidden property="dto(op)" value="${op}"/>
    <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
    <c:if test="${op=='update' || op=='delete'}">
        <html:hidden property="dto(taskTypeId)"/>
        <html:hidden property="dto(version)"/>
    </c:if>
</html:form>
</td>
</tr>
</table>