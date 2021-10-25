<%@ page import="com.piramide.elwis.utils.ProjectConstants" %>
<%@ include file="/Includes.jsp" %>

<tags:initBootstrapDatepicker/>
<tags:initBootstrapSelectPopup/>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<script language="javascript" type="text/javascript">
    function resetProjectItems() {
        document.getElementById("subProjectsField").selectedIndex = 0;
        document.getElementById("assigneeField").selectedIndex = 0;
        document.getElementById("projectActivityField").selectedIndex = 0;
    }
</script>
<fmt:message key="Common.yes" var="invoiceableRes"/>
<fmt:message key="Common.no" var="noInvoiceableRes"/>

<fmt:message key="ProjectTime.status.entered" var="enteredRes"/>
<fmt:message key="ProjectTime.status.released" var="releasedRes"/>
<fmt:message key="ProjectTime.status.confirmed" var="confirmedRes"/>
<fmt:message key="ProjectTime.status.notConfirmed" var="notConfirmedRes"/>
<fmt:message key="ProjectTime.status.invoiced" var="invoicedRes"/>

<c:set var="enteredStatus" value="<%=ProjectConstants.ProjectTimeStatus.ENTERED.getAsString()%>"/>
<c:set var="releasedStatus" value="<%=ProjectConstants.ProjectTimeStatus.RELEASED.getAsString()%>"/>
<c:set var="confirmedStatus" value="<%=ProjectConstants.ProjectTimeStatus.CONFIRMED.getAsString()%>"/>
<c:set var="notConfirmedStatus" value="<%=ProjectConstants.ProjectTimeStatus.NOT_CONFIRMED.getAsString()%>"/>
<c:set var="invoicedStatus" value="<%=ProjectConstants.ProjectTimeStatus.INVOICED.getAsString()%>"/>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>
<fmt:message var="timePattern" key="timePattern"/>
<c:set var="projectTimeStatuses" value="${app2:getProjectTimeStatuses(pageContext.request)}"/>
<c:set var="isProjectSelected" value="${not empty projectTimesAdvancedSearchForm.params.projectId}"/>

<c:set var="projectTimeStatuses" value="${app2:getProjectTimeStatuses(pageContext.request)}"/>

