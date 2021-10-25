<%@ include file="/Includes.jsp" %>

<tags:initSelectPopup/>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0" class="container">
    <tr>
        <td height="20" class="title" colspan="4"><fmt:message key="Scheduler.Report.TaskSingleList"/></td>
    </tr>

    <html:form action="/Report/TaskSingleList/Execute.do" focus="parameter(task)" styleId="taskSingleList">
        <tr>
            <TD class="label" width="15%"><fmt:message key="Scheduler.Task"/></TD>
            <TD class="contain" width="35%">
                <html:hidden property="parameter(taskId)" styleId="fieldTaskId_id" />
                <app:text property="parameter(task)" styleId="fieldTaskName_id" styleClass="middleText"
                          maxlength="40" tabindex="1" readonly="true"/>
                <tags:selectPopup url="/scheduler/TaskListPopUp.do" name="searchTasks" titleKey="Common.search"
                                  hide="false" submitOnSelect="false" tabindex="1" />
                <tags:clearSelectPopup keyFieldId="fieldTaskId_id" nameFieldId="fieldTaskName_id"
                                       titleKey="Common.clear" tabindex="2"/>
            </TD>
            <td class="contain" width="50%" colspan="2"></td>
        </tr>
        <c:set var="reportFormats" value="${taskSingleReportListForm.reportFormats}" scope="request"/>
        <c:set var="pageSizes" value="${taskSingleReportListForm.pageSizes}" scope="request"/>

        <tags:reportOptionsTag/>

        <tr>
            <td colspan="4" class="button">
                <html:submit styleClass="button" property="parameter(generate)" tabindex="57"><fmt:message
                        key="Campaign.Report.generate"/></html:submit>
                <html:button property="reset1" tabindex="58" styleClass="button"
                             onclick="formReset('taskSingleList')">
                    <fmt:message
                            key="Common.clear"/></html:button>
            </td>
        </tr>

        <titus:reportInitializeConstantsTag/>

        <titus:reportTag id="constactSingleList" title="Scheduler.Report.TaskSingleList"
                         pageOrientation="${PAGE_ORIENTATION_LANDSCAPE}"
                         locale="${sessionScope.user
                         .valueMap['locale']}"
                         timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>
    </html:form>
</table>

