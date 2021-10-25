<%@ include file="/Includes.jsp" %>

<html:form styleClass="form-horizontal"
           action="/TaskParticipant/Group/Delete.do?dto(title)=${app2:encode(param['dto(title)'])}&taskId=${param.taskId}">
    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormPanelClasses()}">
            <html:hidden property="dto(userGroupId)" value="${param.userGroupId}"/>
            <html:hidden property="dto(op)" value="deleteGroup"/>
            <html:hidden property="dto(taskId)" value="${param.taskId}"/>
            <html:hidden property="dto(group)" value="true"/>
            <fieldset>
                <legend class="title">
                    <fmt:message key="Task.participant.delete"/> ?
                </legend>
                <p>
                    <fmt:message
                            key="Appointment.userGroupDelete.message"/>&nbsp;<strong>${param['dto(taskGroupName)']}</strong>
                    ?
                </p>
            </fieldset>
        </div>


        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:checkAccessRight functionality="TASKUSER" permission="DELETE">
                <c:url var="url" value="/scheduler/TaskParticipant/Forward/Delete.do">
                    <c:param name="size" value="${param.size}"/>
                    <c:param name="taskId" value="${param.taskId}"/>
                    <c:param name="dto(withReferences)" value="true"/>
                    <c:param name="dto(scheduledUserId)" value="${param.scheduledUserId}"/>
                    <c:param name="dto(participantName)" value="${param.participantName}"/>
                    <c:param name="dto(taskGroupName)" value="${param.taskGroupName}"/>
                    <c:param name="index" value="${param.index}"/>
                    <c:param name="dto(title)" value="${param.title}"/>
                    <c:param name="group" value="false"/>
                </c:url>

                <html:button property="dto(answer)" styleClass="${app2:getFormButtonClasses()}"
                             onclick="location.href='${url}'" tabindex="9">
                    <fmt:message key="Task.participant.delete"/>
                </html:button>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="TASKUSER" permission="DELETE">
                <app2:securitySubmit operation="${op}" functionality="TASKUSER"
                                     styleClass="${app2:getFormButtonClasses()}" tabindex="10">
                    <fmt:message key="Appointment.deleteGroup"/>
                </app2:securitySubmit>
            </app2:checkAccessRight>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="25">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>

</html:form>

