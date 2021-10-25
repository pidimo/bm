<%@ page import="com.piramide.elwis.utils.SchedulerConstants" %>
<%@ taglib uri="/WEB-INF/scheduler.tld" prefix="cal" %>
<%@ include file="/Includes.jsp" %>

<c:set var="returnType" value="<%= SchedulerConstants.RETURN_DAY%>"/>
<br>
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" >
<tr>
    <td>

    <table width="100%" border="0" align="center" cellpadding="3" cellspacing="1" class="container">
    <tr>
            <td>
<cal:DailyView url="/scheduler/AppointmentView.do?module=${param.module}"
 todayColor="#DECACA" tableWidth="100%" imgPathScheduler="${baselayout}"
 addURL="/scheduler/Appointment/Forward/Create.do?create=true&module=${param.module}&dto(returnType)=${returnType}"
 modURL="/scheduler/Appointment/Forward/Update.do?update=true&module=${param.module}"
 delURL="/scheduler/Appointment/Forward/View/Delete.do?dto(withReferences)=true&module=${param.module}" 
 />
<%--wz_tooltip.js always the end--%>
<tags:jscript language="JavaScript" src="/js/common/wz_tooltip.js"/>

            </td>
        </tr>
    </table>
    </td>
</tr>
</table>