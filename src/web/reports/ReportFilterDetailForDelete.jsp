<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.report.filter.delete" scope="request"/>

<fmt:message   var="title" key="Report.filter.title.delete" scope="request"/>
<fmt:message   var="button" key="Common.delete" scope="request"/>
<c:set var="action" value="/Report/Filter/Delete" scope="request"/>
<c:set var="op" value="delete" scope="request"/>

<c:set var="pagetitle" value="Report.filters" scope="request"/>
<c:set var="windowTitle" value="Report.filter.title.delete" scope="request"/>


<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ReportTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/reports/ReportFilter.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/reports/ReportFilter.jsp" scope="request"/>
        <c:set var="tabs" value="/ReportTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>