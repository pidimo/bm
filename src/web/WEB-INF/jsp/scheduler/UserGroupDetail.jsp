<%@ include file="/Includes.jsp" %>

<html:form styleClass="form-horizontal"
           action="/AppointmentParticipant/Group/Delete.do?index=${param.index}&dto(title)=${app2:encode(param['dto(title)'])}&appointmentId=${param.appointmentId}">
    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <html:hidden property="dto(userGroupId)" value="${param.userGroupId}"/>
                <html:hidden property="dto(op)" value="deleteGroup"/>
                <html:hidden property="dto(appointmentId)" value="${param.appointmentId}"/>

                <legend class="title">
                    <fmt:message key="Appointment.participant.delete"/>
                </legend>

                <p>
                    <fmt:message
                            key="Appointment.userGroupDelete.message"/>&nbsp;<strong>${param['dto(appointmentGroupName)']}</strong>
                    ?
                </p>
            </fieldset>
        </div>
        <div class="${app2:getFormButtonWrapperClasses()}">
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
                <html:button property="dto(answer)" styleClass="${app2:getFormButtonClasses()}"
                             onclick="location.href='${url}'"
                             tabindex="9">
                    <fmt:message key="Appointment.participant.delete"/>
                </html:button>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="APPOINTMENTPARTICIPANT" permission="DELETE">
                <app2:securitySubmit operation="${op}" functionality="APPOINTMENTPARTICIPANT"
                                     styleClass="${app2:getFormButtonClasses()}"
                                     tabindex="10">
                    <fmt:message key="Appointment.deleteGroup"/>
                </app2:securitySubmit>
            </app2:checkAccessRight>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="25">
                <fmt:message key="Common.cancel"/>
            </html:cancel>

        </div>
    </div>
</html:form>
