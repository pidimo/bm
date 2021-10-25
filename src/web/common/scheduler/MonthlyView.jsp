<%@ page import="com.piramide.elwis.utils.SchedulerConstants" %>
<%@ include file="/Includes.jsp" %>
<%@ taglib uri="/WEB-INF/scheduler.tld" prefix="cal" %>

<c:set var="returnType" value="<%= SchedulerConstants.RETURN_MONTH%>"/>
<br>
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" >
<tr>
    <td>

    <table width="100%" border="0" align="center" cellpadding="3" cellspacing="1" class="container">
    <tr>
            <td >

<cal:MonthlyView url="/scheduler/AppointmentView.do?module=${param.module}"
   todayColor="#DECACA"
   holidayColor="#ff5d0c"
   addURL="/scheduler/Appointment/Forward/Create.do?create=true&module=${param.module}&dto(returnType)=${returnType}"
   modURL="/scheduler/Appointment/Forward/Update.do?update=true&module=${param.module}"
   delURL="/scheduler/Appointment/Forward/View/Delete.do?dto(withReferences)=true&module=scheduler"
   tableWidth="100%" imgPathScheduler="${baselayout}"
 />
   <%--delURL="/scheduler/Appointment/Forward/Delete.do?dto(withReferences)=true&module=${param.module}"--%>
 <%--wz_tooltip.js always the end--%>
 <tags:jscript language="JavaScript" src="/js/common/wz_tooltip.js"/>

            </td>
        </tr>
    </table>
    </td>
</tr>
</table>
