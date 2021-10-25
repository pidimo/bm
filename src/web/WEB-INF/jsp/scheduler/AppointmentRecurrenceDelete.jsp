<%@ include file="/Includes.jsp" %>

<html:form
        action="${action}?dto(withReferences)=true&module=scheduler&dto(appointmentId)=${appointmentForm.dtoMap.appointmentId}&type=${param.type}&operation=delete&calendar=${param.calendar}&dto(currentDate)=${param.currentDate}">
    <html:hidden property="dto(calendar)" value="${param.calendar}"/>
    <html:hidden property="dto(currentDate)" value="${param.currentDate}"/>
    <html:hidden property="dto(title)" value="${param['dto(title)']}"/>
    <div class="${app2:getFormClassesLarge()}">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <fmt:message key="Appointment.delete"/>
                </legend>
                <fmt:message var="datePattern" key="datePattern"/>
                <c:if test="${!param.root}">
                    <fmt:message key="Appointment.deleteCurrentMessage"/>
                    <strong>${param.date}</strong> ?
                </c:if>
                <c:if test="${param.root}">
                    <fmt:message key="Appointment.deleteCurrentRootMessage"/>
                </c:if>
            </fieldset>
        </div>
        <div class="row">
            <div class="col-xs-12">
                <c:if test="${!param.root}">
                    <app2:checkAccessRight functionality="APPOINTMENT" permission="DELETE">
                        <c:url var="url"
                               value="/scheduler/Appointment/View/Delete.do?dto(appointmentId)=${appointmentForm.dtoMap.appointmentId}&type=${param.type}&dto(currentDate)=${param.currentDate}&calendar=${param.calendar}&dto(title)=${app2:encode(param['dto(title)'])}"/>
                        <html:button property="dto(answer)" styleClass="button ${app2:getFormButtonClasses()} marginButton" onclick="location.href='${url}'"
                                     tabindex="9">
                            <fmt:message key="Appointment.deleteCurrent"/>
                        </html:button>
                    </app2:checkAccessRight>
                </c:if>
                <app2:securitySubmit operation="${op}" functionality="APPOINTMENT"
                                     styleClass="button ${app2:getFormButtonClasses()} marginButton" tabindex="10">
                    <fmt:message key="Appointment.deleteSerie"/>
                </app2:securitySubmit>

                <c:url var="urlCancel" value="/scheduler/Appointment/Forward/View/Delete.do">
                    <c:param name="redirect" value="true"/>
                    <c:param name="type" value="${param.type}"/>
                    <c:param name="oldType" value="${param.type}"/>
                    <c:param name="calendar" value="${param.calendar}"/>
                    <c:param name="currentDate" value="${param.currentDate}"/>
                </c:url>
                <html:button property="dto(cancel)" styleClass="button ${app2:getFormButtonCancelClasses()} marginButton"
                             onclick="location.href='${urlCancel}'" tabindex="11">
                    <fmt:message key="Common.cancel"/>
                </html:button>
            </div>
        </div>
    </div>
</html:form>