<%@ include file="/Includes.jsp" %>

<ul class="dropdown-menu">
    <app2:checkAccessRight functionality="CAMPAIGN" permission="VIEW">
        <li>
            <app:link page="/campaign/List.do?module=campaign"
                      contextRelative="true" addModuleParams="false" addModuleName="false"><fmt:message key="Common.search"/></app:link>
        </li>
        <li>
            <app:link page="/campaign/AdvancedSearch.do?advancedListForward=CampaignAdvancedSearch&module=campaign" contextRelative="true" addModuleParams="false" addModuleName="false">
                <fmt:message key="Common.advancedSearch"/>
            </app:link>
        </li>
        <app2:checkAccessRight functionality="CAMPAIGNCASCADEDELETE" permission="DELETE">
            <li>
                <app:link page="/campaign/Campaign/Forward/Cascade/DeleteByRange.do?module=campaign" contextRelative="true" addModuleParams="false" addModuleName="false">
                    <fmt:message key="Campaign.cascadeDelete.shortcut"/>
                </app:link>
            </li>
        </app2:checkAccessRight>

    </app2:checkAccessRight>

    <%--reports--%>
    <tags:bootstrapMenu titleKey="Report.plural" addSeparator="true" >
        <tags:bootstrapMenuItem action="/campaign/Report/CampaignList.do" contextRelative="true" titleKey="Campaign.Report.CampaignList" functionality="CAMPAIGN" permission="VIEW"/>
        <tags:bootstrapMenuItem action="/campaign/Report/CampaignContactList.do?campaignId=${param.campaignId}&parameter(campaignId)=${param.campaignId}" contextRelative="true" titleKey="Campaign.Report.RecipientList" functionality="CAMPAIGNCONTACTS" permission="VIEW"/>
        <tags:bootstrapMenuItem action="/campaign/Report/CampaignActivityList.do?campaignId=${param.campaignId}" contextRelative="true" titleKey="Campaign.Report.CampaignActivityList" functionality="CAMPAIGNACTIVITY" permission="VIEW"/>
        <tags:bootstrapReportsMenu module="campaign"/>
    </tags:bootstrapMenu>
</ul>

