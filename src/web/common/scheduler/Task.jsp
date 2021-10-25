<%@ page import="com.piramide.elwis.cmd.campaignmanager.ActivityCampaignReadCmd" %>
<%@ page import="com.piramide.elwis.utils.ContactConstants" %>
<%@ page import="com.piramide.elwis.web.schedulermanager.form.TaskForm" %>
<%@ page import="net.java.dev.strutsejb.AppLevelException" %>
<%@ page import="net.java.dev.strutsejb.dto.ResultDTO" %>
<%@ page import="net.java.dev.strutsejb.web.BusinessDelegate" %>
<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<tags:initSelectPopup/>
<tags:initSelectPopupAdvanced
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
<c:set var="personType"><%= ContactConstants.ADDRESSTYPE_PERSON %></c:set>

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

<html:form action="${action}" focus="dto(title)">
<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
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
<tr>
<td>
<br>
<table cellSpacing=0 cellPadding=4 width="100%" border=0 align="center">
    <tr>
        <td class="button">
            <c:if test="${'update' ==  op && null != navigationOptions}">
                <html:select property="dto(navigationOption)"
                             styleClass="select"
                             styleId="top_navigation_select">
                    <html:options collection="navigationOptions"
                                  property="value"
                                  labelProperty="label"/>
                </html:select>
                <html:button property=""
                             onclick="applyNavigation('top_navigation_select')"
                             styleClass="button">
                    <fmt:message key="Common.go"/>
                </html:button>
            </c:if>

            <c:if test="${taskForm.dtoMap.itParticipates || sessionScope.user.valueMap['userId'] == taskForm.dtoMap.userId}">
                <app2:securitySubmit operation="${op}" property="dto(save)" functionality="TASK" styleClass="button"
                                     tabindex="1">
                    ${button}
                </app2:securitySubmit>
            </c:if>
            <c:if test="${'update' == op && sessionScope.user.valueMap['userId'] == taskForm.dtoMap.userId}">

                <app:url var="url" contextRelative="true"
                         value="${module}/Task/Forward/Delete.do?dto(title)=${app2:encode(param['dto(title)'])}&dto(recordUserId)=${sessionScope.user.valueMap['userId']}${id}&index=${param.index}&dto(taskId)=${param.taskId}&dto(withReferences)=true&dto(companyId)=${sessionScope.user.valueMap['companyId']}"/>

                <app2:checkAccessRight functionality="TASK" permission="DELETE">
                    <html:button onclick="location.href='${url}'" styleClass="button" tabindex="2" property="dto(save)">
                        <fmt:message key="Common.delete"/>
                    </html:button>
                </app2:checkAccessRight>
            </c:if>
            <html:cancel styleClass="button" tabindex="3">
                <fmt:message key="Common.cancel"/>
            </html:cancel>

            <%--top links--%>
            &nbsp;
            <c:if test="${param.module != 'scheduler' && op == 'update' && sessionScope.user.valueMap['userId'] == taskForm.dtoMap.userId}">
                <app2:checkAccessRight functionality="TASKUSER" permission="VIEW">
                    &nbsp;|&nbsp;
                    <app:link
                            page="/scheduler/Task/ParticipantTaskList.do?taskId=${taskForm.dtoMap.taskId}&index=1&dto(title)=${app2:encode(taskForm.dtoMap.title)}"
                            contextRelative="true" addModuleParams="false" styleClass="folderTabLink">
                        <fmt:message key="Scheduler.Task.users"/>
                    </app:link>
                </app2:checkAccessRight>
            </c:if>
            <app2:checkAccessRight functionality="COMMUNICATION" permission="CREATE">
                <c:if test="${address_Id != null && address_Id != '' && op == 'update'}">
                    &nbsp;|&nbsp;
                    <app:link
                            page="/${module_}/Communication/Task/Forward/Create.do?taskId=${taskForm.dtoMap.taskId}&dto(addressId)=${taskForm.dtoMap.addressId}&dto(contactPersonId)=${taskForm.dtoMap.contactPersonId}&tabKey=Contacts.Tab.communications&processId=${param.processId}&contactId=${taskForm.dtoMap.addressId}&addressId=${taskForm.dtoMap.addressId}"
                            contextRelative="true" addModuleParams="false" styleClass="folderTabLink">
                        <fmt:message key="Communication.Title.new"/>
                    </app:link>
                </c:if>
            </app2:checkAccessRight>

            <%--address and contact person detail--%>
            <c:if test="${not empty taskForm.dtoMap['addressId'] and 'update' == op}">
                <app2:checkAccessRight functionality="CONTACT" permission="VIEW">
                    <c:set var="addressMap" value="${app2:getAddressMap(taskForm.dtoMap['addressId'])}"/>
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
                    <app:link page="${addressEditLink}" styleClass="folderTabLink" contextRelative="true"
                              addModuleParams="false">
                        <fmt:message key="Contact.detail"/>
                    </app:link>
                </c:if>

                <c:if test="${not empty taskForm.dtoMap['contactPersonId']}">
                    <app2:checkAccessRight functionality="CONTACTPERSON" permission="UPDATE">
                        &nbsp;|&nbsp;
                        <app:link page="/contacts/ContactPerson/Forward/Update.do?contactId=${taskForm.dtoMap['addressId']}&dto(addressId)=${taskForm.dtoMap['addressId']}&dto(contactPersonId)=${taskForm.dtoMap['contactPersonId']}&tabKey=Contacts.Tab.contactPersons"
                                  styleClass="folderTabLink" contextRelative="true" addModuleParams="false">
                            <fmt:message key="ContactPerson.detail"/>
                        </app:link>
                    </app2:checkAccessRight>
                </c:if>
            </c:if>

        </td>
    </tr>
