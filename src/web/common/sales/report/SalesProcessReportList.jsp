<%@ page import="com.jatun.titus.reportgenerator.util.ReportGeneratorConstants" %>
<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<tags:initSelectPopup/>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<br>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="container">
<html:form action="/Report/SalesProcessList/Execute.do" focus="parameter(contact)" styleId="processStyle">
<tr>
    <td height="20" class="title" colspan="4">
        <fmt:message key="SalesProcess.Report.SalesProcessList"/>
    </td>
</tr>
<TR>
    <TD class="label" width="15%"><fmt:message key="Appointment.contact"/></TD>
    <TD class="contain" width="35%">
        <html:hidden property="parameter(addressId)" styleId="fieldAddressId_id"/>
        <app:text property="parameter(contact)" styleId="fieldAddressName_id" styleClass="middleText" maxlength="40"
                  tabindex="1" readonly="true"/>
        <tags:selectPopup url="/contacts/SearchAddress.do" name="searchAddress" titleKey="Common.search"/>
        <tags:clearSelectPopup keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id"
                               titleKey="Common.clear"/>
    </TD>

    <TD class="label" width="12%"><fmt:message key="SalesProcess.probability"/></TD>
    <TD class="contain" width="38%">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:numberText property="parameter(probability1)" styleClass="numberText"
                        numberType="integer" tabindex="5" maxlength="3"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:numberText property="parameter(probability2)" styleId="probability2" styleClass="numberText" tabindex="6"
                        maxlength="3" numberType="integer"/>

    </TD>
</TR>
<TR>
    <TD class="topLabel"><fmt:message key="SalesProcess.employee"/></TD>
    <TD class="containTop">
        <fanta:select property="parameter(employeeId)" listName="employeeBaseList" firstEmpty="true"
                      labelProperty="employeeName" valueProperty="employeeId" styleClass="middleSelect"
                      module="/contacts" tabIndex="2">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </TD>
    <td class="topLabel"><fmt:message key="SalesProcess.value"/></td>
    <td class="containTop">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:numberText property="parameter(value1)" styleClass="numberText" tabindex="7" maxlength="12"
                        numberType="decimal" maxInt="10" maxFloat="2"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:numberText property="parameter(value2)" styleId="value2" styleClass="numberText" tabindex="8"
                        maxlength="12" numberType="decimal" maxInt="10" maxFloat="2"/>
    </td>
</TR>
<tr>
    <td class="label"><fmt:message key="SalesProcess.priority"/></td>
    <td class="contain">
        <fanta:select property="parameter(priorityId)" listName="sProcessPriorityList"
                      labelProperty="name" valueProperty="id" styleClass="select"
                      module="/sales" firstEmpty="true" tabIndex="3">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </TD>
    <TD class="label">
        <fmt:message key="SalesProcess.startDate"/>
    </td>
    <TD class="contain">
        <fmt:message key="datePattern" var="datePattern"/>
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:dateText property="parameter(startCreateDate)" maxlength="10" tabindex="9" styleId="startDate"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:dateText property="parameter(endCreateDate)" maxlength="10" tabindex="10" styleId="endDate"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
    </td>
</tr>
<tr>
    <td class="label"><fmt:message key="SalesProcess.status"/></TD>
    <td class="contain">
        <fanta:select property="parameter(statusId)" listName="statusList"
                      labelProperty="statusName" valueProperty="statusId" styleClass="select"
                      module="/sales" firstEmpty="true" tabIndex="4">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
    <td class="label">
        <fmt:message key="SalesProcess.endDate"/>
    </td>
    <td class="contain">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:dateText property="parameter(startDueDate)" maxlength="10" tabindex="11" styleId="startDueDate"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:dateText property="parameter(endDueDate)" maxlength="10" tabindex="12" styleId="endDueDate"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
    </td>
</tr>
<tr>
    <td colspan="4">
        <titus:reportGroupSortTag width="100%">
            <titus:reportGroupSortColumnTag name="processName" labelKey="SalesProcess.name" />
            <titus:reportGroupSortColumnTag name="contactName" labelKey="SalesProcess.contact"/>
            <titus:reportGroupSortColumnTag name="employeeName" labelKey="SalesProcess.employee" />
            <titus:reportGroupSortColumnTag name="priorityName" labelKey="SalesProcess.priority" />
            <titus:reportGroupSortColumnTag name="statusName" labelKey="SalesProcess.status" />
            <titus:reportGroupSortColumnTag name="endDate" labelKey="SalesProcess.endDate" isDefault="true" defaultOrder="true" isDate="true" isDefaultGrouping="true" defaultDateGrouping="<%=ReportGeneratorConstants.DATE_FILTER_MONTH%>"/>
            <titus:reportGroupSortColumnTag name="probability" labelKey="SalesProcess.probability" />
        </titus:reportGroupSortTag>
    </td>
