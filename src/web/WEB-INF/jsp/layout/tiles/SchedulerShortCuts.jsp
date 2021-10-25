<%@ page import="com.piramide.elwis.cmd.schedulermanager.LightlyTaskCmd"%>
<%@ page import="com.piramide.elwis.utils.SchedulerConstants" %>
<%@ page import="net.java.dev.strutsejb.dto.ResultDTO" %>
<%@ page import="net.java.dev.strutsejb.web.BusinessDelegate" %>
<%@ include file="/Includes.jsp" %>

<c:if test="${empty sessionScope.user.valueMap.schedulerUserId}">
  ${app2:initializeSchedulerUserId(pageContext.request)}
</c:if>

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

<ul class="dropdown-menu">

  <app2:checkAccessRight functionality="APPOINTMENT" permission="VIEW">
    <c:choose>
      <c:when test="${isSharedCalendar}">
        <c:import url="/WEB-INF/jsp/scheduler/CalendarOverviewLinks.jsp"/>
      </c:when>
      <c:otherwise>
        <c:import url="/WEB-INF/jsp/scheduler/CalendarLinks.jsp"/>
      </c:otherwise>
    </c:choose>

    <c:if test="${!isSharedCalendar}">
      <li>
        <app:link page="/scheduler/AppointmentList.do?simple=true" contextRelative="true" addModuleParams="false" addModuleName="false">
          <fmt:message key="Scheduler.appoinmentSearch"/>
        </app:link>
      </li>
    </c:if>

  </app2:checkAccessRight>

  <c:if test="${param.type != null and param.calendar!=null}">
    <c:set var="back" value="&calendar=${param.calendar}" scope="request"/>
  </c:if>


  <c:if test="${isUser && !isSharedCalendar}">
    <app2:checkAccessRight functionality="TASK" permission="VIEW">
      <li>
        <app:link page="/scheduler/TaskList.do?simple=true" contextRelative="true" addModuleParams="false" addModuleName="false">
          <fmt:message key="Scheduler.taskSearch"/>
        </app:link>
      </li>
    </app2:checkAccessRight>
  </c:if>


  <c:if test="${isUser && !isSharedCalendar}">
    <app2:checkAccessRight functionality="GRANTACCESS" permission="VIEW">
      <li role="separator" class="divider"></li>
      <li>
        <app:link page="/scheduler/GrantAccess/List.do" contextRelative="true" addModuleParams="false" addModuleName="false">
          <fmt:message key="Scheduler.grantAccess"/>
        </app:link>
      </li>
    </app2:checkAccessRight>
  </c:if>

  <app2:checkAccessRight functionality="APPOINTMENT" permission="VIEW">
    <li role="separator" class="divider"></li>
    <li>
      <a href="#">
        <fmt:message key="Scheduler.user.viewCalendar"/>
        <span class="caret"></span>
      </a>

      <ul class="dropdown-menu">
        <c:set var="listUserView" value="${app2:getUserViewCalendar(pageContext.request)}" scope="request"/>
        <c:forEach var="item" items="${listUserView}" varStatus="statusVar">
          <li>
            <a href="#" onclick="changeSchedulerUser(${item.value})">
              <c:out value="${item.label}"/>
            </a>
          </li>
        </c:forEach>

        <html:hidden property="calendar" value="${param.calendar}" styleId="calendar"/>
        <html:hidden property="type" value="${param.type}" styleId="type"/>
      </ul>

    </li>
  </app2:checkAccessRight>

  <%--reports--%>
  <app2:checkAccessRight functionality="TASK" permission="VIEW">

    <c:if test="${isUser && !isSharedCalendar}">
      <tags:bootstrapMenu titleKey="Report.plural" addSeparator="true">
        <tags:bootstrapMenuItem action="/scheduler/Report/TaskList.do"
                                contextRelative="true"
                                titleKey="Task.Report.TaskList" functionality="TASK"
                                permission="VIEW"/>
        <tags:bootstrapMenuItem
                action="/scheduler/Report/TaskSingleList.do?parameter(taskId)=${taskId}&parameter(task)=${app2:encode(title1)}"
                contextRelative="true"
                titleKey="Scheduler.Report.TaskSingleList"
                functionality="TASK" permission="VIEW"/>
      </tags:bootstrapMenu>
    </c:if>
  </app2:checkAccessRight>

</ul>


<%--process when is overview of other calendar in scheduler module--%>
<c:if test="${param.module == 'scheduler'}">
    <c:if test="${!isUser or not empty param.sharedCalendar}">

        <c:set var="userViewId" value="${sessionScope.user.valueMap.schedulerUserId}"/>
        <c:if test="${not empty param.sharedCalendar}">
            <c:set var="userViewId" value="${param.sharedCalendar}"/>
        </c:if>

        <c:forEach var="item" items="${listUserView}" varStatus="statusVar">
            <c:if test="${userViewId eq item.value}">
                <c:set var="userViewName" value="${item.label}" scope="request"/>
            </c:if>
        </c:forEach>

        <%--import to add in body heder fragment--%>
        <c:import var="bodyHeaderFragment" url="/WEB-INF/jsp/layout/tiles/SchedulerBodyHeaderFragment.jsp"
                  scope="request"/>
    </c:if>
</c:if>

