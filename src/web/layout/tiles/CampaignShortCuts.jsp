<%@ include file="/Includes.jsp" %>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
    <tr>
        <td align="left" width="30%" class="moduleShortCut">

            <app2:checkAccessRight functionality="CAMPAIGN" permission="VIEW">
                &nbsp;|&nbsp;
                <app:link page="/List.do" addModuleParams="false"><fmt:message key="Common.search"/></app:link>
            </app2:checkAccessRight>
            &nbsp;|
        </td>
        <td  align="right" class="moduleShortCut" width="5%" nowrap="nowrap">
            <tags:pullDownMenu titleKey="Report.plural" align="right" >
                <tags:pullDownMenuItem action="/Report/CampaignList.do" titleKey="Campaign.Report.CampaignList" functionality="CAMPAIGN" permission="VIEW"/>
                <tags:pullDownMenuItem action="/Report/CampaignContactList.do?campaignId=${param.campaignId}&parameter(campaignId)=${param.campaignId}" titleKey="Campaign.Report.RecipientList" functionality="CAMPAIGNCONTACTS" permission="VIEW"/>
                <tags:pullDownMenuItem action="/Report/CampaignActivityList.do?campaignId=${param.campaignId}" titleKey="Campaign.Report.CampaignActivityList" functionality="CAMPAIGNACTIVITY" permission="VIEW"/>
                <tags:reportsMenu module="campaign"/>
            </tags:pullDownMenu>
        </td>
    </tr>
</table>