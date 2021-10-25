<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.report.edit" scope="request"/>

<fmt:message var="title" key="Reports.report.update" scope="request"/>
<fmt:message var="button" key="Common.save" scope="request"/>

<c:set var="action" value="/Report/Update" scope="request"/>
<c:set var="op" value="update" scope="request"/>
<c:set var="windowTitle" value="Reports.report.update" scope="request"/>
<c:set var="pagetitle" value="Reports.report.plural" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ReportTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/reports/Report.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/ReportTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/reports/Report.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>