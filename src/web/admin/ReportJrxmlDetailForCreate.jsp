<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.reportJrxml.create" scope="request"/>

<fmt:message  var="title" key="Reports.report.new" scope="request"/>
<fmt:message  var="button" key="Common.save" scope="request"/>

<c:set var="action" value="/Report/Jrxml/Create" scope="request"/>
<c:set var="op" value="create" scope="request"/>
<c:set var="windowTitle" value="Reports.report.new" scope="request"/>
<c:set var="pagetitle" value="Reports.report.plural" scope="request"/>
<c:set target="${reportJrxmlForm.dtoMap}" property="state" value="0"/>
<%--<c:set var="reportJrxmlButtonsPath" value="/common/reports/ReportJrxmlAdminButtons.jsp" scope="request"/>--%>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="reportJrxmlButtonsPath" value="/WEB-INF/jsp/reports/ReportJrxmlAdminButtons.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/reports/ReportJrxml.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="reportJrxmlButtonsPath" value="/common/reports/ReportJrxmlAdminButtons.jsp" scope="request"/>
        <c:set var="body" value="/common/reports/ReportJrxml.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>