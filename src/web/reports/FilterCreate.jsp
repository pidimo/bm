<%@ include file="/Includes.jsp" %>

<fmt:message   var="title" key="Report.title.filterCreate" scope="request"/>
<fmt:message   var="button" key="Common.save" scope="request"/>
<c:set var="action" value="/Report/Filter/Create" scope="request"/>
<%--<c:set var="op" value="create" scope="request"/>--%>

<c:set var="pagetitle" value="Report.filters" scope="request"/>
<c:set var="windowTitle" value="Report.title.filterCreate" scope="request"/>
<c:set var="body" value="/common/reports/FilterCreate.jsp" scope="request"/>
<c:set var="tabs" value="/ReportTabs.jsp" scope="request"/>
<c:import url="${sessionScope.layout}/main.jsp"/>