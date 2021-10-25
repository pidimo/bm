<%@ page import="com.piramide.elwis.utils.ProjectConstants" %>
<%@ include file="/Includes.jsp" %>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>
<fmt:message var="timePattern" key="timePattern"/>
<c:set var="projectTimeStatuses" value="${app2:getProjectTimeStatuses(pageContext.request)}"/>

<c:set var="enteredStatus" value="<%=ProjectConstants.ProjectTimeStatus.ENTERED.getAsString()%>"/>
<c:set var="releasedStatus" value="<%=ProjectConstants.ProjectTimeStatus.RELEASED.getAsString()%>"/>
<c:set var="confirmedStatus" value="<%=ProjectConstants.ProjectTimeStatus.CONFIRMED.getAsString()%>"/>
<c:set var="notConfirmedStatus" value="<%=ProjectConstants.ProjectTimeStatus.NOT_CONFIRMED.getAsString()%>"/>
<c:set var="invoicedStatus" value="<%=ProjectConstants.ProjectTimeStatus.INVOICED.getAsString()%>"/>

<div class="${app2:getListWrapperClasses()}">
    <form>
        <fieldset>
            <legend class="title">
                <fmt:message key="SubProject.timesRegistered"/>
            </legend>
        </fieldset>
    </form>
    <div class="table-responsive">
        <fanta:table mode="bootstrap" styleClass="${app2:getFantabulousTableClases()}"
                     list="subProjectTimeList"
                     action="SubProject/ProjectTime/List.do"
                     width="100%"
                     id="projectTime"
                     imgPath="${baselayout}">

            <app:url var="editLink"
                     value="ProjectTime/Forward/Update.do?dto(timeId)=${projectTime.timeId}&dto(userName)=${app2:encode(projectTime.userName)}&dto(status)=${projectTime.status}&dto(projectId)=${projectTime.projectId}&dto(userId)=${projectTime.userId}&dto(assigneeId)=${projectTime.assigneeId}&viewButton=true&tabKey=Project.Tab.times"/>

            <app:url var="deleteLink"
                     value="ProjectTime/Forward/Delete.do?dto(timeId)=${projectTime.timeId}&dto(userName)=${app2:encode(projectTime.userName)}&dto(status)=${projectTime.status}&dto(projectId)=${projectTime.projectId}&dto(userId)=${projectTime.userId}&dto(assigneeId)=${projectTime.assigneeId}&viewButton=true&dto(withReferences)=true&tabKey=Project.Tab.times"/>

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
                                        useJScript="true"
                                        action="javascript:goParentURL('${editLink}')"
                                        styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphEdit()}"
                                        width="50%"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="PROJECTTIME" permission="DELETE">
                    <fanta:actionColumn name="delete"
                                        title="Common.delete"
                                        useJScript="true"
                                        action="javascript:goParentURL('${deleteLink}')"
                                        styleClass="listItem"
                                        headerStyle="listHeader"
                                        glyphiconClass="${app2:getClassGlyphTrash()}"
                                        width="50%"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="userName"
                              useJScript="true"
                              action="javascript:goParentURL('${editLink}')"
                              styleClass="listItem"
                              title="ProjectTime.userName"
                              headerStyle="listHeader"
                              orderable="true"
                              maxLength="30"
                              width="18%"/>
            <fanta:dataColumn name="assigneeName"
                              styleClass="listItem"
                              title="ProjectTime.assignee"
                              headerStyle="listHeader"
                              orderable="true"
                              maxLength="31"
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
            <fanta:dataColumn name="toBeInvoiced"
                              styleClass="listItemCenter"
                              title="ProjectTime.invoceable"
                              headerStyle="listHeader"
                              orderable="true"
                              maxLength="25"
                              renderData="false"
                              width="10%">
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
                              width="10%">
                <c:set var="statusLabel" value="${app2:searchLabel(projectTimeStatuses, projectTime.status)}"/>
                <fanta:textShorter title="${statusLabel}">
                    ${statusLabel}
                </fanta:textShorter>
            </fanta:dataColumn>
        </fanta:table>
    </div>
</div>