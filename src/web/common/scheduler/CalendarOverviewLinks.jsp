<%@ page import="com.piramide.elwis.utils.SchedulerConstants"%>
<%@ include file="/Includes.jsp" %>
<c:set var="SHAREDCALENDARS" value="<%=SchedulerConstants.OVERVIEW_SHAREDCALENDARS%>"/>

|
<app:link page="/Appointment/Overview.do?type=1&sharedCalendar=${SHAREDCALENDARS}" addModuleParams="false">
    <fmt:message key="Scheduler.today"/>
</app:link>
|
<app:link page="/Appointment/Overview.do?type=2&sharedCalendar=${SHAREDCALENDARS}" addModuleParams="false">
    <fmt:message key="Scheduler.thisWeek"/>
</app:link>
|
<app:link page="/Appointment/Overview.do?type=3&sharedCalendar=${SHAREDCALENDARS}" addModuleParams="false">
    <fmt:message key="Scheduler.thisMonth"/>
</app:link>
|
<app:link page="/Appointment/Overview.do?type=4&sharedCalendar=${SHAREDCALENDARS}" addModuleParams="false">
    <fmt:message key="Scheduler.thisYear"/>
</app:link>

