<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.reportJrxml.artifact.create" scope="request"/>

<fmt:message  var="title" key="Report.artifact.new" scope="request"/>
<fmt:message  var="button" key="Common.save" scope="request"/>

<c:set var="action" value="/Report/Artifact/Create" scope="request"/>
<c:set var="op" value="create" scope="request"/>
<c:set var="windowTitle" value="Report.artifact.new" scope="request"/>
<c:set var="pagetitle" value="Report.artifacts" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ReportJrxmlAdminTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/reports/ReportArtifact.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/ReportJrxmlAdminTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/reports/ReportArtifact.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>