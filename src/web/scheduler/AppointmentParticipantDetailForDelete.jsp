<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.appointment.participant.delete" scope="request"/>

<fmt:message var="button" key="Common.delete" scope="request"/>
<c:set var="action" value="/AppointmentParticipant/Delete" scope="request"/>
<c:set var="op" value="delete" scope="request"/>
<c:set var="windowTitle" value="Appointment.participant.delete" scope="request"/>

<c:set var="pagetitle" value="Appointment.participant.plural" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/AppointmentTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/scheduler/AppointmentParticipant.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/AppointmentTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/scheduler/AppointmentParticipant.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>