</table>

<table border="0" cellpadding="0" cellspacing="0" width="100%" align="center" class="container">
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
<tr>
    <td colspan="4" class="title">
        <c:out value="${title}"/>
    </td>
</tr>
<tr>
    <td width="14%" class="label"><fmt:message key="Task.taskName"/></TD>
    <td width="36%" class="contain">
        <app:text property="dto(title)" styleClass="middleText" maxlength="40" tabindex="4"
                  view="${op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}"/>
    </td>

    <TD class="label" width="15%"><fmt:message key="Appointment.contact"/></TD>
    <TD class="contain">
        <html:hidden property="dto(clear)"/>
        <html:hidden property="dto(addressId)" styleId="fieldAddressId_id"/>
        <c:choose>
            <c:when test="${noSch && param.module != 'sales'}">
                <html:hidden property="dto(name)" styleId="fieldAddressName_id"/>
                <app:text property="dto(contact)" value="${addressName}" styleClass="middleText" maxlength="40"
                          tabindex="17" view="${true}" readonly="true"/>

                <c:if test="${not empty addressEditLink}">
                    <app:link action="${addressEditLink}" contextRelative="true" tabindex="17">
                        <html:img src="${baselayout}/img/edit.gif" titleKey="Common.edit" border="0"/>
                    </app:link>
                </c:if>

            </c:when>
            <c:otherwise>

                <c:set var="isOnlyView" value="${op == 'delete' || noSch =='true' || taskForm.dtoMap.clear == 'true' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}"/>

                <app:text property="dto(contact)" styleId="fieldAddressName_id" styleClass="middleText" maxlength="40"
                          tabindex="17"
                          view="${isOnlyView}"
                          readonly="true"/>

                <c:if test="${not empty addressEditLink}">
                    <app:link action="${addressEditLink}" contextRelative="true" tabindex="17">
                        <html:img src="${baselayout}/img/edit.gif" titleKey="Common.edit" border="0" align="${isOnlyView ? '' : 'middle'}"/>
                    </app:link>
                </c:if>

                <tags:selectPopup url="/contacts/SearchAddress.do" name="searchAddress" titleKey="Common.search"
                                  hide="${isOnlyView}"
                                  submitOnSelect="true"/>
                <tags:clearSelectPopup keyFieldId="fieldAddressId_id" nameFieldId="fieldAddressName_id"
                                       titleKey="Common.clear" submitOnClear="true"
                                       hide="${isOnlyView}"/>
            </c:otherwise>
        </c:choose>

    </TD>
