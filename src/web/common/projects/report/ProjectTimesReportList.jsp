<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<tags:initSelectPopup/>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>
<fmt:message key="datePattern" var="datePattern"/>
<fmt:message key="Common.yes" var="invoiceableRes"/>
<fmt:message key="Common.no" var="noInvoiceableRes"/>

<fmt:message key="ProjectTime.status.entered" var="enteredRes"/>
<fmt:message key="ProjectTime.status.released" var="releasedRes"/>
<fmt:message key="ProjectTime.status.confirmed" var="confirmedRes"/>
<fmt:message key="ProjectTime.status.notConfirmed" var="notConfirmedRes"/>
<fmt:message key="ProjectTime.status.invoiced" var="invoicedRes"/>

<c:set var="isProjectSelected" value="${not empty projectTimesReportForm.params.projectId}"/>
<br>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="container">
<html:form action="/Project/ProjectTimesReportList/Execute.do" focus="parameter(projectName_FIELD)" styleId="projectTimesReportForm">
<tr>
    <td height="20" class="title" colspan="4">
        <fmt:message key="${pagetitle}"/>
    </td>
</tr>
<TR>
    <TD class="label" width="15%"><fmt:message key="Project.report.projectName"/></TD>
    <TD class="contain" width="35%">
        <html:hidden property="parameter(projectId)" styleId="projectId_id"/>
        <app:text property="parameter(projectName_FIELD)" styleClass="mediumText" readonly="true"
                  styleId="projectName_id" tabindex="1"/>
        <tags:selectPopup url="/projects/Project/ProjectListPopUp.do" name="searchProject"
                          titleKey="Common.search" tabindex="2" submitOnSelect="true"/>
        <tags:clearSelectPopup keyFieldId="projectId_id" nameFieldId="projectName_id"
                               titleKey="Common.clear" tabindex="3" submitOnClear="true"/>
    </TD>
    <%-------------------------Column divider-------------------------------%>
    <TD class="label" width="15%">
        <fmt:message key="Project.report.subProject"/>
    </TD>
    <TD class="contain" width="35%">
        <c:choose>
            <c:when test="${isProjectSelected}">
                <fanta:select property="parameter(subProjectId)" listName="subProjectListForSelect"
                              labelProperty="subProjectName" valueProperty="subProjectId" styleClass="mediumSelect"
                              module="/projects" firstEmpty="true" tabIndex="7">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}" />
                    <fanta:parameter field="projectId" value="${projectTimesReportForm.params.projectId}" />
                </fanta:select>
            </c:when>
            <c:otherwise>
                <app:text property="parameter(subProjectName)" tabindex="7" styleClass="mediumText" />
            </c:otherwise>
        </c:choose>

    </TD>

</tr>
<TR>
    <TD class="label" width="15%">
        <fmt:message key="ProjectTime.assignee"/>
    </TD>
    <TD class="contain" width="35%">
        <c:choose>
            <c:when test="${isProjectSelected}">
                <fanta:select property="parameter(assigneeAddressId)" listName="projectUserForSelectList"
                      labelProperty="userName" valueProperty="addressId" styleClass="mediumSelect"
                      module="/projects" firstEmpty="true" tabIndex="4"
                      >
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}" />
                    <fanta:parameter field="projectId" value="${projectTimesReportForm.params.projectId}" />
                </fanta:select>
            </c:when>
            <c:otherwise>
                <app:text property="parameter(assigneeName1@_assigneeName2@_assigneeSearchName)" tabindex="4"
                 styleClass="mediumText" />
            </c:otherwise>
        </c:choose>

    </TD>
    <%-------------------------Column divider-------------------------------%>
    <td class="label">
        <fmt:message key="Project.report.timePeriod"/>
    </td>
    <td class="contain">
        <fmt:message key="datePattern" var="datePattern"/>
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:dateText property="parameter(startProjectTime)" maxlength="10" tabindex="8" styleId="startProjectTime"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:dateText property="parameter(endProjectTime)" maxlength="10" tabindex="9" styleId="endProjectTime"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
    </td>
