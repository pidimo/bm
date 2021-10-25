<%@ page import="com.piramide.elwis.utils.ProjectConstants" %>
<%@ include file="/Includes.jsp" %>

<calendar:initialize/>
<tags:initSelectPopup/>
<tags:jscript language="JavaScript" src="/js/common.jsp"/>

<script language="javascript" type="text/javascript">
    function resetProjectItems(){
        document.getElementById("subProjectsField").selectedIndex=0;
        document.getElementById("assigneeField").selectedIndex=0;
        document.getElementById("projectActivityField").selectedIndex=0;
    }
</script>
<fmt:message key="Common.yes" var="invoiceableRes"/>
<fmt:message key="Common.no" var="noInvoiceableRes"/>

<fmt:message key="ProjectTime.status.entered" var="enteredRes"/>
<fmt:message key="ProjectTime.status.released" var="releasedRes"/>
<fmt:message key="ProjectTime.status.confirmed" var="confirmedRes"/>
<fmt:message key="ProjectTime.status.notConfirmed" var="notConfirmedRes"/>
<fmt:message key="ProjectTime.status.invoiced" var="invoicedRes"/>

<c:set var="enteredStatus" value="<%=ProjectConstants.ProjectTimeStatus.ENTERED.getAsString()%>"/>
<c:set var="releasedStatus" value="<%=ProjectConstants.ProjectTimeStatus.RELEASED.getAsString()%>"/>
<c:set var="confirmedStatus" value="<%=ProjectConstants.ProjectTimeStatus.CONFIRMED.getAsString()%>"/>
<c:set var="notConfirmedStatus" value="<%=ProjectConstants.ProjectTimeStatus.NOT_CONFIRMED.getAsString()%>"/>
<c:set var="invoicedStatus" value="<%=ProjectConstants.ProjectTimeStatus.INVOICED.getAsString()%>"/>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<fmt:message var="datePattern" key="datePattern"/>
<c:set var="projectTimeStatuses" value="${app2:getProjectTimeStatuses(pageContext.request)}"/>
<c:set var="isProjectSelected" value="${not empty projectTimesAdvancedSearchForm.params.projectId}"/>

<c:set var="projectTimeStatuses" value="${app2:getProjectTimeStatuses(pageContext.request)}"/>

