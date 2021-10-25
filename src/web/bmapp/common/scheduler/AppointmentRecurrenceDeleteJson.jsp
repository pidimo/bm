<%@ include file="/Includes.jsp" %>

<c:choose>
  <c:when test="${empty param.root || param.root}">
    <fmt:message var="alertMessage" key="Appointment.deleteCurrentRootMessage"/>
  </c:when>
  <c:otherwise>
    <fmt:message var="msgCurrent" key="Appointment.deleteCurrentMessage"/>
    <c:set var="alertMessage" value="${msgCurrent} ${param.date}?"/>
  </c:otherwise>
</c:choose>

{
"entity" : {
    "appointmentId" : "${appointmentForm.dtoMap['appointmentId']}",
    "title" : "${app2:escapeJSON(appointmentForm.dtoMap['title'])}",

    "currentDate" : "${param.currentDate}",
    "alertMessage" : "${app2:escapeJSON(alertMessage)}",


    "isRoot" : "${empty param.root ? true : param.root}"
}
}
