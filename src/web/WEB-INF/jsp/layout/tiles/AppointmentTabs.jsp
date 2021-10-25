<%@ page import="com.piramide.elwis.cmd.schedulermanager.LightlySchedulerCmd,
                 net.java.dev.strutsejb.dto.ResultDTO,
                 net.java.dev.strutsejb.web.BusinessDelegate" %>
<%@ include file="/Includes.jsp" %>

<c:set var="tabHeaderLabel" value="Scheduler.Appointment" scope="request"/> <%--send the app. resource key.--%>
<%
  boolean errorPage = false;
  LightlySchedulerCmd lightlySchedulerCmd = new LightlySchedulerCmd();
  String appointmentId = null;
  if (request.getParameter("dto(appointmentId)") != null && !"".equals(request.getParameter("dto(appointmentId)"))) {
    appointmentId = request.getParameter("dto(appointmentId)");
  } else {
    appointmentId = request.getParameter("appointmentId");
  }
  lightlySchedulerCmd.putParam("appointmentId", appointmentId);
  try {
    ResultDTO resultDTO = BusinessDelegate.i.execute(lightlySchedulerCmd, request);
    request.setAttribute("title", resultDTO.get("title"));
    request.setAttribute("isPrivate", resultDTO.get("isPrivate"));
    if (resultDTO.isFailure()) {
      errorPage = true;
    }
  } catch (Exception e) {
    errorPage = true;
  }
  request.setAttribute("errorPage", new Boolean(errorPage));
%>

<c:if test="${!errorPage}">
  <c:set var="tabHeaderValue" value="${title}" scope="request"/>

  <jsp:useBean id="tabItems" class="java.util.LinkedHashMap" scope="request"/>

  <app2:checkAccessRight functionality="APPOINTMENT" permission="VIEW">
    <c:set target="${tabItems}" property="Appointment.Tab.detail"
           value="/Appointment/Forward/Update.do?update=true&onlyView=${param.onlyView}&dto(title)=${app2:encode(title)}&dto(private)=${isPrivate}&calendar=${param.calendar}&type=${param.type}"/>
  </app2:checkAccessRight>
  <app2:checkAccessRight functionality="APPOINTMENTPARTICIPANT" permission="VIEW">
    <c:set target="${tabItems}" property="Appointment.Tab.participants"
           value="/Appointment/ParticipantList.do?onlyView=${param.onlyView}&dto(title)=${app2:encode(title)}&calendar=${param.calendar}&type=${param.type}"/>
  </app2:checkAccessRight>

  <jsp:useBean id="tabParams" class="java.util.LinkedHashMap" scope="request"/>

</c:if>


