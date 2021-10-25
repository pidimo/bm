<%@ page import="org.joda.time.DateTime,
                 org.joda.time.DateTimeZone" %>
<%@ include file="/Includes.jsp" %>
<br>

<fmt:message var="datePattern" key="datePattern"/>
<c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
<%
    DateTime dateTime = new DateTime((DateTimeZone) pageContext.getAttribute("timeZone")).withTime(23, 59, 59, 0);
    pageContext.setAttribute("todayMillis", new Long(dateTime.getMillis()));
%>

<script>
    function select(id, name) {
        opener.selectField('fieldTaskId_id', id, 'fieldTaskName_id', name);
    }
</script>
<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">

    <tr>
        <td class="title" height="20" colspan="2">
            <fmt:message key="Scheduler.taskSearch"/>
        </td>
    </tr>

<tr>
    <td class="label">
        <fmt:message key="Common.search"/>
    </td>
    <html:form action="/TaskListPopUp.do" focus="parameter(title)">
        <td class="contain" nowrap>
            <html:text property="parameter(title)" styleClass="largeText"/>
            &nbsp;
            <html:submit styleClass="button">
                <fmt:message key="Common.go"/>
            </html:submit>
        </td>
    </html:form>
</tr>

<tr>
    <td colspan="2" align="center" class="alpha">
        <fanta:alphabet action="TaskListPopUp.do" parameterName="titleAlphabet"/>
    </td>
</tr>
</table>
<br/>
<table width="100%" border="0" align="center" cellpadding="3" cellspacing="0" class="container">
<tr>
<td align="center" width="100%">

<fanta:table align="center" id="task" action="TaskListPopUp.do" imgPath="${baselayout}">
<fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
    <fanta:actionColumn name="" title="Common.select"
                        useJScript="true"
                        action="javascript:select('${app2:mapElement(task, 'taskId')}', '${app2:jscriptEncode(app2:mapElement(task, 'title'))}');"
                        styleClass="listItem" headerStyle="listHeader" width="100%"
                        image="${baselayout}/img/import.gif"/>
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
</fanta:columnGroup>
<fanta:dataColumn name="title" styleClass="listItem" title="Task.taskName"
                  headerStyle="listHeader" width="25%" orderable="true" maxLength="20"/>

<fanta:dataColumn name="status" styleClass="listItem" title="Task.status" headerStyle="listHeader"
                  width="12%" orderable="false" renderData="false" maxLength="15">
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
                  headerStyle="listHeader" width="13%" orderable="true">
    ${task.priorityName}
</fanta:dataColumn>
<fanta:dataColumn name="startDate" styleClass="listItem" title="Task.startDate" headerStyle="listHeader"
                  width="15%" orderable="true" renderData="false">
    ${app2:getDateWithTimeZone(task.startDate, timeZone, datePattern)}
</fanta:dataColumn>
<fanta:dataColumn name="expireDate" styleClass="listItem${expireClass}" title="Task.expireDate"
                  headerStyle="listHeader" width="15%" orderable="true" renderData="false">
    ${app2:getDateWithTimeZone(task.expireDate, timeZone, datePattern)}
</fanta:dataColumn>

<fanta:dataColumn name="ownerName" maxLength="20" styleClass="listItem" title="Task.createdBy"
                  headerStyle="listHeader" width="15%" orderable="true"/>

</fanta:table>
</td>
</tr>
</table>