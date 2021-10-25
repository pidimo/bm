<%@ page import="com.piramide.elwis.utils.SchedulerConstants"%>
<%@ taglib uri="/WEB-INF/scheduler.tld" prefix="cal" %>
<%@ include file="/Includes.jsp" %>

<br>
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" >
<tr>
    <td>

    <table width="100%" border="0" align="center" cellpadding="3" cellspacing="1" class="container">
    <tr>
        <td>
            <c:set var="SHAREDCALENDARS" value="<%=SchedulerConstants.OVERVIEW_SHAREDCALENDARS%>"/>
            <cal:DailyOverview url="/scheduler/Appointment/Overview.do?module=${param.module}&sharedCalendar=${SHAREDCALENDARS}"
             todayColor="#DECACA" tableWidth="100%" imgPathScheduler="${baselayout}"
             />
            <%--wz_tooltip.js always the end--%>
            <tags:jscript language="JavaScript" src="/js/common/wz_tooltip.js"/>
            </td>
        </tr>
    </table>
    </td>
</tr>
</table>