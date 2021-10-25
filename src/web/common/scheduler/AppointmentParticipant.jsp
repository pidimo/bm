<%@ include file="/Includes.jsp" %>

<html:form action="${action}?index=${param.index}&calendar=${param.calendar}&type=${param.type}&dto(title)=${app2:encode(param['dto(title)'])}" focusIndex="2" >
    <table border="0" align="center" cellpadding="0" cellspacing="0"  class="container" width="60%">
        <tr>
            <td colspan="2" class="title">
                <fmt:message key="Appointment.participant.delete"/>
            </td>
        </tr>
        <tr>
            <td class="label" width="15%"><fmt:message key="Appointment.userName"/></td>
            <td class="contain" width="75%"><app:text property="dto(participantName)" view="${op == 'delete'}"/></td>
        </tr>
        <tr>
            <td class="label" width="25%"><fmt:message key="Appointment.appType"/></td>
            <td class="contain" width="75%">
                  <c:if test="${param.userType == '1'}">
                    <fmt:message key="User.intenalUser" />
                  </c:if>
                  <c:if test="${param.userType == '0'}">
                    <fmt:message key="User.externalUser" />
                  </c:if>
            </td>
        </tr>
        <c:if test="${param['dto(appointmentGroupName)'] != ''}" >
            <tr>
                <td class="label" width="25%"><fmt:message key="Appointment.groupName"/></td>
                <td class="contain" width="75%"><app:text property="dto(appointmentGroupName)" view="${op == 'delete'}"/></td>
            </tr>
        </c:if>
        <html:hidden property="dto(op)" value="${op}"/>
            <c:if test="${op=='update' || op=='delete'}">
                <html:hidden property="dto(scheduledUserId)"/>
            </c:if>

      </table>
      <table cellSpacing=0 cellPadding=4 width="60%" border=0 align="center">
            <TR>
                 <TD class="button">
                    <c:if test="${op == 'create' || op == 'update'}">
<app2:securitySubmit operation="${op}" property="dto(submit)"  functionality="APPOINTMENTPARTICIPANT" styleClass="button" tabindex="19">
  <fmt:message key="Common.save"/>
</app2:securitySubmit>
                    </c:if>
                    <c:if test="${op == 'delete'}">
<app2:securitySubmit operation="DELETE" property="dto(delete)"  functionality="APPOINTMENTPARTICIPANT" styleClass="button" tabindex="19">
  <fmt:message   key="Common.delete"/>
</app2:securitySubmit>
                    </c:if>
                    <html:cancel styleClass="button" tabindex="2"><fmt:message   key="Common.cancel"/></html:cancel>
                </TD>
            </TR>
       </table>
    </table>
</html:form>