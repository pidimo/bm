<%@ include file="/Includes.jsp" %>
|
<app:link page="/AppointmentView.do?type=1" addModuleParams="false">
    <fmt:message key="Scheduler.today"/>
</app:link>
|
<app:link page="/AppointmentView.do?type=2" addModuleParams="false">
    <fmt:message key="Scheduler.thisWeek"/>
</app:link>
|
<app:link page="/AppointmentView.do?type=3" addModuleParams="false">
    <fmt:message key="Scheduler.thisMonth"/>
</app:link>
|
<app:link page="/AppointmentView.do?type=4" addModuleParams="false">
    <fmt:message key="Scheduler.thisYear"/>
</app:link>