</tr>
<tr>

    <td class="topLabel"><fmt:message key="Task.taskType"/></TD>
    <td class="contain">
        <fanta:select property="dto(taskTypeId)" listName="taskTypeList"
                      labelProperty="taskTypeName" valueProperty="taskTypeId" styleClass="middleSelect"
                      readOnly="${op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}"
                      module="/catalogs" firstEmpty="true" tabIndex="5">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>

    <TD class="label"><fmt:message key="Appointment.contactPerson"/></TD>
    <TD class="contain">
        <c:choose>
            <c:when test="${'update' == op}">

                <fanta:select onChange="mySubmit();" property="dto(contactPersonId)" tabIndex="18"
                              listName="searchContactPersonList" module="/contacts" firstEmpty="true"
                              labelProperty="contactPersonName" valueProperty="contactPersonId"
                              styleClass="middleSelect"
                              readOnly="${op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    <fanta:parameter field="addressId"
                                     value="${not empty taskForm.dtoMap['addressId']?taskForm.dtoMap['addressId']:0}"/>
                </fanta:select>
            </c:when>
            <c:otherwise>
                <fanta:select property="dto(contactPersonId)" tabIndex="18" listName="searchContactPersonList"
                              module="/contacts" firstEmpty="true" labelProperty="contactPersonName"
                              valueProperty="contactPersonId" styleClass="middleSelect"
                              readOnly="${op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    <fanta:parameter field="addressId"
                                     value="${not empty taskForm.dtoMap['addressId']?taskForm.dtoMap['addressId']:0}"/>
                </fanta:select>
            </c:otherwise>
        </c:choose>

    </TD>

</tr>

<tr>
    <td class="label"><fmt:message key="Task.priority"/></TD>
    <td class="contain">
        <fanta:select property="dto(priorityId)" listName="selectPriorityList"
                      labelProperty="name" valueProperty="id" styleClass="middleSelect"
                      readOnly="${op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}"
                      module="/catalogs" firstEmpty="true" tabIndex="6">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="type" value="SCHEDULER"/>
        </fanta:select>
    </TD>
    <td class="label"><fmt:message key="Task.status"/></TD>
    <td class="contain">
        <html:select property="dto(status)" styleClass="select"
                     readonly="${op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}"
                     tabindex="19">
            <html:option value=""/>
            <html:options collection="statusList" property="value" labelProperty="label"/>
        </html:select>
    </td>
</tr>
<tr>
    <td class="label"><fmt:message key="Task.startDate"/></td>
    <td class="contain">
        <app:dateText property="dto(startDate)"
                      styleId="startDate"
                      calendarPicker="${op != 'delete'}"
                      datePatternKey="${datePattern}"
                      styleClass="dateText"
                      view="${op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}"
                      maxlength="10"
                      tabindex="7"
                      currentDate="true"/>
        &nbsp;
        <html:select property="dto(startHour)" tabindex="8" styleClass="select"
                     readonly="${op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}"
                     styleId="startHour" style="width:40">
            <html:options collection="hourList" property="value" labelProperty="label"/>
        </html:select> :
        <html:select property="dto(startMin)" tabindex="9" styleClass="select"
                     readonly="${op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}"
                     styleId="startMin" style="width:40">
            <html:options collection="intervalMinList" property="value" labelProperty="label"/>
        </html:select>

    </td>
    <td class="label"><fmt:message key="Task.percent"/></TD>
    <td class="contain">
        <c:set var="percents" value="${app2:defaultProbabilities()}"/>
        <html:select property="dto(percent)" styleClass="select"
                     readonly="${op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}"
                     tabindex="20">
            <html:option value="">&nbsp;</html:option>
            <html:options collection="percents" property="value" labelProperty="label"/>
        </html:select>
        <fmt:message key="Common.probabilitySymbol"/>
    </td>
</tr>

