<%@ tag import="com.jatun.titus.reportgenerator.util.ReportGeneratorConstants" %>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>
<%@ attribute name="optionsTitleKey" %>
<%@ attribute name="showDateFilter" %>

<c:if test="${empty tabNumber}">
    <c:set var="tabNumber" value="50"/>
</c:if>

<c:set var="CSV_FORMAT" value="<%=ReportGeneratorConstants.REPORT_FORMAT_CSV%>"/>

<script language="JavaScript" type="text/javascript">
    //report format funtinos
    function changeReportFormat(selectBox) {
        var value = selectBox.value;

        if(value == '${CSV_FORMAT}') {
            document.getElementById('tdCharsetLabel').style.display = '';
            document.getElementById('tdCharsetContain').style.display = '';
            document.getElementById('tdCharsetEmpty').style.display = 'none';

            document.getElementById('tdDelimLabel').style.display = '';
            document.getElementById('tdDelimContain').style.display = '';
            document.getElementById('tdDelimEmpty').style.display = 'none';

            document.getElementById('trCsvSetting').style.display = '';
        } else {
            document.getElementById('tdCharsetLabel').style.display = 'none';
            document.getElementById('tdCharsetContain').style.display = 'none';
            document.getElementById('tdCharsetEmpty').style.display = '';

            document.getElementById('tdDelimLabel').style.display = 'none';
            document.getElementById('tdDelimContain').style.display = 'none';
            document.getElementById('tdDelimEmpty').style.display = '';

            if('true' != '${showDateFilter}') {
                document.getElementById('trCsvSetting').style.display = 'none';
            }
        }
    }

    function setDefaultCsvConfig(delimiter, charset) {
        document.getElementById('csvDelimiterId').value = delimiter;
        document.getElementById('reportCharsetId').value = charset;
    }
</script>

<tr>
    <td height="20" class="title" colspan="4">
        <c:choose>
            <c:when test="${not empty optionsTitleKey}">
                <fmt:message key="${optionsTitleKey}"/>
            </c:when>
            <c:otherwise>
                <fmt:message key="ReportGenerator.reportOptions"/>
            </c:otherwise>
        </c:choose>

    </td>
</tr>
<tr>
    <td class="label" ><fmt:message key="ReportGenerator.reportTitle"/></td>
    <td class="contain"><html:text property="reportTitle" styleClass="mediumText"
                                                              tabindex="${tabNumber+1}"/></td>
    <td class="label"><fmt:message key="ReportGenerator.reportFormat"/></td>
    <td class="contain">
        <!--remove rtf format-->
        <c:set var="rtfFormat" value="<%=ReportGeneratorConstants.REPORT_FORMAT_RTF%>"/>
        <html:select property="reportFormat" styleId="format" styleClass="mediumSelect" tabindex="${tabNumber+4}"
                onchange="changeReportFormat(this)"
                onkeyup="changeReportFormat(this)">
            <c:forEach var="rFormat" items="${reportFormats}">
                <c:if test="${rFormat.reportFormat != rtfFormat}">
                    <html:option value="${rFormat.reportFormat}"><c:out value="${rFormat.reportFormatName}"/></html:option>
                </c:if>
            </c:forEach>
        </html:select>

        <c:set var="reportFormatVar" value="<%=request.getParameter(\"reportFormat\")%>"/>
        <c:set var="isCsvSelected" value="${CSV_FORMAT eq reportFormatVar}"/>
        <c:set var="csvFieldDelimiters" value="${app2:getCsvFieldDelimiters()}" scope="request"/>
    </td>
