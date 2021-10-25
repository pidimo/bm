<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<tags:initSelectPopup/>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>
<fmt:message key="datePattern" var="datePattern"/>


<fmt:message key="Project.status.entered" var="enteredLabel"/>
<fmt:message key="Project.status.opened" var="openedLabel"/>
<fmt:message key="Project.status.closed" var="closedLabel"/>
<fmt:message key="Project.status.finished" var="finishedLabel"/>
<fmt:message key="Project.status.invoiced" var="invoicedLabel"/>

<br>
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="container">
<html:form action="/Project/ProjectReportList/Execute.do" focus="parameter(responsibleId)" styleId="projectReportForm">
<tr>
    <td height="20" class="title" colspan="4">
        <fmt:message key="${pagetitle}"/>
    </td>
</tr>
<TR>
    <TD class="label" width="15%"><fmt:message key="Project.responsible"/></TD>
    <TD class="contain" width="35%">
        <fanta:select property="parameter(responsibleId)" listName="userBaseList"
                                          labelProperty="name" valueProperty="id" styleClass="middleSelect"
                                          module="/admin" firstEmpty="true" tabIndex="1">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
    </TD>
    <%-------------------------Column divider-------------------------------%>
    <TD class="label" width="15%"><fmt:message key="Project.customer"/></TD>
    <TD class="contain" width="35%">

        <app:text property="parameter(name1@_name2@_searchName)" styleClass="mediumText" tabindex="11"/>
    </TD>
</tr>
<TR>
    <TD class="label" width="15%"><fmt:message key="Project.status"/></TD>
    <TD class="contain" width="35%">
        <c:set var="statusesOptions" value="${app2:getProjectStatuses(pageContext.request)}"/>
            <html:select property="parameter(status)" styleClass="middleSelect" tabindex="2">
                <html:option value="">&nbsp;</html:option>
                <html:options collection="statusesOptions" property="value" labelProperty="label"/>
            </html:select>
    </TD>

    <%-------------------------Column divider-------------------------------%>
    <TD class="label" width="15%"><fmt:message key="Project.toBeInvoiced"/></TD>
    <TD class="contain" width="35%">
        <c:set var="toBeInvoicedOptions" value="${app2:getToBeInvoicedTypes(pageContext.request)}"/>
        <html:select property="parameter(toBeInvoiced)" styleClass="mediumSelect" tabindex="14">
            <html:option value="">&nbsp;</html:option>
            <html:options collection="toBeInvoicedOptions" property="value" labelProperty="label"/>
        </html:select>
    </TD>
</tr>

<TR>
    <TD class="label" width="15%"><fmt:message key="Project.startDate"/></TD>
    <TD class="contain" width="35%">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:dateText property="parameter(startStartDate)" maxlength="10" tabindex="3" styleId="startStartDate"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:dateText property="parameter(endStartDate)" maxlength="10" tabindex="4" styleId="endStartDate"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
    </td>

    <%-------------------------Column divider-------------------------------%>
    <TD class="label" width="15%"><fmt:message key="Project.hasTimeLimit"/></TD>
    <TD class="contain" width="35%">
        <html:select property="parameter(hasTimeLimit)" styleClass="mediumSelect" tabindex="15">
            <html:option value=""></html:option>
            <html:option value="1"><fmt:message key="Common.yes"/> </html:option>
            <html:option value="0"><fmt:message key="Common.no"/> </html:option>
        </html:select>
        <%--<html:checkbox property="parameter(hasTimeLimit)" tabindex="15"
                                           styleClass="radio" value="1"/>--%>
    </td>
</tr>
<TR>
    <TD class="label" width="15%"><fmt:message key="Project.endDate"/></TD>
    <TD class="contain" width="35%">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:dateText property="parameter(startEndDate)" maxlength="10" tabindex="5" styleId="startEndDate"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:dateText property="parameter(endEndDate)" maxlength="10" tabindex="6" styleId="endEndDate"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
    </td>
    <%-------------------------Column divider-------------------------------%>
    <TD class="label" width="15%"><fmt:message key="Project.account"/></TD>
    <TD class="contain" width="35%">
        <fanta:select property="parameter(accountId)"
                      listName="accountList" labelProperty="name" valueProperty="accountId"
                      styleClass="mediumSelect" module="/catalogs" firstEmpty="true" tabIndex="16">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
</tr>
<TR>
    <TD class="label" width="15%"><fmt:message key="Project.plannedInvoice"/></TD>
    <TD class="contain" width="35%">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:numberText property="parameter(plannedAmount1)" styleClass="numberText" tabindex="7" maxlength="8"
                        numberType="decimal" maxInt="6" maxFloat="1"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:numberText property="parameter(plannedAmount2)" styleClass="numberText" tabindex="8"
                        maxlength="8" numberType="decimal" maxInt="6" maxFloat="1"/>
    </TD>
    <%-------------------------Column divider-------------------------------%>
    <TD class="label" width="15%"><fmt:message key="Project.totalInvoice"/></TD>
    <TD class="contain" width="35%">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:numberText property="parameter(totalInvoiceAmount1)" styleClass="numberText" tabindex="17" maxlength="8"
                        numberType="decimal" maxInt="6" maxFloat="1"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:numberText property="parameter(totalInvoiceAmount2)" styleClass="numberText" tabindex="18"
                        maxlength="8" numberType="decimal" maxInt="6" maxFloat="1"/>
    </TD>