<tr>
    <td class="topLabel" rowspan="2"><fmt:message key="Task.expireDate"/></TD>
    <td class="containTop" rowspan="2">
        <html:radio property="dto(date)" styleId="noDate" value="false" onclick="deshabilita()" styleClass="radio"
                    tabindex="10"
                    disabled="${op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}"/>
        <fmt:message key="Task.NoDueDate"/>
        &nbsp;&nbsp;
        <html:radio property="dto(date)" styleId="date" value="true" onclick="habilita()" styleClass="radio"
                    tabindex="11"
                    disabled="${op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}"/>
        <fmt:message key="Task.Date"/>
        <div id="dueDate_"  ${taskForm.dtoMap.date == 'false' ? "style=\"display: none;\"":""}>
            <br>
            <app:dateText property="dto(expireDate)" styleId="expireDate"
                          calendarPicker="${op != 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}"
                          datePatternKey="${datePattern}" styleClass="dateText"
                          view="${op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}"
                          maxlength="11" tabindex="12" currentDate="true"/>
            &nbsp;
            <html:select property="dto(expireHour)" styleId="expireHour" tabindex="13" styleClass="select"
                         readonly="${op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}"
                         style="width:40">
                <html:options collection="hourList" property="value" labelProperty="label"/>
            </html:select>
            <c:if test="${!(!taskForm.dtoMap.date && ((op == 'update' && sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId) || op == 'delete'))}">
                :
            </c:if>
            <html:select property="dto(expireMin)" styleId="expireMin" tabindex="14" styleClass="select"
                         readonly="${op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}"
                         style="width:40">
                <html:options collection="intervalMinList" property="value" labelProperty="label"/>
            </html:select>
        </div>
    </td>
    <c:if test="${op == 'create'}">
        <td class="label">
            <fmt:message key="Task.AssignedA"/>
        </td>
        <td class="contain">
            <fanta:select property="dto(userTaskId)" listName="userList"
                          labelProperty="userName" valueProperty="userId" styleClass="middleSelect"
                          module="/scheduler" firstEmpty="true" tabIndex="21">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            </fanta:select>
        </td>
    </c:if>
    <c:if test="${op != 'create'}">
        <td class="label">
            <fmt:message key="Task.createdBy"/>
        </td>
        <td class="contain">
            <app:write id="userid"
                       value="${taskForm.dtoMap.userId}"
                       fieldName="username"
                       tableName="elwisuser" name="true"/>
        </td>
    </c:if>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Task.createDateTime"/>
    </td>
    <TD class="contain">
        <c:if test="${'create' != op && null != taskForm.dtoMap['createDateTime']}">
            <html:hidden property="dto(createDateTime)"/>
            <c:out value="${app2:getDateWithTimeZone(taskForm.dtoMap['createDateTime'], currentDateTimeZone, dateTimePattern)}"/>
        </c:if>
    </TD>
</tr>

<tr>
    <TD class="label">
        <fmt:message key="Document.salesAsociated"/>
    </TD>
    <TD class="contain">
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
                <app:text property="dto(processName)" value="${processName}" styleClass="middleText" maxlength="30"
                          view="${true}" readonly="true" styleId="fieldProcessName_id" tabindex="15"/>
            </c:when>
            <c:otherwise>
                <app:text property="dto(processName)" styleClass="middleText" maxlength="30"
                          view="${op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}"
                          readonly="true" styleId="fieldProcessName_id" tabindex="15"/>
                <tags:selectPopup url="${param_value}" name="searchSalesProcess"
                                  titleKey="Common.search"
                                  hide="${op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}"
                                  submitOnSelect="true"
                                  width="900" heigth="480" scrollbars="false"/>
                <tags:clearSelectPopup keyFieldId="fieldProcessId_id" nameFieldId="fieldProcessName_id"
                                       titleKey="Common.clear"
                                       hide="${op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}"
                                       submitOnClear="true"/>
            </c:otherwise>
        </c:choose>
    </td>

    <td class="label">
        <fmt:message key="Task.updateDateTime"/>
    </td>
    <td class="contain">
        <c:if test="${null != taskForm.dtoMap['updateDateTime']}">
            <html:hidden property="dto(updateDateTime)"/>
            <c:out value="${app2:getDateWithTimeZone(taskForm.dtoMap['updateDateTime'], currentDateTimeZone, dateTimePattern)}"/>
        </c:if>
    </td>
</tr>

<tr>
    <c:set var="showCampaign" value="${false}"/>
    <c:if test="${not empty taskForm.dtoMap['activityId'] && op == 'update'}">
        <c:set var="showCampaign" value="${true}"/>
    </c:if>

    <td class="label">
        <fmt:message key="Task.Notification"/>
    </td>
    <td class="contain" colspan="${showCampaign ? '0':'3'}">
        <html:checkbox property="dto(notification)"
                       disabled="${op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}"
                       tabindex="16" styleClass="radio"/>
    </td>

    <c:if test="${showCampaign}">
        <td class="label">
            <fmt:message key="Campaign"/>
        </td>
        <TD class="contain">
            <c:out value="${campaignName}"/>

            <c:set var="editLink"
                   value="campaign/Campaign/Forward/Update.do?campaignId=${campaignId}&index=0&dto(campaignId)=${campaignId}&dto(campaignName)=${app2:encode(campaignName)}&dto(operation)=update"/>
            <app:link action="${editLink}" contextRelative="true" tabindex="22">
                <html:img src="${baselayout}/img/edit.gif" titleKey="Common.edit" border="0"/>
            </app:link>
        </TD>
    </c:if>
</tr>

