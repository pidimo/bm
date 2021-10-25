<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="helpResourceKey" value="help.admin.report.userGroup" scope="request"/>

<c:set var="pagetitle" value="Admin.Report.UserGroupList" scope="request"/>
<c:set var="windowTitle" value="Admin.Report.UserGroupList" scope="request"/>


<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/admin/report/UserGroupReportList.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/admin/report/UserGroupReportList.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>