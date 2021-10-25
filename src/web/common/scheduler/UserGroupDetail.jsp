<%@ include file="/Includes.jsp" %>
<table align="center" width="60%" border="0" cellpadding="0" cellspacing="0">
<tr>
<td>
<html:form action="/AppointmentParticipant/Group/Delete.do?index=${param.index}&dto(title)=${app2:encode(param['dto(title)'])}&appointmentId=${param.appointmentId}">
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
     <html:hidden property="dto(userGroupId)" value="${param.userGroupId}" />
     <html:hidden property="dto(op)" value="deleteGroup"/>
     <html:hidden property="dto(appointmentId)" value="${param.appointmentId}"/>
    <tr>
        <td colspan="2" class="title" width="100%">
            <fmt:message key="Appointment.participant.delete"/>
        </td>
    </tr>
    <tr>
        <td colspan="2" class="contain" width="70%">
            <fmt:message key="Appointment.userGroupDelete.message"/>&nbsp;<strong>${param['dto(appointmentGroupName)']}</strong> ?
        </td>
    </tr>
    </table>
    <table cellSpacing=0 cellPadding=4 width="100%" border=0 align="center">
      <tr>
          <td class="button">
<app2:checkAccessRight functionality="APPOINTMENTPARTICIPANT" permission="DELETE">
                   <c:url var="url" value="/scheduler/AppointmentParticipant/Forward/Delete.do">
                    <c:param name="dto(withReferences)" value="true"/>
                    <c:param name="dto(participantName)" value="${param.participantName}"/>
                    <c:param name="dto(scheduledUserId)" value="${param.scheduledUserId}"/>
                    <c:param name="dto(appointmentGroupName)" value="${param.appointmentGroupName}"/>
                    <c:param name="dto(title)" value="${param['dto(title)']}"/>
                    <c:param name="userType" value="${param.userType}"/>
                    <c:param name="appointmentId" value="${param.appointmentId}"/>
                    <c:param name="index" value="1"/>
                   </c:url>
                   <html:button  property="dto(answer)" styleClass="button" onclick="location.href='${url}'" tabindex="9">
                        <fmt:message key="Appointment.participant.delete"/>
                   </html:button>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="APPOINTMENTPARTICIPANT" permission="DELETE">
              <app2:securitySubmit operation="${op}" functionality="APPOINTMENTPARTICIPANT" styleClass="button" tabindex="10" >
                   <fmt:message key="Appointment.deleteGroup"/>
              </app2:securitySubmit>
</app2:checkAccessRight>
               <html:cancel  styleClass="button" tabindex="25" >
                   <fmt:message    key="Common.cancel"/>
               </html:cancel>

          </TD>
      </TR>
   </table>
</html:form>
</td>
</tr>
</table>