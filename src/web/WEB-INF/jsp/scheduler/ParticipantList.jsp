<%@ include file="/Includes.jsp" %>

<c:set  var="userPermissionIsNULL" value="${publicSchedulerUserPermission == null && privateSchedulerUserPermission == null}"/>
<c:if test="${!userPermissionIsNULL}">
    <fanta:label var="private" listName="lightAppointmentList" module="/scheduler" patron="0"
                 label="private" columnOrder="private">
        <fanta:parameter field="appointmentId" value="${not empty param.appointmentId ? param.appointmentId : 0}"/>
    </fanta:label>
</c:if>

<c:set var="isPublicApp" value="${private eq '0'}"/>
<c:set var="rightEditAppointment" value="${userPermissionIsNULL ? true : app2:hasEditAppointmentPermission(publicSchedulerUserPermission, privateSchedulerUserPermission, isPublicApp)}" />


<c:if test="${!param.onlyView}">

    <div class="${app2:getFormButtonWrapperClasses()}">
        <app2:checkAccessRight functionality="APPOINTMENTPARTICIPANT" permission="CREATE">
            <c:if test="${rightEditAppointment}">
           <c:url var="url" value="/scheduler/AppointmentParticipant/Forward/Group/Create.do">
                <c:param name="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
                <c:param name="appointmentId" value="${param.appointmentId}"/>
                <c:param name="index" value="${param.index}"/>
                <c:param name="dto(title)" value="${param['dto(title)']}"/>
                <c:param name="type" value="${param.type}"/>
                <c:param name="calendar" value="${param.calendar}"/>
            </c:url>
            <html:button property="" styleClass="${app2:getFormButtonClasses()}" onclick="location.href='${url}'" >
                <fmt:message key="Appoinment.addUserGroup"/>
            </html:button>
            </c:if>
         </app2:checkAccessRight>

            <app2:checkAccessRight functionality="APPOINTMENTPARTICIPANT" permission="CREATE">
            <c:if test="${rightEditAppointment}">
                    <c:url var="url1" value="/scheduler/AppointmentParticipant/Forward/Create.do?parameter(active)=1" >
                        <c:param name="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
                        <c:param name="appointmentId" value="${param.appointmentId}"/>
                        <c:param name="index" value="${param.index}"/>
                        <c:param name="dto(title)" value="${param['dto(title)']}"/>
                        <c:param name="type" value="${param.type}"/>
                        <c:param name="calendar" value="${param.calendar}"/>
                    </c:url>
                    <html:button property="" styleClass="${app2:getFormButtonClasses()}" onclick="location.href='${url1}'" >
                        <fmt:message key="Appoinment.addUser"/>
                    </html:button>
                    </c:if>
            </app2:checkAccessRight>
    </div>

</c:if>

<div class="table-responsive">
    <fanta:table mode="bootstrap" list="participantList"
                 styleClass="${app2:getFantabulousTableClases()}"
                 width="100%"
                 id="participant"
                 action="Appointment/ParticipantList.do?appointmentId=${param.appointmentId}&onlyView=${param.onlyView}&calendar=${param.calendar}&type=${param.type}"
                 imgPath="${baselayout}">
        <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="2%" >
            <c:set var="deleteAction" value="AppointmentParticipant/Forward/Delete.do?dto(withReferences)=true&dto(scheduledUserId)=${participant.scheduledUserId}&dto(participantName)=${app2:encode(participant.userName)}&dto(appointmentGroupName)=${app2:encode(participant.groupName)}&appointmentId=${participant.appId}&userGroupId=${participant.groupId}&dto(title)=${app2:encode(param['dto(title)'])}&index=${param.index}&calendar=${param.calendar}&userType=${participant.type}"/>
            <fanta:actionColumn name="" styleClass="listItem" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="APPOINTMENTPARTICIPANT" permission="DELETE">
                    <c:choose>
                        <c:when test="${participant.appUserId != participant.userId && participant.appUserId == sessionScope.user.valueMap.schedulerUserId and rightEditAppointment}" >
                            <html:link action="${deleteAction}" titleKey="Common.delete">
                                <span class="${app2:getClassGlyphTrash()}"></span>
                            </html:link>
                        </c:when>
                        <c:otherwise>
                            &nbsp;
                        </c:otherwise>
                    </c:choose>
                </app2:checkAccessRight>
            </fanta:actionColumn>
        </fanta:columnGroup>
        <fanta:dataColumn name="userName" styleClass="listItem" title="Appointment.userName"  headerStyle="listHeader" width="40%" orderable="true"/>
        <fanta:dataColumn name="type" styleClass="listItem" title="User.typeUser"  renderData="false"   headerStyle="listHeader" width="35%" orderable="true">
            <c:if test="${participant.type == '1'}">
                <fmt:message key="User.intenalUser" />
            </c:if>
            <c:if test="${participant.type == '0'}">
                <fmt:message key="User.externalUser" />
            </c:if>
        </fanta:dataColumn>
        <fanta:dataColumn  name="groupName" styleClass="listItem2" title="Appointment.groupName"  headerStyle="listHeader" width="30%" orderable="true" maxLength="25" />
    </fanta:table>
</div>