</tr>
<tr>
    <td class="label"><fmt:message key="ReportGenerator.reportPageSize"/></td>
    <td class="contain">
        <html:select property="reportPageSize" styleId="pageSize" styleClass="mediumSelect" tabindex="${tabNumber+2}">
            <html:options collection="pageSizes" property="pageSize" labelProperty="pageSizeName"/>
        </html:select>
    </td>

    <td id="tdCharsetLabel" ${isCsvSelected ? "" : "style=\"display: none;\""} class="label">
        <fmt:message key="ReportGenerator.charset"/>
    </td>
    <td id="tdCharsetContain" ${isCsvSelected ? "" : "style=\"display: none;\""} class="contain">
        <c:set var="reportCharsetList" value="${app2:getReportCharsetList(pageContext.request)}" scope="request"/>
        <html:select property="reportCharset" styleId="reportCharsetId" styleClass="mediumSelect" tabindex="${tabNumber+5}">
            <html:options collection="reportCharsetList" property="value" labelProperty="label"/>
        </html:select>
    </td>

    <%--empty td--%>
    <td id="tdCharsetEmpty" ${isCsvSelected ? "style=\"display: none;\"" : ""} class="contain" colspan="2">&nbsp;</td>

</tr>

<tr id="trCsvSetting" ${(isCsvSelected or showDateFilter) ? "" : "style=\"display: none;\""}>
    <c:choose>
        <c:when test="${showDateFilter==true}">
            <td class="label"><fmt:message key="ReportGenerator.reportDateFilters"/></td>
            <td class="contain">
                <html:select property="reportGroupingDateFilter" styleId="dateFilter" styleClass="mediumSelect" tabindex="${tabNumber+3}">
                    <html:options collection="dateFilters" property="dateFilter" labelProperty="dateFilterName"/>
                </html:select>
            </td>
        </c:when>
        <c:otherwise>
            <td class="contain" colspan="2">&nbsp;</td>
        </c:otherwise>
    </c:choose>

    <td id="tdDelimLabel" ${isCsvSelected ? "" : "style=\"display: none;\""} class="label">
        <fmt:message key="ReportGenerator.csvDelimiter"/>
    </td>
    <td id="tdDelimContain" ${isCsvSelected ? "" : "style=\"display: none;\""} class="contain">
        <html:select property="csvDelimiter" styleId="csvDelimiterId" styleClass="mediumSelect" tabindex="${tabNumber+6}">
            <html:options collection="csvFieldDelimiters" property="value" labelProperty="label"/>
        </html:select>
    </td>

    <%--empty td--%>
    <td id="tdDelimEmpty" ${isCsvSelected ? "style=\"display: none;\"" : ""} class="contain" colspan="2">&nbsp;</td>
</tr>

<%--set default csv delimiter user config--%>
<c:set var="csvDelimiterVar" value="<%=request.getParameter(\"csvDelimiter\")%>"/>
<c:if test="${empty csvDelimiterVar}">
    <c:set var="userDelimiterConfig" value="${app2:readUserCsvDelimiterConfig(pageContext.request)}"/>
    <c:set var="userReportCharset" value="${app2:readUserReportCharsetConfig(pageContext.request)}"/>
    <script language="JavaScript" type="text/javascript">
        setDefaultCsvConfig('${userDelimiterConfig}', '${userReportCharset}');
    </script>
</c:if>


<%--

To have the same layout for the rows of the report options and the report filter fields below src is not required
   anymore.

<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="container">
    <tr>
        <td height="20" class="title" colspan="4">
            <fmt:message key="ReportGenerator.reportOptions"/>
        </td>
    </tr>
    <tr>
        <td class="label" width="15%" ><fmt:message key="ReportGenerator.reportTitle"/></td>
        <td class="contain" width="35%"><html:text property="reportTitle" styleClass="mediumText" tabindex="${tabNumber+1}" /></td>
        <td class="label" width="15%"><fmt:message key="ReportGenerator.reportFormat"/></td>
        <td class="contain" width="35%">
            <html:select property="reportFormat" styleId="format" styleClass="mediumSelect" tabindex="${tabNumber+3}" >
               <html:options collection="reportFormats"  property="reportFormat" labelProperty="reportFormatName"/>
            </html:select>
        </td>
    </tr>
    <tr>
        <td class="label" width="15%"><fmt:message key="ReportGenerator.reportPageSize"/></td>
        <td class="contain" width="35%">
            <html:select property="reportPageSize" styleId="pageSize" styleClass="mediumSelect" tabindex="${tabNumber+2}" >
               <html:options collection="pageSizes"  property="pageSize" labelProperty="pageSizeName"/>
            </html:select>
        </td>
        <td class="contain" width="50%" colspan="2">&nbsp;</td>
    </tr>
</table>--%>
