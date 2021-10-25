<%@ page import="com.piramide.elwis.cmd.campaignmanager.ActivityCampaignReadCmd" %>
<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ page import="com.piramide.elwis.web.schedulermanager.form.TaskForm" %>
<%@ page import="net.java.dev.strutsejb.AppLevelException" %>
<%@ page import="net.java.dev.strutsejb.dto.ResultDTO" %>
<%@ page import="net.java.dev.strutsejb.web.BusinessDelegate" %>
<%@ include file="/Includes.jsp" %>

<tags:initBootstrapDatepicker/>
<tags:initBootstrapSelectPopup/>
<tags:initBootstrapSelectPopupAdvanced
        fields="fieldProcessId_id, fieldProcessName_id, percent_id, fieldAddressId_id, fieldAddressName_id"/>
<%
    if (request.getAttribute("taskForm") != null) {
        TaskForm form = (TaskForm) request.getAttribute("taskForm");

        if (form.getDto("activityId") != null && !"".equals(form.getDto("activityId").toString())) {
            ActivityCampaignReadCmd cmd = new ActivityCampaignReadCmd();
            cmd.putParam("activityId", form.getDto("activityId"));
            ResultDTO resultDto = new ResultDTO();
            try {
                resultDto = BusinessDelegate.i.execute(cmd, request);
            } catch (AppLevelException e) {
            }
            request.setAttribute("campaignName", resultDto.get("campaignName"));
            request.setAttribute("campaignId", resultDto.get("campaignId"));
        }
    }

    pageContext.setAttribute("statusList", JSPHelper.getPriorityStatusList(request));
    pageContext.setAttribute("intervalMinList", JSPHelper.getIntervalMin());
    pageContext.setAttribute("hourList", JSPHelper.getHours());
%>
<c:set var="personType"><%= ContactConstants.ADDRESSTYPE_PERSON %>
</c:set>

<c:if test="${empty createTaskUrl}">
    <c:set var="createTaskUrl" value="/scheduler/Task/Forward/Create.do" scope="request"/>
</c:if>

<c:if test="${empty taskTab}">
    <c:set var="taskTab" value="Scheduler.Tasks" scope="request"/>
</c:if>

<c:if test="${empty createSalesProcessActionUrl}">
    <c:set var="createSalesProcessActionUrl" value="/sales/SalesProcess/Action/Forward/Create.do" scope="request"/>
</c:if>
<c:if test="${empty salesProcessTab}">
    <c:set var="salesProcessTab" value="SalesProcess.Tab.detail" scope="request"/>
</c:if>

<app2:jScriptUrl
        url="${createTaskUrl}?processId=${taskForm.dtoMap['processId']}&dto(processId)=${taskForm.dtoMap['processId']}&dto(processName)=${app2:encode(taskForm.dtoMap['processName'])}&dto(addressId)=${taskForm.dtoMap['addressId']}&dto(contact)=${app2:encode(taskForm.dtoMap['contact'])}&dto(clear)=true&tabKey=${taskTab}"
        var="newTask"/>

<app2:jScriptUrl
        url="${createSalesProcessActionUrl}?processId=${taskForm.dtoMap['processId']}&dto(processId)=${taskForm.dtoMap['processId']}&dto(processName)=${app2:encode(taskForm.dtoMap['processName'])}&addressId=${taskForm.dtoMap['addressId']}&dto(addressId)=${taskForm.dtoMap['addressId']}&tabKey=${salesProcessTab}"
        var="newSalesProcessAction"/>

<script language="JavaScript" type="text/javascript">
    function changeStartDateValue() {
        $("#expireDate").val($("#startDate").val());
    }
    function applyNavigation(id) {
        selectBox = document.getElementById(id);

        optionObject = selectBox.options[selectBox.selectedIndex];

        if (null != optionObject) {
            if ('1' == optionObject.value) {
                location.href = ${newTask};
            }

            if ('2' == optionObject.value) {
                location.href = ${newSalesProcessAction};
            }
        }
    }

    function habilita() {
        document.getElementById('dueDate_').style.display = "";
    }

    function deshabilita() {
        document.getElementById('dueDate_').style.display = "none";
    }
    function mySubmit() {
        document.forms[0].submit();
    }

</script>

<fmt:message var="datePattern" key="datePattern"/>
<fmt:message var="dateTimePattern" key="dateTimePattern"/>
<c:set var="currentDateTimeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
<c:set var="navigationOptions" value="${app2:getTaskNavigationOptions(pageContext.request, taskForm)}"/>

<div class="${app2:getFormWrapperTwoColumns()}">

