<%@ page import="com.piramide.elwis.utils.SchedulerConstants" %>
<%@ include file="/Includes.jsp" %>
<%@ taglib uri="/WEB-INF/scheduler.tld" prefix="cal" %>

<c:set var="SHAREDCALENDARS" value="<%=SchedulerConstants.OVERVIEW_SHAREDCALENDARS%>"/>
<cal:MonthlyOverviewBootstrap url="/scheduler/Appointment/Overview.do?module=${param.module}&sharedCalendar=${SHAREDCALENDARS}"
                     todayColor="#DECACA"
                     holidayColor="#ff5d0c"
                     tableWidth="100%" imgPathScheduler="${baselayout}"
        />

<%--wz_tooltip.js always the end--%>
<tags:jscript language="JavaScript" src="/js/common/wz_tooltip_5.31/wz_tooltip.js"/>