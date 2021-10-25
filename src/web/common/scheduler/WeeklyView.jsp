<%@ page import="com.piramide.elwis.utils.SchedulerConstants" %>
<%@ include file="/Includes.jsp" %>
<%@ taglib uri="/WEB-INF/scheduler.tld" prefix="cal" %>

<c:set var="returnType" value="<%= SchedulerConstants.RETURN_WEEK%>"/>

<br>
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" >
<tr>
    <td>

    <table width="100%" border="0" align="center" cellpadding="3" cellspacing="1" class="container">
    <tr>
            <td >

<cal:WeeklyView  url="/scheduler/AppointmentView.do?module=scheduler"
                 todayColor="#DECACA"
                 holidayColor="#ff5d0c"
                 addURL="/scheduler/Appointment/Forward/Create.do?create=true&module=scheduler&dto(returnType)=${returnType}"
                 modURL="/scheduler/Appointment/Forward/Update.do?update=true&module=scheculer"
                 delURL="/scheduler/Appointment/Forward/View/Delete.do?dto(withReferences)=true&module=scheduler"                 
                 tableWidth="100%" imgPathScheduler="${baselayout}"
 />
<%--delURL="/scheduler/Appointment/Forward/Delete.do?dto(withReferences)=true&module=scheduler"--%>

<%--wz_tooltip.js always the end--%>
 <tags:jscript language="JavaScript" src="/js/common/wz_tooltip.js"/>

            </td>
        </tr>
    </table>
    </td>
</tr>
</table>
