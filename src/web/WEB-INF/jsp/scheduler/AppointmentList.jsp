<%@ page import="com.piramide.elwis.utils.SchedulerConstants" %>
<%@ page import="com.piramide.elwis.web.admin.session.User" %>
<%@ page import="com.piramide.elwis.web.common.util.RequestUtils" %>
<%@ page import="org.joda.time.DateTime" %>
<%@ page import="org.joda.time.DateTimeZone" %>
<%@ include file="/Includes.jsp" %>

<script language="JavaScript">
    function onSubmit() {
        document.forms[1].submit();
    }
</script>
<c:set var="timeZone" value="${timeZone}"/>
<%
    User user = RequestUtils.getUser(request);
    DateTimeZone zone = null;
    String timeZone = null;
    if (user != null) {
        zone = (DateTimeZone) user.getValue("dateTimeZone");
    } else {
        zone = DateTimeZone.getDefault();
    }
    DateTime createDateTime = new DateTime(zone);
    long date = createDateTime.getMillis();
%>
<c:set var="returnType" value="<%= SchedulerConstants.RETURN_SEARCHLIST%>"/>

<html:form action="/AppointmentList.do?simple=true" focus="parameter(title)" styleClass="form-horizontal">
    <fieldset>
        <legend class="title">
            <fmt:message key="Scheduler.appoinmentSearch"/>
        </legend>
        <div class="${app2:getSearchWrapperClasses()}">
            <label class="${app2:getFormLabelOneSearchInput()} text-left" for="title_id">
                <fmt:message key="Common.search"/>
            </label>

            <div class="${app2:getFormOneSearchInput()}">
                <div class="input-group">
                    <html:text property="parameter(title)" styleId="title_id"
                               styleClass="largeText ${app2:getFormInputClasses()}"/>
               <span class="input-group-btn">
                   <html:submit styleClass="button ${app2:getFormButtonClasses()}"><fmt:message
                           key="Common.go"/></html:submit>
               </span>
                </div>
            </div>
            <c:if test="${!app2:hasOnlyAnonymousAppPermissionPublicAndPrivate(publicSchedulerUserPermission, privateSchedulerUserPermission)}">
           <span class="pull-left">
               <app:link action="/AppointmentAdvancedList.do" styleClass="btn btn-link">
                   <fmt:message key="Common.advancedSearch"/>
               </app:link>
           </span>
            </c:if>
        </div>
    </fieldset>
</html:form>
<div class="${app2:getAlphabetWrapperClasses()}">
    <fanta:alphabet action="AppointmentList.do?simple=true" mode="bootstrap" parameterName="titleAlphabet"/>
</div>
<app2:checkAccessRight functionality="APPOINTMENT" permission="CREATE">
    <%-- rightAddAppointment and back variables set on SchedulerShortCuts.jsp--%>
    <c:if test="${rightAddAppointment}">
        <c:set var="newButtonsTable" scope="page">
            <app:url value="/Appointment/Forward/Create.do?create=true&dto(returnType)=${returnType}&type=5${back}"
                     addModuleParams="false" var="newAppointmentUrl"/>
            <div class="${app2:getFormButtonWrapperClasses()}">
                <input type="button" class="button ${app2:getFormButtonClasses()}"
                       value="<fmt:message key="Common.new"/>"
                       onclick="window.location ='${newAppointmentUrl}'">
            </div>
        </c:set>
    </c:if>
</app2:checkAccessRight>
<c:out value="${newButtonsTable}" escapeXml="false"/>

<c:set var="currentDate" value="date"/>
<c:set var="userPermissionIsNULL"
       value="${publicSchedulerUserPermission == null && privateSchedulerUserPermission == null}"/>