<c:set var="currentDateTimeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
<html:form action="${action}" focus="dto(title)" styleClass="form-horizontal">
    <c:set var="module" value="${param.module}"/>

    <c:choose>
        <c:when test="${param.module != 'scheduler'}">
            <c:set var="module" value="${param.module}"/>
            <c:if test="${param.module == 'sales'}">
                <c:set var="id" value="&processId=${param.processId}&taskId=${taskForm.dtoMap.taskId}"/>
                <c:set var="address_Id" value="${taskForm.dtoMap['addressId']}"/>
                <c:set var="module_" value="sales"/>
                <c:set var="index" value="1"/>
            </c:if>
            <c:if test="${param.module == 'contacts'}">
                <c:set var="id" value="&contactId=${param.contactId}&taskId=${taskForm.dtoMap.taskId}"/>
                <html:hidden property="dto(from)"/>
                <c:set var="address_Id" value="${param.contactId}"/>
                <c:set var="index" value="2"/>
                <c:set var="module_" value="contacts"/>
            </c:if>
        </c:when>
        <c:otherwise>
            <c:set var="module_" value="contacts"/>
            <c:set var="address_Id" value="${taskForm.dtoMap['addressId']}"/>
            <c:set var="index" value="2"/>
        </c:otherwise>
    </c:choose>

    <div class="row">
        <c:if test="${'update' ==  op && null != navigationOptions}">
            <div class="col-xs-12 col-sm-12 col-md-4 marginButton">
                <div class="col-xs-10 paddingRemove">
                    <html:select property="dto(navigationOption)"
                                 styleClass="${app2:getFormSelectClasses()}"
                                 styleId="top_navigation_select">
                        <html:options collection="navigationOptions"
                                      property="value"
                                      labelProperty="label"/>
                    </html:select>
                </div>
                <div class="col-xs-2 paddingRightRemove">
                    <html:button property=""
                                 onclick="applyNavigation('top_navigation_select')"
                                 styleClass="${app2:getFormButtonClasses()}">
                        <fmt:message key="Common.go"/>
                    </html:button>
                </div>
            </div>
        </c:if>
        <div class="col-xs-12 col-sm-12 col-md-8 marginButton">
            <c:if test="${taskForm.dtoMap.itParticipates || sessionScope.user.valueMap['userId'] == taskForm.dtoMap.userId}">
                <app2:securitySubmit operation="${op}" property="dto(save)" functionality="TASK"
                                     styleClass="${app2:getFormButtonClasses()}"
                                     tabindex="1">
                    ${button}
                </app2:securitySubmit>
            </c:if>
            <c:if test="${'update' == op && sessionScope.user.valueMap['userId'] == taskForm.dtoMap.userId}">
                <app:url var="url" contextRelative="true"
                         value="${module}/Task/Forward/Delete.do?dto(title)=${app2:encode(param['dto(title)'])}&dto(recordUserId)=${sessionScope.user.valueMap['userId']}${id}&index=${param.index}&dto(taskId)=${param.taskId}&dto(withReferences)=true&dto(companyId)=${sessionScope.user.valueMap['companyId']}"/>

                <app2:checkAccessRight functionality="TASK" permission="DELETE">
                    <html:button onclick="location.href='${url}'" styleClass="${app2:getFormButtonClasses()}"
                                 tabindex="2"
                                 property="dto(save)">
                        <fmt:message key="Common.delete"/>
                    </html:button>
                </app2:checkAccessRight>
            </c:if>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="3">
                <fmt:message key="Common.cancel"/>
            </html:cancel>

                <%--top links--%>
            <c:if test="${param.module != 'scheduler' && op == 'update' && sessionScope.user.valueMap['userId'] == taskForm.dtoMap.userId}">
                <app2:checkAccessRight functionality="TASKUSER" permission="VIEW">
                    &nbsp;|&nbsp;
                    <app:link
                            page="/scheduler/Task/ParticipantTaskList.do?taskId=${taskForm.dtoMap.taskId}&index=1&dto(title)=${app2:encode(taskForm.dtoMap.title)}"
                            contextRelative="true" addModuleParams="false" styleClass="folderTabLink btn btn-link">
                        <fmt:message key="Scheduler.Task.users"/>
                    </app:link>
                </app2:checkAccessRight>
            </c:if>
            <app2:checkAccessRight functionality="COMMUNICATION" permission="CREATE">
                <c:if test="${address_Id != null && address_Id != '' && op == 'update'}">
                    &nbsp;|&nbsp;
                    <app:link
                            page="/${module_}/Communication/Task/Forward/Create.do?taskId=${taskForm.dtoMap.taskId}&dto(addressId)=${taskForm.dtoMap.addressId}&dto(contactPersonId)=${taskForm.dtoMap.contactPersonId}&tabKey=Contacts.Tab.communications&processId=${param.processId}&contactId=${taskForm.dtoMap.addressId}&addressId=${taskForm.dtoMap.addressId}"
                            contextRelative="true" addModuleParams="false" styleClass="folderTabLink btn btn-link">
                        <fmt:message key="Communication.Title.new"/>
                    </app:link>
                </c:if>
            </app2:checkAccessRight>

                <%--address and contact person detail--%>
            <c:if test="${not empty taskForm.dtoMap['addressId'] and 'update' == op}">
                <app2:checkAccessRight functionality="CONTACT" permission="VIEW">
                    <c:set var="addressMap"
                           value="${app2:getAddressMap(taskForm.dtoMap['addressId'])}"/>
                    <c:choose>
                        <c:when test="${personType == addressMap['addressType']}">
                            <c:set var="addressEditLink"
                                   value="/contacts/Person/Forward/Update.do?contactId=${taskForm.dtoMap['addressId']}&dto(addressId)=${taskForm.dtoMap['addressId']}&dto(addressType)=${addressMap['addressType']}&dto(name1)=${app2:encode(addressMap['name1'])}&dto(name2)=${app2:encode(addressMap['name2'])}&dto(name3)=${app2:encode(addressMap['name3'])}&index=0"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="addressEditLink"
                                   value="/contacts/Organization/Forward/Update.do?contactId=${taskForm.dtoMap['addressId']}&dto(addressId)=${taskForm.dtoMap['addressId']}&dto(addressType)=${addressMap['addressType']}&dto(name1)=${app2:encode(addressMap['name1'])}&dto(name2)=${app2:encode(addressMap['name2'])}&dto(name3)=${app2:encode(addressMap['name3'])}&index=0"/>
                        </c:otherwise>
                    </c:choose>
                </app2:checkAccessRight>

                <c:if test="${not empty addressEditLink}">
                    &nbsp;|&nbsp;
                    <app:link page="${addressEditLink}" styleClass="folderTabLink btn btn-link"
                              contextRelative="true"
                              addModuleParams="false">
                        <fmt:message key="Contact.detail"/>
                    </app:link>
                </c:if>

                <c:if test="${not empty taskForm.dtoMap['contactPersonId']}">
                    <app2:checkAccessRight functionality="CONTACTPERSON" permission="UPDATE">
                        &nbsp;|&nbsp;
                        <app:link
                                page="/contacts/ContactPerson/Forward/Update.do?contactId=${taskForm.dtoMap['addressId']}&dto(addressId)=${taskForm.dtoMap['addressId']}&dto(contactPersonId)=${taskForm.dtoMap['contactPersonId']}&tabKey=Contacts.Tab.contactPersons"
                                styleClass="folderTabLink btn btn-link" contextRelative="true"
                                addModuleParams="false">
                            <fmt:message key="ContactPerson.detail"/>
                        </app:link>
                    </app2:checkAccessRight>
                </c:if>
            </c:if>
        </div>
    </div>
    <div class="${app2:getFormPanelClasses()}">

        <html:hidden property="dto(op)" value="${op}"/>
        <html:hidden property="dto(companyId)" value="${sessionScope.user.valueMap['companyId']}"/>
        <html:hidden property="dto(recordUserId)" value="${sessionScope.user.valueMap['userId']}"/>
        <html:hidden property="dto(itParticipates)"/>
        <html:hidden property="dto(type)" value="${type}"/>

        <c:if test="${'update' == op || 'delete' == op}">
            <html:hidden property="dto(version)"/>
            <html:hidden property="dto(participantVersion)"/>
            <html:hidden property="dto(taskId)"/>
            <html:hidden property="dto(userId)"/>
        </c:if>
        <c:if test="${'create' == op }">
            <html:hidden property="dto(userId)" value="${sessionScope.user.valueMap['userId']}"/>
        </c:if>
        <c:if test="${sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}">
            <html:hidden property="dto(noEdit)" value="true"/>
        </c:if>
        <c:if test="${'update' == op && not empty taskForm.dtoMap['activityId']}">
            <html:hidden property="dto(activityId)"/>
        </c:if>
        <fieldset>
            <legend class="title">
                <c:out value="${title}"/>
            </legend>

            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="title_id">
                        <fmt:message key="Task.taskName"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId)}">
                        <app:text property="dto(title)" styleId="title_id"
                                  styleClass="${app2:getFormInputClasses()} middleText" maxlength="60"
                                  tabindex="4"
                                  view="${op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="fieldAddressName_id">
                        <fmt:message key="Appointment.contact"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(op == 'delete' || noSch =='true' || taskForm.dtoMap.clear == 'true' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId)}">
                        <html:hidden property="dto(clear)"/>
                        <html:hidden property="dto(addressId)" styleId="fieldAddressId_id"/>
                        <c:choose>
                            <c:when test="${noSch && param.module != 'sales'}">
                                <div class="row col-xs-12">
                                    <app:text property="dto(contact)" value="${addressName}"
                                              styleClass="${app2:getFormInputClasses()} middleText"
                                              maxlength="40"
                                              tabindex="5" view="${true}" readonly="true"/>
                                    <html:hidden property="dto(name)" styleId="fieldAddressName_id"/>
                                </div>
                                <c:if test="${not empty addressEditLink}">
                                        <span class="pull-right">
                                            <app:link action="${addressEditLink}" styleClass=" "
                                                      titleKey="Common.edit"
                                                      contextRelative="true" tabindex="6">
                                                <span class="glyphicon glyphicon-edit"></span>
                                            </app:link>
                                        </span>
                                </c:if>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </c:when>
                            <c:otherwise>
                                <c:set var="isOnlyView"
                                       value="${op == 'delete' || noSch =='true' || taskForm.dtoMap.clear == 'true' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}"/>
                                <c:choose>
                                    <c:when test="${taskForm.dtoMap.clear == 'true' && address_Id != null && address_Id != '' && op == 'update' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId || param.module != 'scheduler'}">
                                       <app:text property="dto(contact)" styleId="fieldAddressName_id"
                                                  styleClass="${app2:getFormInputClasses()} middleText"
                                                  maxlength="40"
                                                  tabindex="5"
                                                  view="${isOnlyView}"
                                                  readonly="true"/>
                                        <div class="pull-right">
                                            <c:if test="${not empty addressEditLink}">
                                                <app:link action="${addressEditLink}" titleKey="Common.edit"
                                                          contextRelative="true"
                                                          tabindex="6" styleClass=" ">
                                                    <span class="glyphicon glyphicon-edit"></span>
                                                </app:link>
                                            </c:if>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="input-group">
                                            <app:text property="dto(contact)" styleId="fieldAddressName_id"
                                                      styleClass="${app2:getFormInputClasses()} middleText"
                                                      maxlength="40"
                                                      tabindex="5"
                                                      view="${isOnlyView}"
                                                      readonly="true"/>
                                    <span class="input-group-btn">
                                        <c:if test="${not empty addressEditLink}">
                                            <app:link action="${addressEditLink}" titleKey="Common.edit"
                                                      contextRelative="true"
                                                      tabindex="6" styleClass="${app2:getFormButtonClasses()}">
                                                <span class="glyphicon glyphicon-edit"></span>
                                            </app:link>
                                        </c:if>

                                        <tags:bootstrapSelectPopup styleId="searchAddress_id"
                                                                   url="/contacts/SearchAddress.do"
                                                                   name="searchAddress"
                                                                   titleKey="Common.search"
                                                                   modalTitleKey="Contact.Title.search"
                                                                   hide="${isOnlyView}"
                                                                   submitOnSelect="true"
                                                                   isLargeModal="true"
                                                                   tabindex="7"/>
                                        <tags:clearBootstrapSelectPopup keyFieldId="fieldAddressId_id"
                                                                        styleClass="${app2:getFormButtonClasses()}"
                                                                        nameFieldId="fieldAddressName_id"
                                                                        titleKey="Common.clear"
                                                                        submitOnClear="true"
                                                                        hide="${isOnlyView}"
                                                                        glyphiconClass="glyphicon-erase" tabindex="8"/>
                                    </span>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="taskTypeId_id">
                        <fmt:message key="Task.taskType"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId)}">
                        <fanta:select property="dto(taskTypeId)" styleId="taskTypeId_id" listName="taskTypeList"
                                      labelProperty="taskTypeName" valueProperty="taskTypeId"
                                      styleClass="${app2:getFormSelectClasses()} middleSelect"
                                      readOnly="${op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}"
                                      module="/catalogs" firstEmpty="true" tabIndex="9">
                            <fanta:parameter field="companyId"
                                             value="${sessionScope.user.valueMap['companyId']}"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="contactPersonId_id">
                        <fmt:message key="Appointment.contactPerson"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId)}">
                        <c:choose>
                            <c:when test="${'update' == op}">
                                <fanta:select onChange="mySubmit();" property="dto(contactPersonId)"
                                              styleId="contactPersonId_id" tabIndex="10"
                                              listName="searchContactPersonList" module="/contacts"
                                              firstEmpty="true"
                                              labelProperty="contactPersonName" valueProperty="contactPersonId"
                                              styleClass="${app2:getFormSelectClasses()}"
                                              readOnly="${op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}">
                                    <fanta:parameter field="companyId"
                                                     value="${sessionScope.user.valueMap['companyId']}"/>
                                    <fanta:parameter field="addressId"
                                                     value="${not empty taskForm.dtoMap['addressId']?taskForm.dtoMap['addressId']:0}"/>
                                </fanta:select>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </c:when>
                            <c:otherwise>
                                <fanta:select property="dto(contactPersonId)" styleId="contactPersonId_id"
                                              tabIndex="11"
                                              listName="searchContactPersonList"
                                              module="/contacts" firstEmpty="true"
                                              labelProperty="contactPersonName"
                                              valueProperty="contactPersonId"
                                              styleClass="${app2:getFormSelectClasses()}"
                                              readOnly="${op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}">
                                    <fanta:parameter field="companyId"
                                                     value="${sessionScope.user.valueMap['companyId']}"/>
                                    <fanta:parameter field="addressId"
                                                     value="${not empty taskForm.dtoMap['addressId']?taskForm.dtoMap['addressId']:0}"/>
                                </fanta:select>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </c:otherwise>
                        </c:choose>

                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Task.priority"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId)}">
                        <fanta:select property="dto(priorityId)" styleId="priorityId_id"
                                      listName="selectPriorityList"
                                      labelProperty="name" valueProperty="id"
                                      styleClass="${app2:getFormSelectClasses()}"
                                      readOnly="${op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}"
                                      module="/catalogs" firstEmpty="true" tabIndex="12">
                            <fanta:parameter field="companyId"
                                             value="${sessionScope.user.valueMap['companyId']}"/>
                            <fanta:parameter field="type" value="SCHEDULER"/>
                        </fanta:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="status_id">
                        <fmt:message key="Task.status"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId)}">
                        <html:select property="dto(status)" styleId="status_id"
                                     styleClass="${app2:getFormSelectClasses()}"
                                     readonly="${op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}"
                                     tabindex="13">
                            <html:option value=""/>
                            <html:options collection="statusList" property="value" labelProperty="label"/>
                        </html:select>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}" for="startDate">
                        <fmt:message key="Task.startDate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId)}">
                        <div class="${app2:getFormGroupClasses()} removeAllMarginButton">
                            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-5 marginButton paddingRemove">
                                <div class="input-group date">
                                    <app:dateText property="dto(startDate)"
                                                  onchange="changeStartDateValue()"
                                                  styleId="startDate"
                                                  mode="bootstrap"
                                                  calendarPicker="${op != 'delete'}"
                                                  datePatternKey="${datePattern}"
                                                  styleClass="${app2:getFormInputClasses()}"
                                                  view="${op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}"
                                                  maxlength="10"
                                                  tabindex="14"
                                                  currentDate="true"
                                                  currentDateTimeZone="${currentDateTimeZone}"/>
                                </div>
                            </div>

                            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-6 col-sm-offset-0 col-md-offset-0 col-lg-offset-1 marginButton paddingRemove">
                                <div class="input-group">
                                    <html:select property="dto(startHour)" tabindex="15"
                                                 styleClass="${app2:getFormSelectClasses()}"
                                                 readonly="${op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}"
                                                 styleId="startHour" style="border-radius:4px">
                                        <html:options collection="hourList" property="value"
                                                      labelProperty="label"/>
                                    </html:select>
                                    <c:choose>
                                        <c:when test="${op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}">
                                            <span>:</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="input-group-addon"
                                                  style="background-color: transparent;border:0px">:</span>
                                        </c:otherwise>
                                    </c:choose>
                                    <html:select property="dto(startMin)" tabindex="16"
                                                 styleClass="${app2:getFormSelectClasses()}"
                                                 readonly="${op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}"
                                                 styleId="startMin" style="border-radius:4px">
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
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Task.percent"/>
                        <fmt:message key="Common.probabilitySymbol"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId)}">
                        <c:set var="percents" value="${app2:defaultProbabilities()}"/>
                        <div class="${app2:getFormGroupClasses()} removeAllMarginButton">
                            <html:select property="dto(percent)" styleClass="${app2:getFormSelectClasses()}"
                                         readonly="${op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}"
                                         tabindex="17">
                                <html:option value="">&nbsp;
                                </html:option>
                                <html:options collection="percents" property="value" labelProperty="label"/>
                            </html:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getRowClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Task.expireDate"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)} form-group removeAllMarginButton">
                        <div class="col-xs-12 paddingRemove">
                            <div class="radiocheck marginButton">
                                <div class="radio radio-default radio-inline">
                                    <html:radio property="dto(date)" styleId="noDate" value="false"
                                                onclick="deshabilita()"
                                                styleClass="radio"
                                                tabindex="18"
                                                disabled="${op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}"/>
                                    <label for="noDate"><fmt:message key="Task.NoDueDate"/></label>
                                </div>
                                <div class="radio radio-default radio-inline">
                                    <html:radio property="dto(date)" styleId="date" value="true"
                                                onclick="habilita()"
                                                styleClass="radio"
                                                tabindex="19"
                                                disabled="${op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}"/>
                                    <label for="date"><fmt:message key="Task.Date"/></label>
                                </div>
                            </div>
                            <div id="dueDate_"  ${taskForm.dtoMap.date == 'false' ? "style=\"display: none;\"":""}>
                                <div class="col-xs-12 col-sm-12 col-md-12 col-lg-5 marginButton paddingRemove">
                                    <div class="input-group date">
                                        <app:dateText property="dto(expireDate)" mode="bootstrap"
                                                      styleId="expireDate"
                                                      calendarPicker="${op != 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}"
                                                      datePatternKey="${datePattern}"
                                                      styleClass="${app2:getFormInputClasses()}"
                                                      view="${op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}"
                                                      maxlength="11" tabindex="20" currentDate="true"
                                                      currentDateTimeZone="${currentDateTimeZone}"/>
                                    </div>
                                </div>
                                <div class="col-xs-12 col-sm-12 col-md-12 col-lg-6 col-sm-offset-0 col-md-offset-0 col-lg-offset-1 marginButton paddingRemove">
                                    <div class="input-group">
                                        <html:select property="dto(expireHour)" styleId="expireHour"
                                                     tabindex="21"
                                                     styleClass="${app2:getFormSelectClasses()}"
                                                     readonly="${op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}"
                                                     style="border-radius:4px">
                                            <html:options collection="hourList" property="value"
                                                          labelProperty="label"/>
                                        </html:select>
                                        <c:if test="${!(!taskForm.dtoMap.date && ((op == 'update' && sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId) || op == 'delete'))}">
                                            <c:choose>
                                                <c:when test="${op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}">
                                                    <span>:</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="input-group-addon"
                                                          style="background-color: transparent;border:0px">:</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:if>
                                        <html:select property="dto(expireMin)" styleId="expireMin" tabindex="22"
                                                     styleClass="${app2:getFormSelectClasses()}"
                                                     readonly="${op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}"
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
                    <c:if test="${op == 'create'}">
                        <div class="${app2:getFormGroupClasses()} removeAllMarginButton">
                            <label class="${app2:getFormLabelClassesTwoColumns()}">
                                <fmt:message key="Task.AssignedA"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                <fanta:select property="dto(userTaskId)" listName="userList"
                                              labelProperty="userName" valueProperty="userId"
                                              styleClass="${app2:getFormSelectClasses()}"
                                              module="/scheduler" firstEmpty="true" tabIndex="23">
                                    <fanta:parameter field="companyId"
                                                     value="${sessionScope.user.valueMap['companyId']}"/>
                                </fanta:select>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${op != 'create'}">
                        <div class="${app2:getFormGroupClasses()} removeAllMarginButton">
                            <label class="${app2:getFormLabelClassesTwoColumns()}">
                                <fmt:message key="Task.createdBy"/>
                            </label>

                            <div class="${app2:getFormContainClassesTwoColumns(true)}">
                                <app:write id="userid"
                                           value="${taskForm.dtoMap.userId}"
                                           fieldName="username"
                                           tableName="elwisuser" name="true"/>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </c:if>
                </div>
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Task.createDateTime"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(true)}">
                        <c:if test="${'create' != op && null != taskForm.dtoMap['createDateTime']}">
                            <html:hidden property="dto(createDateTime)"/>
                            <c:out value="${app2:getDateWithTimeZone(taskForm.dtoMap['createDateTime'], currentDateTimeZone, dateTimePattern)}"/>
                        </c:if>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Document.salesAsociated"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(noSch && param.module != 'contacts' || op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId)}">
                        <html:hidden property="dto(processId)" styleId="fieldProcessId_id"/>
                        <html:hidden property="dto(percentId)" styleId="percent_id"/>

                        <c:choose>
                            <c:when test="${address_Id != '' && address_Id != null}">
                                <c:set var="param_value"
                                       value="/sales/SalesProcess/SearchSalesProcess.do?parameter(addressId)=${address_Id}"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="param_value" value="/sales/SalesProcess/SearchSalesProcess.do"/>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${noSch && param.module != 'contacts'}">
                                <app:text property="dto(processName)" value="${processName}"
                                          styleClass="${app2:getFormInputClasses()}"
                                          maxlength="30"
                                          view="${true}" readonly="true" styleId="fieldProcessName_id"
                                          tabindex="24"/>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </c:when>
                            <c:otherwise>
                                <div class="input-group">
                                    <app:text property="dto(processName)"
                                              styleClass="${app2:getFormInputClasses()}" maxlength="30"
                                              view="${op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}"
                                              readonly="true" styleId="fieldProcessName_id" tabindex="25"/>
                                            <span class="input-group-btn">
                                                <tags:bootstrapSelectPopup styleId="searchSalesProcess_id"
                                                                           url="${param_value}"
                                                                           name="searchSalesProcess"
                                                                           titleKey="Common.search"
                                                                           modalTitleKey="SalesProcess.Title.simpleSearch"
                                                                           hide="${op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}"
                                                                           submitOnSelect="true"
                                                                           isLargeModal="true"
                                                                           tabindex="26"/>
                                                <tags:clearBootstrapSelectPopup keyFieldId="fieldProcessId_id"
                                                                                nameFieldId="fieldProcessName_id"
                                                                                titleKey="Common.clear"
                                                                                hide="${op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}"
                                                                                submitOnClear="true"
                                                                                styleClass="${app2:getFormButtonClasses()}"
                                                                                glyphiconClass="glyphicon-erase"
                                                                                tabindex="27"/>
                                            </span>
                                </div>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </c:otherwise>
                        </c:choose>

                    </div>
                </div>
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Task.updateDateTime"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(op == 'delete' || 'update' == op )}">
                        <c:if test="${null != taskForm.dtoMap['updateDateTime']}">
                            <html:hidden property="dto(updateDateTime)"/>
                            <c:out value="${app2:getDateWithTimeZone(taskForm.dtoMap['updateDateTime'], currentDateTimeZone, dateTimePattern)}"/>
                        </c:if>
                    </div>
                </div>
            </div>
            <div class="row">
                <c:set var="showCampaign" value="${false}"/>
                <c:if test="${not empty taskForm.dtoMap['activityId'] && op == 'update'}">
                    <c:set var="showCampaign" value="${true}"/>
                </c:if>
                <div class="${app2:getFormGroupClassesTwoColumns()}">
                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                        <fmt:message key="Task.Notification"/>
                    </label>

                    <div class="${app2:getFormContainClassesTwoColumns(null)}" colspan="${showCampaign ? '0':'3'}">
                        <div class="radiocheck">
                            <div class="checkbox checkbox-default">
                                <html:checkbox property="dto(notification)"
                                               styleId="notification_id"
                                               disabled="${op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}"
                                               tabindex="28"/>
                                <label for="notification_id"></label>
                            </div>
                        </div>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
                <c:if test="${showCampaign}">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Campaign"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(true)}">
                            <div class="row col-xs-12">
                                <c:out value="${campaignName}"/>
                                <c:set var="editLink"
                                       value="campaign/Campaign/Forward/Update.do?campaignId=${campaignId}&index=0&dto(campaignId)=${campaignId}&dto(campaignName)=${app2:encode(campaignName)}&dto(operation)=update"/>
                            </div>
                            <app:link action="${editLink}" styleClass="pull-right" titleKey="Common.edit"
                                      contextRelative="true"
                                      tabindex="29">
                                <span class="glyphicon glyphicon-edit"></span>
                            </app:link>
                        </div>
                    </div>
                </c:if>
            </div>
            <div class="col-xs-12">
                <div class="form-group">
                    <label class="control-label col-xs-12 label-left row">
                        <fmt:message key="Appointment.description"/>
                    </label>

                    <div class="col-xs-12 row">
                        <html:textarea property="dto(descriptionText)"
                                       styleClass="${app2:getFormInputClasses()} mediumDetail"
                                       style="height:120px;width:99%;"
                                       tabindex="30"
                                       readonly="${op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}"/>
                        <html:hidden property="dto(freeTextId)"/>
                        <span class="glyphicon form-control-feedback iconValidation"></span>
                    </div>
                </div>
            </div>
            <c:if test="${op != 'create' && taskForm.dtoMap.itParticipates}">
                <div class="row">
                    <div class="col-xs-12">
                        <fieldset>
                            <legend class="title">
                                <fmt:message key="Task.detailTask"/>
                            </legend>

                            <div class="row">
                                <div class="${app2:getFormGroupClassesTwoColumns()}">
                                    <label class="${app2:getFormLabelClassesTwoColumns()}">
                                        <fmt:message key="Task.StatusYourTask"/>
                                    </label>

                                    <div class="${app2:getFormContainClassesTwoColumns(null)}">
                                        <html:hidden property="dto(oldAssignedStatus)"/>
                                        <html:hidden property="dto(participant)" value="true"/>
                                        <html:select property="dto(statusId)"
                                                     styleClass="${app2:getFormSelectClasses()}"
                                                     readonly="${op == 'delete'}" tabindex="31">
                                            <html:option value=""/>
                                            <html:options collection="statusList" property="value"
                                                          labelProperty="label"/>
                                        </html:select>
                                        <span class="glyphicon form-control-feedback iconValidation"></span>
                                    </div>
                                </div>
                            </div>
                            <div class="col-xs-12">
                                <div class="form-group">
                                    <label class="control-label col-xs-12 label-left row">
                                        <fmt:message key="Task.notes"/>
                                    </label>

                                    <div class="col-xs-12 row">
                                        <html:textarea property="dto(noteValue)"
                                                       styleClass="${app2:getFormSelectClasses()}"
                                                       tabindex="32"
                                                       style="height:120px;"
                                                       readonly="${op == 'delete'}"/>
                                        <span class="glyphicon form-control-feedback iconValidation"></span>
                                    </div>
                                </div>
                            </div>
                        </fieldset>
                    </div>
                </div>
            </c:if>
        </fieldset>
    </div>

    <div class="row">
        <c:if test="${'update' ==  op && null != navigationOptions}">
            <div class="col-xs-12 col-sm-12 col-md-4 marginButton">
                <div class="col-xs-10 paddingRemove">
                    <html:select property="dto(navigationOption)"
                                 styleClass="${app2:getFormSelectClasses()}"
                                 styleId="bottom_navigation_select"
                                 tabindex="33">
                        <html:options collection="navigationOptions"
                                      property="value"
                                      labelProperty="label"/>
                    </html:select>
                </div>
                <div class="col-xs-2 paddingRightRemove">
                    <html:button property=""
                                 onclick="applyNavigation('bottom_navigation_select');"
                                 styleClass="${app2:getFormButtonClasses()}"
                                 tabindex="34">
                        <fmt:message key="Common.go"/>
                    </html:button>
                </div>
            </div>
        </c:if>
        <div class="col-xs-12 col-sm-12 col-md-8 marginButton">
            <c:if test="${taskForm.dtoMap.itParticipates || sessionScope.user.valueMap['userId'] == taskForm.dtoMap.userId}">
                <app2:securitySubmit operation="${op}" property="dto(save)" functionality="TASK"
                                     styleClass="${app2:getFormButtonClasses()}" tabindex="35">
                    ${button}
                </app2:securitySubmit>
            </c:if>
            <c:if test="${'update' == op && sessionScope.user.valueMap['userId'] == taskForm.dtoMap.userId}">
                <app2:checkAccessRight functionality="TASK" permission="DELETE">
                    <html:button onclick="location.href='${url}'" tabindex="36"
                                 styleClass="${app2:getFormButtonClasses()}"
                                 property="dto(save)">
                        <fmt:message key="Common.delete"/>
                    </html:button>
                </app2:checkAccessRight>
            </c:if>
            <html:cancel styleClass="${app2:getFormButtonCancelClasses()}" tabindex="37">
                <fmt:message key="Common.cancel"/>
            </html:cancel>
        </div>
    </div>
    </div>
    <c:if test="${taskForm.dtoMap.userId==sessionScope.user.valueMap['userId'] && op!='create'}">
        <fanta:list listName="participantTaskListForTaskView" module="/scheduler">
            <fanta:parameter field="companyId"
                             value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="taskId" value="${taskForm.dtoMap.taskId}"/>
            <fanta:parameter field="ownerUserId" value="${taskForm.dtoMap.userId}"/>
        </fanta:list>
        <c:if test="${not empty participantTaskListForTaskView.result}">
            <div class="col-xs-12">
                <div class="${app2:getFormPanelClasses()}">
                    <fieldset>
                        <legend class="title">
                            <fmt:message key="Task.participantsComent"/>
                        </legend>

                        <c:forEach var="participantComment"
                                   items="${participantTaskListForTaskView.result}">
                            <div class="${app2:getFormGroupClasses()}">
                                <div class="contain">
                                    <label class="control-label">
                                            ${participantComment.userName}
                                    </label>
                                    &nbsp;
                                        ${app2:getUserTaskStatusLabel(sessionScope.user.valueMap['locale'],participantComment.status)}
                                </div>

                                <p>
                                    <c:out value="${app2:convertTextToHtml(participantComment.freeTextValue)}"
                                           escapeXml="false"/>
                                </p>
                            </div>
                        </c:forEach>
                    </fieldset>
                </div>
            </div>
        </c:if>
    </c:if>
</html:form>
</div>

<tags:jQueryValidation formName="taskForm"/>