<%@ include file="/Includes.jsp" %>

<tags:initBootstrapDatepicker/>
<tags:initBootstrapSelectPopup/>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>
<fmt:message key="datePattern" var="datePattern"/>
<fmt:message var="timePattern" key="timePattern"/>
<fmt:message key="Common.yes" var="invoiceableRes"/>
<fmt:message key="Common.no" var="noInvoiceableRes"/>

<fmt:message key="ProjectTime.status.entered" var="enteredRes"/>
<fmt:message key="ProjectTime.status.released" var="releasedRes"/>
<fmt:message key="ProjectTime.status.confirmed" var="confirmedRes"/>
<fmt:message key="ProjectTime.status.notConfirmed" var="notConfirmedRes"/>
<fmt:message key="ProjectTime.status.invoiced" var="invoicedRes"/>

<c:set var="isProjectSelected" value="${not empty projectTimesReportForm.params.projectId}"/>

<script language="JavaScript" type="text/javascript">
    function changeStartDateValue() {
        $("#endProjectTime").val($("#startProjectTime").val());
    }
</script>

<c:set var="currentDateTimeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
<html:form action="/Project/ProjectTimesReportList/Execute.do" focus="parameter(projectName_FIELD)"
           styleId="projectTimesReportForm" styleClass="form-horizontal">
    <div class="${app2:getFormPanelClasses()}">
        <fieldset>
            <legend class="title">
                <fmt:message key="${pagetitle}"/>
            </legend>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="projectName_id">
                        <fmt:message key="Project.report.projectName"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <div class="input-group">
                            <app:text property="parameter(projectName_FIELD)"
                                      styleClass="${app2:getFormInputClasses()} mediumText" readonly="true"
                                      styleId="projectName_id" tabindex="1"/>
                            <html:hidden property="parameter(projectId)" styleId="projectId_id"/>
                            <span class="input-group-btn">
                                <tags:bootstrapSelectPopup styleId="searchProject_id"
                                                           url="/projects/Project/ProjectListPopUp.do"
                                                           name="searchProject"
                                                           titleKey="Common.search" modalTitleKey="Project.Title.search"
                                                           tabindex="2" submitOnSelect="true"
                                                           isLargeModal="true"/>
                                <tags:clearBootstrapSelectPopup keyFieldId="projectId_id" nameFieldId="projectName_id"
                                                                titleKey="Common.clear" tabindex="3"
                                                                submitOnClear="true"/>
                            </span>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                    <%-------------------------Column divider-------------------------------%>
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="subProjectId_id">
                        <fmt:message key="Project.report.subProject"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <c:choose>
                            <c:when test="${isProjectSelected}">
                                <fanta:select property="parameter(subProjectId)" styleId="subProjectId_id"
                                              listName="subProjectListForSelect"
                                              labelProperty="subProjectName" valueProperty="subProjectId"
                                              styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                              module="/projects" firstEmpty="true" tabIndex="4">
                                    <fanta:parameter field="companyId"
                                                     value="${sessionScope.user.valueMap['companyId']}"/>
                                    <fanta:parameter field="projectId"
                                                     value="${projectTimesReportForm.params.projectId}"/>
                                </fanta:select>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </c:when>
                            <c:otherwise>
                                <app:text property="parameter(subProjectName)" tabindex="4"
                                          styleClass="${app2:getFormInputClasses()} mediumText"/>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="assigneeAddressId_id">
                        <fmt:message key="ProjectTime.assignee"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <c:choose>
                            <c:when test="${isProjectSelected}">
                                <fanta:select property="parameter(assigneeAddressId)" styleId="assigneeAddressId_id"
                                              listName="projectUserForSelectList"
                                              labelProperty="userName" valueProperty="addressId"
                                              styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                              module="/projects" firstEmpty="true" tabIndex="5">
                                    <fanta:parameter field="companyId"
                                                     value="${sessionScope.user.valueMap['companyId']}"/>
                                    <fanta:parameter field="projectId"
                                                     value="${projectTimesReportForm.params.projectId}"/>
                                </fanta:select>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </c:when>
                            <c:otherwise>
                                <app:text property="parameter(assigneeName1@_assigneeName2@_assigneeSearchName)"
                                          tabindex="5"
                                          styleClass="${app2:getFormInputClasses()} mediumText"/>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </c:otherwise>
                        </c:choose>

                    </div>
                </div>
                    <%-------------------------Column divider-------------------------------%>
                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="startProjectTime">
                        <fmt:message key="Project.report.timePeriod"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <div class="row">
                            <fmt:message key="datePattern" var="datePattern"/>
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <div class="input-group date">
                                    <fmt:message key="Common.from" var="from"/>
                                    <app:dateText property="parameter(startProjectTime)" maxlength="10" tabindex="6"
                                                  styleId="startProjectTime"
                                                  calendarPicker="true" datePatternKey="${datePattern}"
                                                  styleClass="dateText ${app2:getFormInputClasses()}"
                                                  convert="true"
                                                  mode="bootstrap"
                                                  placeHolder="${from}"
                                                  onchange="changeStartDateValue()"
                                                  currentDateTimeZone="${currentDateTimeZone}"/>
                                </div>
                            </div>
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <div class="input-group date">
                                    <fmt:message key="Common.to" var="to"/>
                                    <app:dateText property="parameter(endProjectTime)" maxlength="10" tabindex="7"
                                                  styleId="endProjectTime"
                                                  calendarPicker="true" datePatternKey="${datePattern}"
                                                  styleClass="dateText ${app2:getFormInputClasses()}"
                                                  convert="true"
                                                  mode="bootstrap"
                                                  placeHolder="${to}"
                                                  currentDateTimeZone="${currentDateTimeZone}"/>
                                </div>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="projectActivityId_id">
                        <fmt:message key="Project.report.activity"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <c:choose>
                            <c:when test="${isProjectSelected}">
                                <fanta:select property="parameter(projectActivityId)" styleId="projectActivityId_id"
                                              listName="projectActivityForSelectList"
                                              labelProperty="activityName" valueProperty="projectActivityId"
                                              styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                              module="/projects" firstEmpty="true" tabIndex="8">
                                    <fanta:parameter field="companyId"
                                                     value="${sessionScope.user.valueMap['companyId']}"/>
                                    <fanta:parameter field="projectId"
                                                     value="${projectTimesReportForm.params.projectId}"/>
                                </fanta:select>
                            </c:when>
                            <c:otherwise>
                                <app:text property="parameter(activityName)" tabindex="8"
                                          styleClass="${app2:getFormInputClasses()} mediumText"/>
                            </c:otherwise>
                        </c:choose>

                    </div>
                </div>
                    <%-------------------------Column divider-------------------------------%>
                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="timeFrom_id">
                        <fmt:message key="ProjectTime.time"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <label class="control-label" for="timeFrom_id">
                                    <fmt:message key="Common.from"/>
                                </label>
                                <app:numberText property="parameter(timeFrom)" styleId="timeFrom_id"
                                                styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                tabindex="9"
                                                maxlength="6"
                                                numberType="decimal" maxInt="4" maxFloat="1"/>

                            </div>
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <label class="control-label">
                                    <fmt:message key="Common.to"/>
                                </label>
                                <app:numberText property="parameter(timeTo)"
                                                styleClass="${app2:getFormInputClasses()} numberText numberInputWidth"
                                                tabindex="10"
                                                maxlength="6" numberType="decimal" maxInt="4" maxFloat="1"/>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="toBeInvoiced_id">
                        <fmt:message key="ProjectTime.toBeInvoiced"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <html:select property="parameter(toBeInvoiced)" styleId="toBeInvoiced_id"
                                     styleClass="${app2:getFormSelectClasses()} mediumSelect" tabindex="11">
                            <html:option value=""></html:option>
                            <html:option value="1"><fmt:message key="Common.yes"/> </html:option>
                            <html:option value="0"><fmt:message key="Common.no"/> </html:option>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                    <%-------------------------Column divider-------------------------------%>

                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Project.report.showDescription"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(view)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-3 marginButton">
                                <div class="radiocheck">
                                    <div class="checkbox checkbox-default">
                                        <html:checkbox property="parameter(showDescription_flag)"
                                                       styleId="showDescription_flag_id" value="true" tabindex="12"/>
                                        <label for="showDescription_flag_id"></label>
                                    </div>
                                </div>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                            <div class="col-xs-12 col-sm-9 marginButton">
                                <div class="row">
                                    <div class="col-xs-12 col-sm-5">
                                        <label class="control-label">
                                            <fmt:message key="ProjectTime.showTimes"/>
                                        </label>
                                    </div>
                                    <div class="col-xs-12 col-sm-7">
                                        <div class="radiocheck">
                                            <div class="checkbox checkbox-default">
                                                <html:checkbox property="parameter(showTimes_flag)"
                                                               styleId="showTimes_flag_id" value="true" tabindex="12"/>
                                                <label for="showTimes_flag_id"></label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
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
        </fieldset>
        <titus:reportGroupSortTag width="100%" mode="bootstrap"
                                  tableStyleClass="${app2:getTableClasesIntoForm()}">
            <titus:reportGroupSortColumnTag name="projectName" labelKey="Project.report.projectName"/>
            <titus:reportGroupSortColumnTag name="assigneeName" labelKey="ProjectTime.assignee"/>
            <titus:reportGroupSortColumnTag name="projectDate" labelKey="ProjectTime.date" isDate="true"/>
            <titus:reportGroupSortColumnTag name="activityName" labelKey="ProjectTime.activityName"/>
            <titus:reportGroupSortColumnTag name="subProjectName" labelKey="ProjectTime.subProjectName"/>
        </titus:reportGroupSortTag>

        <tags:bootstrapReportOptionsTag/>
    </div>
    <div class="${app2:getFormButtonWrapperClasses()}">
        <html:submit styleClass="${app2:getFormButtonClasses()}" property="parameter(generate)" tabindex="57">
            <fmt:message key="Campaign.Report.generate"/>
        </html:submit>
        <html:button property="reset1" tabindex="58" styleClass="${app2:getFormButtonClasses()}"
                     onclick="resetForm(document.projectTimesReportForm,${isProjectSelected}, ${isProjectSelected})">
            <fmt:message key="Common.clear"/>
        </html:button>
    </div>

    <titus:reportInitializeConstantsTag/>
    <titus:reportTag id="incomingInvoiceReportList" title="Project.Report.ProjectTimesList"
                     pageOrientation="${PAGE_ORIENTATION_LANDSCAPE}"
                     locale="${sessionScope.user.valueMap['locale']}"
                     timeZone="${sessionScope.user.valueMap['dateTimeZone']}"/>

    <titus:reportFieldTag name="projectName" resourceKey="Project.report.projectName" type="${FIELD_TYPE_STRING}"
                          width="17" fieldPosition="1" groupField="projectId"/>
    <titus:reportFieldTag name="assigneeName" resourceKey="ProjectTime.assignee" type="${FIELD_TYPE_STRING}"
                          width="18" fieldPosition="2" groupField="assigneeAddressId"/>
    <titus:reportFieldTag name="projectDate" resourceKey="ProjectTime.date" type="${FIELD_TYPE_DATEINT}"
                          patternKey="datePattern" width="11" fieldPosition="3"/>
    <titus:reportFieldTag name="activityName" resourceKey="ProjectTime.activityName" type="${FIELD_TYPE_STRING}"
                          width="15" fieldPosition="5"/>
    <titus:reportFieldTag name="status" resourceKey="ProjectTime.status" type="${FIELD_TYPE_STRING}" width="8"
                          fieldPosition="6"
                          conditionMethod="com.piramide.elwis.utils.ReportHelper.getProjectTimeStatus status [${enteredRes}] [${releasedRes}] [${confirmedRes}] [${notConfirmedRes}] [${invoicedRes}]"/>
    <titus:reportFieldTag name="subProjectName" resourceKey="ProjectTime.subProjectName" type="${FIELD_TYPE_STRING}"
                          width="13" fieldPosition="7" groupField="subProjectId"/>
    <titus:reportFieldTag name="time" resourceKey="ProjectTime.report.timeToInvoice"
                          type="${FIELD_TYPE_CALCULATEDDECIMALNUMBER}" align="${FIELD_ALIGN_RIGHT}" width="9"
                          fieldPosition="8"
                          patternKey="numberFormat.2DecimalPlaces"
                          conditionMethod="com.piramide.elwis.utils.ReportHelper.getDoubleValueByCondition time toBeInvoiced [1]"
                          totalizatorOp="${CALCULATION_SUM}" totalizatorKey="ReportGenerator.totalSum"/>
    <titus:reportFieldTag name="time2" resourceKey="ProjectTime.report.timeToNotInvoice"
                          type="${FIELD_TYPE_CALCULATEDDECIMALNUMBER}" align="${FIELD_ALIGN_RIGHT}" width="9"
                          fieldPosition="9"
                          patternKey="numberFormat.2DecimalPlaces"
                          conditionMethod="com.piramide.elwis.utils.ReportHelper.getDoubleValueByCondition time toBeInvoiced [0]"
                          totalizatorOp="${CALCULATION_SUM}" totalizatorKey="ReportGenerator.totalSum"/>

    <titus:reportFieldTag name="fromDateTime" resourceKey="ProjectTime.timeFrom" type="${FIELD_TYPE_DATELONG}"
                          width="8" fieldPosition="10"
                          patternKey="timePattern"/><%--this exeedes the 100% but is managed in action--%>
    <titus:reportFieldTag name="toDateTime" patternKey2="" resourceKey="ProjectTime.timeTo" type="${FIELD_TYPE_DATELONG}"
                          width="8" fieldPosition="11"
                          patternKey="timePattern"/><%--this exeedes the 100% but is managed in action--%>
    <titus:reportFieldTag name="freeTextValue" resourceKey="ProjectTime.description" type="${FIELD_TYPE_STRING}"
                          width="15" fieldPosition="12"/><%--this exeedes the 100% but is managed in action--%>

</html:form>

<tags:jQueryValidation formName="projectTimesReportForm"/>