<br>
<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0" class="container">
<html:form action="${action}" focus="parameter(projectId)" styleId="projectTimesAdvancedSearchForm">
    <tr>
        <td height="20" class="title" colspan="4">
            <fmt:message key="${pagetitle}"/>
        </td>
    </tr>
    <tr>
        <td>
            <table width="100%" border="0" align="center" cellpadding="3" cellspacing="0">
                <TR>
                    <TD class="label" width="15%"><fmt:message key="Project.report.projectName"/></TD>
                    <TD class="contain" width="35%">
                        <html:hidden property="parameter(projectId)" styleId="projectId_id"/>
                        <app:text property="parameter(projectName_FIELD)" styleClass="mediumText" readonly="true"
                                  styleId="projectName_id" tabindex="1"/>
                        <tags:selectPopup url="/projects/Project/ProjectListPopUp.do" name="searchProject"
                                          titleKey="Common.search" tabindex="2" submitOnSelect="true"/>
                        <tags:clearSelectPopup keyFieldId="projectId_id" nameFieldId="projectName_id"
                                               titleKey="Common.clear" tabindex="3" submitOnClear="true"
                                onclick="resetProjectItems()"/>
                    </TD>
                        <%-------------------------Column divider-------------------------------%>
                    <TD class="label" width="15%">
                        <fmt:message key="Project.report.subProject"/>
                    </TD>
                    <TD class="contain" width="35%">
                        <c:choose>
                            <c:when test="${isProjectSelected}">
                                <fanta:select property="parameter(subProjectId)" listName="subProjectListForSelect"
                                              labelProperty="subProjectName" valueProperty="subProjectId"
                                              styleClass="mediumSelect" styleId="subProjectsField"
                                              module="/projects" firstEmpty="true" tabIndex="7">
                                    <fanta:parameter field="companyId"
                                                     value="${sessionScope.user.valueMap['companyId']}"/>
                                    <fanta:parameter field="projectId"
                                                     value="${projectTimesAdvancedSearchForm.params.projectId}"/>
                                </fanta:select>
                            </c:when>
                            <c:otherwise>
                                <app:text property="parameter(subProjectName)" tabindex="7" styleClass="mediumText"/>
                            </c:otherwise>
                        </c:choose>

                    </TD>

                </tr>
                <TR>
                    <TD class="label" width="15%">
                        <fmt:message key="ProjectTime.assignee"/>
                    </TD>
                    <TD class="contain" width="35%">
                        <c:choose>
                            <c:when test="${isProjectSelected}">
                                <fanta:select property="parameter(assigneeAddressId)"
                                              listName="projectUserForSelectList"
                                              labelProperty="userName" valueProperty="addressId"
                                              styleClass="mediumSelect" styleId="assigneeField"
                                              module="/projects" firstEmpty="true" tabIndex="4"
                                        >
                                    <fanta:parameter field="companyId"
                                                     value="${sessionScope.user.valueMap['companyId']}"/>
                                    <fanta:parameter field="projectId"
                                                     value="${projectTimesAdvancedSearchForm.params.projectId}"/>
                                </fanta:select>
                            </c:when>
                            <c:otherwise>
                                <app:text property="parameter(assigneeName1@_assigneeName2@_assigneeSearchName)"
                                          tabindex="4"
                                          styleClass="mediumText"/>
                            </c:otherwise>
                        </c:choose>

                    </TD>
                        <%-------------------------Column divider-------------------------------%>
                    <TD class="label">
                        <fmt:message key="ProjectTime.status"/>
                    </TD>
                    <TD class="contain">
                        <html:select property="parameter(status)"
                                     styleClass="mediumSelect"
                                     tabindex="8">
                            <html:option value=""/>
                            <html:options collection="projectTimeStatuses"
                                          property="value"
                                          labelProperty="label"/>
                        </html:select>
                    </TD>
                </TR>
                <TR>
                    <TD class="label" width="15%">
                        <fmt:message key="Project.report.activity"/>
                    </TD>
                    <TD class="contain" width="35%">
                        <c:choose>
                            <c:when test="${isProjectSelected}">
                                <fanta:select property="parameter(projectActivityId)"
                                              listName="projectActivityForSelectList"
                                              labelProperty="activityName" valueProperty="projectActivityId"
                                              styleClass="mediumSelect" styleId="projectActivityField"
                                              module="/projects" firstEmpty="true" tabIndex="5">
                                    <fanta:parameter field="companyId"
                                                     value="${sessionScope.user.valueMap['companyId']}"/>
                                    <fanta:parameter field="projectId"
                                                     value="${projectTimesAdvancedSearchForm.params.projectId}"/>
                                </fanta:select>
                            </c:when>
                            <c:otherwise>
                                <app:text property="parameter(activityName)" tabindex="5" styleClass="mediumText"/>
                            </c:otherwise>
                        </c:choose>

                    </TD>
                        <%-------------------------Column divider-------------------------------%>
                    <td class="label">
                        <fmt:message key="Project.report.timePeriod"/>
                    </td>
                    <td class="contain">
                        <fmt:message key="datePattern" var="datePattern"/>
                        <fmt:message key="Common.from"/>
                        &nbsp;
                        <app:dateText property="parameter(startProjectTime)" maxlength="10" tabindex="9"
                                      styleId="startProjectTime"
                                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                                      convert="true"/>
                        &nbsp;
                        <fmt:message key="Common.to"/>
                        &nbsp;
                        <app:dateText property="parameter(endProjectTime)" maxlength="10" tabindex="10"
                                      styleId="endProjectTime"
                                      calendarPicker="true" datePatternKey="${datePattern}" styleClass="dateText"
                                      convert="true"/>
                    </td>
                </tr>
                <TR>
                    <TD class="label" width="15%">
                        <fmt:message key="ProjectTime.toBeInvoiced"/>
                    </TD>
                    <TD class="contain" width="35%">
                        <html:select property="parameter(toBeInvoiced)" styleClass="mediumSelect" tabindex="6">
                            <html:option value=""></html:option>
                            <html:option value="1"><fmt:message key="Common.yes"/> </html:option>
                            <html:option value="0"><fmt:message key="Common.no"/> </html:option>
                        </html:select>
                    </TD>
                    <%---------------------Column divider---------------------------------%>
                    <TD class="label" width="15%"><fmt:message key="ProjectTime.time"/></TD>
                    <TD class="contain" width="35%">
                        <fmt:message key="Common.from"/>
                        &nbsp;
                        <app:numberText property="parameter(timeFrom)" styleClass="numberText" tabindex="11"
                                        maxlength="6"
                                        numberType="decimal" maxInt="4" maxFloat="1"/>
                        &nbsp;
                        <fmt:message key="Common.to"/>
                        &nbsp;
                        <app:numberText property="parameter(timeTo)" styleClass="numberText" tabindex="12"
                                        maxlength="6" numberType="decimal" maxInt="4" maxFloat="1"/>
                    </TD>
                </tr>
                <tr>
                    <td colspan="4" align="center" class="alpha">
                        <fanta:alphabet action="${action}?parameter(submit_button)=submit" parameterName="projectNameAlpha"/>
                    </td>
                </tr>
            </table>
            <br/>
        </td>
    </tr>

    <tr>
        <td colspan="4" class="button">
            <html:submit styleClass="button" tabindex="31" property="parameter(submit_button)"><fmt:message
                    key="Common.go"/></html:submit>
            <html:button property="reset1" tabindex="32" styleClass="button"
                         onclick="resetForm(document.projectTimesAdvancedSearchForm, ${isProjectSelected} , ${isProjectSelected})">
                <fmt:message key="Common.clear"/>
            </html:button>
        </td>
    </tr>

