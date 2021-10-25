<%@ include file="/Includes.jsp" %>

<tags:initBootstrapDatepicker/>
<tags:initBootstrapSelectPopup/>

<tags:initBootstrapSelectPopupAdvanced
        fields="fieldProcessId_id, fieldProcessName_id, percent_id, fieldAddressId_id, fieldAddressName_id"/>
<%
    pageContext.setAttribute("statusList", JSPHelper.getPriorityStatusList(request));
    pageContext.setAttribute("intervalMinList", JSPHelper.getIntervalMin());
    pageContext.setAttribute("hourList", JSPHelper.getHours());
%>

<script language="JavaScript">
    function changeStartDateValue() {
        $("#expireDate").val($("#startDate").val());
    }
    function habilita() {
        document.getElementById('dueDate_').style.display = "";
    }

    function deshabilita() {
        document.getElementById('dueDate_').style.display = "none";
    }

    function setUserCreateName(selectBox) {
        document.getElementById('userCreateName').value = selectBox.options[selectBox.selectedIndex].text;
    }

</script>

<c:set var="currentDateTimeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>

<div class="${app2:getFormWrapperTwoColumns()}">
    <html:form action="/Campaign/Task/Create.do" focus="dto(title)" styleClass="form-horizontal">
        <%--<html:hidden property="dto(recordUserId)" value="${sessionScope.user.valueMap['userId']}"/>--%>
        <%--<html:hidden property="dto(type)" value="${type}"  />--%>

        <html:hidden property="dto(userId)" value="${sessionScope.user.valueMap['userId']}"/>
        <html:hidden property="dto(activityId)"/>
        <html:hidden property="dto(campaignId)" value="${param.campaignId}"/>
        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" property="dto(save)" functionality="TASK"
                                 styleClass="${app2:getFormButtonClasses()}" tabindex="18">
                <fmt:message key="Campaign.activity.task.buttonCreate"/>
            </app2:securitySubmit>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="19">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>

        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <fmt:message key="Campaign.activity.task.title.createConfirmation"/>
                </legend>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="title_id">
                            <fmt:message key="Task.taskName"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                            <app:text property="dto(title)" styleId="title_id"
                                      styleClass="${app2:getFormInputClasses()} middleText" maxlength="40" tabindex="1"
                                      view="${op == 'delete'}"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="status_id">
                            <fmt:message key="Task.status"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                            <html:select property="dto(status)" styleId="status_id"
                                         styleClass="${app2:getFormSelectClasses()}" readonly="${op == 'delete'}"
                                         tabindex="2">
                                <html:option value=""/>
                                <html:options collection="statusList" property="value" labelProperty="label"/>
                            </html:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <fmt:message var="datePattern" key="datePattern"/>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="startDate">
                            <fmt:message key="Task.startDate"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                            <div class="${app2:getFormGroupClasses()} removeAllMarginButton">
                                <div class="col-xs-12 col-sm-12 col-md-12 col-lg-5 marginButton paddingRemove">
                                    <div class="input-group date">
                                        <app:dateText property="dto(startDate)" styleId="startDate"
                                                      onchange="changeStartDateValue()"
                                                      mode="bootstrap"
                                                      calendarPicker="${op != 'delete'}" datePatternKey="${datePattern}"
                                                      styleClass="${app2:getFormInputClasses()}"
                                                      view="${op == 'delete'}" maxlength="10"
                                                      tabindex="3"
                                                      currentDate="true"
                                                      currentDateTimeZone="${currentDateTimeZone}"/>
                                    </div>
                                </div>
                                <div class="col-xs-12 col-sm-12 col-md-12 col-lg-6 col-sm-offset-0 col-md-offset-0 col-lg-offset-1 marginButton paddingRemove">
                                    <div class="input-group">
                                        <html:select property="dto(startHour)" tabindex="4"
                                                     styleClass="${app2:getFormSelectClasses()}"
                                                     readonly="${op == 'delete'}" styleId="startHour"
                                                     style="border-radius:4px">
                                            <html:options collection="hourList" property="value" labelProperty="label"/>
                                        </html:select>
                                        <c:choose>
                                            <c:when test="op == 'delete'">
                                                <span>:</span>
                                            </c:when>
                                            <c:otherwise>
                                            <span class="input-group-addon"
                                                  style="background-color: transparent;border:0px">:</span>
                                            </c:otherwise>
                                        </c:choose>
                                        <html:select property="dto(startMin)" tabindex="5"
                                                     styleClass="${app2:getFormSelectClasses()}"
                                                     readonly="${op == 'delete'}" styleId="startMin"
                                                     style="border-radius:4px">
                                            <html:options collection="intervalMinList" property="value"
                                                          labelProperty="label"/>
                                        </html:select>
                                    </div>
                                </div>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="percent_id">
                            <fmt:message key="Task.percent"/>
                            (<fmt:message key="Common.probabilitySymbol"/>)
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                            <c:set var="percents" value="${app2:defaultProbabilities()}"/>
                            <html:select property="dto(percent)" styleId="percent_id"
                                         styleClass="${app2:getFormSelectClasses()}" readonly="${op == 'delete'}"
                                         tabindex="6">
                                <html:option value="">&nbsp;</html:option>
                                <html:options collection="percents" property="value" labelProperty="label"/>
                            </html:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Task.expireDate"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')} form-group removeAllMarginButton">
                            <div class="col-xs-12 paddingRemove">
                                <div class="radiocheck marginButton">
                                    <div class="radio radio-default radio-inline">
                                        <html:radio property="dto(date)" styleId="noDate" value="false"
                                                    onclick="deshabilita()"
                                                    styleClass="radio" tabindex="7" disabled="${op == 'delete'}"/>
                                        <label for="noDate"> <fmt:message key="Task.NoDueDate"/></label>
                                    </div>
                                    <div class="radio radio-default radio-inline">
                                        <html:radio property="dto(date)" styleId="date" value="true"
                                                    onclick="habilita()"
                                                    styleClass="radio" tabindex="8" disabled="${op == 'delete'}"/>
                                        <label for="date"><fmt:message key="Task.Date"/></label>
                                    </div>
                                </div>
                                <div id="dueDate_"  ${taskForm.dtoMap.date == 'false' ? "style=\"display: none;\"":""}>
                                    <div class="col-xs-12 col-sm-12 col-md-12 col-lg-5 marginButton paddingRemove">
                                        <div class="input-group date">
                                            <app:dateText property="dto(expireDate)" mode="bootstrap"
                                                          styleId="expireDate"
                                                          calendarPicker="${op != 'delete'}"
                                                          datePatternKey="${datePattern}"
                                                          styleClass="${app2:getFormInputClasses()}"
                                                          view="${op == 'delete'}" maxlength="11"
                                                          tabindex="9" currentDate="true"
                                                          currentDateTimeZone="${currentDateTimeZone}"/>
                                        </div>
                                    </div>
                                    <div class="col-xs-12 col-sm-12 col-md-12 col-lg-6 col-sm-offset-0 col-md-offset-0 col-lg-offset-1 marginButton paddingRemove">
                                        <div class="input-group">
                                            <html:select property="dto(expireHour)" styleId="expireHour" tabindex="10"
                                                         styleClass="${app2:getFormSelectClasses()}"
                                                         readonly="${op == 'delete'}"
                                                         style="border-radius:4px">
                                                <html:options collection="hourList" property="value"
                                                              labelProperty="label"/>
                                            </html:select>
                                            <c:choose>
                                                <c:when test="${op == 'delete'}">
                                                    <span>:</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="input-group-addon"
                                                          style="background-color: transparent;border:0px">:</span>
                                                </c:otherwise>
                                            </c:choose>
                                            <html:select property="dto(expireMin)" styleId="expireMin" tabindex="11"
                                                         styleClass="${app2:getFormSelectClasses()}"
                                                         readonly="${op == 'delete'}"
                                                         style="border-radius:4px">
                                                <html:options collection="intervalMinList" property="value"
                                                              labelProperty="label"/>
                                            </html:select>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="priorityId_id">
                            <fmt:message key="Task.priority"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                            <fanta:select property="dto(priorityId)" styleId="priorityId_id"
                                          listName="selectPriorityList"
                                          labelProperty="name" valueProperty="id"
                                          styleClass="${app2:getFormSelectClasses()}"
                                          readOnly="${op == 'delete'}" module="/catalogs" firstEmpty="true"
                                          tabIndex="12">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                                <fanta:parameter field="type" value="SCHEDULER"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                        <%--</div>--%>
                        <%--<div class="row">--%>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="taskTypeId_id">
                            <fmt:message key="Task.taskType"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                            <fanta:select property="dto(taskTypeId)" styleId="taskTypeId_id" listName="taskTypeList"
                                          labelProperty="taskTypeName" valueProperty="taskTypeId"
                                          styleClass="${app2:getFormSelectClasses()}"
                                          readOnly="${op == 'delete'}" module="/catalogs" firstEmpty="true"
                                          tabIndex="13">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="taskCreateUserId_id">
                            <fmt:message key="Campaign.activity.task.createdBy"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(op == 'delete')}">
                            <c:set var="users"
                                   value="${app2:getActivityCreateTaskUsers(campaignTaskForm.dtoMap['activityId'], pageContext.request)}"/>
                            <html:hidden property="dto(taskCreateUserName)" styleId="userCreateName"/>
                            <html:select property="dto(taskCreateUserId)" styleId="taskCreateUserId_id"
                                         styleClass="${app2:getFormSelectClasses()} middleSelect" tabindex="14"
                                         readonly="${op == 'delete'}"
                                         onchange="javascript:setUserCreateName(this);"
                                         onkeyup="javascript:setUserCreateName(this);">
                                <html:option value="">&nbsp;</html:option>
                                <html:options collection="users" property="value" labelProperty="label"/>
                            </html:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="col-xs-12">
                    <div class="${app2:getFormGroupClasses()}">
                        <label class="control-label col-xs-12 label-left row" for="descriptionText_id">
                            <fmt:message key="Campaign.activity.task.description"/>
                        </label>

                        <div class="col-xs-12 row">
                            <html:textarea property="dto(descriptionText)" styleId="descriptionText_id"
                                           styleClass="${app2:getFormInputClasses()} mediumDetail"
                                           style="height:120px;"
                                           tabindex="15"
                                           readonly="${op == 'delete'}"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
            </fieldset>
        </div>

        <div class="${app2:getFormButtonWrapperClasses()}">
            <app2:securitySubmit operation="${op}" property="dto(save)" functionality="TASK"
                                 styleClass="${app2:getFormButtonClasses()}" tabindex="16">
                <fmt:message key="Campaign.activity.task.buttonCreate"/>
            </app2:securitySubmit>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="17">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </html:form>
</div>

<tags:jQueryValidation formName="campaignTaskForm"/>