<TR>
    <TD class="topLabel" colspan="4">
        <fmt:message key="Appointment.description"/><br>
        <html:textarea property="dto(descriptionText)" styleClass="mediumDetail" style="height:120px;width:99%;"
                       tabindex="23"
                       readonly="${op == 'delete' || sessionScope.user.valueMap['userId'] != taskForm.dtoMap.userId}"/>
        <html:hidden property="dto(freeTextId)"/>
    </td>
</TR>

<c:if test="${op != 'create' && taskForm.dtoMap.itParticipates}">
    <tr>
        <td colspan="4" class="title">
            <fmt:message key="Task.detailTask"/>
        </td>
    </tr>
    <tr>
        <td class="topLabel">
            <fmt:message key="Task.StatusYourTask"/>
        </td>
        <td class="containTop" colspan="3">
            <html:hidden property="dto(oldAssignedStatus)"/>
            <html:hidden property="dto(participant)" value="true"/>
            <html:select property="dto(statusId)" styleClass="middleSelect" readonly="${op == 'delete'}" tabindex="23">
                <html:option value=""/>
                <html:options collection="statusList" property="value" labelProperty="label"/>
            </html:select>
        </td>
    <tr>
    <tr>
        <td class="topLabel" colspan="4">
            <fmt:message key="Task.notes"/>
            <html:textarea property="dto(noteValue)" styleClass="mediumDetail" tabindex="24"
                           style="height:120px;width:99%;"
                           readonly="${op == 'delete'}"/>
        </td>
    </tr>
</c:if>
</table>
</td>
</tr>
<tr>
    <td>
        <table cellSpacing=0 cellPadding=4 width="100%" border=0 align="center">
            <tr>
                <td class="button" nowrap>
                    <c:if test="${'update' ==  op && null != navigationOptions}">
                        <html:select property="dto(navigationOption)"
                                     styleClass="select"
                                     styleId="bottom_navigation_select"
                                     tabindex="25">
                            <html:options collection="navigationOptions"
                                          property="value"
                                          labelProperty="label"/>
                        </html:select>
                        <html:button property=""
                                     onclick="applyNavigation('bottom_navigation_select');"
                                     styleClass="button"
                                     tabindex="26">
                            <fmt:message key="Common.go"/>
                        </html:button>
                    </c:if>
                    <c:if test="${taskForm.dtoMap.itParticipates || sessionScope.user.valueMap['userId'] == taskForm.dtoMap.userId}">
                        <app2:securitySubmit operation="${op}" property="dto(save)" functionality="TASK"
                                             styleClass="button" tabindex="27">
                            ${button}
                        </app2:securitySubmit>
                    </c:if>
                    <c:if test="${'update' == op && sessionScope.user.valueMap['userId'] == taskForm.dtoMap.userId}">
                        <app2:checkAccessRight functionality="TASK" permission="DELETE">
                            <html:button onclick="location.href='${url}'" tabindex="28" styleClass="button"
                                         property="dto(save)">
                                <fmt:message key="Common.delete"/>
                            </html:button>
                        </app2:checkAccessRight>
                    </c:if>
                    <html:cancel styleClass="button" tabindex="29">
                        <fmt:message key="Common.cancel"/>
                    </html:cancel>
                </td>
            </tr>
            <c:if test="${taskForm.dtoMap.userId==sessionScope.user.valueMap['userId'] && op!='create'}">
                <tr>
                    <td>
                        <fanta:list listName="participantTaskListForTaskView" module="/scheduler">
                            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            <fanta:parameter field="taskId" value="${taskForm.dtoMap.taskId}"/>
                            <fanta:parameter field="ownerUserId" value="${taskForm.dtoMap.userId}"/>
                        </fanta:list>
                        <c:if test="${not empty participantTaskListForTaskView.result}">
                            <table width="100%" border="0" align="center" cellpadding="3" cellspacing="0">
                                <tr>
                                    <td class="title" colspan="2"><fmt:message key="Task.participantsComent"/></td>
                                </tr>
                                <c:forEach var="participantComment" items="${participantTaskListForTaskView.result}">
                                    <tr>
                                        <td class="contain" width="97%">
                                                ${participantComment.userName} &nbsp;
                                                ${app2:getUserTaskStatusLabel(sessionScope.user.valueMap['locale'],participantComment.status)}
                                            &nbsp;
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="label">
                                            <c:out value="${app2:convertTextToHtml(participantComment.freeTextValue)}"
                                                   escapeXml="false"/>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </table>
                        </c:if>

                    </td>
                </tr>
            </c:if>
        </table>
    </td>
</tr>
</table>
</html:form>