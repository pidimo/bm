<%@ include file="/Includes.jsp" %>

<c:set var="schedulerAccessRightCheckerCode" value="${app2:registerUserToCheckAccessRights(pageContext.request)}"/>
<c:set var="appointmentFilters" value="${app2:getSingleAppointmentFilters(pageContext.request)}"/>
<c:set var="defaultFilter" value="${app2:getDefaultSingleAppointmentFilter()}"/>

<fmt:message var="dateTimePattern" key="dateTimePattern"/>
<fmt:message var="datePattern" key="datePattern"/>

<div class="${app2:getListWrapperClasses()}">

    <html:form action="/AppointmentList.do" focus="parameter(title)" styleClass="form-horizontal">

        <div class="row wrapperSearch">

            <div class="${app2:getFormGroupTwoSearchInput()}">
                <label class="${app2:getFormLabelClasses()} label-left">
                    <fmt:message key="Common.search"/>
                </label>

                <div class="${app2:getFormContainClasses(null)}">
                    <html:text property="parameter(title)"
                               styleClass="${app2:getFormInputClasses()} largeText"
                               tabindex="1"/>
                </div>
            </div>

            <div class="form-group col-sm-5">
                <label class="${app2:getFormLabelClasses()}"></label>

                <div class="${app2:getFormContainClasses(null)}">
                    <html:select property="parameter(stateFilter)" styleClass="${app2:getFormSelectClasses()} select"
                                 tabindex="3">
                        <html:options collection="appointmentFilters" property="value" labelProperty="label"/>
                    </html:select>
                </div>
            </div>

            <div class="form-group col-sm-1">
                <div class="col-sm-12">
                    <html:submit styleClass="${app2:getFormButtonClasses()}" tabindex="2">
                        <fmt:message key="Common.go"/>
                    </html:submit>
                </div>
            </div>

        </div>

        <div class="${app2:getAlphabetWrapperClasses()}">
            <fanta:alphabet action="AppointmentList.do" parameterName="titleAlphabet" mode="bootstrap"/>
        </div>
    </html:form>

    <app2:checkAccessRight functionality="APPOINTMENT" permission="CREATE">

        <c:set var="newButtonsTable" scope="page">
            <div class="${app2:getFormButtonWrapperClasses()}">
                <tags:buttonsTable>
                    <app:url
                            value="/Appointment/Forward/Create.do?create=true&type=5"
                            addModuleParams="true" var="newAppointmentUrl"/>
                    <input type="button"
                           class="${app2:getFormButtonClasses()}"
                           value="<fmt:message
                           key="Common.new"/>"
                           onclick="window.location ='${newAppointmentUrl}'">
                </tags:buttonsTable>
            </div>
        </c:set>

    </app2:checkAccessRight>


    <c:out value="${newButtonsTable}" escapeXml="false"/>

    <div class="table-responsive">
        <fanta:table mode="bootstrap" width="100%"
                     id="appointment"
                     action="AppointmentList.do"
                     imgPath="${baselayout}"
                     styleClass="${app2:getFantabulousTableClases()}">

            <c:set var="timeZone"
                   value="${app2:getSchedulerUserDateTimeZone(appointment.appUserId, pageContext.request)}"/>
            <c:set var="isPublicApp" value="${appointment.private eq '0'}"/>
            <c:set var="isOwner" value="${sessionScope.user.valueMap.userId == appointment.appUserId}"/>
            <c:set var="accessRightsMap"
                   value="${app2:getSchedulerUserPermissions(appointment.appUserId, pageContext.request, schedulerAccessRightCheckerCode)}"/>
            <c:set var="rightEditAppointment"
                   value="${app2:hasEditAppointmentPermission(accessRightsMap['public'], accessRightsMap['private'], isPublicApp)}"/>
            <c:set var="rightDelAppointment"
                   value="${app2:hasDelAppointmentPermission(accessRightsMap['public'], accessRightsMap['private'], isPublicApp)}"/>
            <c:set var="onlyAnonymousPermission"
                   value="${app2:hasOnlyAnonymousAppPermission(accessRightsMap['public'], accessRightsMap['private'], isPublicApp)}"/>
            <c:set var="renderRow" value="${onlyAnonymousPermission ? 'false' :'true'}"/>
            <c:set var="editAction"
                   value="Appointment/Forward/Update.do?type=6&dto(appointmentId)=${appointment.appointmentId}&appointmentId=${appointment.appointmentId}&dto(title)=${app2:encode(appointment.title)}&dto(userId)=${appointment.userId}&dto(private)=${!isPublicApp}&update=true${not (isOwner && rightEditAppointment) ? '&onlyView=true' : ''}&advancedListForward=AppointmentAdvancedSearch"/>
            <c:set var="deleteAction"
                   value="Appointment/Forward/Delete.do?type=6&dto(withReferences)=true&dto(appointmentId)=${appointment.appointmentId}&appointmentId=${appointment.appointmentId}&dto(title)=${app2:encode(appointment.title)}&dto(userId)=${appointment.userId}&dto(private)=${!isPublicApp}&operation=delete&fromView=true&advancedListForward=AppointmentAdvancedSearch"/>

            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="APPOINTMENT" permission="VIEW">
                    <c:choose>
                        <c:when test="${onlyAnonymousPermission}">
                            <fanta:actionColumn name="update"
                                                styleClass="listItem"
                                                headerStyle="listHeader"
                                                width="50%">
                                &nbsp;
                            </fanta:actionColumn>
                        </c:when>
                        <c:otherwise>
                            <fanta:actionColumn name="update"
                                                label="Common.update"
                                                action="${editAction}"
                                                title="Common.update"
                                                styleClass="listItem"
                                                headerStyle="listHeader"
                                                width="50%"
                                                glyphiconClass="${app2:getClassGlyphEdit()}"/>
                        </c:otherwise>
                    </c:choose>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="APPOINTMENT" permission="DELETE">
                    <fanta:actionColumn name="delete"
                                        styleClass="listItem"
                                        headerStyle="listHeader"
                                        width="50%">
                        <c:choose>
                            <c:when test="${rightDelAppointment or isOwner}">
                                <app:link action="${deleteAction}" titleKey="Common.delete">
                                    <span class="${app2:getClassGlyphTrash()}"></span>
                                </app:link>
                            </c:when>
                            <c:otherwise>
                                &nbsp;
                            </c:otherwise>
                        </c:choose>
                    </fanta:actionColumn>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="title"
                              action="${editAction}"
                              styleClass="listItem"
                              title="Appointment.name"
                              headerStyle="listHeader"
                              width="25%"
                              orderable="true"
                              maxLength="50"
                              renderData="${renderRow}"
                              renderUrl="${renderRow}">
                <fmt:message key="Scheduler.appointment.anonymous"/>
            </fanta:dataColumn>
            <fanta:dataColumn name="appTypeName"
                              styleClass="listItem"
                              title="Appointment.appType"
                              headerStyle="listHeader"
                              width="10%"
                              orderable="true"
                              renderData="${renderRow}">
                <fmt:message key="Scheduler.appointment.anonymous"/>
            </fanta:dataColumn>
            <fanta:dataColumn name="contactPersonName"
                              styleClass="listItem"
                              title="Appointment.contactPerson"
                              headerStyle="listHeader"
                              width="15%"
                              orderable="true"
                              renderData="${renderRow}">
                <fmt:message key="Scheduler.appointment.anonymous"/>
            </fanta:dataColumn>
            <fanta:dataColumn name="userName"
                              styleClass="listItem"
                              title="ContactAppointment.employee"
                              headerStyle="listHeader"
                              width="15%"
                              orderable="true"
                              renderData="${renderRow}">
                <fmt:message key="Scheduler.appointment.anonymous"/>
            </fanta:dataColumn>
            <fanta:dataColumn name="startDate"
                              styleClass="listItem"
                              title="Appointment.startDate"
                              headerStyle="listHeader"
                              width="15%"
                              orderable="true"
                              renderData="false">
                <c:choose>
                    <c:when test="${appointment.isAllDay == '1'}">
                        ${app2:getDateWithTimeZone(appointment.startDate, timeZone, datePattern)}
                    </c:when>
                    <c:otherwise>
                        ${app2:getDateWithTimeZone(appointment.startDate, timeZone, dateTimePattern)}
                    </c:otherwise>
                </c:choose>
            </fanta:dataColumn>
            <fanta:dataColumn name="endDate"
                              styleClass="listItem2"
                              title="Appointment.endDate"
                              headerStyle="listHeader"
                              width="15%"
                              orderable="true"
                              renderData="false">

                <c:choose>
                    <c:when test="${appointment.isAllDay == '1'}">
                        ${app2:getDateWithTimeZone(appointment.endDate, timeZone, datePattern)}
                    </c:when>
                    <c:otherwise>
                        ${app2:getDateWithTimeZone(appointment.endDate, timeZone, dateTimePattern)}
                    </c:otherwise>
                </c:choose>
            </fanta:dataColumn>
        </fanta:table>

        ${app2:clearSchedulerUserPermissions(pageContext.request, schedulerAccessRightCheckerCode)}

    </div>
    <c:out value="${newButtonsTable}" escapeXml="false"/>
</div>