<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.report.role.list" scope="request"/>

<c:set var="pagetitle" value="ReportRole.plural" scope="request"/>
<c:set var="windowTitle" value="ReportRole.plural" scope="request"/>

<c:set var="reportId"><%=request.getParameter("reportId")%>
</c:set>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/reports/ReportRoleList.jsp" scope="request"/>
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ReportTabs.jsp" scope="request"/>
        <c:if test="${app2:isJrxmlSourceTypeReport(reportId)}">
            <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ReportJrxmlTabs.jsp" scope="request"/>
        </c:if>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/reports/ReportRoleList.jsp" scope="request"/>
        <c:set var="tabs" value="/ReportTabs.jsp" scope="request"/>
        <c:if test="${app2:isJrxmlSourceTypeReport(reportId)}">
            <c:set var="tabs" value="/ReportJrxmlTabs.jsp" scope="request"/>
        </c:if>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>