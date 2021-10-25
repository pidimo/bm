<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.appointment.delete" scope="request"/>

<fmt:message var="titlePage" key="Appointment.delete" scope="request"/>
<fmt:message var="button" key="Common.delete" scope="request"/>
<fmt:message var="buttonGoTo" key="Appointment.deleteAndGoTo" scope="request"/>

<c:set var="action" value="/Appointment/Delete" scope="request"/>
<c:set var="op" value="delete" scope="request"/>
<c:set var="ope" value="DELETE" scope="request"/>
<c:set var="windowTitle" value="Appointment.delete" scope="request"/>
<c:set var="operation" value="DELETE" scope="request"/>
<c:set var="pagetitle" value="Appointment.plural" scope="request"/>


<c:set var="enableSaveAndGotoOptions" value="false" scope="request"/>
<c:set var="enableContactInputField" value="false" scope="request"/>
<c:set var="enableCalendarSelectField" value="true" scope="request"/>
<c:set var="deleteUrl"
       value="/contacts/Appointment/Forward/Delete.do?dto(title)=${app2:encode(param['dto(title)'])}&contactId=${param.contactId}&appointmentId=${param['dto(appointmentId)']}&dto(userId)=${param['dto(userId)']}&dto(private)=${param['dto(private)']}&operation=delete"
       scope="request"/>
<c:set var="pageSchedulerUserId" value="${param['dto(userId)']}" scope="request"/>


<c:choose>
       <c:when test="${sessionScope.isBootstrapUI}">
              <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ContactTabs.jsp" scope="request"/>
              <c:set var="body" value="/WEB-INF/jsp/scheduler/Appointment.jsp" scope="request"/>
              <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
       </c:when>
       <c:otherwise>
              <c:set var="tabs" value="/ContactTabs.jsp" scope="request"/>
              <c:set var="body" value="/common/scheduler/Appointment.jsp" scope="request"/>
              <c:import url="${sessionScope.layout}/main.jsp"/>
       </c:otherwise>
</c:choose>