</TR>
<TR>
    <TD class="label" width="15%">
        <fmt:message key="Project.report.activity"/>
    </TD>
    <TD class="contain" width="35%">
        <c:choose>
            <c:when test="${isProjectSelected}">
                <fanta:select property="parameter(projectActivityId)" listName="projectActivityForSelectList"
                              labelProperty="activityName" valueProperty="projectActivityId" styleClass="mediumSelect"
                              module="/projects" firstEmpty="true" tabIndex="5">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}" />
                    <fanta:parameter field="projectId" value="${projectTimesReportForm.params.projectId}" />
                </fanta:select>
            </c:when>
            <c:otherwise>
                <app:text property="parameter(activityName)" tabindex="5" styleClass="mediumText" />
            </c:otherwise>
        </c:choose>

    </TD>
    <%-------------------------Column divider-------------------------------%>
    <TD class="label" width="15%"><fmt:message key="ProjectTime.time"/></TD>
    <TD class="contain" width="35%">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:numberText property="parameter(timeFrom)" styleClass="numberText" tabindex="10" maxlength="6"
                        numberType="decimal" maxInt="4" maxFloat="1"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:numberText property="parameter(timeTo)" styleClass="numberText" tabindex="11"
                        maxlength="6" numberType="decimal" maxInt="4" maxFloat="1"/>
    </TD>
</tr>
<TR>
    <TD class="label" width="15%">
        <fmt:message key="ProjectTime.toBeInvoiced"/>
    </TD>
    <TD class="contain" width="35%">
        <html:select property="parameter(toBeInvoiced)" styleClass="mediumSelect" tabindex="6">
            <html:option value=""></html:option>
            <html:option value="1"><fmt:message key="Common.yes"/> </html:option>
            <html:option value="0"><fmt:message key="Common.no"/> </html:option>
        </html:select>
    </TD>
    <%-------------------------Column divider-------------------------------%>
    <TD class="label" width="15%"><fmt:message key="Project.report.showDescription"/></TD>
    <TD class="contain" width="35%">
        <html:checkbox property="parameter(showDescription_flag)" value="true" tabindex="12"/>
    </TD>
</tr>    
<tr>

</tr>
<%-- LAST
<tr>
    <TD class="label" width="15%" colspan="1">
        <fmt:message key="Project.report.groupBy"/>
    </TD>
    <TD class="contain" width="85%" colspan="3">
        &nbsp;&nbsp;<html:checkbox property="parameter(groupBy_0)" value="true" tabindex="13"/>&nbsp;<fmt:message key="Project.report.project"/>
        &nbsp;&nbsp;<html:checkbox property="parameter(groupBy_1)" value="true" tabindex="14"/>&nbsp;<fmt:message key="ProjectTime.date"/>
        &nbsp;&nbsp;<html:checkbox property="parameter(groupBy_2)" value="true" tabindex="15"/>&nbsp;<fmt:message key="ProjectTime.assignee"/>
        &nbsp;&nbsp;<html:checkbox property="parameter(groupBy_3)" value="true" tabindex="16"/>&nbsp;<fmt:message key="Project.report.subProject"/>
        &nbsp;&nbsp;<html:checkbox property="parameter(groupBy_4)" value="true" tabindex="17"/>&nbsp;<fmt:message key="Project.report.activity"/>
    </TD>
</tr>
--%>


<c:set var="reportFormats" value="${projectTimesReportForm.reportFormats}" scope="request"/>
<c:set var="pageSizes" value="${projectTimesReportForm.pageSizes}" scope="request"/>
<tr>
    <td colspan="4">
        <titus:reportGroupSortTag width="100%">
            <titus:reportGroupSortColumnTag name="projectName" labelKey="Project.report.projectName"/>
            <titus:reportGroupSortColumnTag name="assigneeName" labelKey="ProjectTime.assignee"/>
            <titus:reportGroupSortColumnTag name="projectDate" labelKey="ProjectTime.date" isDate="true"/>
            <titus:reportGroupSortColumnTag name="activityName" labelKey="ProjectTime.activityName"/>
            <titus:reportGroupSortColumnTag name="subProjectName" labelKey="ProjectTime.subProjectName"/>
        </titus:reportGroupSortTag>
    </td>
