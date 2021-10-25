<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.report.role.deleteSelected" scope="request"/>

<fmt:message var="title" key="ReportRole.deleteSelectedElements" scope="request"/>
<fmt:message var="button" key="Common.delete" scope="request"/>

<c:set var="action" value="/ReportRole/DeleteSelected" scope="request"/>
<c:set var="op" value="deleteSelectedElements" scope="request"/>
<c:set var="windowTitle" value="ReportRole.deleteSelectedElements" scope="request"/>
<c:set var="pagetitle" value="ReportRole.plural" scope="request"/>

<c:set var="reportId"><%=request.getParameter("reportId")%>
</c:set>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ReportTabs.jsp" scope="request"/>
        <c:if test="${app2:isJrxmlSourceTypeReport(reportId)}">
            <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ReportJrxmlTabs.jsp" scope="request"/>
        </c:if>
        <c:set var="body" value="/WEB-INF/jsp/reports/ReportRoleDeleteSelectedElements.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/ReportTabs.jsp" scope="request"/>
        <c:if test="${app2:isJrxmlSourceTypeReport(reportId)}">
            <c:set var="tabs" value="/ReportJrxmlTabs.jsp" scope="request"/>
        </c:if>
        <c:set var="body" value="/common/reports/ReportRoleDeleteSelectedElements.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>