<%@ include file="/Includes.jsp" %>

<c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
<fmt:message   var="dateTimePattern" key="dateTimePattern"/>

<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td>
            <table width="80%" border="0" cellpadding="0" cellspacing="0" align="center">
                <tr>
                    <td>
                        <fanta:table list="campaignSentLogList" width="100%" id="campaignSentLog"
                                     action="CampaignSentLog/List.do"
                                     imgPath="${baselayout}">

                            <c:set var="editAction"
                                   value="SentLogContact/List.do?campaignSentLogId=${campaignSentLog.campaignSentLogId}&activityId=${campaignSentLog.activityId}"/>
                            <c:set var="deleteAction"
                                   value="CampaignSentLog/Forward/Delete.do?dto(withReferences)=true&campaignSentLogId=${campaignSentLog.campaignSentLogId}&dto(campaignSentLogId)=${campaignSentLog.campaignSentLogId}"/>
                            <fanta:columnGroup title="Common.action" width="5%" headerStyle="listHeader">
                                <app2:checkAccessRight functionality="CAMPAIGNSENTLOG" permission="VIEW">
                                    <fanta:actionColumn name="" title="Common.update" action="${editAction}"
                                                        styleClass="listItem" headerStyle="listHeader"
                                                        image="${baselayout}/img/edit.gif"/>
                                </app2:checkAccessRight>
                                <app2:checkAccessRight functionality="CAMPAIGNSENTLOG" permission="DELETE">
                                    <fanta:actionColumn name="" title="Common.delete" action="${deleteAction}"
                                                        styleClass="listItem" headerStyle="listHeader"
                                                        image="${baselayout}/img/delete.gif"/>
                                </app2:checkAccessRight>
                            </fanta:columnGroup>

                            <fanta:dataColumn name="generationTime" action="${editAction}" styleClass="listItem"
                                              title="CampaignSentLog.generationTime" headerStyle="listHeader" width="20%"
                                              orderable="true" renderData="false">
                                <c:if test="${not empty campaignSentLog.generationTime}">
                                    <c:out value="${app2:getDateWithTimeZone(campaignSentLog.generationTime, timeZone, dateTimePattern)}"/>
                                </c:if>
                            </fanta:dataColumn>
                            <fanta:dataColumn name="userName" styleClass="listItem"
                                              title="CampaignSentLog.user" headerStyle="listHeader" width="35%"
                                              orderable="true" maxLength="30">
                            </fanta:dataColumn>
                            <fanta:dataColumn name="title" styleClass="listItem"
                                              title="CampaignSentLog.activity" headerStyle="listHeader" width="25%"
                                              orderable="true" maxLength="25">
                            </fanta:dataColumn>
                            <fanta:dataColumn name="totalSent" styleClass="listItem2"
                                              title="CampaignSentLog.summary" headerStyle="listHeader" width="15%"
                                              renderData="false">
                                <c:out value="${app2:getCampaignSentLogSummary(campaignSentLog.campaignSentLogId)}"/>
                            </fanta:dataColumn>
                        </fanta:table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