</tr>
<tags:reportOptionsTag />

<tr>
    <td colspan="4" class="button">
        <html:submit styleClass="button" property="parameter(generate)" tabindex="57">
            <fmt:message key="Campaign.Report.generate"/>
        </html:submit>
        <html:button property="reset1" tabindex="58" styleClass="button" onclick="resetForm(document.projectTimesReportForm,${isProjectSelected}, ${isProjectSelected})">
            <fmt:message key="Common.clear"/>
        </html:button>
    </td>
</tr>

    <titus:reportInitializeConstantsTag/>
    <titus:reportTag id="incomingInvoiceReportList" title="Project.Report.ProjectTimesList"
                     pageOrientation="${PAGE_ORIENTATION_LANDSCAPE}"
                     locale="${sessionScope.user.valueMap['locale']}"
                     timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>

    <titus:reportFieldTag name="projectName" resourceKey="Project.report.projectName" type="${FIELD_TYPE_STRING}" width="18" fieldPosition="1" groupField="projectId"/>
    <titus:reportFieldTag name="assigneeName" resourceKey="ProjectTime.assignee" type="${FIELD_TYPE_STRING}" width="18" fieldPosition="2" groupField="assigneeAddressId"/>
    <titus:reportFieldTag name="projectDate" resourceKey="ProjectTime.date" type="${FIELD_TYPE_DATEINT}" patternKey="datePattern" width="10" fieldPosition="3"/>
    <titus:reportFieldTag name="activityName" resourceKey="ProjectTime.activityName" type="${FIELD_TYPE_STRING}" width="15" fieldPosition="5"/>
    <titus:reportFieldTag name="status" resourceKey="ProjectTime.status" type="${FIELD_TYPE_STRING}" width="8" fieldPosition="6"
                          conditionMethod="com.piramide.elwis.utils.ReportHelper.getProjectTimeStatus status [${enteredRes}] [${releasedRes}] [${confirmedRes}] [${notConfirmedRes}] [${invoicedRes}]"/>
    <titus:reportFieldTag name="subProjectName" resourceKey="ProjectTime.subProjectName" type="${FIELD_TYPE_STRING}" width="13" fieldPosition="7" groupField="subProjectId"/>
    <titus:reportFieldTag name="time" resourceKey="ProjectTime.report.timeToInvoice" type="${FIELD_TYPE_CALCULATEDDECIMALNUMBER}" align="${FIELD_ALIGN_RIGHT}" width="9" fieldPosition="8"
                          patternKey="numberFormat.2DecimalPlaces" conditionMethod="com.piramide.elwis.utils.ReportHelper.getDoubleValueByCondition time toBeInvoiced [1]" totalizatorOp="${CALCULATION_SUM}" totalizatorKey="ReportGenerator.totalSum"/>
    <titus:reportFieldTag name="time2" resourceKey="ProjectTime.report.timeToNotInvoice" type="${FIELD_TYPE_CALCULATEDDECIMALNUMBER}" align="${FIELD_ALIGN_RIGHT}" width="9" fieldPosition="9"
                          patternKey="numberFormat.2DecimalPlaces" conditionMethod="com.piramide.elwis.utils.ReportHelper.getDoubleValueByCondition time toBeInvoiced [0]" totalizatorOp="${CALCULATION_SUM}" totalizatorKey="ReportGenerator.totalSum"/>
    <titus:reportFieldTag name="freeTextValue" resourceKey="ProjectTime.description" type="${FIELD_TYPE_STRING}" width="20" fieldPosition="10"/><%--this exeedes the 100% but is managed in action--%>

</html:form>
</table>