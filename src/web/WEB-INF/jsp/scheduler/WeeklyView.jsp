<%@ page import="com.piramide.elwis.utils.SchedulerConstants" %>
<%@ include file="/Includes.jsp" %>
<%@ taglib uri="/WEB-INF/scheduler.tld" prefix="cal" %>

<c:set var="returnType" value="<%= SchedulerConstants.RETURN_WEEK%>"/>

<tags:jscript language="JavaScript" src="/js/cacheable/jquery-dotdotdot/1.7.4/jquery.dotdotdot.min.js"/>

<script type="text/javascript">

    $(document).ready(function(){

        $(".spanDotdotdotKey").dotdotdot({
            ellipsis	: '',
            height		: 16,
            wrap		: 'letter'
        });

        $(".divDotdotdotKey").dotdotdot({
            ellipsis	: '',
            wrap		: 'letter'
        });
    });
</script>

<div>
    <cal:WeeklyBootstrapView url="/scheduler/AppointmentView.do?module=scheduler"
                             todayColor="#DECACA"
                             holidayColor="#ff5d0c"
                             addURL="/scheduler/Appointment/Forward/Create.do?create=true&module=scheduler&dto(returnType)=${returnType}"
                             modURL="/scheduler/Appointment/Forward/Update.do?update=true&module=scheculer"
                             delURL="/scheduler/Appointment/Forward/View/Delete.do?dto(withReferences)=true&module=scheduler"
                             tableWidth="100%" imgPathScheduler="${baselayout}"/>
</div>

<tags:jscript language="JavaScript" src="/js/common/wz_tooltip_5.31/wz_tooltip.js"/>

