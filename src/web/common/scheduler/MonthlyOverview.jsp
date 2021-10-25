<%@ page import="com.piramide.elwis.utils.SchedulerConstants"%>
<%@ include file="/Includes.jsp" %>
<%@ taglib uri="/WEB-INF/scheduler.tld" prefix="cal" %>

<br>
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" >
<tr>
    <td>

    <table width="100%" border="0" align="center" cellpadding="3" cellspacing="1" class="container">
        <tr>
            <td >
                <c:set var="SHAREDCALENDARS" value="<%=SchedulerConstants.OVERVIEW_SHAREDCALENDARS%>"/>
                <cal:MonthlyOverview url="/scheduler/Appointment/Overview.do?module=${param.module}&sharedCalendar=${SHAREDCALENDARS}"
                   todayColor="#DECACA"
                   holidayColor="#ff5d0c"
                   tableWidth="100%" imgPathScheduler="${baselayout}"
                 />
                
                 <%--wz_tooltip.js always the end--%>
                 <tags:jscript language="JavaScript" src="/js/common/wz_tooltip.js"/>
            </td>
        </tr>
    </table>
    </td>
</tr>
</table>
