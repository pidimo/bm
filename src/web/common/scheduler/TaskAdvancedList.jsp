<%@ page import="org.joda.time.DateTime,
                 org.joda.time.DateTimeZone" %>
<%@ include file="/Includes.jsp" %>
<calendar:initialize/>
<tags:initSelectPopup/>
<%
    pageContext.setAttribute("statusList", JSPHelper.getPriorityStatusList(request));
    pageContext.setAttribute("addressTypeList", JSPHelper.getAddressTypeList(request));
%>
<br>
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

<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">
<tr>
    <td height="20" class="title" colspan="4">
        <fmt:message key="Scheduler.task.advancedSearch"/>
    </td>
</tr>
<html:form action="/TaskAdvancedList.do" focus="parameter(title)">
<tr>
    <TD width="13%" class="label">
        <fmt:message key="Task.taskName"/>
    </td>
    <TD class="contain" width="30%">
        <html:text property="parameter(title)" styleClass="mediumText" tabindex="1"/>
    </TD>
    <td class="label" valign="middle" width="15%">
        <fmt:message key="Task.priority"/>
    </td>
    <td class="contain" valign="middle" width="42%">
        <html:hidden property="parameter(userId)" value="${sessionScope.user.valueMap['userId']}"/>
        <fanta:select property="parameter(priorityId)" listName="priorityList" labelProperty="name"
                      valueProperty="id" styleClass="mediumSelect" module="/catalogs" firstEmpty="true"
                      tabIndex="7">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
            <fanta:parameter field="type" value="SCHEDULER"/>
        </fanta:select>
    </td>

</tr>
<tr>
    <td class="label">
        <fmt:message key="Task.status"/>
    </TD>
    <TD class="contain">
        <html:select property="parameter(userStatus)" styleClass="mediumSelect" tabindex="2">
            <html:option value=""/>
            <html:options collection="statusList" property="value" labelProperty="label"/>
        </html:select>
    </TD>
    <TD class="label"><fmt:message key="Appointment.contact"/></TD>
    <td class="contain">
        <app:text property="parameter(addressName1@_addressName2@_addressName3@_searchName)" styleClass="mediumText"
                  maxlength="40" tabindex="8"/>
    </td>
</tr>
<tr>
    <td class="label">
        <fmt:message key="Task.status.closedOpen"/>
    </td>
    <td class="contain">
        <html:select property="parameter(concluded_status)" styleClass="mediumSelect" tabindex="3">
            <html:option value=""/>
            <html:options collection="statusList" property="value" labelProperty="label"/>
        </html:select>
    </td>
    <TD class="topLabel"><fmt:message key="Task.taskType"/></TD>
    <TD class="contain">
        <fanta:select property="parameter(taskTypeId)" listName="taskTypeList"
                      labelProperty="taskTypeName" valueProperty="taskTypeId" styleClass="mediumSelect"
                      module="/catalogs" firstEmpty="true" tabIndex="9">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
</tr>
<tr>
    <td class="label" valign="middle">
        <fmt:message key="Common.assignedFrom"/>
    </td>
    <td class="contain" valign="middle">
        <fanta:select property="parameter(createdUserId)" listName="userList" labelProperty="userName"
                      valueProperty="userId" styleClass="mediumSelect" module="/scheduler" firstEmpty="true"
                      tabIndex="4">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
    <TD class="label">
        <fmt:message key="Task.startDate"/>
    </TD>
    <TD class="contain">
        <fmt:message key="datePattern" var="datePattern"/>
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:dateText property="parameter(startDateFrom)" maxlength="10" tabindex="10" styleId="startRange"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                      parseLongAsDate="true"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:dateText property="parameter(startDateTo)" maxlength="10" tabindex="11" styleId="endRange"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                      parseLongAsDate="true"/>
    </TD>

</tr>
<tr>
    <td class="label" valign="middle"><fmt:message key="Common.assignedTo"/></td>
    <td class="contain" valign="middle">
        <fanta:select property="parameter(scheduledUserId)" listName="userList" labelProperty="userName"
                      valueProperty="userId" styleClass="mediumSelect" module="/scheduler" firstEmpty="true"
                      tabIndex="5">
            <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        </fanta:select>
    </td>
    <TD class="label">
        <fmt:message key="Task.expireDate"/>
    </TD>
    <TD class="contain">
        <fmt:message key="Common.from"/>
        &nbsp;
        <app:dateText property="parameter(expireDateFrom)" maxlength="10" tabindex="12" styleId="startRange2"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                      parseLongAsDate="true"/>
        &nbsp;
        <fmt:message key="Common.to"/>
        &nbsp;
        <app:dateText property="parameter(expireDateTo)" maxlength="10" tabindex="13" styleId="endRange2"
                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                      parseLongAsDate="true"/>
    </TD>
