<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="helpResourceKey" value="help.sales.report.contractsOverview" scope="request"/>

<c:set var="pagetitle" value="SalesProcess.Report.ContractsOverviewList" scope="request"/>
<c:set var="windowTitle" value="SalesProcess.Report.ContractsOverviewList" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/sales/report/ContractsOverviewReportList.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/sales/report/ContractsOverviewReportList.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>