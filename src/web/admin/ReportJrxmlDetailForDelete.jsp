<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.reportJrxml.delete" scope="request"/>

<fmt:message  var="title" key="Reports.report.delete" scope="request"/>
<fmt:message  var="button" key="Common.delete" scope="request"/>

<c:set var="action" value="/Report/Jrxml/Delete" scope="request"/>
<c:set var="op" value="delete" scope="request"/>
<c:set var="windowTitle" value="Reports.report.delete" scope="request"/>
<c:set var="pagetitle" value="Reports.report.plural" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="reportJrxmlButtonsPath" value="/WEB-INF/jsp/reports/ReportJrxmlAdminButtons.jsp" scope="request"/>
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ReportJrxmlAdminTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/reports/ReportJrxml.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="reportJrxmlButtonsPath" value="/common/reports/ReportJrxmlAdminButtons.jsp" scope="request"/>
        <c:set var="tabs" value="/ReportJrxmlAdminTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/reports/ReportJrxml.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>