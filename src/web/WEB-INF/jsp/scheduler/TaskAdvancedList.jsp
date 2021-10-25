<%@ page import="org.joda.time.DateTime,
                 org.joda.time.DateTimeZone" %>
<%@ include file="/Includes.jsp" %>

<tags:initBootstrapDatepicker/>
<tags:initBootstrapSelectPopup/>

<%
    pageContext.setAttribute("statusList", JSPHelper.getPriorityStatusList(request));
    pageContext.setAttribute("addressTypeList", JSPHelper.getAddressTypeList(request));
%>

<script language="JavaScript">
    function onSubmit() {
        document.forms[1].submit();
    }

    function myReset() {
        var form = document.taskListForm;

        for (i = 0; i < form.elements.length; i++) {
            if (form.elements[i].type == "text") {
                form.elements[i].value = "";
            } else if (form.elements[i].name == "beginsWithParam(title)") {
                form.elements[i].checked = false;
            } else if (form.elements[i].type == "select-one") {
                form.elements[i].value = "";
            }
        }
    }
</script>

<div class="${app2:getListWrapperClasses()}">
    <html:form action="/TaskAdvancedList.do" focus="parameter(title)" styleClass="form-horizontal">
        <div class="${app2:getFormPanelClasses()}">
            <fieldset>
                <legend class="title">
                    <fmt:message key="Scheduler.task.advancedSearch"/>
                </legend>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="title_id">
                            <fmt:message key="Task.taskName"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:text property="parameter(title)" styleId="title_id"
                                       styleClass="${app2:getFormInputClasses()} mediumText" tabindex="1"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="priorityId_id">
                            <fmt:message key="Task.priority"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:hidden property="parameter(userId)" value="${sessionScope.user.valueMap['userId']}"/>
                            <fanta:select property="parameter(priorityId)" styleId="priorityId_id"
                                          listName="priorityList" labelProperty="name"
                                          valueProperty="id" styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                          module="/catalogs"
                                          firstEmpty="true"
                                          tabIndex="2">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                                <fanta:parameter field="type" value="SCHEDULER"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="userStatus_id">
                            <fmt:message key="Task.status"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:select property="parameter(userStatus)" styleId="userStatus_id"
                                         styleClass="${app2:getFormSelectClasses()} mediumSelect" tabindex="3">
                                <html:option value=""/>
                                <html:options collection="statusList" property="value" labelProperty="label"/>
                            </html:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}"
                               for="addressName1@_addressName2@_addressName3@_searchName_id">
                            <fmt:message key="Appointment.contact"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <app:text property="parameter(addressName1@_addressName2@_addressName3@_searchName)"
                                      styleId="addressName1@_addressName2@_addressName3@_searchName_id"
                                      styleClass="${app2:getFormInputClasses()} mediumText"
                                      maxlength="40" tabindex="4"/>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="concluded_status_id">
                            <fmt:message key="Task.status.closedOpen"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:select property="parameter(concluded_status)" styleId="concluded_status_id"
                                         styleClass="${app2:getFormSelectClasses()} mediumSelect" tabindex="5">
                                <html:option value=""/>
                                <html:options collection="statusList" property="value" labelProperty="label"/>
                            </html:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="taskTypeId_id">
                            <fmt:message key="Task.taskType"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(taskTypeId)" styleId="taskTypeId_id"
                                          listName="taskTypeList"
                                          labelProperty="taskTypeName" valueProperty="taskTypeId"
                                          styleClass="${app2:getFormSelectClasses()} mediumSelect"
                                          module="/catalogs" firstEmpty="true" tabIndex="6">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Common.assignedFrom"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(createdUserId)" listName="userList"
                                          labelProperty="userName"
                                          valueProperty="userId"
                                          styleClass="${app2:getFormSelectClasses()} mediumSelect" module="/scheduler"
                                          firstEmpty="true"
                                          tabIndex="7">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getRowClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}">
                            <fmt:message key="Task.startDate"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fmt:message key="datePattern" var="datePattern"/>
                            <div class="row">
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <fmt:message key="Common.from" var="from"/>
                                    <div class="input-group date">
                                        <app:dateText property="parameter(startDateFrom)" maxlength="10" tabindex="8"
                                                      styleId="startRange"
                                                      mode="bootstrap"
                                                      placeHolder="${from}"
                                                      calendarPicker="true" datePatternKey="${datePattern}"
                                                      styleClass="${app2:getFormInputClasses()}"
                                                      parseLongAsDate="true"/>
                                    </div>
                                </div>
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <fmt:message key="Common.to" var="to"/>
                                    <div class="input-group date">
                                        <app:dateText property="parameter(startDateTo)" maxlength="10" tabindex="9"
                                                      styleId="endRange"
                                                      calendarPicker="true" datePatternKey="${datePattern}"
                                                      mode="bootstrap"
                                                      placeHolder="${to}"
                                                      styleClass="${app2:getFormInputClasses()}"
                                                      parseLongAsDate="true"/>
                                    </div>
                                </div>
                                <span class="glyphicon form-control-feedback iconValidation"></span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="scheduledUserId_id">
                            <fmt:message key="Common.assignedTo"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <fanta:select property="parameter(scheduledUserId)" listName="userList"
                                          styleId="scheduledUserId_id"
                                          labelProperty="userName"
                                          valueProperty="userId"
                                          styleClass="${app2:getFormSelectClasses()} mediumSelect" module="/scheduler"
                                          firstEmpty="true"
                                          tabIndex="10">
                                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                            </fanta:select>
                            <span class="glyphicon form-control-feedback iconValidation"></span>
                        </div>
                    </div>
                    <div class="${app2:getRowClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="startRange2">
                            <fmt:message key="Task.expireDate"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <div class="row">
                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <div class="input-group date">
                                        <app:dateText property="parameter(expireDateFrom)" maxlength="10" tabindex="11"
                                                      styleId="startRange2"
                                                      calendarPicker="true" datePatternKey="${datePattern}"
                                                      mode="bootstrap"
                                                      placeHolder="${from}"
                                                      styleClass="${app2:getFormInputClasses()}"
                                                      parseLongAsDate="true"/>
                                    </div>
                                </div>

                                <div class="col-xs-12 col-sm-6 marginButton">
                                    <div class="input-group date">
                                        <app:dateText property="parameter(expireDateTo)" maxlength="10" tabindex="12"
                                                      styleId="endRange2"
                                                      calendarPicker="true" datePatternKey="${datePattern}"
                                                      mode="bootstrap"
                                                      placeHolder="${to}"
                                                      styleClass="${app2:getFormInputClasses()}"
                                                      parseLongAsDate="true"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="${app2:getFormGroupClassesTwoColumns()}">
                        <label class="${app2:getFormLabelClassesTwoColumns()}" for="campaignName_id">
                            <fmt:message key="Campaign"/>
                        </label>

                        <div class="${app2:getFormContainClassesTwoColumns(null)}">
                            <html:text property="parameter(campaignName)" styleId="campaignName_d"
                                       styleClass="${app2:getFormInputClasses()} mediumText" tabindex="13"/>
                        </div>
                    </div>
                </div>

                <div class="${app2:getFormButtonWrapperClasses()}">
                    <html:submit styleClass="${app2:getFormButtonClasses()}" tabindex="14">
                        <fmt:message key="Common.go"/>
                    </html:submit>
                    <html:button property="reset1" tabindex="15" styleClass="${app2:getFormButtonClasses()}"
                                 onclick="myReset()">
                        <fmt:message key="Common.clear"/>
                    </html:button>
                </div>
            </fieldset>
        </div>
        <div class="${app2:getAlphabetWrapperClasses()}">
            <fanta:alphabet action="TaskAdvancedList.do" parameterName="titleAlphabet" mode="bootstrap"/>
        </div>
    </html:form>

    <fmt:message var="datePattern" key="datePattern"/>
    <c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
    <%
        DateTime dateTime = new DateTime((DateTimeZone) pageContext.getAttribute("timeZone")).withTime(23, 59, 59, 0);
        pageContext.setAttribute("todayMillis", new Long(dateTime.getMillis()));
    %>


    <app2:checkAccessRight functionality="TASK" permission="CREATE">
        <%-- isUser variable set on SchedulerShortCuts.jsp--%>
        <c:if test="${isUser}">
            <c:set var="newButtonsTable" scope="page">
                <div class="${app2:getFormButtonWrapperClasses()}">
                    <tags:buttonsTable>
                        <app:url value="/Task/Forward/Create.do?advancedListForward=TaskAdvancedSearch"
                                 addModuleParams="false" var="newTaskUrl"/>
                        <input type="button" class="${app2:getFormButtonClasses()}"
                               value="<fmt:message key="Common.new"/>"
                               onclick="window.location ='${newTaskUrl}'">
                    </tags:buttonsTable>
                </div>
            </c:set>
        </c:if>
    </app2:checkAccessRight>

    <c:out value="${newButtonsTable}" escapeXml="false"/>
    <div class="table-responsive">
        <fanta:table list="allTaskAdvancedSearchList" mode="bootstrap" styleClass="${app2:getFantabulousTableLargeClases()}"
                     align="center" id="task" action="TaskAdvancedList.do"
                     imgPath="${baselayout}">
            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <c:choose>
                    <c:when test="${task.ownerId != sessionScope.user.valueMap['userId']}">
                        <c:set var="isParticipant" value="true"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="isParticipant" value=""/>
                    </c:otherwise>
                </c:choose>
                <c:set var="editAction"
                       value="Task/Forward/Update.do?taskId=${task.taskId}&dto(title)=${app2:encode(task.title)}&dto(recordUserId)=${sessionScope.user.valueMap['userId']}${id}&isParticipant=${isParticipant}&advancedListForward=TaskAdvancedSearch"/>
                <c:set var="deleteAction"
                       value="Task/Forward/Delete.do?dto(withReferences)=true&taskId=${task.taskId}&userId=${task.userId}&dto(title)=${app2:encode(task.title)}&dto(recordUserId)=${sessionScope.user.valueMap['userId']}&dto(taskId)=${task.taskId}${id}&operation=delete&advancedListForward=TaskAdvancedSearch"/>
                <c:choose>
                    <c:when test="${task.ownerId == sessionScope.user.valueMap['userId']}">
                        <c:set var="statusValue" value="${task.status}"/>
                    </c:when>
                    <c:otherwise>
                        <fanta:label var="statusValue" listName="participantStatusList" module="/scheduler"
                                     patron="0"
                                     label="status" columnOrder="status">
                            <fanta:parameter field="taskId" value="${not empty task.taskId?task.taskId:0}"/>
                            <fanta:parameter field="userId" value="${sessionScope.user.valueMap['userId']}"/>
                        </fanta:label>
                    </c:otherwise>
                </c:choose>
                <c:remove var="expireClass"/>
                <c:if test="${app2:stringDateIsBefore(task.expireDate, todayMillis) and (task.status != '3')}">
                    <c:set var="expireClass" value=" task_expireColor"/>
                </c:if>
                <app2:checkAccessRight functionality="TASK" permission="VIEW">
                    <fanta:actionColumn name="update" label="Common.update" action="${editAction}"
                                        title="Common.update"
                                        styleClass="listItem" headerStyle="listHeader" width="50%"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"/>
                </app2:checkAccessRight>

                <app2:checkAccessRight functionality="TASK" permission="DELETE">
                    <fanta:actionColumn name="" styleClass="listItem" headerStyle="listHeader" width="50%">
                        <c:choose>
                            <c:when test="${task.ownerId == sessionScope.user.valueMap['userId']}">
                                <html:link action="${deleteAction}" titleKey="Common.delete">
                                    <span class="${app2:getClassGlyphTrash()}"></span>
                                </html:link>
                            </c:when>
                            <c:otherwise>
                                &nbsp;
                            </c:otherwise>
                        </c:choose>
                    </fanta:actionColumn>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="title" action="${editAction}" styleClass="listItem" title="Task.taskName"
                              headerStyle="listHeader" width="18%" orderable="true" maxLength="27"/>
            <fanta:dataColumn name="status" styleClass="listItem" title="Task.status" headerStyle="listHeader"
                              width="8%" orderable="false" renderData="false" maxLength="10">

                <c:if test="${statusValue == '1'}">
                    <c:set var="status_" value="Task.InProgress"/>
                </c:if>
                <c:if test="${statusValue == '2'}">
                    <c:set var="status_" value="Task.notInit"/>
                </c:if>
                <c:if test="${statusValue == '3'}">
                    <c:set var="status_" value="Scheduler.Task.Concluded"/>
                </c:if>
                <c:if test="${statusValue == '4'}">
                    <c:set var="status_" value="Task.Deferred"/>
                </c:if>
                <c:if test="${statusValue == '5'}">
                    <c:set var="status_" value="Task.ToCheck"/>
                </c:if>

                <fanta:textShorter title="${status_}" enableOutputEncode="true">
                    <fmt:message key="${status_}"/>
                </fanta:textShorter>

            </fanta:dataColumn>
            <fanta:dataColumn name="sequence" renderData="false" styleClass="listItem" title="Task.priority"
                              headerStyle="listHeader" width="8%" orderable="true">
                ${task.priorityName}
            </fanta:dataColumn>
            <fanta:dataColumn name="startDate" styleClass="listItem" title="Task.startDate"
                              headerStyle="listHeader"
                              width="8%" orderable="true" renderData="false">
                ${app2:getDateWithTimeZone(task.startDate, timeZone, datePattern)}
            </fanta:dataColumn>
            <fanta:dataColumn name="expireDate" styleClass="listItem${expireClass}" title="Task.expireDate"
                              headerStyle="listHeader" width="8%" orderable="true" renderData="false">
                ${app2:getDateWithTimeZone(task.expireDate, timeZone, datePattern)}
            </fanta:dataColumn>

            <fanta:dataColumn name="contactName" maxLength="22" styleClass="listItem"
                              title="Appointment.contact"
                              headerStyle="listHeader" width="15%" orderable="true"/>

            <fanta:dataColumn name="ownerName" maxLength="22" styleClass="listItem" title="Task.createdBy"
                              headerStyle="listHeader" width="20%" orderable="true"/>

            <fanta:dataColumn renderData="false" name="" styleClass="listItem2Center"
                              title="Scheduler.Task.users"
                              headerStyle="listHeader" width="20%" orderable="false">
                <fanta:select property="" listName="participantTaskList" labelProperty="userName"
                              valueProperty="taskId"
                              module="/scheduler" styleClass="${app2:getFormSelectClasses()}">
                    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                    <fanta:parameter field="taskId" value="${not empty task.taskId?task.taskId:0}"/>
                </fanta:select>
            </fanta:dataColumn>
        </fanta:table>
    </div>
    <c:out value="${newButtonsTable}" escapeXml="false"/>
</div>
<tags:jQueryValidation formName="taskListForm"/>
