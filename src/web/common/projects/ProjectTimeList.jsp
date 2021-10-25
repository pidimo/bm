<%@ page import="com.piramide.elwis.utils.ProjectConstants" %>
<%@ include file="/Includes.jsp" %>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>
<c:set var="projectTimeStatuses" value="${app2:getProjectTimeStatuses(pageContext.request)}"/>

<c:set var="enteredStatus" value="<%=ProjectConstants.ProjectTimeStatus.ENTERED.getAsString()%>"/>
<c:set var="releasedStatus" value="<%=ProjectConstants.ProjectTimeStatus.RELEASED.getAsString()%>"/>
<c:set var="confirmedStatus" value="<%=ProjectConstants.ProjectTimeStatus.CONFIRMED.getAsString()%>"/>
<c:set var="notConfirmedStatus" value="<%=ProjectConstants.ProjectTimeStatus.NOT_CONFIRMED.getAsString()%>"/>
<c:set var="invoicedStatus" value="<%=ProjectConstants.ProjectTimeStatus.INVOICED.getAsString()%>"/>


<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="container">
<%--<html:form action="/ProjectUser/List.do" focus="parameter(lastName@_firstName@_searchName)">
    <TR>
        <td class="label" width="15%"><fmt:message key="Common.search"/></td>
        <td align="left" class="contain" width="85%">
            <html:text property="parameter(lastName@_firstName@_searchName)" styleClass="largeText" maxlength="40"/>
            &nbsp;
            <html:submit styleClass="button"><fmt:message key="Common.go"/></html:submit>
        </td>
    </TR>
</html:form>--%>
<%--<tr>
    <td colspan="2" align="center" class="alpha">
        <fanta:alphabet action="ProjectUser/List.do" parameterName="lastName"/>
    </td>
</tr>--%>
<tr>
    <app2:checkAccessRight functionality="PROJECTTIME" permission="CREATE">
        <c:set var="projectId" value="${param.projectId}" scope="request"/>
        <c:set var="operation" value="forwardToCreate" scope="request"/>
        <c:import url="/common/projects/ProjectTimeButtonFragment.jsp"/>
    </app2:checkAccessRight>
</tr>
<tr>
<td align="center">
<fanta:table list="projectTimeList"
             action="ProjectTime/List.do"
             width="100%"
             id="projectTime"
             imgPath="${baselayout}">

<c:set var="editLink"
       value="ProjectTime/Forward/Update.do?dto(timeId)=${projectTime.timeId}&dto(userName)=${app2:encode(projectTime.userName)}&dto(status)=${projectTime.status}&dto(projectId)=${projectTime.projectId}&dto(userId)=${projectTime.userId}&dto(assigneeId)=${projectTime.assigneeId}&viewButton=true"/>

<c:set var="deleteLink"
       value="ProjectTime/Forward/Delete.do?dto(timeId)=${projectTime.timeId}&dto(userName)=${app2:encode(projectTime.userName)}&dto(status)=${projectTime.status}&dto(projectId)=${projectTime.projectId}&dto(userId)=${projectTime.userId}&dto(assigneeId)=${projectTime.assigneeId}&viewButton=true&dto(withReferences)=true"/>

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
                            image="${baselayout}/img/edit.gif"
                            width="50%"/>
    </app2:checkAccessRight>
    <app2:checkAccessRight functionality="PROJECTTIME" permission="DELETE">
        <fanta:actionColumn name="delete"
                            title="Common.delete"
                            action="${deleteLink}"
                            styleClass="listItem"
                            headerStyle="listHeader"
                            image="${baselayout}/img/delete.gif"
                            width="50%"/>
    </app2:checkAccessRight>
</fanta:columnGroup>
<fanta:dataColumn name="userName"
                  action="${editLink}"
                  styleClass="listItem"
                  title="ProjectTime.userName"
                  headerStyle="listHeader"
                  orderable="true"
                  maxLength="25"
                  width="18%"/>
<fanta:dataColumn name="assigneeName"
                  styleClass="listItem"
                  title="ProjectTime.assignee"
                  headerStyle="listHeader"
                  orderable="true"
                  maxLength="25"
                  width="18%"/>
<fanta:dataColumn name="date"
                  styleClass="listItem"
                  title="ProjectTime.date"
                  headerStyle="listHeader"
                  orderable="true"
                  maxLength="25"
                  renderData="false"
                  width="10%">
    <fmt:formatDate var="dateValue" value="${app2:intToDate(projectTime.date)}"
                    pattern="${datePattern}"/>
    ${dateValue}&nbsp;
</fanta:dataColumn>
<fanta:dataColumn name="time"
                  styleClass="listItemRight"
                  title="ProjectTime.time"
                  headerStyle="listHeader"
                  orderable="true"
                  maxLength="25"
                  renderData="false"
                  width="7%">
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
                  width="11%"/>
<fanta:dataColumn name="subProjectName"
                  styleClass="listItem"
                  title="ProjectTime.subProjectName"
                  headerStyle="listHeader"
                  orderable="true"
                  maxLength="25"
                  width="11%"/>
<fanta:dataColumn name="toBeInvoiced"
                  styleClass="listItemCenter"
                  title="ProjectTime.invoceable"
                  headerStyle="listHeader"
                  orderable="true"
                  maxLength="25"
                  renderData="false"
                  width="10%">
    <c:if test="${projectTime.toBeInvoiced == '1'}">
        <img align="center" alt="" src="<c:out value="${baselayout}"/>/img/check.gif" border="0"/>
    </c:if>
</fanta:dataColumn>
<fanta:dataColumn name="status"
                  styleClass="listItem2 ${statusClass}"
                  title="ProjectTime.status"
                  headerStyle="listHeader"
                  orderable="true"
                  maxLength="25"
                  renderData="false"
                  width="10%">
    <c:set var="statusLabel" value="${app2:searchLabel(projectTimeStatuses, projectTime.status)}"/>
    <fanta:textShorter title="${statusLabel}">
        ${statusLabel}
    </fanta:textShorter>
</fanta:dataColumn>
</fanta:table>
</td>
</tr>
<tr>
    <app2:checkAccessRight functionality="PROJECTTIME" permission="CREATE">
        <c:set var="projectId" value="${param.projectId}" scope="request"/>
        <c:set var="operation" value="forwardToCreate" scope="request"/>
        <c:import url="/common/projects/ProjectTimeButtonFragment.jsp"/>
    </app2:checkAccessRight>
</tr>

</table>