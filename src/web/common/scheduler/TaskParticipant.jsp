<%@ include file="/Includes.jsp" %>
<%
 pageContext.setAttribute("statusList", JSPHelper.getPriorityStatusList(request));
 %>
<html:form action="${action}?index=${param.index}&dto(title)=${app2:encode(param['dto(title)'])}" focusIndex="2" >
    <table border="0" align="center" cellpadding="0" cellspacing="0"  class="container" width="50%">
        <tr>
            <td colspan="2" class="title">
                <c:out value="${title}"/>
            </td>
        </tr>
        <tr>
            <td class="label" width="30%"><fmt:message key="Appointment.userName"/></td>
            <td class="contain" width="70%"><app:text property="dto(participantName)" view="true"/></td>
        </tr>
        <tr>  
            <td class="label" width="30%"><fmt:message key="Appointment.groupName"/></td>
            <td class="contain" width="70%"><app:text property="dto(taskGroupName)" view="true"/></td>
        </tr>
        <tr>
            <td class="label" width="30%"><fmt:message   key="Task.status"/></td>
            <td class="contain" width="70%">
            <html:select property="dto(statusId)" styleClass="select" readonly="${op == 'delete'}" tabindex="10">
                    <html:option value="" />
                    <html:options collection="statusList"  property="value" labelProperty="label" />
            </html:select>
            </td>
        </tr>
        <tr>
            <td class="label" width="30%" colspan="2" ><fmt:message  key="Task.notes"/><br>
                <html:textarea  property="dto(noteValue)" styleClass="mediumDetail" style="height:100px;width:99%;"  readonly="${op == 'delete'}" />
            </td>
        </tr>
        <html:hidden property="dto(op)" value="${op}"/>
            <c:if test="${op=='update' || op=='delete'}">
                <html:hidden property="dto(scheduledUserId)"/>
                <html:hidden property="dto(version)"/>
                <html:hidden property="dto(group)" value="${param.group}" />
            </c:if>            
      </table>
  <table cellSpacing=0 cellPadding=4 width="50%" border=0 align="center">
        <TR>
             <TD class="button">
<app2:checkAccessRight functionality="TASKUSER" permission="UPDATE">
    <c:if test="${op == 'create' || op == 'update'}">
        <html:submit property="dto(submit)" styleClass="button" tabindex="0"><fmt:message key="Common.save"/></html:submit>
    </c:if>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="TASKUSER" permission="DELETE">
    <c:if test="${op == 'delete'}">
        <html:submit property="dto(delete)" styleClass="button" tabindex="1" ><fmt:message   key="Common.delete"/></html:submit>
    </c:if>
</app2:checkAccessRight>
                <html:cancel styleClass="button" tabindex="2"><fmt:message   key="Common.cancel"/></html:cancel>
            </TD>
        </TR>
   </table>
 </table>
</html:form>