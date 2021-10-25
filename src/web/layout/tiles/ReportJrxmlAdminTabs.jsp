<%@ page import="com.piramide.elwis.cmd.reports.LightlyReportCmd" %>
<%@ page import="net.java.dev.strutsejb.dto.ResultDTO" %>
<%@ page import="net.java.dev.strutsejb.web.BusinessDelegate" %>
<%@ include file="/Includes.jsp" %>



<%
    LightlyReportCmd cmd = new LightlyReportCmd();
    cmd.putParam("reportId", Integer.valueOf(request.getParameter("reportId")));
    cmd.setOp("read");


    ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);
    request.setAttribute("initReportTable", resultDTO.get("initialTableReference"));
    request.setAttribute("reportModule", resultDTO.get("module"));

    Integer reportType = (Integer) resultDTO.get("reportType");
    Integer reportCompanyId = (Integer) resultDTO.get("companyId");

    request.setAttribute("reportType", reportType);
    request.setAttribute("reportCompanyId", reportCompanyId);
%>


<c:set var="tabHeaderLabel" value="Report" scope="request"/>
<c:set var="reportId"><%=request.getParameter("reportId")%></c:set>
<c:set var="tabHeaderValue" scope="request">
    <fanta:label listName="lightReportList" module="/reports" patron="0" columnOrder="reportName">
        <fanta:parameter field="companyId" value="${not empty reportCompanyId ? reportCompanyId : sessionScope.user.valueMap['companyId']}"/>
        <fanta:parameter field="reportId" value="${empty reportId?0:reportId}"/>
    </fanta:label>
</c:set>


<c:import url="${layout}/TabHeader.jsp"/>
<jsp:useBean id="tabItems" class="java.util.LinkedHashMap" scope="request"/>


<app2:checkAccessRight functionality="JRXMLREPORT" permission="VIEW">
    <c:choose>
        <c:when test="${not empty param.reportId}">
            <c:set target="${tabItems}" property="Report.Tab.detail"
                   value="/Report/Jrxml/Forward/Update.do?dto(reportId)=${param.reportId}"/>
        </c:when>
        <c:otherwise>
            <c:set target="${tabItems}" property="Report.Tab.detail" value="/Report/Jrxml/Forward/Create.do"/>
        </c:otherwise>
    </c:choose>
</app2:checkAccessRight>

<app2:checkAccessRight functionality="JRXMLREPORT" permission="VIEW">
    <c:set target="${tabItems}" property="Report.tab.artifacts" value="/Report/Artifact/List.do"/>
</app2:checkAccessRight>

<app2:checkAccessRight functionality="JRXMLREPORT" permission="VIEW">
    <c:set target="${tabItems}" property="Report.tab.filters" value="/Report/QueryParam/List.do"/>
</app2:checkAccessRight>

<app2:checkAccessRight functionality="JRXMLREPORT" permission="EXECUTE">
    <c:set target="${tabItems}" property="EXECUTE" value="/Report/Jrxml/Forward/Execute.do?dto(reportId)=${param.reportId}"/>
</app2:checkAccessRight>

<jsp:useBean id="tabParams" class="java.util.LinkedHashMap" scope="request"/>
<c:import url="${sessionScope.layout}/submenu.jsp"/>