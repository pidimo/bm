<%@ page import="com.piramide.elwis.utils.SchedulerConstants,
                 org.joda.time.DateTime" %>
<%@ page import="org.joda.time.DateTimeZone" %>
<%@ include file="/Includes.jsp" %>


<fmt:message var="datePattern" key="datePattern"/>
<c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
<%
    DateTime dateTime = new DateTime((DateTimeZone) pageContext.getAttribute("timeZone")).withTime(23, 59, 59, 0);
    pageContext.setAttribute("todayMillis", new Long(dateTime.getMillis()));
    pageContext.setAttribute("taskShowStatusCollection", JSPHelper.getTaskStatusFilterList(request));
    pageContext.setAttribute("DEFAULT_STATUS", SchedulerConstants.SHOW_NOT_CLOSED);
%>

<div class="${app2:getListWrapperClasses()}">
    <html:form action="/TaskList.do?simple=true&fromSearchForm=true" focus="parameter(title)"
               styleClass="form-horizontal">
        <c:if test="${param.module == 'scheduler'}">
            <fieldset>
                <legend class="title">
                    <fmt:message key="Scheduler.taskSearch"/>
                </legend>
            </fieldset>
        </c:if>
        <div class="${app2:getFormGroupClasses()}">
            <div class="row col-xs-12 col-sm-5 marginButton">
                <label class="control-label col-xs-12 col-sm-3 label-left">
                    <fmt:message key="Common.search"/>
                </label>

                <div class="col-xs-12 col-sm-9">
                    <html:text property="parameter(title)" styleClass="${app2:getFormInputClasses()}"/>
                </div>
            </div>
            <div class="row col-xs-12 col-sm-7">
                <c:choose>
                    <c:when test="${setDefaultStatusInView}">
                        <div class="col-xs-12 col-sm-6 marginButton">
                            <html:select property="parameter(concluded_status)"
                                         styleClass="${app2:getFormSelectClasses()}"
                                         value="${DEFAULT_STATUS}">
                                <html:options collection="taskShowStatusCollection" property="value"
                                              labelProperty="label"/>
                            </html:select>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="col-xs-12 col-sm-6 marginButton">
                            <html:select property="parameter(concluded_status)"
                                         styleClass="${app2:getFormSelectClasses()}">
                                <html:options collection="taskShowStatusCollection" property="value"
                                              labelProperty="label"/>
                            </html:select>
                        </div>
                    </c:otherwise>
                </c:choose>
                <div class="col-xs-12 col-sm-2 marginButton">
                    <html:submit styleClass="${app2:getFormButtonClasses()}">
                        <fmt:message key="Common.go"/>
                    </html:submit>
                </div>
                <c:if test="${param.module == 'scheduler'}">
                    <div class="col-xs-12 col-sm-4 marginButton">
                        <app:link styleClass="btn btn-link" action="/TaskAdvancedList.do">
                            <fmt:message key="Common.advancedSearch"/>
                        </app:link>
                    </div>
                </c:if>
            </div>
        </div>
    </html:form>

    <div class="${app2:getAlphabetWrapperClasses()}">
        <fanta:alphabet action="TaskList.do?simple=true&fromSearchForm=true" parameterName="titleAlphabet" mode="bootstrap"/>
    </div>

    <c:if test="${param.module != 'scheduler'}">
        <c:if test="${param.module == 'sales'}">
            <c:set var="id" value="&processId=${param.processId}"/>
            <c:set var="names"
                   value="addressName=${app2:encode(addressName)}&processName=${app2:encode(processName)}"/>
        </c:if>
        <c:if test="${param.module == 'contacts'}">
            <c:set var="id" value="&contactId=${param.contactId}"/>
            <c:set var="names" value="addressName=${app2:encode(addressName)}"/>
        </c:if>
        <app2:checkAccessRight functionality="TASK" permission="CREATE">
            <html:form action="/Task/Forward/Create.do?${names}">
                <html:submit styleClass="${app2:getFormButtonClasses()} marginButton">
                    <fmt:message key="Common.new"/>
                </html:submit>
            </html:form>
        </app2:checkAccessRight>
    </c:if>

    <c:out value="${param.module == 'scheduler' ? '<br>' : ''}" escapeXml="false"/>

    <c:if test="${param.module == 'scheduler'}">
        <app2:checkAccessRight functionality="TASK" permission="CREATE">
            <%-- isUser variable set on SchedulerShortCuts.jsp--%>
            <c:if test="${isUser}">
                <c:set var="newButtonsTable" scope="page">
                    <div class="${app2:getFormButtonWrapperClasses()}">
                        <tags:buttonsTable>
                            <app:url value="/Task/Forward/Create.do"
                                     addModuleParams="false" var="newTaskUrl"/>
                            <input type="button" class="${app2:getFormButtonClasses()}"
                                   value="<fmt:message key="Common.new"/>"
                                   onclick="window.location ='${newTaskUrl}'">
                        </tags:buttonsTable>
                    </div>
                </c:set>
            </c:if>
        </app2:checkAccessRight>
    </c:if>
    <c:out value="${newButtonsTable}" escapeXml="false"/>
    <div class="table-responsive">
        <fanta:table mode="bootstrap" styleClass="${app2:getFantabulousTableLargeClases()}" align="center" id="task"
                     action="TaskList.do?simple=true" imgPath="${baselayout}">
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
                       value="Task/Forward/Update.do?taskId=${task.taskId}&dto(title)=${app2:encode(task.title)}&dto(recordUserId)=${sessionScope.user.valueMap['userId']}${id}&isParticipant=${isParticipant}"/>
                <c:set var="deleteAction"
                       value="Task/Forward/Delete.do?dto(withReferences)=true&taskId=${task.taskId}&userId=${task.userId}&dto(title)=${app2:encode(task.title)}&dto(recordUserId)=${sessionScope.user.valueMap['userId']}&dto(taskId)=${task.taskId}${id}&operation=delete"/>

                <c:if test="${param.module == 'scheduler'}">
                    <c:choose>
                        <c:when test="${task.ownerId == sessionScope.user.valueMap['userId']}">
                            <c:set var="statusValue" value="${task.status}"/>
                        </c:when>
                        <c:otherwise>
                            <fanta:label var="statusValue" listName="participantStatusList" module="/scheduler"
                                         patron="0"
                                         label="status" columnOrder="status">
                                <fanta:parameter field="taskId" value="${not empty task.taskId?task.taskId:0}"/>
                                <fanta:parameter field="userId"
                                                 value="${sessionScope.user.valueMap['userId']}"/>
                            </fanta:label>
                        </c:otherwise>
                    </c:choose>
                </c:if>
                <c:if test="${param.module != 'scheduler'}">
                    <c:set var="statusValue" value="${task.status}"/>
                </c:if>

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
                                <app:link action="${deleteAction}" titleKey="Common.delete">
                                    <span class="${app2:getClassGlyphTrash()}"></span>
                                </app:link>
                            </c:when>
                            <c:otherwise>
                                &nbsp;
                            </c:otherwise>
                        </c:choose>
                    </fanta:actionColumn>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="title" action="${editAction}" styleClass="listItem" title="Task.taskName"
                              headerStyle="listHeader" width="20%" orderable="true" maxLength="20"/>

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

            <c:if test="${param.module != 'contacts'}">
                <fanta:dataColumn name="contactName" maxLength="22" styleClass="listItem"
                                  title="Appointment.contact"
                                  headerStyle="listHeader" width="15%" orderable="true"/>
            </c:if>

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

    <c:if test="${param.module != 'scheduler'}">
        <app2:checkAccessRight functionality="TASK" permission="CREATE">
            <html:form action="/Task/Forward/Create.do?${names}">
                <div class="${app2:getFormButtonWrapperClasses()}">
                    <html:submit styleClass="${app2:getFormButtonClasses()}">
                        <fmt:message key="Common.new"/>
                    </html:submit>
                </div>
            </html:form>
        </app2:checkAccessRight>
    </c:if>
</div>