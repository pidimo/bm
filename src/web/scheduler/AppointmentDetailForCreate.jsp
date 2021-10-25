<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<c:set var="helpResourceKey" value="help.appointment.create" scope="request"/>

<fmt:message var="titlePage" key="Appointment.new" scope="request"/>
<fmt:message var="button" key="Common.save" scope="request"/>
<fmt:message var="buttonGoTo" key="Appointment.saveAndGoTo" scope="request"/>

<c:set var="action" value="/Appointment/Create" scope="request"/>
<c:set var="op" value="create" scope="request"/>
<c:set var="ope" value="CREATE" scope="request"/>
<c:set var="windowTitle" value="Appointment.new" scope="request"/>
<c:set var="operation" value="CREATE" scope="request"/>
<c:set var="pagetitle" value="Appointment.plural" scope="request"/>


<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/scheduler/Appointment.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/scheduler/Appointment.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>
