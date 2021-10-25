<%@ include file="/Includes.jsp" %>

<c:set var="tabHeaderLabel" value="Scheduler.Task" scope="request"/> <%--send the app. resource key.--%>
<%--
    boolean errorPage = false;
    LightlyTaskCmd  lightlyTaskCmd = new LightlyTaskCmd();
    String taskId = null;
    if(request.getParameter("taskId")!=null && !"".equals(request.getParameter("taskId")))
        taskId = request.getParameter("taskId");
    else
        taskId = request.getParameter("dto(taskId)");

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
--%>
<c:set var="tabHeaderValue" value="${title1}" scope="request"/>


<jsp:useBean id="tabItems" class="java.util.LinkedHashMap" scope="request"/>
<app2:checkAccessRight functionality="TASK" permission="VIEW">
    <c:set target="${tabItems}" property="Appointment.Tab.detail"
           value="/Task/Forward/Update.do?taskId=${taskId}&dto(title)=${app2:encode(title1)}&update=true&dto(recordUserId)=${sessionScope.user.valueMap['userId']}"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="TASKUSER" permission="VIEW">
    <c:if test="${sessionScope.user.valueMap['userId'] == userId}">
        <c:set target="${tabItems}" property="Scheduler.Task.users"
               value="/Task/ParticipantTaskList.do?taskId=${taskId}&dto(title)=${app2:encode(title1)}"/>
    </c:if>
</app2:checkAccessRight>

<jsp:useBean id="tabParams" class="java.util.LinkedHashMap" scope="request"/>
<c:set target="${tabParams}" property="dto(taskId)" value="${param.taskId}"/>
