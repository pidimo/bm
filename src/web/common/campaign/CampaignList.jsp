<%@ include file="/Includes.jsp" %>
<%@ taglib uri="/WEB-INF/app2.tld" prefix="app2" %>

<tags:jscript language="JavaScript" src="/js/common.jsp"/>
<tags:jscript language="JavaScript" src="/js/contacts/contact.jsp"/>

<br>
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
<tr>
    <td height="20" class="title">
        <fmt:message key="Campaign.Title.search"/>

        <br>
    </td>
</tr>
<tr>
    <td>
        <table width="100%" border="0" align="center" cellpadding="3" cellspacing="0">

            <TR>
                <td class="label">
                    <fmt:message key="Common.search"/>
                </td>
                <html:form action="/List.do" focus="parameter(campaignName)">

                    <td class="contain" colspan="3" nowrap>
                        <html:text property="parameter(campaignName)" styleClass="largeText"/>
                        &nbsp;
                        <html:submit styleClass="button">
                            <fmt:message key="Common.go"/>
                        </html:submit>
                        &nbsp;
                        <app:link action="/AdvancedSearch.do?advancedListForward=CampaignAdvancedSearch">&nbsp;
                            <fmt:message key="Common.advancedSearch"/>
                        </app:link>
                    </td>
                </html:form>
            </TR>
            <!-- choose alphbet to simple and advanced search  -->
            <tr>
                <td colspan="4" align="center" class="alpha">
                    <fanta:alphabet action="List.do" parameterName="campaignNameAlpha"/>
                </td>
            </tr>
        </table>
        <br/>
        <app2:checkAccessRight functionality="CAMPAIGN" permission="CREATE">
            <c:set var="newButtonsTable" scope="page">
                <tags:buttonsTable>
                    <app:url
                            value="/Campaign/Forward/Create.do?dto(operation)=create&dto(companyId)=${sessionScope.user.valueMap['companyId']}"
                            addModuleParams="false" var="newCampaignUrl"/>
                    <input type="button" class="button" value="<fmt:message key="Common.new"/>"
                           onclick="window.location ='${newCampaignUrl}'">
                </tags:buttonsTable>
            </c:set>
        </app2:checkAccessRight>
        <c:out value="${newButtonsTable}" escapeXml="false"/>
        <fanta:table list="campaignList" width="100%" id="campaign" action="List.do" imgPath="${baselayout}">
            <c:set var="editAction"
                   value="Campaign/Forward/Update.do?campaignId=${campaign.campaignId}&index=0&dto(campaignId)=${campaign.campaignId}&dto(campaignName)=${app2:encode(campaign.campaignName)}&dto(operation)=update"/>
            <c:set var="deleteAction"
                   value="Campaign/Forward/Delete.do?campaignId=${campaign.campaignId}&index=0&dto(withReferences)=true&dto(campaignId)=${campaign.campaignId}&dto(campaignName)=${app2:encode(campaign.campaignName)}"/>
            <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
                <app2:checkAccessRight functionality="CAMPAIGN" permission="VIEW">
                    <fanta:actionColumn name="update" label="Common.update" action="${editAction}"
                                        title="Common.update" styleClass="listItem" headerStyle="listHeader"
                                        width="50%" image="${baselayout}/img/edit.gif"/>
                </app2:checkAccessRight>
                <app2:checkAccessRight functionality="CAMPAIGN" permission="DELETE">
                    <fanta:actionColumn name="del" label="Common.delete" action="${deleteAction}"
                                        title="Common.delete" styleClass="listItem" headerStyle="listHeader"
                                        width="50%" image="${baselayout}/img/delete.gif"/>
                </app2:checkAccessRight>
            </fanta:columnGroup>
            <fanta:dataColumn name="campaignName" action="${editAction}" styleClass="listItem"
                              title="Campaign.mailing" headerStyle="listHeader" width="25%" orderable="true">
                &nbsp;
            </fanta:dataColumn>
            <fanta:dataColumn name="employeeName" styleClass="listItem" title="Campaign.responsibleEmployee"
                              headerStyle="listHeader" width="25%" orderable="true"/>
            <fanta:dataColumn name="status" styleClass="listItem" title="Campaign.status" headerStyle="listHeader"
                              width="13%" orderable="true" renderData="false">
                <c:if test="${campaign.status == '1'}">
                    <fmt:message key="Campaign.preparation"/>
                </c:if>
                <c:if test="${campaign.status == '2'}">
                    <fmt:message key="Campaign.sent"/>
                </c:if>
                <c:if test="${campaign.status == '3'}">
                    <fmt:message key="Campaign.cancel"/>
                </c:if>
            </fanta:dataColumn>
            <fanta:dataColumn name="recordDate" styleClass="listItem" title="Campaign.dateCreation"
                              headerStyle="listHeader" width="16%" orderable="true" renderData="false">
                <fmt:message var="datePattern" key="datePattern"/>
                <fmt:formatDate var="dateValue1" value="${app2:intToDate(campaign.recordDate)}"
                                pattern="${datePattern}"/>
                ${dateValue1}&nbsp;
            </fanta:dataColumn>
            <fanta:dataColumn name="closeDate" styleClass="listItem2" title="Campaign.closeDate"
                              headerStyle="listHeader" width="16%" orderable="true" renderData="false">
                <fmt:message var="datePattern" key="datePattern"/>
                <fmt:formatDate var="dateValue" value="${app2:intToDate(campaign.closeDate)}"
                                pattern="${datePattern}"/>
                ${dateValue}&nbsp;
            </fanta:dataColumn>


        </fanta:table>
        <c:out value="${newButtonsTable}" escapeXml="false"/>
    </td>
</tr>
</table>
