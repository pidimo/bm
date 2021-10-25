<%@ include file="/Includes.jsp" %>

<html:form styleClass="form-horizontal"
           action="${action}?index=${param.index}&calendar=${param.calendar}&type=${param.type}&dto(title)=${app2:encode(param['dto(title)'])}"
           focusIndex="2">
    <div class="${app2:getFormClasses()}">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>

                <legend class="title">
                    <fmt:message key="Appointment.participant.delete"/>
                </legend>

                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}">
                        <fmt:message key="Appointment.userName"/>
                    </label>

                    <div class="${app2:getFormContainClasses(op == 'delete')}">
                        <app:text property="dto(participantName)"
                                  view="${op == 'delete'}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClasses()}">
                    <label class="${app2:getFormLabelClasses()}">
                        <fmt:message key="Appointment.appType"/>
                    </label>

                    <div class="${app2:getFormContainClasses(true)}">
                        <c:if test="${param.userType == '1'}">
                            <fmt:message key="User.intenalUser"/>
                        </c:if>
                        <c:if test="${param.userType == '0'}">
                            <fmt:message key="User.externalUser"/>
                        </c:if>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <c:if test="${param['dto(appointmentGroupName)'] != ''}">
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="${app2:getFormLabelClasses()}">
                            <fmt:message key="Appointment.groupName"/>
                        </label>

                        <div class="${app2:getFormContainClasses(op == 'delete')}">
                            <app:text property="dto(appointmentGroupName)"
                                      view="${op == 'delete'}"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </c:if>
                <html:hidden property="dto(op)" value="${op}"/>
                <c:if test="${op=='update' || op=='delete'}">
                    <html:hidden property="dto(scheduledUserId)"/>
                </c:if>
            </fieldset>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <c:if test="${op == 'create' || op == 'update'}">
                <app2:securitySubmit operation="${op}" property="dto(submit)" functionality="APPOINTMENTPARTICIPANT"
                                     styleClass="${app2:getFormButtonClasses()}" tabindex="19">
                    <fmt:message key="Common.save"/>
                </app2:securitySubmit>
            </c:if>
            <c:if test="${op == 'delete'}">
                <app2:securitySubmit operation="DELETE" property="dto(delete)"
                                     functionality="APPOINTMENTPARTICIPANT" styleClass="${app2:getFormButtonClasses()}"
                                     tabindex="19">
                    <fmt:message key="Common.delete"/>
                </app2:securitySubmit>
            </c:if>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="2">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
</html:form>