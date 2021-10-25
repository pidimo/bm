<%@ include file="/Includes.jsp" %>

<tags:initBootstrapSelectPopup/>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<div class="${app2:getFormWrapperTwoColumns()}">
    <div class="${app2:getFormPanelClasses()}">

        <legend class="title">
            <fmt:message key="Scheduler.Report.TaskSingleList"/>
        </legend>

        <html:form action="/Report/TaskSingleList/Execute.do" focus="parameter(task)" styleId="taskSingleList">

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="fieldTaskName_id">
                        <fmt:message key="Scheduler.Task"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <div class="input-group">
                            <app:text property="parameter(task)"
                                      styleId="fieldTaskName_id"
                                      styleClass="${app2:getFormInputClasses()} middleText"
                                      maxlength="40"
                                      tabindex="1"
                                      readonly="true"/>
                            <html:hidden property="parameter(taskId)" styleId="fieldTaskId_id"/>
                                <span class="input-group-btn">
                                    <tags:bootstrapSelectPopup url="/scheduler/TaskListPopUp.do"
                                                               name="searchTasks"
                                                               styleId="taskSelectPopup_id"
                                                               titleKey="Common.search"
                                                               isLargeModal="true"
                                                               hide="false"
                                                               modalTitleKey="Scheduler.taskSearch"
                                                               submitOnSelect="false"
                                                               tabindex="1"/>
                                    <tags:clearBootstrapSelectPopup keyFieldId="fieldTaskId_id"
                                                                    nameFieldId="fieldTaskName_id"
                                                                    titleKey="Common.clear"
                                                                    tabindex="2"/>
                                </span>
                        </div>
                    </div>
                </div>

            </div>

            <c:set var="reportFormats" value="${taskSingleReportListForm.reportFormats}" scope="request"/>
            <c:set var="pageSizes" value="${taskSingleReportListForm.pageSizes}" scope="request"/>

            <tags:bootstrapReportOptionsTag/>

            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}" property="parameter(generate)" tabindex="57">
                    <fmt:message key="Campaign.Report.generate"/>
                </html:submit>
                <html:button property="reset1" tabindex="58" styleClass="${app2:getFormButtonClasses()}" onclick="formReset('taskSingleList')">
                    <fmt:message key="Common.clear"/>
                </html:button>
            </div>

            <titus:reportInitializeConstantsTag/>

            <titus:reportTag id="constactSingleList" title="Scheduler.Report.TaskSingleList"
                             pageOrientation="${PAGE_ORIENTATION_LANDSCAPE}"
                             locale="${sessionScope.user
                         .valueMap['locale']}"
                             timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>
        </html:form>
    </div>
</div>
<tags:jQueryValidation formName="taskSingleReportListForm"/>