<html:form action="${action}" focus="parameter(projectId)" styleId="projectTimesAdvancedSearchForm"
           styleClass="form-horizontal">
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

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <div class="input-group">
                            <app:text property="parameter(projectName_FIELD)"
                                      styleClass="mediumText ${app2:getFormInputClasses()}" readonly="true"
                                      styleId="projectName_id" tabindex="1"/>
                            <html:hidden property="parameter(projectId)" styleId="projectId_id"/>
                        <span class="input-group-btn">
                            <tags:bootstrapSelectPopup url="/projects/Project/ProjectListPopUp.do"
                                                       name="searchProject"
                                                       styleId="searchProject_id"
                                                       isLargeModal="true"
                                                       titleKey="Common.search"
                                                       modalTitleKey="Project.Title.search"
                                                       tabindex="2" submitOnSelect="true"/>
                            <tags:clearBootstrapSelectPopup keyFieldId="projectId_id" nameFieldId="projectName_id"
                                                            titleKey="Common.clear" tabindex="3" submitOnClear="true"
                                                            onclick="resetProjectItems()"/>
                        </span>
                        </div>
                    </div>
                </div>
                    <%-------------------------Column divider-------------------------------%>
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="subProjectsField">
                        <fmt:message key="Project.report.subProject"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <c:choose>
                            <c:when test="${isProjectSelected}">
                                <fanta:select property="parameter(subProjectId)"
                                              listName="subProjectListForSelect"
                                              labelProperty="subProjectName" valueProperty="subProjectId"
                                              styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                              styleId="subProjectsField"
                                              module="/projects" firstEmpty="true" tabIndex="4">
                                    <fanta:parameter field="companyId"
                                                     value="${sessionScope.user.valueMap['companyId']}"/>
                                    <fanta:parameter field="projectId"
                                                     value="${projectTimesAdvancedSearchForm.params.projectId}"/>
                                </fanta:select>
                            </c:when>
                            <c:otherwise>
                                <app:text property="parameter(subProjectName)" tabindex="4"
                                          styleClass="mediumText ${app2:getFormInputClasses()}"/>
                            </c:otherwise>
                        </c:choose>

                    </div>
                </div>

            </div>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="assigneeField">
                        <fmt:message key="ProjectTime.assignee"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <c:choose>
                            <c:when test="${isProjectSelected}">
                                <fanta:select property="parameter(assigneeAddressId)"
                                              listName="projectUserForSelectList"
                                              labelProperty="userName" valueProperty="addressId"
                                              styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                              styleId="assigneeField"
                                              module="/projects" firstEmpty="true" tabIndex="5"
                                        >
                                    <fanta:parameter field="companyId"
                                                     value="${sessionScope.user.valueMap['companyId']}"/>
                                    <fanta:parameter field="projectId"
                                                     value="${projectTimesAdvancedSearchForm.params.projectId}"/>
                                </fanta:select>
                            </c:when>
                            <c:otherwise>
                                <app:text property="parameter(assigneeName1@_assigneeName2@_assigneeSearchName)"
                                          tabindex="5"
                                          styleClass="mediumText ${app2:getFormSelectClasses()}"/>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <%-------------------------Column divider-------------------------------%>
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="ProjectTime.status"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <html:select property="parameter(status)"
                                     styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                     tabindex="6">
                            <html:option value=""/>
                            <html:options collection="projectTimeStatuses"
                                          property="value"
                                          labelProperty="label"/>
                        </html:select>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="activityName_id">
                        <fmt:message key="Project.report.activity"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <c:choose>
                            <c:when test="${isProjectSelected}">
                                <fanta:select property="parameter(projectActivityId)"
                                              listName="projectActivityForSelectList"
                                              labelProperty="activityName" valueProperty="projectActivityId"
                                              styleClass="mediumSelect ${app2:getFormSelectClasses()}"
                                              styleId="projectActivityField"
                                              module="/projects" firstEmpty="true" tabIndex="7">
                                    <fanta:parameter field="companyId"
                                                     value="${sessionScope.user.valueMap['companyId']}"/>
                                    <fanta:parameter field="projectId"
                                                     value="${projectTimesAdvancedSearchForm.params.projectId}"/>
                                </fanta:select>
                            </c:when>
                            <c:otherwise>
                                <app:text property="parameter(activityName)" tabindex="7"
                                          styleId="activityName_id"
                                          styleClass="mediumText ${app2:getFormInputClasses()}"/>
                            </c:otherwise>
                        </c:choose>

                    </div>
                </div>
                <div class="${app2:getRowClassesTwoColumns()}">
                        <%-------------------------Column divider-------------------------------%>
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="startProjectTime">
                        <fmt:message key="Project.report.timePeriod"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <fmt:message key="datePattern" var="datePattern"/>
                        <fmt:message key="Common.from" var="from"/>
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 MarginButton">
                                <div class="input-group date">
                                    <app:dateText property="parameter(startProjectTime)" maxlength="10" tabindex="8"
                                                  styleId="startProjectTime"
                                                  placeHolder="${from}"
                                                  mode="bootstrap"
                                                  calendarPicker="true" datePatternKey="${datePattern}"
                                                  styleClass="dateText ${app2:getFormInputClasses()}"
                                                  convert="true"/>
                                </div>
                            </div>
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <fmt:message key="Common.to" var="to"/>
                                <div class="input-group date">
                                    <app:dateText property="parameter(endProjectTime)" maxlength="10" tabindex="9"
                                                  styleId="endProjectTime"
                                                  placeHolder="${to}"
                                                  mode="bootstrap"
                                                  calendarPicker="true" datePatternKey="${datePattern}"
                                                  styleClass="dateText ${app2:getFormInputClasses()}"
                                                  convert="true"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="toBeInvoiced_id">
                        <fmt:message key="ProjectTime.toBeInvoiced"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <html:select property="parameter(toBeInvoiced)"
                                     styleId="toBeInvoiced_id"
                                     styleClass="mediumSelect ${app2:getFormSelectClasses()}" tabindex="10">
                            <html:option value=""></html:option>
                            <html:option value="1"><fmt:message key="Common.yes"/> </html:option>
                            <html:option value="0"><fmt:message key="Common.no"/> </html:option>
                        </html:select>
                    </div>
                </div>
                <div class="${app2:getRowClassesTwoColumns()}">
                        <%---------------------Column divider---------------------------------%>
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="ProjectTime.time"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                        <div class="row">
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <label class="control-label" for="timeFrom_id">
                                    <fmt:message key="Common.from"/>
                                </label>
                                <app:numberText property="parameter(timeFrom)"
                                                styleId="timeFrom_id"
                                                styleClass="numberText ${app2:getFormInputClasses()} numberText numberInputWidth"
                                                tabindex="11"
                                                maxlength="6"
                                                numberType="decimal" maxInt="4" maxFloat="1"/>
                            </div>
                            <div class="col-xs-12 col-sm-6 marginButton">
                                <label class="control-label" for="timeTo_id">
                                    <fmt:message key="Common.to"/>
                                </label>
                                <app:numberText property="parameter(timeTo)"
                                                styleId="timeTo_id"
                                                styleClass="numberText ${app2:getFormInputClasses()} numberText numberInputWidth"
                                                tabindex="12"
                                                maxlength="6" numberType="decimal" maxInt="4" maxFloat="1"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <html:submit styleClass="button ${app2:getFormButtonClasses()}" tabindex="31"
                                 property="parameter(submit_button)"><fmt:message
                            key="Common.go"/></html:submit>
                    <html:button property="reset1" tabindex="32" styleClass="button ${app2:getFormButtonClasses()}"
                                 onclick="resetForm(document.projectTimesAdvancedSearchForm, ${isProjectSelected} , ${isProjectSelected})">
                        <fmt:message key="Common.clear"/>
                    </html:button>
                </div>
            </div>
        </fieldset>
    </div>
    <div class="${app2:getAlphabetWrapperClasses()}">
        <fanta:alphabet action="${action}?parameter(submit_button)=submit"
                        mode="bootstrap"
                        parameterName="projectNameAlpha"/>
    </div>

