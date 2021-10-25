<%@ include file="/Includes.jsp" %>

<c:if test="${param.type != null and param.calendar != null}">
    <c:set var="back" value="&type=${param.type}&calendar=${param.calendar}"/>
</c:if>

<c:if test="${param.taskId != null}">
    <c:set var="fantaAction" value="TaskParticipant/Forward/Group/Create.do?dto(taskId)=${param.taskId}"
           scope="request"/>
    <c:set var="Action"
           value="TaskParticipant/Create.do?dto(companyId)=${sessionScope.user.valueMap['companyId']}&dto(taskId)=${param.taskId}&dto(type)=group&dto(title)=${app2:encode(param['dto(title)'])}"
           scope="request"/>
</c:if>

<c:if test="${param.appointmentId != null}">
    <c:set var="fantaAction"
           value="AppointmentParticipant/Forward/Group/Create.do?dto(appointmentId)=${param.appointmentId}${back}"
           scope="request"/>
    <c:set var="Action"
           value="AppointmentParticipant/Create.do?dto(companyId)=${sessionScope.user.valueMap['companyId']}&dto(appointmentId)=${param.appointmentId}&dto(type)=group&dto(title)=${app2:encode(param['dto(title)'])}${back}"
           scope="request"/>
</c:if>

<html:form action="${Action}">
    <div class="${app2:getFormButtonWrapperClasses()}">
        <html:cancel styleClass="button ${app2:getFormButtonCancelClasses()}" property="dto(cancel)"><fmt:message
                key="Common.cancel"/>
        </html:cancel>
    </div>
</html:form>
<div class="table-responsive">
    <fanta:table mode="bootstrap" list="userGroupSchedulerList"
                 styleClass="${app2:getFantabulousTableClases()}"
                 width="100%" id="userGroup" action="${fantaAction}"
                 imgPath="${baselayout}" align="center">
        <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%"/>

        <fanta:actionColumn name="import" title="Common.action"
                            action="${Action}&dto(userGroupId)=${userGroup.userGroupId}"
                            styleClass="listItem"
                            headerStyle="listHeader" width="2%"
                            glyphiconClass="${app2:getClassGlyphImport()}"/>
        <fanta:dataColumn name="groupName" styleClass="listItem2" title="Appointment.groupName"
                          headerStyle="listHeader" width="95%" orderable="true" maxLength="50"/>
    </fanta:table>
</div>
