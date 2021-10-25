<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<c:set var="helpResourceKey" value="help.appointment.participant.userGroupDelete" scope="request"/>

<fmt:message var="titlePage" key="Appointment.participant.delete" scope="request"/>
<fmt:message var="button" key="Common.delete" scope="request"/>
<c:set var="op" value="delete" scope="request"/>
<c:set var="ope" value="DELETE" scope="request"/>
<c:set var="windowTitle" value="Appointment.participant.delete" scope="request"/>
<c:set var="pagetitle" value="Appointment.plural" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/AppointmentTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/scheduler/UserGroupDetail.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/AppointmentTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/scheduler/UserGroupDetail.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>