<%@ include file="/Includes.jsp" %>
<tags:jscript language="JavaScript" src="/js/contacts/contact.jsp"/>

<script>
    function select(id, name) {
        opener.selectField('fieldCampaignCopyId_id', id, 'fieldCampaignName_id', name);
    }
</script>

<table width="100%" border="0" align="center" cellpadding="3" cellspacing="1" class="container">
    <tr>
        <td height="20" class="title" colspan="4">
            <fmt:message key="Campaign.Title.search"/>
        </td>
    </tr>
    <TR>
        <td class="label">
            <fmt:message key="Common.search"/>
        </td>
        <html:form action="/Campaign/Forward/Copy.do" focus="parameter(campaignName)">
            <td class="contain" colspan="3" nowrap>
                <html:text property="parameter(campaignName)" styleClass="largeText"/>
                &nbsp;&nbsp;
                <html:submit styleClass="button">
                    <fmt:message key="Common.go"/>
                </html:submit>
                &nbsp;
            </td>
        </html:form>
    </TR>
    <tr>
        <td colspan="4" align="center" class="alpha">
            <fanta:alphabet action="Campaign/Forward/Copy.do" parameterName="campaignNameAlpha"/>
        </td>
    </tr>
</table>
<br>
<fanta:table list="campaignImportList" width="100%" id="campaign" action="Campaign/Forward/Copy.do"
             imgPath="${baselayout}">

    <fanta:columnGroup title="Common.action" headerStyle="listHeader" width="5%">
        <app2:checkAccessRight functionality="CAMPAIGN" permission="CREATE">
            <fanta:actionColumn name="import"
                                title="Common.import"
                                useJScript="true"
                                action="javascript:select('${campaign.campaignId}', '${app2:jscriptEncode(campaign.campaignName)}');"
                                styleClass="listItem" headerStyle="listHeader" width="50%"
                                image="${baselayout}/img/import.gif">
            </fanta:actionColumn>
        </app2:checkAccessRight>
    </fanta:columnGroup>
    <fanta:dataColumn name="campaignName" styleClass="listItem" title="Campaign.mailing"
                      headerStyle="listHeader" width="30%" orderable="true" maxLength="25">
        &nbsp;
    </fanta:dataColumn>
    <c:set var="viewLink" value="javascript:viewContactDetailInfo(1,${campaign.employeeId});"/>
    <fanta:dataColumn name="employeeName" action="${viewLink}" styleClass="listItem"
                      title="Campaign.responsibleEmployee" headerStyle="listHeader" width="30%"
                      maxLength="25" orderable="true" useJScript="true">
        &nbsp;
    </fanta:dataColumn>
    <fanta:dataColumn name="startDate" styleClass="listItem" title="Campaign.dateCreation"
                      headerStyle="listHeader" width="15%" orderable="true" renderData="false">
        <fmt:message var="datePattern" key="datePattern"/>
        <fmt:formatDate var="dateValue1" value="${app2:intToDate(campaign.startDate)}"
                        pattern="${datePattern}"/>
        ${dateValue1}&nbsp;
    </fanta:dataColumn>

    <fanta:dataColumn name="recordDate" styleClass="listItem" title="Campaign.sendDate"
                      headerStyle="listHeader" width="15%" orderable="true" renderData="false">
        <fmt:message var="datePattern" key="datePattern"/>
        <fmt:formatDate var="dateValue" value="${app2:intToDate(campaign.recordDate)}"
                        pattern="${datePattern}"/>
        ${dateValue}&nbsp;
    </fanta:dataColumn>
</fanta:table>




