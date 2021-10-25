<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<tags:initSelectPopup/>
<tags:initSelectPopupAdvanced
        fields="fieldProcessId_id, fieldProcessName_id, percent_id, fieldAddressId_id, fieldAddressName_id"/>
<tags:initSelectPopupEven fields="user_key, user_name"/>
<%
    pageContext.setAttribute("statusList", JSPHelper.getPriorityStatusList(request));
    pageContext.setAttribute("notificationList", JSPHelper.getNotificationList(request));

%>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>
<script>
    function goSubmit() {
        document.forms[0].submit();
    }

    function clearValues() {
        document.getElementById('fieldAddressId_id').value = "";
        document.getElementById('fieldAddressName_id').value = "";
        goSubmit();
    }
</script>


<table border="0" cellpadding="0" cellspacing="0" width="95%" class="container" align="center">
<tr>
    <td height="20" class="title" colspan="4"><fmt:message key="Scheduler.Report.TaskList"/></td>
</tr>

<html:form action="/Report/TaskList/Execute.do" focus="parameter(ownerName)" styleId="schedulerStyle">
<TR>
    <TD width="15%" class="label"><fmt:message key="Common.assignedFrom"/></TD>
    <TD class="contain" width="30%">

        <html:hidden property="parameter(createdUserId)" styleId="fieldViewUserId_id"/>
        <app:text property="parameter(ownerName)" styleClass="mediumText" tabindex="1" maxlength="40"
                  styleId="fieldViewUserName_id" readonly="true"/>
        <tags:selectPopup
                url="/scheduler/ImportUserList.do?dto(companyId)=${sessionScope.user.valueMap['companyId']}&dto(userId)=${sessionScope.user.valueMap['userId']}"
                name="searchUser"
                titleKey="Scheduler.grantAccess.searchUser"
                width="630" heigth="480" imgWidth="17" imgHeight="19" />
        <tags:clearSelectPopup keyFieldId="fieldViewUserId_id"
                               nameFieldId="fieldViewUserName_id"
                               titleKey="Common.clear"/>
    </TD>
    <TD class="label" width="17%">
        <fmt:message key="Task.startDate"/>
    </td>
    <TD class="contain" width="38%">
        <fmt:message key="datePattern" var="datePattern"/>
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:dateText property="parameter(startRange)" maxlength="10" tabindex="6" styleId="startDate"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:dateText property="parameter(endRange)" maxlength="10" tabindex="7" styleId="endDate"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
    </TD>
</TR>
<TR>
    <td class="label"><fmt:message key="Common.assignedTo"/></TD>
    <td class="contain">
    <html:hidden property="parameter(scheduledUserId)" styleId="user_key" />
<app:text property="parameter(fromUserName)" tabindex="2" styleClass="mediumText" maxlength="40"
          styleId="user_name" readonly="true"/>
<tags:selectPopup url="/scheduler/ImportUserList.do?other=true&dto(companyId)=${sessionScope.user.valueMap['companyId']}"
                  name="searchUser"
                  titleKey="Scheduler.grantAccess.searchUser"
                  width="630"
                  heigth="480"
                  imgWidth="17"
                  imgHeight="19"
        />

<tags:clearSelectPopup keyFieldId="user_key"
                       nameFieldId="user_name"
                       titleKey="Common.clear"/>
    </td>
    <td class="label">
        <fmt:message key="Common.expireDate"/>
    </td>
    <td class="contain">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:dateText property="parameter(startFinishRange)" maxlength="10" tabindex="8" styleId="startFinishRange"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:dateText property="parameter(endFinishRange)" maxlength="10" tabindex="9" styleId="endFinishRange"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText" convert="true"/>
    </td>
</tr>
<tr>
    <td class="label"><fmt:message key="Document.salesAsociated"/></TD>
    <td class="contain">
        <html:hidden property="parameter(processId)" styleId="fieldProcessId_id"/>
        <html:hidden property="parameter(percentId)" styleId="percent_id"/>
        <app:text property="parameter(processName)" styleId="fieldProcessName_id" styleClass="mediumText" maxlength="30"
                  tabindex="3" readonly="true"/>
        <tags:selectPopup
                url="/sales/SalesProcess/SearchSalesProcess.do?parameter(addressId)=${taskReportListForm.params.addressId}"
                name="searchSalesProcess"
                titleKey="Common.search" width="900" heigth="480" scrollbars="false"/>
        <tags:clearSelectPopup keyFieldId="fieldProcessId_id" nameFieldId="fieldProcessName_id"
                               titleKey="Common.clear"/>
    </td>
    <td class="label"><fmt:message key="Task.percent"/></TD>
    <td class="contain">
        <fmt:message key="Common.from"/>
        &nbsp;
        <html:text property="parameter(percent1)" styleClass="numberText" tabindex="10"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <html:text property="parameter(percent2)" styleId="percent2" styleClass="numberText" tabindex="11"/>
    </td>
</TR>

<TR>
    <TD class="label"><fmt:message key="Appointment.contact"/></TD>
    <TD class="contain">
        <html:hidden property="parameter(addressId)" styleId="fieldAddressId_id"/>
        <app:text property="parameter(contactName)" styleId="fieldAddressName_id" styleClass="mediumText" maxlength="40"
                  tabindex="2" readonly="true"/>
        <tags:selectPopup url="/contacts/SearchAddress.do" name="searchAddress" titleKey="Common.search"
                          submitOnSelect="true"/>
        <tags:clearSelectPopup onclick="clearValues()" keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id"
                               titleKey="Common.clear"/>
            <%--<html:img src="${baselayout}/img/clear.gif" titleKey="Common.clear" border="0" onclick="clearValues()"/>--%>

    </TD>
    <TD class="topLabel"><fmt:message key="Task.taskType"/></TD>
    <TD class="contain">
        <fanta:select property="parameter(taskTypeId)" listName="taskTypeList"
                      labelProperty="taskTypeName" valueProperty="taskTypeId" styleClass="select"
                      module="/catalogs" firstEmpty="true" tabIndex="12">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </TD>
