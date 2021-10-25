<%@ page import="com.piramide.elwis.utils.CampaignConstants" %>
<%@ include file="/Includes.jsp" %>

<c:set var="state_inProgres">
    <%=CampaignConstants.ActivityStatus.IN_PROGRESS.getConstant()%>
</c:set>
<c:set var="state_concluded">
    <%=CampaignConstants.ActivityStatus.CONCLUDED.getConstant()%>
</c:set>
<c:set var="state_planned">
    <%=CampaignConstants.ActivityStatus.PLANNED.getConstant()%>
</c:set>
<fmt:message var="datePattern" key="datePattern"/>

<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
<tr>
    <td>
        <table width="80%" border="0" cellpadding="3" cellspacing="0" align="center" class="container">
            <tr>
                <html:form action="/CampaignAvtivity/List.do" focus="parameter(title)">
                    <td class="label">
                        <fmt:message key="Common.search"/>
                    </td>
                    <td class="contain">
                        <html:text property="parameter(title)" styleClass="largeText"/>
                        &nbsp;
                        <html:submit styleClass="button">
                            <fmt:message key="Common.go"/>
                        </html:submit>&nbsp;
                    </td>
                </html:form>
            </tr>
            <tr>
                <td colspan="2" align="center" class="alpha">
                    <fanta:alphabet action="CampaignAvtivity/List.do" parameterName="title"/>
                </td>
            </tr>
        </table>
    </td>
</tr>
<tr>
    <td>
        <table width="80%" border="0" cellpadding="0" cellspacing="0" align="center">
            <tr>
                <td class="button">
                    <html:form action="/CampaignActivity/Forward/Create.do">
                        <app2:securitySubmit operation="create" functionality="CAMPAIGNACTIVITY" styleClass="button">
                            <fmt:message key="Common.new"/>
                        </app2:securitySubmit>
                    </html:form>
                </td>
            </tr>
            <tr>
                <td>
                    <fanta:table list="campaignActivityList" width="100%" id="activity"
                                 action="CampaignAvtivity/List.do"
                                 imgPath="${baselayout}">

                        <c:set var="editAction"
                               value="CampaignActivity/Forward/Update.do?dto(activityId)=${activity.activityId}&dto(title)=${app2:encode(activity.title)}"/>
                        <c:set var="deleteAction"
                               value="CampaignActivity/Forward/Delete.do?dto(withReferences)=true&dto(activityId)=${activity.activityId}&dto(title)=${app2:encode(activity.title)}"/>

                        <fanta:columnGroup title="Common.action" width="5%" headerStyle="listHeader">

                            <app2:checkAccessRight functionality="CAMPAIGNACTIVITY" permission="VIEW">
                                <fanta:actionColumn name="" title="Common.update" action="${editAction}"
                                                    styleClass="listItem" headerStyle="listHeader"
                                                    image="${baselayout}/img/edit.gif"/>
                            </app2:checkAccessRight>
                            <app2:checkAccessRight functionality="CAMPAIGNACTIVITY" permission="DELETE">
                                <fanta:actionColumn name="" title="Common.delete" action="${deleteAction}"
                                                    styleClass="listItem" headerStyle="listHeader"
                                                    image="${baselayout}/img/delete.gif"/>
                            </app2:checkAccessRight>
                        </fanta:columnGroup>

                        <fanta:dataColumn name="title" action="${editAction}" styleClass="listItem"
                                          title="CampaignActivity.title" headerStyle="listHeader" width="50%"
                                          orderable="true" maxLength="25">
                        </fanta:dataColumn>

                        <fanta:dataColumn name="startDate" styleClass="listItem"
                                          title="CampaignActivity.startDate" headerStyle="listHeader" width="15%"
                                          orderable="true" renderData="false" maxLength="25">
                            <fmt:formatDate var="dateValue1" value="${app2:intToDate(activity.startDate)}"
                                            pattern="${datePattern}"/>
                            ${dateValue1}&nbsp;
                        </fanta:dataColumn>

                        <fanta:dataColumn name="closeDate" styleClass="listItem"
                                          title="CampaignActivity.closeDate" headerStyle="listHeader" width="15%"
                                          orderable="true" renderData="false" maxLength="25">
                            <fmt:formatDate var="dateValue2" value="${app2:intToDate(activity.closeDate)}"
                                            pattern="${datePattern}"/>
                            ${dateValue2}&nbsp;
                        </fanta:dataColumn>

                        <fanta:dataColumn name="state" styleClass="listItem2"
                                          title="CampaignActivity.state" headerStyle="listHeader" width="20%"
                                          renderData="false"
                                          maxLength="25">
                            <c:choose>
                                <c:when test="${activity.state == state_inProgres}">
                                    <fmt:message key="Activity.status.InProgress"/>
                                </c:when>
                                <c:when test="${activity.state == state_concluded}">
                                    <fmt:message key="Activity.status.concluded"/>
                                </c:when>
                                <c:when test="${activity.state == state_planned}">
                                    <fmt:message key="Activity.status.planned"/>
                                </c:when>
                            </c:choose>
                        </fanta:dataColumn>
                    </fanta:table>
                </td>
            </tr>
        </table>
    </td>
</tr>
</table>