</tr>

<tr>
    <td class="label" valign="middle"><fmt:message key="Campaign"/></td>
    <td class="contain" valign="middle" colspan="3">
        <html:text property="parameter(campaignName)" styleClass="mediumText" tabindex="6"/>
    </td>
</tr>
<tr>
    <td colspan="4" align="center" class="alpha">
        <fanta:alphabet action="TaskAdvancedList.do" parameterName="titleAlphabet"/>
    </td>
</tr>
<tr>
    <td class="button" colspan="4">
        <html:submit styleClass="button" tabindex="14">
            <fmt:message key="Common.go"/>
        </html:submit>
        <html:button property="reset1" tabindex="15" styleClass="button" onclick="myReset()">
            <fmt:message key="Common.clear"/>
        </html:button>
        &nbsp;
    </td>
</tr>
</html:form>

<tr>
<td colspan="4" align="center">
<br>
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
            <tags:buttonsTable>
                <app:url value="/Task/Forward/Create.do?advancedListForward=TaskAdvancedSearch"
                         addModuleParams="false" var="newTaskUrl"/>
                <input type="button" class="button" value="<fmt:message key="Common.new"/>"
                       onclick="window.location ='${newTaskUrl}'">
            </tags:buttonsTable>
        </c:set>
    </c:if>
</app2:checkAccessRight>

<c:out value="${newButtonsTable}" escapeXml="false"/>
<fanta:table list="allTaskAdvancedSearchList" align="center" id="task" action="TaskAdvancedList.do"
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
            <fanta:label var="statusValue" listName="participantStatusList" module="/scheduler" patron="0"
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
        <fanta:actionColumn name="update" label="Common.update" action="${editAction}" title="Common.update"
                            styleClass="listItem" headerStyle="listHeader" width="50%"
                            image="${baselayout}/img/edit.gif"/>
    </app2:checkAccessRight>

    <app2:checkAccessRight functionality="TASK" permission="DELETE">
        <fanta:actionColumn name="" styleClass="listItem" headerStyle="listHeader" width="50%">
            <c:choose>
                <c:when test="${task.ownerId == sessionScope.user.valueMap['userId']}">
                    <html:link action="${deleteAction}" titleKey="Common.delete">
                        <html:img src="${baselayout}/img/delete.gif" titleKey="Common.delete" border="0"/>
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
<fanta:dataColumn name="startDate" styleClass="listItem" title="Task.startDate" headerStyle="listHeader"
                  width="8%" orderable="true" renderData="false">
    ${app2:getDateWithTimeZone(task.startDate, timeZone, datePattern)}
</fanta:dataColumn>
<fanta:dataColumn name="expireDate" styleClass="listItem${expireClass}" title="Task.expireDate"
                  headerStyle="listHeader" width="8%" orderable="true" renderData="false">
    ${app2:getDateWithTimeZone(task.expireDate, timeZone, datePattern)}
</fanta:dataColumn>

<fanta:dataColumn name="contactName" maxLength="22" styleClass="listItem" title="Appointment.contact"
                  headerStyle="listHeader" width="15%" orderable="true"/>

<fanta:dataColumn name="ownerName" maxLength="22" styleClass="listItem" title="Task.createdBy"
                  headerStyle="listHeader" width="20%" orderable="true"/>

<fanta:dataColumn renderData="false" name="" styleClass="listItem2Center" title="Scheduler.Task.users"
                  headerStyle="listHeader" width="20%" orderable="false">
    <fanta:select property="" listName="participantTaskList" labelProperty="userName" valueProperty="taskId"
                  module="/scheduler" styleClass="select">
        <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        <fanta:parameter field="taskId" value="${not empty task.taskId?task.taskId:0}"/>
    </fanta:select>
</fanta:dataColumn>
</fanta:table>
<c:out value="${newButtonsTable}" escapeXml="false"/>
</td>
</tr>

</table>