<%@ page import="org.joda.time.DateTime,
                 org.joda.time.DateTimeZone" %>
<%@ include file="/Includes.jsp" %>

<fmt:message var="datePattern" key="datePattern"/>
<c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
<%
    DateTime dateTime = new DateTime((DateTimeZone) pageContext.getAttribute("timeZone")).withTime(23, 59, 59, 0);
    pageContext.setAttribute("todayMillis", new Long(dateTime.getMillis()));
%>

<html:form action="/SearchTask.do" focus="parameter(title)" styleClass="form-horizontal">
    <div class="${app2:getSearchWrapperClasses()}">
        <label class="${app2:getFormLabelOneSearchInput()} label-left">
            <fmt:message key="Common.search"/>
        </label>

        <div class="${app2:getFormOneSearchInput()}">
            <div class="input-group">
                <html:text property="parameter(title)" styleClass="${app2:getFormInputClasses()} largeText"/>
                <div class="input-group-btn">
                    <html:submit styleClass="${app2:getFormButtonClasses()}">
                        <fmt:message key="Common.go"/>
                    </html:submit>
                </div>
            </div>
        </div>
    </div>
</html:form>

<div class="${app2:getAlphabetWrapperClasses()}">
    <fanta:alphabet action="SearchTask.do" parameterName="titleAlphabet" mode="bootstrap"/>
</div>

<div class="table-responsive">
    <fanta:table mode="bootstrap" list="taskSimpleList" styleClass="${app2:getFantabulousTableClases()}" width="100%" id="task"
                 action="SearchTask.do" imgPath="${baselayout}">
        <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
            <fanta:actionColumn name="" title="Common.select"
                                useJScript="true"
                                action="javascript:opener.selectField('taskId_id', '${task.taskId}', 'taskName_id', '${app2:jscriptEncode(task.title)}');"
                                styleClass="listItem" headerStyle="listHeader" width="50%"
                                image="${baselayout}/img/import.gif"/>
        </fanta:columnGroup>
        <fanta:dataColumn name="title" styleClass="listItem" title="Task.taskName"
                          headerStyle="listHeader" width="20%" orderable="true" maxLength="27"/>

        <fanta:dataColumn name="sequence" renderData="false" styleClass="listItem" title="Task.priority"
                          headerStyle="listHeader" width="10%" orderable="true">
            ${task.priorityName}
        </fanta:dataColumn>
        <fanta:dataColumn name="startDate" styleClass="listItem" title="Task.startDate" headerStyle="listHeader"
                          width="10%" orderable="true" renderData="false">
            ${app2:getDateWithTimeZone(task.startDate, timeZone, datePattern)}
        </fanta:dataColumn>
        <fanta:dataColumn name="expireDate" styleClass="listItem${expireClass}" title="Task.expireDate"
                          headerStyle="listHeader" width="10%" orderable="true" renderData="false">
            ${app2:getDateWithTimeZone(task.expireDate, timeZone, datePattern)}
        </fanta:dataColumn>
        <fanta:dataColumn name="ownerName" maxLength="22" styleClass="listItem" title="Task.createdBy"
                          headerStyle="listHeader" width="20%" orderable="true"/>
        <fanta:dataColumn renderData="false" name="" styleClass="listItem2Center" title="Scheduler.Task.users"
                          headerStyle="listHeader" width="15%" orderable="false">
            <fanta:select property="" listName="participantTaskList" labelProperty="userName"
                          valueProperty="taskId"
                          module="/scheduler" styleClass="${app2:getFormSelectClasses()}">
                <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
                <fanta:parameter field="taskId" value="${not empty task.taskId?task.taskId:0}"/>
            </fanta:select>
        </fanta:dataColumn>
    </fanta:table>
</div>






