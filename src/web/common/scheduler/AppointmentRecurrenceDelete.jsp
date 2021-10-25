<%@ include file="/Includes.jsp" %>
<table align="center" width="60%" border="0" cellpadding="0" cellspacing="0">
<tr>
<td>
<html:form action="${action}?dto(withReferences)=true&module=scheduler&dto(appointmentId)=${appointmentForm.dtoMap.appointmentId}&type=${param.type}&operation=delete&calendar=${param.calendar}&dto(currentDate)=${param.currentDate}" >
    <table width="100%" border="0" cellpadding="0" cellspacing="0">
     <html:hidden property="dto(calendar)" value="${param.calendar}" />
     <html:hidden property="dto(currentDate)" value="${param.currentDate}" />
     <html:hidden property="dto(title)" value="${param['dto(title)']}" />

    <tr>
        <td colspan="2" class="title" width="100%">
            <fmt:message key="Appointment.delete"/>
        </td>
    </tr>
    <tr>
        <td class="contain" width="70%" >
            <fmt:message var="datePattern" key="datePattern"/>
            <c:if test="${!param.root}">
                    <fmt:message key="Appointment.deleteCurrentMessage"/>
                    &nbsp;<strong>${param.date}</strong> ?
            </c:if>
            <c:if test="${param.root}">
                    <fmt:message key="Appointment.deleteCurrentRootMessage"/>
            </c:if>
        </td>
    </tr>                    
    </table>
    <table cellSpacing=0 cellPadding=4 width="100%" border=0 align="center">
      <tr>
          <td class="button">
          <c:if test="${!param.root}">                                
              <app2:checkAccessRight functionality="APPOINTMENT" permission="DELETE">
                   <c:url var="url" value="/scheduler/Appointment/View/Delete.do?dto(appointmentId)=${appointmentForm.dtoMap.appointmentId}&type=${param.type}&dto(currentDate)=${param.currentDate}&calendar=${param.calendar}&dto(title)=${app2:encode(param['dto(title)'])}"/>
                   <html:button  property="dto(answer)" styleClass="button" onclick="location.href='${url}'" tabindex="9">
                        <fmt:message key="Appointment.deleteCurrent"/>
                   </html:button>
              </app2:checkAccessRight>
            </c:if>
              <app2:securitySubmit operation="${op}" functionality="APPOINTMENT" styleClass="button" tabindex="10" >
                <fmt:message key="Appointment.deleteSerie"/>
              </app2:securitySubmit>

              <c:url var="urlCancel" value="/scheduler/Appointment/Forward/View/Delete.do">
                <c:param name="redirect" value="true"/>
                <c:param name="type" value="${param.type}"/>
                <c:param name="oldType" value="${param.type}"/>
                <c:param name="calendar" value="${param.calendar}"/>
                <c:param name="currentDate" value="${param.currentDate}"/>
              </c:url>
                   <html:button  property="dto(cancel)" styleClass="button" onclick="location.href='${urlCancel}'" tabindex="11">
                             <fmt:message key="Common.cancel"/>
                   </html:button>
          </TD>
      </TR>
   </table>
</html:form>
</td>
</tr>
</table>