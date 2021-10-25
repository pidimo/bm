<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="helpResourceKey" value="help.appointment.participant.list" scope="request"/>

<c:set var="windowTitle" value="Appointment.participantList" scope="request"/>
<c:set var="pagetitle" value="Appointment.participantList" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/AppointmentTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/scheduler/ParticipantList.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/AppointmentTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/scheduler/ParticipantList.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>