</tr>
<TR>
    <TD class="label" width="15%"><fmt:message key="Project.plannedNoInvoice"/></TD>
    <TD class="contain" width="35%">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:numberText property="parameter(plannedNoAmount1)" styleClass="numberText" tabindex="9" maxlength="8"
                        numberType="decimal" maxInt="6" maxFloat="1"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:numberText property="parameter(plannedNoAmount2)" styleClass="numberText" tabindex="10"
                        maxlength="8" numberType="decimal" maxInt="6" maxFloat="1"/>
    </TD>

    <%-------------------------Column divider-------------------------------%>
    <TD class="label" width="15%"><fmt:message key="Project.totalNoInvoice"/></TD>
    <TD class="contain" width="35%">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:numberText property="parameter(totalNoInvoiceAmount1)" styleClass="numberText" tabindex="19" maxlength="8"
                        numberType="decimal" maxInt="6" maxFloat="1"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:numberText property="parameter(totalNoInvoiceAmount2)" styleClass="numberText" tabindex="20"
                        maxlength="8" numberType="decimal" maxInt="6" maxFloat="1"/>
    </TD>
</tr>

<c:set var="reportFormats" value="${projectReportForm.reportFormats}" scope="request"/>
<c:set var="pageSizes" value="${projectReportForm.pageSizes}" scope="request"/>
<tr>
    <td colspan="4">
        <titus:reportGroupSortTag width="100%">
            <titus:reportGroupSortColumnTag name="projectName" labelKey="Project.name"/>
            <titus:reportGroupSortColumnTag name="userName" labelKey="Project.responsible"/>
            <titus:reportGroupSortColumnTag name="customerName" labelKey="Project.customer"/>
            <titus:reportGroupSortColumnTag name="startDate" labelKey="Project.startDate" isDate="true"/>
            <titus:reportGroupSortColumnTag name="endDate" labelKey="Project.endDate" isDate="true"/>
        </titus:reportGroupSortTag>
    </td>
</tr>
<tags:reportOptionsTag/>


<tr>
    <td colspan="4" class="button">
        <html:submit styleClass="button" property="parameter(generate)" tabindex="57"><fmt:message
                key="Campaign.Report.generate"/></html:submit>
        <html:button property="reset1" tabindex="58" styleClass="button" onclick="resetForm(document.projectReportForm)">
            <fmt:message
                    key="Common.clear"/></html:button>
    </td>
</tr>

<titus:reportInitializeConstantsTag/>

<titus:reportTag id="projectReportList" title="Project.Report.ProjectList"
                 locale="${sessionScope.user.valueMap['locale']}"
                 timeZone="${sessionScope.user.valueMap['dateTimeZone']}"
                 pageOrientation="${PAGE_ORIENTATION_LANDSCAPE}"/>

<titus:reportFieldTag name="projectName" resourceKey="Project.name" type="${FIELD_TYPE_STRING}" width="16" fieldPosition="1"/>
<titus:reportFieldTag name="userName" resourceKey="Project.responsible" type="${FIELD_TYPE_STRING}" width="14" fieldPosition="2"/>
<titus:reportFieldTag name="customerName" resourceKey="Project.customer" type="${FIELD_TYPE_STRING}" width="14" fieldPosition="3"/>

<titus:reportFieldTag name="startDate" resourceKey="Project.startDate" type="${FIELD_TYPE_DATEINT}" patternKey="datePattern" width="8" fieldPosition="4"/>
<titus:reportFieldTag name="endDate" resourceKey="Project.endDate" type="${FIELD_TYPE_DATEINT}" patternKey="datePattern" width="8" fieldPosition="5"/>

<titus:reportFieldTag name="plannedInvoice" resourceKey="Project.plannedInvoice" type="${FIELD_TYPE_DECIMALNUMBER}"  width="8" fieldPosition="6"
       patternKey="numberFormat.1DecimalPlaces" align="${FIELD_ALIGN_RIGHT}" totalizatorOp="${CALCULATION_SUM}" totalizatorKey="ReportGenerator.totalSum" />

<titus:reportFieldTag name="totalInvoice" resourceKey="Project.totalInvoice" type="${FIELD_TYPE_DECIMALNUMBER}"  width="8" fieldPosition="7"
       patternKey="numberFormat.1DecimalPlaces" align="${FIELD_ALIGN_RIGHT}" totalizatorOp="${CALCULATION_SUM}" totalizatorKey="ReportGenerator.totalSum" />

<titus:reportFieldTag name="plannedNoInvoice" resourceKey="Project.plannedNoInvoice" type="${FIELD_TYPE_DECIMALNUMBER}"  width="8" fieldPosition="8"
       patternKey="numberFormat.1DecimalPlaces" align="${FIELD_ALIGN_RIGHT}" totalizatorOp="${CALCULATION_SUM}" totalizatorKey="ReportGenerator.totalSum" />

<titus:reportFieldTag name="totalNoInvoice" resourceKey="Project.totalNoInvoice" type="${FIELD_TYPE_DECIMALNUMBER}"  width="8" fieldPosition="9"
           patternKey="numberFormat.1DecimalPlaces" align="${FIELD_ALIGN_RIGHT}" totalizatorOp="${CALCULATION_SUM}" totalizatorKey="ReportGenerator.totalSum" />

<titus:reportFieldTag name="status" resourceKey="Project.status" type="${FIELD_TYPE_STRING}" width="8" fieldPosition="10"
        conditionMethod="com.piramide.elwis.utils.ReportHelper.getProjectStatusLabel status [${enteredLabel}] [${openedLabel}] [${closedLabel}] [${finishedLabel}] [${invoicedLabel}]"/>

</html:form>
</table>