</html:form>
<tr>
    <td align="center" colspan="4">
        <fanta:table list="projectTimeAdvancedSearchList"
                     action="${action}"
                     width="100%"
                     id="projectTime"
                     imgPath="${baselayout}">

            <c:set var="editLink"
                   value="ProjectTime/Forward/Update.do?dto(timeId)=${projectTime.timeId}&dto(userName)=${app2:encode(projectTime.userName)}&dto(status)=${projectTime.status}&dto(projectId)=${projectTime.projectId}&projectId=${projectTime.projectId}&dto(userId)=${projectTime.userId}&dto(assigneeId)=${projectTime.assigneeAddressId}&viewButton=true&index=1"/>

            <c:set var="deleteLink"
                   value="ProjectTime/Forward/Delete.do?dto(timeId)=${projectTime.timeId}&dto(userName)=${app2:encode(projectTime.userName)}&dto(status)=${projectTime.status}&dto(projectId)=${projectTime.projectId}&projectId=${projectTime.projectId}&dto(userId)=${projectTime.userId}&dto(assigneeId)=${projectTime.assigneeAddressId}&viewButton=true&dto(withReferences)=true&index=1"/>

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
            <fanta:dataColumn name="projectName"
                              action="${editLink}"
                              styleClass="listItem"
                              title="Project.report.projectName"
                              headerStyle="listHeader"
                              orderable="true"
                              maxLength="25"
                              width="15%"/>
            <fanta:dataColumn name="userName"
                              action="${editLink}"
                              styleClass="listItem"
                              title="ProjectTime.userName"
                              headerStyle="listHeader"
                              orderable="true"
                              maxLength="25"
                              width="15%"/>
            <fanta:dataColumn name="assigneeName"
                              styleClass="listItem"
                              title="ProjectTime.assignee"
                              headerStyle="listHeader"
                              orderable="true"
                              maxLength="25"
                              width="15%"/>
            <fanta:dataColumn name="projectDate"
                              styleClass="listItem"
                              title="ProjectTime.date"
                              headerStyle="listHeader"
                              orderable="true"
                              maxLength="25"
                              renderData="false"
                              width="8%">
                <fmt:formatDate var="dateValue" value="${app2:intToDate(projectTime.projectDate)}"
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
                              width="10%"/>
            <fanta:dataColumn name="subProjectName"
                              styleClass="listItem"
                              title="ProjectTime.subProjectName"
                              headerStyle="listHeader"
                              orderable="true"
                              maxLength="25"
                              width="10%"/>
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
</table>