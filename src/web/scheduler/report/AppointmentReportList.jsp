<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="helpResourceKey" value="help.scheduler.report.appointment" scope="request"/>

<c:set var="pagetitle" value="Scheduler.Report.AppointmentList" scope="request"/>
<c:set var="windowTitle" value="Scheduler.Report.AppointmentList" scope="request"/>
<c:set var="body" value="/common/scheduler/report/AppointmentReportList.jsp" scope="request"/>
<c:import url="${sessionScope.layout}/main.jsp"/>