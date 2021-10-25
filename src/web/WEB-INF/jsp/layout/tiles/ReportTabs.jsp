<%@ page import="com.piramide.elwis.cmd.reports.LightlyReportCmd" %>
<%@ page import="com.piramide.elwis.utils.ReportConstants" %>
<%@ page import="net.java.dev.strutsejb.dto.ResultDTO" %>
<%@ page import="net.java.dev.strutsejb.web.BusinessDelegate" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ include file="/Includes.jsp" %>


<%
    LightlyReportCmd cmd = new LightlyReportCmd();
    cmd.putParam("reportId", Integer.valueOf(request.getParameter("reportId")));
    cmd.setOp("read");


    ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);
    request.setAttribute("initReportTable", resultDTO.get("initialTableReference"));
    request.setAttribute("reportModule", resultDTO.get("module"));

    Integer reportType = (Integer) resultDTO.get("reportType");

    request.setAttribute("reportType", reportType);
    Map mapTabs = new HashMap();
    if (ReportConstants.SUMMARY_TYPE.equals(reportType) ||
            ReportConstants.MATRIX_TYPE.equals(reportType)) {
        mapTabs.put("showColumnGroupTab", Boolean.valueOf(true));
        mapTabs.put("showChartTab", Boolean.valueOf(true));
    } else {
        mapTabs.put("showColumnGroupTab", Boolean.valueOf(false));
        mapTabs.put("showChartTab", Boolean.valueOf(false));
    }


    Boolean showColumnGroupTab = (Boolean) mapTabs.get("showColumnGroupTab");
    Boolean showChartTab = (Boolean) mapTabs.get("showChartTab");

    request.setAttribute("showColumnGroupTab", showColumnGroupTab);
    request.setAttribute("showChartTabTab", showChartTab);
%>


<c:set var="tabHeaderLabel" value="Report" scope="request"/>
<c:set var="reportId"><%=request.getParameter("reportId")%>
</c:set>
<c:set var="tabHeaderValue" scope="request">
    <fanta:label listName="lightReportList" module="/reports" patron="0" columnOrder="reportName">
        <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        <fanta:parameter field="reportId" value="${empty reportId?0:reportId}"/>
    </fanta:label>
</c:set>


<jsp:useBean id="tabItems" class="java.util.LinkedHashMap" scope="request"/>


<app2:checkAccessRight functionality="REPORT" permission="VIEW">
    <c:choose>
        <c:when test="${not empty param.reportId}">
            <c:set target="${tabItems}" property="Report.Tab.detail"
                   value="/Report/Forward/Update.do?dto(reportId)=${param.reportId}"/>
        </c:when>
        <c:otherwise>
            <c:set target="${tabItems}" property="Report.Tab.detail" value="/Report/Forward/Create.do"/>
        </c:otherwise>
    </c:choose>
</app2:checkAccessRight>

<app2:checkAccessRight functionality="COLUMN" permission="VIEW">
    <c:set target="${tabItems}" property="Report.tab.columns"
           value="/Report/Forward/Columns.do?dto(reportId)=${param.reportId}"/>
</app2:checkAccessRight>

<app2:checkAccessRight functionality="FILTER" permission="VIEW">
    <c:set target="${tabItems}" property="Report.tab.filters" value="/Report/Filter/List.do"/>
</app2:checkAccessRight>

<app2:checkAccessRight functionality="GROUP" permission="VIEW">
    <c:if test="${showColumnGroupTab == true}">
        <c:set target="${tabItems}" property="Report.Tab.grouping"
               value="/Report/Forward/Grouping.do?dto(reportId)=${param.reportId}"/>
    </c:if>
</app2:checkAccessRight>

<app2:checkAccessRight functionality="TOTALIZE" permission="VIEW">
    <c:set target="${tabItems}" property="Report.Tab.totalize"
           value="/Report/Forward/Totalize.do?dto(reportId)=${param.reportId}"/>
</app2:checkAccessRight>

<app2:checkAccessRight functionality="CHART" permission="VIEW">
    <c:if test="${showChartTabTab ==  true}">
        <c:set target="${tabItems}" property="Report.ChartType"
               value="/ChartType/Forward/Update.do?dto(reportId)=${param.reportId}&dto(op)=read"/>
    </c:if>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="REPORTROLE" permission="VIEW">
    <c:set target="${tabItems}" property="Report.Tab.Roles"
           value="/ReportRole/List.do?dto(reportId)=${param.reportId}"/>
</app2:checkAccessRight>

<app2:checkAccessRight functionality="REPORT" permission="EXECUTE">
    <c:set target="${tabItems}" property="EXECUTE" value="/Report/Forward/Execute.do?dto(reportId)=${param.reportId}"/>
</app2:checkAccessRight>

<jsp:useBean id="tabParams" class="java.util.LinkedHashMap" scope="request"/>
