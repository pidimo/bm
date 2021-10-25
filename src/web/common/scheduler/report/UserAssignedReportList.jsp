<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<tags:initSelectPopup/>
<%
    pageContext.setAttribute("typeUserList", JSPHelper.getUserTypeList(request));
    pageContext.setAttribute("statusList", JSPHelper.getPriorityStatusList(request));
%>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<script>
    function myReset() {
        var form = document.userAssignedReportListForm;
        for (i = 0; i < form.elements.length; i++) {
            if (form.elements[i].type == "text") {
                form.elements[i].value = "";
            } else if (form.elements[i].type == "select-one") {
                form.elements[i].value = "";
            }
        }
    }
</script>

<table border="0" cellpadding="0" cellspacing="0" width="75%" class="container" align="center">
    <tr>
        <td height="20" class="title" colspan="4"><fmt:message key="Scheduler.Report.UserAssignedList"/></td>
    </tr>

    <html:form action="/Report/UserAssignedList/Execute.do?parameter(userId)=${sessionScope.user.valueMap['userId']}"
               focus="parameter(taskId)" styleId="schedulerStyle">
        <TR>
            <TD width="18%" class="label"><fmt:message key="Scheduler.Task"/></TD>
            <TD class="contain" width="37%">
                <html:hidden property="parameter(taskId)" styleId="taskId_id"/>
                <app:text property="parameter(taskName)" styleId="taskName_id" styleClass="mediumText" maxlength="40"
                          tabindex="1" readonly="true"/>
                <tags:selectPopup url="/scheduler/SearchTask.do" name="taskList" titleKey="Common.search"
                                  width="820"
                                  heigth="450"/>

                <tags:clearSelectPopup keyFieldId="taskId_id" nameFieldId="taskName_id" titleKey="Common.clear"/>
            </TD>
            <td class="label" width="15%"><fmt:message key="User.typeUser"/></td>
            <td class="contain" width="30%">
                <html:select property="parameter(type)" styleClass="select" tabindex="3">
                    <html:option value=""/>
                    <html:options collection="typeUserList" property="value" labelProperty="label"/>
                </html:select>
            </td>
        </tr>
        <tr>
            <td class="label">
                <fmt:message key="Appointment.groupName"/>
            </td>
            <td class="contain">
                <fanta:select property="parameter(userGroupId)" listName="userGroupList" tabIndex="2" firstEmpty="true"
                              labelProperty="groupName" valueProperty="userGroupId" styleClass="mediumSelect"
                              module="/admin">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                </fanta:select>
            </td>
            <td class="label"><fmt:message key="Task.status"/></TD>
            <td class="contain">
                <html:select property="parameter(userTaskStatusId)" styleClass="select" tabindex="4">
                    <html:option value=""/>
                    <html:options collection="statusList" property="value" labelProperty="label"/>
                </html:select>
            </td>
        </tr>

        <c:set var="reportFormats" value="${userAssignedReportListForm.reportFormats}" scope="request"/>
        <c:set var="pageSizes" value="${userAssignedReportListForm.pageSizes}" scope="request"/>

        <tags:reportOptionsTag/>

        <tr>
            <td colspan="4" class="button">
                <html:submit styleClass="button" tabindex="31"><fmt:message key="Campaign.Report.generate"/>
                </html:submit>
                <html:button property="reset1" tabindex="32" styleClass="button" onclick="formReset('schedulerStyle')">
                    <fmt:message
                            key="Common.clear"/></html:button>
            </td>
        </tr>
        <c:set var="internalUser"><fmt:message key="User.intenalUser"/></c:set>
        <c:set var="externalUser"><fmt:message key="User.externalUser"/></c:set>
        <c:set var="progress"><fmt:message key="Task.InProgress"/></c:set>
        <c:set var="noInit"><fmt:message key="Task.notInit"/></c:set>
        <c:set var="concluded"><fmt:message key="Scheduler.Task.Concluded"/></c:set>
        <c:set var="deferred"><fmt:message key="Task.Deferred"/></c:set>
        <c:set var="toCheck"><fmt:message key="Task.ToCheck"/></c:set>

        <titus:reportInitializeConstantsTag/>
        <titus:reportTag id="userAssignedTaskReportList" title="Scheduler.Report.UserAssignedList"
                         locale="${sessionScope.user.valueMap['locale']}"
                         timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>

        <titus:reportFieldTag name="title" resourceKey="Scheduler.Task" type="${FIELD_TYPE_STRING}" width="0"
                              fieldPosition="20" isGroupingColumn="true"/>
        <titus:reportFieldTag name="userName" resourceKey="User.user" type="${FIELD_TYPE_STRING}" width="50"
                              fieldPosition="2"/>
        <titus:reportFieldTag name="type" resourceKey="Appointment.appType" type="${FIELD_TYPE_STRING}" width="15"
                              conditionMethod="com.piramide.elwis.utils.ReportHelper.getOnlyOneResource type [${internalUser}] [${externalUser}] [1] [0]"
                              fieldPosition="3"/>
        <titus:reportFieldTag name="groupName" resourceKey="Appointment.groupName" type="${FIELD_TYPE_STRING}"
                              width="20" fieldPosition="4"/>
        <titus:reportFieldTag name="status" resourceKey="Task.status" type="${FIELD_TYPE_STRING}" width="15"
                              conditionMethod="com.piramide.elwis.utils.ReportHelper.getTaskUserStatus status [${progress}] [${noInit}] [${concluded}] [${deferred}] [${toCheck}] [1] [2] [3] [4] [5]"
                              fieldPosition="5"/>
    </html:form>
</table>
