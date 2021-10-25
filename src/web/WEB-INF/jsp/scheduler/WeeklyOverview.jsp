<%@ page import="com.piramide.elwis.utils.SchedulerConstants" %>
<%@ include file="/Includes.jsp" %>
<%@ taglib uri="/WEB-INF/scheduler.tld" prefix="cal" %>

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
    <c:set var="SHAREDCALENDARS" value="<%=SchedulerConstants.OVERVIEW_SHAREDCALENDARS%>"/>
    <cal:WeeklyBootstrapOverview
            url="/scheduler/Appointment/Overview.do?module=${param.module}&sharedCalendar=${SHAREDCALENDARS}"
            todayColor="#DECACA"
            holidayColor="#ff5d0c"
            tableWidth="100%" imgPathScheduler="${baselayout}"/>

    <tags:jscript language="JavaScript" src="/js/common/wz_tooltip_5.31/wz_tooltip.js"/>
</div>