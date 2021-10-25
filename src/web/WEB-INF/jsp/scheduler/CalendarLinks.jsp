<%@ include file="/Includes.jsp" %>

<li>
    <app:link page="/scheduler/AppointmentView.do?type=1" contextRelative="true" addModuleParams="false" addModuleName="false">
        <fmt:message key="Scheduler.today"/>
    </app:link>
</li>

<li>
    <app:link page="/scheduler/AppointmentView.do?type=2" contextRelative="true" addModuleParams="false" addModuleName="false">
        <fmt:message key="Scheduler.thisWeek"/>
    </app:link>
</li>

<li>
    <app:link page="/scheduler/AppointmentView.do?type=3" contextRelative="true" addModuleParams="false" addModuleName="false">
        <fmt:message key="Scheduler.thisMonth"/>
    </app:link>
</li>

<li>
    <app:link page="/scheduler/AppointmentView.do?type=4" contextRelative="true" addModuleParams="false" addModuleName="false">
        <fmt:message key="Scheduler.thisYear"/>
    </app:link>
</li>
