<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<c:set var="helpResourceKey" value="help.appointment.recurrenceDelete" scope="request"/>

<fmt:message var="titlePage" key="Appointment.delete" scope="request"/>
<fmt:message var="button" key="Common.delete" scope="request"/>
<c:set var="action" value="/Appointment/Forward/Delete" scope="request"/>
<c:set var="op" value="delete" scope="request"/>
<c:set var="ope" value="DELETE" scope="request"/>
<c:set var="windowTitle" value="Appointment.delete" scope="request"/>
<c:set var="operation" value="DELETE" scope="request"/>
<c:set var="pagetitle" value="Appointment.plural" scope="request"/>



<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/scheduler/AppointmentRecurrenceDelete.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/scheduler/AppointmentRecurrenceDelete.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>