</TR>
<tr>
    <td class="label"><fmt:message key="Task.status"/></td>
    <td class="contain">
        <html:select property="parameter(status)" styleClass="select" tabindex="4">
            <html:option value=""/>
            <html:options collection="statusList" property="value" labelProperty="label"/>
        </html:select>
    </td>
    <td class="label"><fmt:message key="Task.Notification"/></TD>
    <td class="contain">
        <html:select property="parameter(notification)" styleClass="select" tabindex="13">
            <html:options collection="notificationList" property="value" labelProperty="label"/>
        </html:select>
    </td>
</TR>
<tr>
    <td  class="label">
        <fmt:message key="Task.status.closedOpen"/>
    </td>
    <td class="contain" >
        <html:select property="parameter(concluded_status)" styleClass="select" tabindex="5">
            <html:option value=""/>
            <html:options collection="statusList" property="value" labelProperty="label"/>
        </html:select>
    </td>

    <td class="label"><fmt:message key="Task.priority"/></TD>
    <td class="contain">
            <fanta:select property="parameter(priorityId)" listName="selectPriorityList"
                      labelProperty="name" valueProperty="id" styleClass="select"
                      module="/catalogs" firstEmpty="true" tabIndex="14">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="type" value="SCHEDULER"/>
        </fanta:select>

    </td>

</tr>

<c:set var="reportFormats" value="${taskReportListForm.reportFormats}" scope="request"/>
<c:set var="pageSizes" value="${taskReportListForm.pageSizes}" scope="request"/>
<tr>
    <td colspan="4">
        <titus:reportGroupSortTag width="100%">
            <titus:reportGroupSortColumnTag name="title" labelKey="Task.taskName"/>
            <titus:reportGroupSortColumnTag name="ownerName" labelKey="Task.createdBy"/>
            <titus:reportGroupSortColumnTag name="priorityName" labelKey="Task.priority"/>
            <titus:reportGroupSortColumnTag name="startDate" labelKey="Task.startDate" isDate="true"/>
            <titus:reportGroupSortColumnTag name="expireDate" labelKey="Task.expireDate" isDate="true"/>
            <titus:reportGroupSortColumnTag name="participantName" labelKey="Common.assignedTo"/>

        </titus:reportGroupSortTag>
    </td>
</tr>
<tags:reportOptionsTag/>

<tr>
    <td colspan="4" class="button">
        <html:submit styleClass="button" property="parameter(generate)" tabindex="57">
            <fmt:message key="Campaign.Report.generate"/></html:submit>
        <html:button property="reset1" tabindex="58" styleClass="button" onclick="formReset('schedulerStyle')">
            <fmt:message key="Common.clear"/></html:button>
    </td>

</tr>

<c:set var="progress"><fmt:message key="Task.InProgress"/></c:set>
<c:set var="noInit"><fmt:message key="Task.notInit"/></c:set>
<c:set var="concluded"><fmt:message key="Scheduler.Task.Concluded"/></c:set>
<c:set var="deferred"><fmt:message key="Task.Deferred"/></c:set>
<c:set var="toCheck"><fmt:message key="Task.ToCheck"/></c:set>
<fmt:message var="datePattern" key="datePattern"/>
<titus:reportInitializeConstantsTag/>

<titus:reportTag id="taskReportList" title="Scheduler.Report.TaskList"  pageOrientation="${PAGE_ORIENTATION_LANDSCAPE}"
                 locale="${sessionScope.user.valueMap['locale']}" timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>

<titus:reportFieldTag name="title" resourceKey="Task.taskName" type="${FIELD_TYPE_STRING}" width="20"
                      fieldPosition="1" /><!-- isGroupingColumn="true" />-->
<titus:reportFieldTag name="ownerName" resourceKey="Task.createdBy" type="${FIELD_TYPE_STRING}" width="20"
                      fieldPosition="2"/>
<titus:reportFieldTag name="priorityName" resourceKey="Task.priority" type="${FIELD_TYPE_STRING}" width="10"
                      fieldPosition="3"/>
<titus:reportFieldTag name="participantStatus" resourceKey="Task.participantStatus" type="${FIELD_TYPE_STRING}" width="10" conditionMethod="com.piramide.elwis.utils.ReportHelper.getTaskUserStatus participantStatus [${progress}] [${noInit}] [${concluded}] [${deferred}] [${toCheck}] [1] [2] [3] [4] [5]"
                      fieldPosition="4"/>
<titus:reportFieldTag name="startDate" resourceKey="Task.startDate" type="${FIELD_TYPE_DATELONG}" width="10"
                      patternKey="datePattern"  fieldPosition="5"/>
<titus:reportFieldTag name="expireDate" resourceKey="Task.expireDate" type="${FIELD_TYPE_DATELONG}" width="10"
                      patternKey="datePattern" fieldPosition="6"/>
<titus:reportFieldTag name="participantName" resourceKey="Common.assignedTo" type="${FIELD_TYPE_STRING}" width="20"
                      fieldPosition="7"/>
</html:form>
</table>