</tr>
<c:set var="reportFormats" value="${salesProcessReportForm.reportFormats}" scope="request"/>
<c:set var="pageSizes" value="${salesProcessReportForm.pageSizes}" scope="request"/>
<c:set var="dateFilters" value="${salesProcessReportForm.dateFilters}" scope="request"/>
<tags:reportOptionsTag />


<tr>
    <td colspan="4" class="button">
        <html:submit styleClass="button" property="parameter(generate)" tabindex="57"><fmt:message
                key="Campaign.Report.generate"/></html:submit>
        <html:button property="reset1" tabindex="58" styleClass="button" onclick="formReset('processStyle')">
            <fmt:message key="Common.clear"/></html:button>
    </td>
</tr>
<titus:reportInitializeConstantsTag/>
<titus:reportTag id="salesProcessReportList" title="SalesProcess.Report.SalesProcessList"
                 pageOrientation="${PAGE_ORIENTATION_LANDSCAPE}"
                 locale="${sessionScope.user.valueMap['locale']}"
                 timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>
<titus:reportFieldTag name="processName" resourceKey="SalesProcess.name" type="${FIELD_TYPE_STRING}" width="13"
                      fieldPosition="1"/>
<titus:reportFieldTag name="contactName" resourceKey="SalesProcess.contact" type="${FIELD_TYPE_STRING}" width="13"
                      fieldPosition="2"/>
<titus:reportFieldTag name="employeeName" resourceKey="SalesProcess.employee" type="${FIELD_TYPE_STRING}" width="13"
                      fieldPosition="3"/>
<titus:reportFieldTag name="priorityName" resourceKey="SalesProcess.priority" type="${FIELD_TYPE_STRING}" width="10"
                      fieldPosition="4"/>
<!--removed temporally because there's no space-->
<%--<titus:reportFieldTag name="startDate" resourceKey="SalesProcess.startDate" type="${FIELD_TYPE_DATEINT}"--%>
                      <%--fieldPosition="5" patternKey="datePattern" width="8" />--%>
<titus:reportFieldTag name="statusName" resourceKey="SalesProcess.status" type="${FIELD_TYPE_STRING}" width="9"
                      fieldPosition="5"/>

<titus:reportFieldTag name="endDate" resourceKey="SalesProcess.endDate" type="${FIELD_TYPE_DATEINT}"
                      fieldPosition="6" patternKey="datePattern" width="8"/>
<titus:reportFieldTag name="endDateCopy" resourceKey="SalesProcess.endDate" type="${FIELD_TYPE_DATEINT}"
                      fieldPosition="7" patternKey="datePattern" width="8"/>

<titus:reportFieldTag name="value" resourceKey="SalesProcess.value" align="${FIELD_ALIGN_RIGHT}"
                      type="${FIELD_TYPE_DECIMALNUMBER}" patternKey="numberFormat.2DecimalPlaces" width="8"
                      fieldPosition="8" totalizatorOp="${CALCULATION_SUM}" totalizatorKey="ReportGenerator.totalSum" />

<titus:reportFieldTag name="probability" resourceKey="SalesProcess.probability" type="${FIELD_TYPE_STRING}" width="8"
                      conditionMethod="com.piramide.elwis.utils.ReportHelper.getConcatenatedString probability [%]"
                      fieldPosition="9" align="${FIELD_ALIGN_RIGHT}"/>
<%--The ponderated value--%>
<titus:reportFieldTag name="processId" resourceKey="SalesProcess.ponderatedValue" align="${FIELD_ALIGN_RIGHT}" type="${FIELD_TYPE_CALCULATEDDECIMALNUMBER}"
                      patternKey="numberFormat.2DecimalPlaces" width="10" conditionMethod="com.piramide.elwis.utils.ReportHelper.getPonderatedValue value probability"
                      fieldPosition="10" totalizatorOp="${CALCULATION_SUM}" totalizatorKey="ReportGenerator.totalSum"/>

<%--<titus:reportFieldTag name="endDateCopy" resourceKey="SalesProcess.endDate" type="${FIELD_TYPE_DATEINT}" width="8"
                      patternKey="datePattern" conditionMethod="com.piramide.elwis.utils.ReportHelper.getGroupingDate endDate {REPORT_DATE_PATTERN} [${datePattern}] [${sessionScope.user.valueMap['locale']}]"
                      fieldPosition="10"/>--%>
</html:form>
</table>
