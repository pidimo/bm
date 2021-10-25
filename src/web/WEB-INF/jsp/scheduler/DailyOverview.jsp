<%@ page import="com.piramide.elwis.utils.SchedulerConstants" %>
<%@ taglib uri="/WEB-INF/scheduler.tld" prefix="cal" %>
<%@ include file="/Includes.jsp" %>

<c:set var="SHAREDCALENDARS" value="<%=SchedulerConstants.OVERVIEW_SHAREDCALENDARS%>"/>
<cal:DailyOverviewBootsatrap url="/scheduler/Appointment/Overview.do?module=${param.module}&sharedCalendar=${SHAREDCALENDARS}"
                   todayColor="#DECACA" tableWidth="100%" imgPathScheduler="${baselayout}"
        />

<%--wz_tooltip.js always the end--%>
<tags:jscript language="JavaScript" src="/js/common/wz_tooltip_5.31/wz_tooltip.js"/>
