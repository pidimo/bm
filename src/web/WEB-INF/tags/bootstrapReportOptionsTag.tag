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

        if (value == '${CSV_FORMAT}') {
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

            if ('true' != '${showDateFilter}') {
                document.getElementById('trCsvSetting').style.display = 'none';
            }
        }
    }

    function setDefaultCsvConfig(delimiter, charset) {
        document.getElementById('csvDelimiterId').value = delimiter;
        document.getElementById('reportCharsetId').value = charset;
    }
</script>

<fieldset>
    <legend class="title">
        <c:choose>
            <c:when test="${not empty optionsTitleKey}">
                <fmt:message key="${optionsTitleKey}"/>
            </c:when>
            <c:otherwise>
                <fmt:message key="ReportGenerator.reportOptions"/>
            </c:otherwise>
        </c:choose>
    </legend>
    <div class="row">
        <div class="${app2:getFormGroupClassesTwoColumns()}">
            <label class="${app2:getFormLabelClassesTwoColumns()}" for="reportTitle_id">
                <fmt:message key="ReportGenerator.reportTitle"/>
            </label>

            <div class="${app2:getFormContainClassesTwoColumns(null)}">
                <html:text property="reportTitle"
                           styleId="reportTitle_id"
                           styleClass="mediumText ${app2:getFormInputClasses()}"
                           tabindex="${tabNumber+1}"/>
                <span class="glyphicon form-control-feedback iconValidation"></span>
            </div>
        </div>
        <div class="${app2:getFormGroupClassesTwoColumns()}">
            <label class="${app2:getFormLabelClassesTwoColumns()}" for="format">
                <fmt:message key="ReportGenerator.reportFormat"/>
            </label>

            <div class="${app2:getFormContainClassesTwoColumns(null)}">
                <c:set var="rtfFormat" value="<%=ReportGeneratorConstants.REPORT_FORMAT_RTF%>"/>
                <html:select property="reportFormat" styleId="format"
                             styleClass="mediumSelect ${app2:getFormSelectClasses()}" tabindex="${tabNumber+2}"
                             onchange="changeReportFormat(this)"
                             onkeyup="changeReportFormat(this)">
                    <c:forEach var="rFormat" items="${reportFormats}">
                        <c:if test="${rFormat.reportFormat != rtfFormat}">
                            <html:option value="${rFormat.reportFormat}"><c:out
                                    value="${rFormat.reportFormatName}"/></html:option>
                        </c:if>
                    </c:forEach>
                </html:select>

                <c:set var="reportFormatVar" value="<%=request.getParameter(\"reportFormat\")%>"/>
                <c:set var="isCsvSelected" value="${CSV_FORMAT eq reportFormatVar}"/>
                <c:set var="csvFieldDelimiters" value="${app2:getCsvFieldDelimiters()}" scope="request"/>
                <span class="glyphicon form-control-feedback iconValidation"></span>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="${app2:getFormGroupClassesTwoColumns()}">
            <label class="${app2:getFormLabelClassesTwoColumns()}" for="pageSize">
                <fmt:message key="ReportGenerator.reportPageSize"/>
            </label>

            <div class="${app2:getFormContainClassesTwoColumns(null)}">
                <html:select property="reportPageSize" styleId="pageSize"
                             styleClass="mediumSelect ${app2:getFormSelectClasses()}" tabindex="${tabNumber+3}">
                    <html:options collection="pageSizes" property="pageSize" labelProperty="pageSizeName"/>
                </html:select>
                <span class="glyphicon form-control-feedback iconValidation"></span>
            </div>
        </div>
        <div class="${app2:getFormGroupClassesTwoColumns()}">
            <label class="${app2:getFormLabelClassesTwoColumns()}"
                   id="tdCharsetLabel" ${isCsvSelected ? "" : "style=\"display: none;\""} for="reportCharsetId">
                <fmt:message key="ReportGenerator.charset"/>
            </label>

            <div class="${app2:getFormContainClassesTwoColumns(null)}"
                 id="tdCharsetContain" ${isCsvSelected ? "" : "style=\"display: none;\""}>
                <c:set var="reportCharsetList" value="${app2:getReportCharsetList(pageContext.request)}"
                       scope="request"/>
                <html:select property="reportCharset" styleId="reportCharsetId"
                             styleClass="mediumSelect ${app2:getFormSelectClasses()}" tabindex="${tabNumber+4}">
                    <html:options collection="reportCharsetList" property="value" labelProperty="label"/>
                </html:select>
                <span class="glyphicon form-control-feedback iconValidation"></span>
            </div>
            <div id="tdCharsetEmpty" ${isCsvSelected ? "style=\"display: none;\"" : ""} class="contain">

            </div>
        </div>

    </div>

    <div class="row" id="trCsvSetting" ${(isCsvSelected or showDateFilter) ? "" : "style=\"display: none;\""}>
        <div class="${app2:getFormGroupClassesTwoColumns()}">
            <c:choose>
                <c:when test="${showDateFilter==true}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="dateFilter">
                        <fmt:message key="ReportGenerator.reportDateFilters"/>
                    </label>
                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <html:select property="reportGroupingDateFilter" styleId="dateFilter"
                                     styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                     tabindex="${tabNumber+3}">
                            <html:options collection="dateFilters" property="dateFilter"
                                          labelProperty="dateFilterName"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="${app2:getFormGroupClassesTwoColumns()}"></div>
                </c:otherwise>
            </c:choose>
        </div>
        <div class="${app2:getFormGroupClassesTwoColumns()}">
            <label class="${app2:getFormLabelClassesTwoColumns()}" id="tdDelimLabel" ${isCsvSelected ? "" : "style=\"display: none;\""} for="csvDelimiterId">
                <fmt:message key="ReportGenerator.csvDelimiter"/>
            </label>
            <div class="${app2:getFormContainClassesTwoColumns(null)}" id="tdDelimContain" ${isCsvSelected ? "" : "style=\"display: none;\""}>
                <html:select property="csvDelimiter" styleId="csvDelimiterId"
                             styleClass="mediumSelect ${app2:getFormSelectClasses()}" tabindex="${tabNumber+5}">
                    <html:options collection="csvFieldDelimiters" property="value" labelProperty="label"/>
                </html:select>
                <span class="glyphicon form-control-feedback iconValidation"></span>
            </div>

            <%--empty td--%>
            <div id="tdDelimEmpty" ${isCsvSelected ? "style=\"display: none;\"" : ""}></div>
        </div>
    </div>

    <%--set default csv delimiter user config--%>
    <c:set var="csvDelimiterVar" value="<%=request.getParameter(\"csvDelimiter\")%>"/>
    <c:if test="${empty csvDelimiterVar}">
        <c:set var="userDelimiterConfig" value="${app2:readUserCsvDelimiterConfig(pageContext.request)}"/>
        <c:set var="userReportCharset" value="${app2:readUserReportCharsetConfig(pageContext.request)}"/>
        <script language="JavaScript" type="text/javascript">
            setDefaultCsvConfig('${userDelimiterConfig}', '${userReportCharset}');
        </script>
    </c:if>
</fieldset>