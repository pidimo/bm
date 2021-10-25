<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.report.chart" scope="request"/>

<c:set var="pagetitle" value="Report.ChartType" scope="request"/>
<c:set var="windowTitle" value="Report.ChartType" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ReportTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/reports/ChartType.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/reports/ChartType.jsp" scope="request"/>
        <c:set var="tabs" value="/ReportTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>

