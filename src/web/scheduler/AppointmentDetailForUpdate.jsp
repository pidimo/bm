<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
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


<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/AppointmentTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/scheduler/Appointment.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/AppointmentTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/scheduler/Appointment.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>
