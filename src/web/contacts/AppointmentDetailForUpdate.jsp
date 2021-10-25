<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.appointment.edit" scope="request"/>

<fmt:message var="titlePage" key="Appointment.update" scope="request"/>
<fmt:message var="button" key="Common.save" scope="request"/>
<fmt:message var="buttonGoTo" key="Appointment.saveAndGoTo" scope="request"/>

<c:set var="action" value="/Appointment/Update" scope="request"/>
<c:set var="op" value="update" scope="request"/>
<c:set var="ope" value="UPDATE" scope="request"/>
<c:set var="windowTitle" value="Appointment.update" scope="request"/>
<c:set var="operation" value="UPDATE" scope="request"/>
<c:set var="pagetitle" value="Appointment.plural" scope="request"/>


<c:set var="enableSaveAndGotoOptions" value="false" scope="request"/>
<c:set var="enableContactInputField" value="false" scope="request"/>
<c:set var="enableCalendarSelectField" value="true" scope="request"/>
<c:set var="deleteUrl"
       value="/contacts/Appointment/Forward/Delete.do?dto(title)=${app2:encode(param['dto(title)'])}&contactId=${param.contactId}&appointmentId=${param['dto(appointmentId)']}&dto(userId)=${param['dto(userId)']}&dto(private)=${param['dto(private)']}&operation=delete"
       scope="request"/>
<c:set var="pageSchedulerUserId" value="${param['dto(userId)']}" scope="request"/>
<c:set var="enableParticipantLink" value="${true}" scope="request"/>


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