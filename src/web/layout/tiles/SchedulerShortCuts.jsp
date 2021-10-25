<%@ page import="com.piramide.elwis.cmd.schedulermanager.LightlyTaskCmd"%>
<%@ page import="com.piramide.elwis.utils.SchedulerConstants" %>
<%@ page import="net.java.dev.strutsejb.dto.ResultDTO" %>
<%@ page import="net.java.dev.strutsejb.web.BusinessDelegate" %>
<%@ include file="/Includes.jsp" %>
<c:set var="isUser" value="${sessionScope.user.valueMap.schedulerUserId == sessionScope.user.valueMap.userId}"
       scope="request"/>
<c:set var="rightAddAppointment"
       value="${app2:hasAddAppointmentPermission(pageContext.request)}"
       scope="request"/>

<%
    boolean errorPage = false;
    LightlyTaskCmd lightlyTaskCmd = new LightlyTaskCmd();
    String taskId = null;
    if(request.getParameter("taskId")!=null && !"".equals(request.getParameter("taskId")))
        taskId = request.getParameter("taskId");
    else
        taskId = request.getParameter("dto(taskId)");
    if(taskId!=null){
        lightlyTaskCmd.putParam("taskId", taskId);
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(lightlyTaskCmd, request);
            request.setAttribute("userId", resultDTO.get("userId"));
            request.setAttribute("title1", resultDTO.get("title"));
            request.setAttribute("taskId", taskId);
            if(resultDTO.isFailure())
                errorPage = true;
        } catch (Exception e) {
            errorPage = true;
        }

        request.setAttribute("errorPage", new Boolean(errorPage));
    }
%>

<app2:jScriptUrl url="/scheduler/${(view=='calendar' ? 'AppointmentView.do' : 'AppointmentList.do')}?simple=true"
                 var="jsChangeSchedulerUserUrl" addModuleParams="false">
    <app2:jScriptUrlParam param="type" value="document.getElementById('type').value"/>
    <app2:jScriptUrlParam param="calendar" value="document.getElementById('calendar').value"/>
    <app2:jScriptUrlParam param="schedulerUserId" value="id"/>
</app2:jScriptUrl>


<c:set var="SHAREDCALENDARS" value="<%=SchedulerConstants.OVERVIEW_SHAREDCALENDARS%>"/>
<c:set var="isSharedCalendar" value="${not empty viewSharedCalendar && viewSharedCalendar eq SHAREDCALENDARS}"/>

<app2:jScriptUrl url="/scheduler/Calendar/Forward/Overview.do?sharedCalendar=${SHAREDCALENDARS}"
                 var="jsSharedCalendarUrl" addModuleParams="false">
    <app2:jScriptUrlParam param="dto(outputViewType)" value="document.getElementById('type').value"/>
</app2:jScriptUrl>

<script>
    function changeSchedulerUser(id) {
        if(id == ${SHAREDCALENDARS}){
            window.location = ${jsSharedCalendarUrl};
        } else {
            window.location = ${jsChangeSchedulerUserUrl};
        }
    }

</script>


<%--
var id = document.getElementById('schedulerUser');
<c:url var="url" value="/scheduler/${(view=='calendar' ? 'AppointmentView.do' : 'AppointmentList.do')}?type=${param.type}&calendar=${param.calendar}"/>
var url = "${url}&schedulerUserId=" + id.value ;
--%>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
<td valign="middle" align="left" class="moduleShortCut" width="100%">

    <app2:checkAccessRight functionality="APPOINTMENT" permission="VIEW">
        <c:choose>
            <c:when test="${isSharedCalendar}">
                <c:import url="/common/scheduler/CalendarOverviewLinks.jsp"/>
            </c:when>
            <c:otherwise>
                <c:import url="/common/scheduler/CalendarLinks.jsp"/>
            </c:otherwise>
        </c:choose>

        <c:if test="${!isSharedCalendar}">
            |
            <app:link page="/AppointmentList.do?simple=true" addModuleParams="false">
                <fmt:message key="Scheduler.appoinmentSearch"/>
            </app:link>
        </c:if>

    </app2:checkAccessRight>
    <c:if test="${param.type != null and param.calendar!=null}">
        <c:set var="back" value="&calendar=${param.calendar}" scope="request"/>
    </c:if>


    <c:if test="${isUser && !isSharedCalendar}">
        <app2:checkAccessRight functionality="TASK" permission="VIEW">
            |
            <app:link page="/TaskList.do?simple=true" addModuleParams="false"><fmt:message key="Scheduler.taskSearch"/>
            </app:link>
        </app2:checkAccessRight>
        &nbsp;|
    </c:if>

</td>
<td align="right" class="moduleShortCut" nowrap="nowrap">
    <c:if test="${isUser && !isSharedCalendar}">
        <app2:checkAccessRight functionality="GRANTACCESS" permission="VIEW">

            <app:link page="/GrantAccess/List.do" addModuleParams="false"><fmt:message
                    key="Scheduler.grantAccess"/></app:link>

        </app2:checkAccessRight>
    </c:if>
</td>

<app2:checkAccessRight functionality="APPOINTMENT" permission="VIEW">
    <td align="right" class="moduleShortCut" nowrap="nowrap">
        |
        <fmt:message key="Scheduler.user.viewCalendar"/>&nbsp;
    </td>
    <td align="left" class="moduleShortCut" nowrap="nowrap" style="vertical-align:top;">

        <c:set var="listUserView" value="${app2:getUserViewCalendar(pageContext.request)}"/>
        <html:select property="schedulerUserId" value="${not empty param.sharedCalendar ? param.sharedCalendar : sessionScope.user.valueMap.schedulerUserId}"
                     styleId="schedulerUser" styleClass="select"
                     onchange="changeSchedulerUser(document.getElementById('schedulerUser').value)">
            <html:options collection="listUserView" property="value" labelProperty="label"/>
        </html:select>
        <html:hidden property="calendar" value="${param.calendar}" styleId="calendar"/>
        <html:hidden property="type" value="${param.type}" styleId="type"/>

    </td>
</app2:checkAccessRight>

<app2:checkAccessRight functionality="TASK" permission="VIEW">
    <c:if test="${isUser && !isSharedCalendar}">
        <td align="left" class="moduleShortCut" width="5%" nowrap="nowrap">
            |
            <tags:pullDownMenu titleKey="Report.plural" align="right">
                <tags:pullDownMenuItem action="/Report/TaskList.do" titleKey="Task.Report.TaskList" functionality="TASK"
                                       permission="VIEW"/>
                <tags:pullDownMenuItem
                        action="/Report/TaskSingleList.do?parameter(taskId)=${taskId}&parameter(task)=${app2:encode(title1)}" titleKey="Scheduler.Report.TaskSingleList"
                        functionality="TASK" permission="VIEW"/>
            </tags:pullDownMenu>
            &nbsp;
        </td>
    </c:if>
</app2:checkAccessRight>



</tr>
</table>