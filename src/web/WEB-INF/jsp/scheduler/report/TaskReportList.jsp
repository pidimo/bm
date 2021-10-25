<%@ include file="/Includes.jsp" %>

<tags:initBootstrapSelectPopupEven fields="user_key, user_name"/>
<tags:initBootstrapDatepicker/>
<tags:initBootstrapSelectPopup/>
<tags:initBootstrapSelectPopupAdvanced
        fields="fieldProcessId_id, fieldProcessName_id, percent_id, fieldAddressId_id, fieldAddressName_id"/>

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

<div class="${app2:getFormWrapperTwoColumns()}">
    <div class="${app2:getFormPanelClasses()}">

        <html:form action="/Report/TaskList/Execute.do"
                   focus="parameter(ownerName)"
                   styleId="schedulerStyle"
                   styleClass="form-horizontal">
            <fieldset>
                <legend class="title">
                    <fmt:message key="Scheduler.Report.TaskList"/>
                </legend>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="fieldAddressName_id">
                            <fmt:message key="Common.assignedFrom"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(view)}">
                            <div class="input-group">
                                <app:text property="parameter(ownerName)"
                                          styleClass="${app2:getFormInputClasses()} mediumText"
                                          tabindex="1"
                                          maxlength="40"
                                          styleId="fieldViewUserName_id"
                                          readonly="true"/>

                                <html:hidden property="parameter(createdUserId)" styleId="fieldViewUserId_id"/>
                <span class="input-group-btn">
                    <tags:bootstrapSelectPopup
                            url="/scheduler/ImportUserList.do?dto(companyId)=${sessionScope.user.valueMap['companyId']}&dto(userId)=${sessionScope.user.valueMap['userId']}"
                            name="searchUser"
                            styleId="ownerNameSelectPopup_id"
                            modalTitleKey="Scheduler.grantAccess.searchUser"
                            isLargeModal="true"
                            tabindex="1"
                            titleKey="Scheduler.grantAccess.searchUser"/>
                    <tags:clearBootstrapSelectPopup keyFieldId="fieldViewUserId_id"
                                                    nameFieldId="fieldViewUserName_id"
                                                    tabindex="1"
                                                    titleKey="Common.clear"/>
                </span>
                            </div>
                        </div>
                    </div>

                    <div class="${app2:getRowClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="startDate">
                            <fmt:message key="Task.startDate"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(view)}">
                            <fmt:message key="datePattern" var="datePattern"/>
                            <fmt:message key="Common.from" var="from"/>
                            <fmt:message key="Common.to" var="to"/>

                            <div class="row">
                                <div class="col-sm-6 marginButton">
                                    <div class="input-group date">
                                        <app:dateText property="parameter(startRange)"
                                                      maxlength="10"
                                                      tabindex="2"
                                                      styleId="startDate"
                                                      calendarPicker="true"
                                                      mode="bootstrap"
                                                      placeHolder="${from}"
                                                      datePatternKey="${datePattern}"
                                                      styleClass="${app2:getFormInputClasses()} dateText"
                                                      convert="true"/>
                                    </div>
                                </div>
                                <fmt:message key="Common.to" var="to"/>

                                <div class="col-sm-6 marginButton">
                                    <div class="input-group date">
                                        <app:dateText property="parameter(endRange)"
                                                      maxlength="10"
                                                      tabindex="3"
                                                      styleId="endDate"
                                                      calendarPicker="true"
                                                      placeHolder="${to}"
                                                      mode="bootstrap"
                                                      datePatternKey="${datePattern}"
                                                      styleClass="${app2:getFormInputClasses()} dateText"
                                                      convert="true"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="user_name">
                            <fmt:message key="Common.assignedTo"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(view)}">

                            <div class="input-group">
                                <app:text property="parameter(fromUserName)"
                                          tabindex="4"
                                          styleClass="${app2:getFormInputClasses()} mediumText"
                                          maxlength="40"
                                          styleId="user_name"
                                          readonly="true"/>
                                <html:hidden property="parameter(scheduledUserId)" styleId="user_key"/>
                <span class="input-group-btn">
                    <tags:bootstrapSelectPopup
                            url="/scheduler/ImportUserList.do?other=true&dto(companyId)=${sessionScope.user.valueMap['companyId']}"
                            name="searchUser"
                            tabindex="4"
                            isLargeModal="true"
                            styleId="fromUserNameSelectPopup_id"
                            modalTitleKey="Scheduler.grantAccess.searchUser"
                            titleKey="Scheduler.grantAccess.searchUser"/>
                    <tags:clearBootstrapSelectPopup keyFieldId="user_key"
                                                    tabindex="4"
                                                    nameFieldId="user_name"
                                                    titleKey="Common.clear"/>
                </span>
                            </div>
                        </div>
                    </div>

                    <div class="${app2:getRowClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="startFinishRange">
                            <fmt:message key="Common.expireDate"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(view)}">

                            <div class="row">
                                <div class="col-sm-6 marginButton">
                                    <div class="input-group date">
                                        <app:dateText property="parameter(startFinishRange)"
                                                      maxlength="10"
                                                      tabindex="5"
                                                      mode="bootstrap"
                                                      styleId="startFinishRange"
                                                      calendarPicker="true"
                                                      datePatternKey="${datePattern}"
                                                      placeHolder="${from}"
                                                      styleClass="${app2:getFormInputClasses()} dateText"
                                                      convert="true"/>
                                    </div>
                                </div>
                                <fmt:message key="Common.to" var="to"/>

                                <div class="col-sm-6 marginButton">
                                    <div class="input-group date">
                                        <app:dateText property="parameter(endFinishRange)"
                                                      maxlength="10"
                                                      tabindex="6"
                                                      styleId="endFinishRange"
                                                      calendarPicker="true"
                                                      mode="bootstrap"
                                                      placeHolder="${to}"
                                                      datePatternKey="${datePattern}"
                                                      styleClass="${app2:getFormInputClasses()} dateText"
                                                      convert="true"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="fieldProcessName_id">
                            <fmt:message key="Document.salesAsociated"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(view)}">

                            <div class="input-group">
                                <app:text property="parameter(processName)"
                                          styleId="fieldProcessName_id"
                                          styleClass="${app2:getFormInputClasses()} mediumText"
                                          maxlength="30"
                                          tabindex="7"
                                          readonly="true"/>
                                <html:hidden property="parameter(processId)" styleId="fieldProcessId_id"/>
                                <html:hidden property="parameter(percentId)" styleId="percent_id"/>
                <span class="input-group-btn">
                    <tags:bootstrapSelectPopup
                            url="/sales/SalesProcess/SearchSalesProcess.do?parameter(addressId)=${taskReportListForm.params.addressId}"
                            name="searchSalesProcess"
                            titleKey="Common.search"
                            isLargeModal="true"
                            tabindex="7"
                            modalTitleKey="SalesProcess.Title.simpleSearch"
                            styleId="processNameSelectPopup_id"/>
                    <tags:clearBootstrapSelectPopup keyFieldId="fieldProcessId_id"
                                                    tabindex="7"
                                                    nameFieldId="fieldProcessName_id"
                                                    titleKey="Common.clear"/>
                </span>
                            </div>
                        </div>
                    </div>

                    <div class="${app2:getRowClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="percent1_id">
                            <fmt:message key="Task.percent"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="row">
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <label class="control-label">
                                        <fmt:message key="Common.from"/>
                                    </label>
                                    <html:text property="parameter(percent1)"
                                               styleId="percent1_id"
                                               styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                               tabindex="8"/>
                                </div>
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <label class="control-label">
                                        <fmt:message key="Common.to"/>
                                    </label>
                                    <html:text property="parameter(percent2)"
                                               styleId="percent2"
                                               styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                               tabindex="9"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="fieldAddressName_id">
                            <fmt:message key="Appointment.contact"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="input-group">
                                <app:text property="parameter(contactName)"
                                          styleId="fieldAddressName_id"
                                          styleClass="${app2:getFormInputClasses()} mediumText"
                                          maxlength="40"
                                          tabindex="10"
                                          readonly="true"/>
                                <html:hidden property="parameter(addressId)" styleId="fieldAddressId_id"/>
                <span class="input-group-btn">
                    <tags:bootstrapSelectPopup url="/contacts/SearchAddress.do"
                                               name="searchAddress"
                                               styleId="contactNameSelectPopup_id"
                                               isLargeModal="true"
                                               tabindex="10"
                                               modalTitleKey="Contact.Title.search"
                                               titleKey="Common.search"
                                               submitOnSelect="true"/>
                    <tags:clearBootstrapSelectPopup onclick="clearValues()"
                                                    keyFieldId="fieldAddressId_id"
                                                    tabindex="10"
                                                    nameFieldId="fieldAddressName_id"
                                                    titleKey="Common.clear"/>
                </span>
                            </div>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="taskTypeId_id">
                            <fmt:message key="Task.taskType"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(taskTypeId)"
                                          listName="taskTypeList"
                                          styleId="taskTypeId_id"
                                          labelProperty="taskTypeName"
                                          valueProperty="taskTypeId"
                                          styleClass="${app2:getFormInputClasses()}  select"
                                          module="/catalogs"
                                          firstEmpty="true"
                                          tabIndex="11">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="status_id">
                            <fmt:message key="Task.status"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:select property="parameter(status)"
                                         styleId="status_id"
                                         styleClass="${app2:getFormInputClasses()} select"
                                         tabindex="12">
                                <html:option value=""/>
                                <html:options collection="statusList" property="value" labelProperty="label"/>
                            </html:select>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="notification_id">
                            <fmt:message key="Task.Notification"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:select property="parameter(notification)"
                                         styleId="notification_id"
                                         styleClass="${app2:getFormInputClasses()} select"
                                         tabindex="13">
                                <html:options collection="notificationList" property="value" labelProperty="label"/>
                            </html:select>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="concluded_status_id">
                            <fmt:message key="Task.status.closedOpen"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:select property="parameter(concluded_status)"
                                         styleId="concluded_status_id"
                                         styleClass="${app2:getFormInputClasses()} select"
                                         tabindex="14">
                                <html:option value=""/>
                                <html:options collection="statusList" property="value" labelProperty="label"/>
                            </html:select>
                        </div>
                    </div>

                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="priorityId_id">
                            <fmt:message key="Task.priority"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(priorityId)"
                                          listName="selectPriorityList"
                                          labelProperty="name"
                                          styleId="priorityId_id"
                                          valueProperty="id"
                                          styleClass="${app2:getFormInputClasses()} select"
                                          module="/catalogs"
                                          firstEmpty="true"
                                          tabIndex="15">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                                <fanta:parameter field="type" value="SCHEDULER"/>
                            </fanta:select>
                        </div>
                    </div>
                </div>
            </fieldset>
            <c:set var="reportFormats" value="${taskReportListForm.reportFormats}" scope="request"/>
            <c:set var="pageSizes" value="${taskReportListForm.pageSizes}" scope="request"/>

            <titus:reportGroupSortTag width="100%" mode="bootstrap"
                                      tableStyleClass="${app2:getTableClasesIntoForm()}">
                <titus:reportGroupSortColumnTag name="title" labelKey="Task.taskName"/>
                <titus:reportGroupSortColumnTag name="ownerName" labelKey="Task.createdBy"/>
                <titus:reportGroupSortColumnTag name="priorityName" labelKey="Task.priority"/>
                <titus:reportGroupSortColumnTag name="startDate" labelKey="Task.startDate" isDate="true"/>
                <titus:reportGroupSortColumnTag name="expireDate" labelKey="Task.expireDate" isDate="true"/>
                <titus:reportGroupSortColumnTag name="participantName" labelKey="Common.assignedTo"/>

            </titus:reportGroupSortTag>

            <tags:bootstrapReportOptionsTag/>

            <div class="${app2:getFormButtonWrapperClasses()}">
                <html:submit styleClass="${app2:getFormButtonClasses()}" property="parameter(generate)" tabindex="57">
                    <fmt:message key="Campaign.Report.generate"/>
                </html:submit>
                <html:button property="reset1" tabindex="58" styleClass="${app2:getFormButtonClasses()}"
                             onclick="formReset('schedulerStyle')">
                    <fmt:message key="Common.clear"/>
                </html:button>
            </div>

            <c:set var="progress"><fmt:message key="Task.InProgress"/></c:set>
            <c:set var="noInit"><fmt:message key="Task.notInit"/></c:set>
            <c:set var="concluded"><fmt:message key="Scheduler.Task.Concluded"/></c:set>
            <c:set var="deferred"><fmt:message key="Task.Deferred"/></c:set>
            <c:set var="toCheck"><fmt:message key="Task.ToCheck"/></c:set>
            <fmt:message var="datePattern" key="datePattern"/>
            <titus:reportInitializeConstantsTag/>

            <titus:reportTag id="taskReportList" title="Scheduler.Report.TaskList"
                             pageOrientation="${PAGE_ORIENTATION_LANDSCAPE}"
                             locale="${sessionScope.user.valueMap['locale']}"
                             timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>

            <titus:reportFieldTag name="title" resourceKey="Task.taskName" type="${FIELD_TYPE_STRING}" width="20"
                                  fieldPosition="1"/><!-- isGroupingColumn="true" />-->
            <titus:reportFieldTag name="ownerName" resourceKey="Task.createdBy" type="${FIELD_TYPE_STRING}" width="20"
                                  fieldPosition="2"/>
            <titus:reportFieldTag name="priorityName" resourceKey="Task.priority" type="${FIELD_TYPE_STRING}" width="10"
                                  fieldPosition="3"/>
            <titus:reportFieldTag name="participantStatus" resourceKey="Task.participantStatus"
                                  type="${FIELD_TYPE_STRING}"
                                  width="10"
                                  conditionMethod="com.piramide.elwis.utils.ReportHelper.getTaskUserStatus participantStatus [${progress}] [${noInit}] [${concluded}] [${deferred}] [${toCheck}] [1] [2] [3] [4] [5]"
                                  fieldPosition="4"/>
            <titus:reportFieldTag name="startDate" resourceKey="Task.startDate" type="${FIELD_TYPE_DATELONG}" width="10"
                                  patternKey="datePattern" fieldPosition="5"/>
            <titus:reportFieldTag name="expireDate" resourceKey="Task.expireDate" type="${FIELD_TYPE_DATELONG}"
                                  width="10"
                                  patternKey="datePattern" fieldPosition="6"/>
            <titus:reportFieldTag name="participantName" resourceKey="Common.assignedTo" type="${FIELD_TYPE_STRING}"
                                  width="20"
                                  fieldPosition="7"/>
        </html:form>

    </div>
</div>