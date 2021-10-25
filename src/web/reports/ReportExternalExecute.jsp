<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.report.externalExecute" scope="request"/>

<fmt:message var="title" key="Report.execute" scope="request"/>
<fmt:message var="button" key="EXECUTE" scope="request"/>

<c:set var="action" value="/Report/Execute" scope="request"/>
<c:set var="windowTitle" value="Report.execute" scope="request"/>
<c:set var="isExternalModule" value="true" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/reports/ReportExecute.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <%--<c:set var="tabs" value="/ReportTabs.jsp" scope="request"/>--%>
        <c:set var="body" value="/common/reports/ReportExecute.jsp" scope="request"/>
        <c:import url="/layout/ui/main.jsp"/>
    </c:otherwise>
</c:choose>