<fmt:message var="dateTimePattern" key="dateTimePattern"/>
<div class="table-responsive">
    <fanta:table mode="bootstrap" list="appointmentList" width="100%"
                 styleClass="${app2:getFantabulousTableClases()}"
                 id="appointment" action="AppointmentList.do?simple=true"
                 imgPath="${baselayout}">

        <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">

            <c:set var="isPublicApp" value="${appointment.private eq '0'}"/>
            <c:set var="rightEditAppointment"
                   value="${userPermissionIsNULL ? true : app2:hasEditAppointmentPermission(publicSchedulerUserPermission, privateSchedulerUserPermission, isPublicApp)}"/>
            <c:set var="rightDelAppointment"
                   value="${userPermissionIsNULL ? true : app2:hasDelAppointmentPermission(publicSchedulerUserPermission, privateSchedulerUserPermission, isPublicApp)}"/>
            <c:set var="onlyAnonymousPermission"
                   value="${userPermissionIsNULL ? false : app2:hasOnlyAnonymousAppPermission(publicSchedulerUserPermission, privateSchedulerUserPermission, isPublicApp)}"/>
            <c:set var="renderRow" value="${onlyAnonymousPermission ? 'false' :'true'}"/>

            <c:set var="isOwner"
                   value="${sessionScope.user.valueMap.schedulerUserId == appointment.appUserId}"/>
            <c:set var="editAction"
                   value="Appointment/Forward/Update.do?type=5&dto(appointmentId)=${appointment.appointmentId}&dto(title)=${app2:encode(appointment.title)}&dto(appUserId)=${appointment.appUserId}&dto(private)=${!isPublicApp}&update=true${not (isOwner && rightEditAppointment) ? '&onlyView=true' : ''}"/>
            <c:set var="deleteAction"
                   value="Appointment/Forward/Delete.do?type=5&dto(withReferences)=true&dto(appointmentId)=${appointment.appointmentId}&dto(title)=${app2:encode(appointment.title)}&dto(private)=${!isPublicApp}&operation=delete&dto(appUserId)=${appointment.appUserId}&fromView=true"/>

            <app2:checkAccessRight functionality="APPOINTMENT" permission="VIEW">
                <c:choose>
                    <c:when test="${onlyAnonymousPermission}">
                        <fanta:actionColumn name="update" styleClass="listItem" headerStyle="listHeader"
                                            width="50%">
                            &nbsp;
                        </fanta:actionColumn>
                    </c:when>
                    <c:otherwise>
                        <fanta:actionColumn name="update" label="Common.update" action="${editAction}"
                                            title="Common.update"
                                            styleClass="listItem" headerStyle="listHeader" width="50%"
                                            glyphiconClass="${app2:getClassGlyphEdit()}"/>
                    </c:otherwise>
                </c:choose>
            </app2:checkAccessRight>

            <app2:checkAccessRight functionality="APPOINTMENT" permission="DELETE">
                <fanta:actionColumn name="delete" styleClass="listItem" headerStyle="listHeader" width="50%">
                    <c:choose>
                        <c:when test="${rightDelAppointment and (appointment.appUserId == sessionScope.user.valueMap['schedulerUserId'])}">
                            <html:link action="${deleteAction}" titleKey="Common.delete">
                                <span class="${app2:getClassGlyphTrash()}"></span>
                            </html:link>
                        </c:when>
                        <c:otherwise>
                            &nbsp;
                        </c:otherwise>
                    </c:choose>
                </fanta:actionColumn>
            </app2:checkAccessRight>
        </fanta:columnGroup>
        <fanta:dataColumn name="title" action="${editAction}" styleClass="listItem" title="Appointment.name"
                          headerStyle="listHeader" width="30%" orderable="true" maxLength="50"
                          renderData="${renderRow}" renderUrl="${renderRow}">
            <fmt:message key="Scheduler.appointment.anonymous"/>
        </fanta:dataColumn>
        <fanta:dataColumn name="appTypeName" styleClass="listItem" title="Appointment.appType"
                          headerStyle="listHeader" width="15%" orderable="true"
                          renderData="${renderRow}">
            <fmt:message key="Scheduler.appointment.anonymous"/>
        </fanta:dataColumn>
        <fanta:dataColumn name="ruleType" styleClass="listItem" title="Appointment.periodRecurrence"
                          headerStyle="listHeader"
                          width="20%" orderable="false" renderData="false">
            <c:choose>
                <c:when test="${onlyAnonymousPermission}">
                    <fmt:message key="Scheduler.appointment.anonymous"/>
                </c:when>
                <c:when test="${appointment.ruleType == '1'}">
                    <fmt:message key="Scheduler.calendarView.daily"/>
                </c:when>
                <c:when test="${appointment.ruleType == '2'}">
                    <fmt:message key="Scheduler.calendarView.weekly"/>
                </c:when>
                <c:when test="${appointment.ruleType == '3'}">
                    <fmt:message key="Scheduler.calendarView.monthly"/>
                </c:when>
                <c:when test="${appointment.ruleType == '4'}">
                    <fmt:message key="Scheduler.calendarView.yearly"/>
                </c:when>
                <c:otherwise>
                    &nbsp;
                </c:otherwise>
            </c:choose>
        </fanta:dataColumn>
        <fanta:dataColumn name="startDate" styleClass="listItem" title="Appointment.startDate"
                          headerStyle="listHeader"
                          width="15%" orderable="true" renderData="false">
            <fmt:message var="datePattern" key="datePattern"/>
            <c:choose>
                <c:when test="${appointment.isAllDay == '1'}">
                    ${app2:getDateWithTimeZone(appointment.startDate, timeZone, datePattern)}
                </c:when>
                <c:otherwise>
                    ${app2:getDateWithTimeZone(appointment.startDate, timeZone, dateTimePattern)}
                </c:otherwise>
            </c:choose>
        </fanta:dataColumn>
        <fanta:dataColumn name="endDate" styleClass="listItem2" title="Appointment.endDate"
                          headerStyle="listHeader" width="15%" orderable="true" renderData="false">
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
</div>

<c:out value="${newButtonsTable}" escapeXml="false"/>
