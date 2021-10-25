<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="helpResourceKey" value="help.appointment.participant.addUser" scope="request"/>

<c:if test="${param.type != null and param.calendar!=null}">
    <c:set var="back" value="&type=${param.type}&calendar=${param.calendar}"/>
</c:if>

<c:set var="windowTitle" value="Admin.User.Title.search" scope="request"/>
<c:set var="pagetitle" value="Admin.User.Title.search" scope="request"/>
<c:set var="action" value="AppointmentParticipant/Forward/Create.do?${back}" scope="request"/>
<c:set var="cancelAction" value="/AppointmentParticipant/Create.do?${back}" scope="request"/>
<c:set var="fantaImportAction"
       value="AppointmentParticipant/Create.do?dto(companyId)=${sessionScope.user.valueMap['companyId']}&dto(appointmentId)=${param.appointmentId}&dto(type)=user${back}"
       scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/AppointmentTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/scheduler/UserImportList.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/AppointmentTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/scheduler/UserImportList.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>
