<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<tags:initSelectPopup/>

<%
    pageContext.setAttribute("reminderList", JSPHelper.getReminderList(request));
    pageContext.setAttribute("recurTypeList", JSPHelper.getRecurTypeList(request));
    pageContext.setAttribute("recurrenceList", JSPHelper.getRecurrenceList(request));
%>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<table border="0" cellpadding="0" cellspacing="0" width="97%" class="container" align="center">
<tr>
    <td height="20" class="title" colspan="4"><fmt:message key="Scheduler.Report.AppointmentList"/></td>
</tr>

<html:form action="/Report/AppointmentList/Execute.do" focus="parameter(addressId)" styleId="schedulerStyle">
<TR>
    <TD class="label" width="15%"><fmt:message key="Appointment.contact"/></TD>
    <TD class="contain" width="30%">
        <fmt:message key="datePattern" var="datePattern"/>
        <html:hidden property="parameter(addressId)" styleId="fieldAddressId_id"/>
        <app:text property="parameter(contactName)" styleId="fieldAddressName_id" styleClass="mediumText" maxlength="40"
                  tabindex="1" readonly="true"/>
        <tags:selectPopup url="/contacts/SearchAddress.do" name="searchAddress" titleKey="Common.search"
                          submitOnSelect="true"/>
        <tags:clearSelectPopup keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id" titleKey="Common.clear"
                               submitOnClear="true"/>
    </TD>

    <TD class="label" width="17%">
        <fmt:message key="Appointment.startDate"/>
    </td>
    <TD class="contain" width="38%">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:dateText property="parameter(startRange)" maxlength="10" tabindex="6" styleId="startRange"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:dateText property="parameter(endRange)" maxlength="10" tabindex="7" styleId="endRange"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
    </TD>
</TR>
<TR>
    <TD class="label"><fmt:message key="Appointment.contactPerson"/></TD>
    <TD class="contain">
        <fanta:select property="parameter(contactPersonId)" tabIndex="2" listName="searchContactPersonList"
                      module="/contacts" firstEmpty="true" labelProperty="contactPersonName"
                      valueProperty="contactPersonId" styleClass="mediumSelect">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="addressId" value="${appointmentReportListForm.params.addressId}"/>
        </fanta:select>
    </TD>
    <TD class="label">
        <fmt:message key="Appointment.endDate"/>
    </td>
    <TD class="contain">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:dateText property="parameter(startFinishRange)" maxlength="10" tabindex="8" styleId="startFinishRange"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:dateText property="parameter(endFinishRange)" maxlength="10" tabindex="9" styleId="endFinishRange"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
    </TD>
</TR>

<TR>
    <TD class="label"><fmt:message key="Appointment.appType"/></TD>
    <TD class="contain">
        <fanta:select property="parameter(appTypeId)" listName="appointmentTypeList"
                      labelProperty="name" valueProperty="appointmentTypeId" styleClass="mediumSelect"
                      module="/scheduler" firstEmpty="true" tabIndex="3">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </TD>
    <TD class="label">
        <fmt:message key="Appointment.repeat"/>
    </td>
    <TD class="contain">
        <html:select property="parameter(isRecurrence)" styleClass="select" tabindex="10">
            <html:options collection="recurrenceList" property="value" labelProperty="label"/>
        </html:select>
    </TD>
</TR>

<TR>
    <TD class="label"><fmt:message key="Task.priority"/></TD>
    <TD class="contain">
        <fanta:select property="parameter(priorityId)" listName="selectPriorityList"
                      labelProperty="name" valueProperty="id" styleClass="mediumSelect"
                      module="/catalogs" firstEmpty="true" tabIndex="4">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="type" value="SCHEDULER"/>
        </fanta:select>
    </TD>
    <TD class="label">
        <fmt:message key="Appointment.periodRecurrence"/>
    </td>
    <td class="contain">
        <html:select property="parameter(ruleType)" styleClass="select" tabindex="11">
            <html:options collection="recurTypeList" property="value" labelProperty="label"/>
        </html:select>
    </TD>
</TR>
<TR>
    <TD class="label"><fmt:message key="Appointment.reminder"/></TD>
    <TD class="contain" colspan="3">
        <html:select property="parameter(isReminder)" styleClass="mediumSelect" tabindex="12">
            <html:options collection="reminderList" property="value" labelProperty="label"/>
        </html:select>
    </TD>
</TR>


<c:set var="reportFormats" value="${appointmentReportListForm.reportFormats}" scope="request"/>
<c:set var="pageSizes" value="${appointmentReportListForm.pageSizes}" scope="request"/>

<tags:reportOptionsTag/>


<tr>
    <td colspan="4" class="button">
        <html:submit styleClass="button" property="parameter(generate)" tabindex="31"><fmt:message
                key="Campaign.Report.generate"/></html:submit>
        <html:button property="reset1" tabindex="32" styleClass="button" onclick="formReset('schedulerStyle')">
            <fmt:message
                    key="Common.clear"/></html:button>
    </td>
</tr>
<c:set var="daily"><fmt:message key="Scheduler.calendarView.daily"/></c:set>
<c:set var="weekly"><fmt:message key="Scheduler.calendarView.weekly"/></c:set>
<c:set var="monthly"><fmt:message key="Scheduler.calendarView.monthly"/></c:set>
<c:set var="yearly"><fmt:message key="Scheduler.calendarView.yearly"/></c:set>
<fmt:message var="dateTimePattern" key="dateTimePattern"/>

<titus:reportInitializeConstantsTag/>
<titus:reportTag id="appointmentReportList" title="Scheduler.Report.AppointmentList"
                 locale="${sessionScope.user.valueMap['locale']}"
                 timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>

<titus:reportFieldTag name="title" resourceKey="Appointment.name" type="${FIELD_TYPE_STRING}" width="35"
                      fieldPosition="1"/>
<titus:reportFieldTag name="appTypeName" resourceKey="Appointment.appType" type="${FIELD_TYPE_STRING}" width="25"
                      fieldPosition="2"/>
<titus:reportFieldTag name="ruleType" resourceKey="Appointment.periodRecurrence" type="${FIELD_TYPE_STRING}" width="10"
                      conditionMethod="com.piramide.elwis.utils.ReportHelper.getRuleTypeResource ruleType [${daily}] [${weekly}] [${monthly}] [${yearly}] [1] [2] [3] [4]"
                      fieldPosition="3"/>

<titus:reportFieldTag name="startDate" resourceKey="Appointment.startDate" type="${FIELD_TYPE_CONDITIONALDATE}" width="15"
                      fieldPosition="4" patternKey="datePattern" patternKey2="dateTimePattern"
                      patternCondition="isAllDay [1]"/>

<titus:reportFieldTag name="endDate" resourceKey="Appointment.endDate" type="${FIELD_TYPE_CONDITIONALDATE}" width="15"
                      fieldPosition="5" patternKey="datePattern" patternKey2="dateTimePattern"
                      patternCondition="isAllDay [1]"
        />

</html:form>
</table>
