<%@ page import="com.piramide.elwis.utils.SchedulerConstants" %>
<%@ include file="/Includes.jsp" %>
<c:set var="SHAREDCALENDARS" value="<%=SchedulerConstants.OVERVIEW_SHAREDCALENDARS%>"/>

<li>
    <app:link page="/scheduler/Appointment/Overview.do?type=1&sharedCalendar=${SHAREDCALENDARS}" contextRelative="true" addModuleParams="false" addModuleName="false">
        <fmt:message key="Scheduler.today"/>
    </app:link>
</li>

<li>
    <app:link page="/scheduler/Appointment/Overview.do?type=2&sharedCalendar=${SHAREDCALENDARS}" contextRelative="true" addModuleParams="false" addModuleName="false">
        <fmt:message key="Scheduler.thisWeek"/>
    </app:link>
</li>

<li>
    <app:link page="/scheduler/Appointment/Overview.do?type=3&sharedCalendar=${SHAREDCALENDARS}" contextRelative="true" addModuleParams="false" addModuleName="false">
        <fmt:message key="Scheduler.thisMonth"/>
    </app:link>
</li>

<li>
    <app:link page="/scheduler/Appointment/Overview.do?type=4&sharedCalendar=${SHAREDCALENDARS}" contextRelative="true" addModuleParams="false" addModuleName="false">
        <fmt:message key="Scheduler.thisYear"/>
    </app:link>
</li>