</html:form>

<div class="table-responsive">
    <fanta:table mode="bootstrap" list="projectTimeAdvancedSearchList"
                 action="${action}"
                 styleClass="${app2:getFantabulousTableLargeClases()}"
                 width="100%"
                 id="projectTime"
                 imgPath="${baselayout}">

        <c:set var="editLink"
               value="ProjectTime/Forward/Update.do?dto(timeId)=${projectTime.timeId}&dto(userName)=${app2:encode(projectTime.userName)}&dto(status)=${projectTime.status}&dto(projectId)=${projectTime.projectId}&projectId=${projectTime.projectId}&dto(userId)=${projectTime.userId}&dto(assigneeId)=${projectTime.assigneeAddressId}&viewButton=true&index=1"/>

        <c:set var="deleteLink"
               value="ProjectTime/Forward/Delete.do?dto(timeId)=${projectTime.timeId}&dto(userName)=${app2:encode(projectTime.userName)}&dto(status)=${projectTime.status}&dto(projectId)=${projectTime.projectId}&projectId=${projectTime.projectId}&dto(userId)=${projectTime.userId}&dto(assigneeId)=${projectTime.assigneeAddressId}&viewButton=true&dto(withReferences)=true&index=1"/>

        <c:remove var="statusClass"/>
        <c:if test="${projectTime.status == enteredStatus}">
            <c:set var="statusClass" value="projectTime_enteredColor"/>
        </c:if>
        <c:if test="${projectTime.status == releasedStatus}">
            <c:set var="statusClass" value="projectTime_releasedColor"/>
        </c:if>
        <c:if test="${projectTime.status == confirmedStatus}">
            <c:set var="statusClass" value="projectTime_confirmedColor"/>
        </c:if>
        <c:if test="${projectTime.status == notConfirmedStatus}">
            <c:set var="statusClass" value="projectTime_notConfirmedColor"/>
        </c:if>
        <c:if test="${projectTime.status == invoicedStatus}">
            <c:set var="statusClass" value="projectTime_invoicedColor"/>
        </c:if>

        <fanta:columnGroup title="Common.action" width="5%" headerStyle="listHeader">
            <app2:checkAccessRight functionality="PROJECTTIME" permission="VIEW">
                <fanta:actionColumn name="edit"
                                    title="Common.update"
                                    action="${editLink}"
                                    styleClass="listItem"
                                    headerStyle="listHeader"
                                    glyphiconClass="${app2:getClassGlyphEdit()}"
                                    width="50%"/>
            </app2:checkAccessRight>
            <app2:checkAccessRight functionality="PROJECTTIME" permission="DELETE">
                <fanta:actionColumn name="delete"
                                    title="Common.delete"
                                    action="${deleteLink}"
                                    styleClass="listItem"
                                    headerStyle="listHeader"
                                    glyphiconClass="${app2:getClassGlyphTrash()}"
                                    width="50%"/>
            </app2:checkAccessRight>
        </fanta:columnGroup>
        <fanta:dataColumn name="projectName"
                          action="${editLink}"
                          styleClass="listItem"
                          title="Project.report.projectName"
                          headerStyle="listHeader"
                          orderable="true"
                          maxLength="25"
                          width="12%"/>
        <fanta:dataColumn name="userName"
                          action="${editLink}"
                          styleClass="listItem"
                          title="ProjectTime.userName"
                          headerStyle="listHeader"
                          orderable="true"
                          maxLength="25"
                          width="13%"/>
        <fanta:dataColumn name="assigneeName"
                          styleClass="listItem"
                          title="ProjectTime.assignee"
                          headerStyle="listHeader"
                          orderable="true"
                          maxLength="25"
                          width="13%"/>
        <fanta:dataColumn name="projectDate"
                          styleClass="listItem"
                          title="ProjectTime.date"
                          headerStyle="listHeader"
                          orderable="true"
                          maxLength="25"
                          renderData="false"
                          width="8%">
            <fmt:formatDate var="dateValue" value="${app2:intToDate(projectTime.projectDate)}"
                            pattern="${datePattern}"/>
            ${dateValue}&nbsp;
        </fanta:dataColumn>
        <fanta:dataColumn name="fromDateTime"
                          styleClass="listItem"
                          title="ProjectTime.timeFrom"
                          headerStyle="listHeader"
                          orderable="true"
                          renderData="false"
                          width="5%">
            ${app2:getDateWithTimeZone(projectTime.fromDateTime, null, timePattern)}
        </fanta:dataColumn>
        <fanta:dataColumn name="toDateTime"
                          styleClass="listItem"
                          title="ProjectTime.timeTo"
                          headerStyle="listHeader"
                          orderable="true"
                          renderData="false"
                          width="5%">
            ${app2:getDateWithTimeZone(projectTime.toDateTime, null, timePattern)}
        </fanta:dataColumn>
        <fanta:dataColumn name="time"
                          styleClass="listItemRight"
                          title="ProjectTime.time"
                          headerStyle="listHeader"
                          orderable="true"
                          maxLength="25"
                          renderData="false"
                          width="6%">
            <fmt:formatNumber var="timeFormattedValue"
                              value="${projectTime.time}" type="number"
                              pattern="${numberFormat}"/>
            ${timeFormattedValue}
        </fanta:dataColumn>
        <fanta:dataColumn name="activityName"
                          styleClass="listItem"
                          title="ProjectTime.activityName"
                          headerStyle="listHeader"
                          orderable="true"
                          maxLength="25"
                          width="10%"/>
        <fanta:dataColumn name="subProjectName"
                          styleClass="listItem"
                          title="ProjectTime.subProjectName"
                          headerStyle="listHeader"
                          orderable="true"
                          maxLength="25"
                          width="10%"/>
        <fanta:dataColumn name="toBeInvoiced"
                          styleClass="listItemCenter"
                          title="ProjectTime.invoceable"
                          headerStyle="listHeader"
                          orderable="true"
                          maxLength="25"
                          renderData="false"
                          width="5%">
            <c:if test="${projectTime.toBeInvoiced == '1'}">
                <span class="${app2:getClassGlyphOk()}"></span>
            </c:if>
        </fanta:dataColumn>
        <fanta:dataColumn name="status"
                          styleClass="listItem2 ${statusClass}"
                          title="ProjectTime.status"
                          headerStyle="listHeader"
                          orderable="true"
                          maxLength="25"
                          renderData="false"
                          width="8%">
            <c:set var="statusLabel" value="${app2:searchLabel(projectTimeStatuses, projectTime.status)}"/>
            <fanta:textShorter title="${statusLabel}">
                ${statusLabel}
            </fanta:textShorter>
        </fanta:dataColumn>
    </fanta:table>
</div>
