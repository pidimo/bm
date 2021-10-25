<%@ include file="/Includes.jsp" %>

<c:set var="tabHeaderLabel" value="Campaign" scope="request"/>
<c:set var="campaignId"><%=request.getParameter("campaignId")%>
</c:set>
<c:set var="tabHeaderValue" scope="request">
    <fanta:label listName="lightCampaignList" module="/campaign" patron="0" columnOrder="campaignName">
        <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        <fanta:parameter field="campaignId" value="${campaignId}"/>
    </fanta:label>
</c:set>

<c:import url="${layout}/TabHeader.jsp"/>

<jsp:useBean id="tabItems" class="java.util.LinkedHashMap" scope="request"/>
<app2:checkAccessRight functionality="CAMPAIGN" permission="VIEW">
    <c:set target="${tabItems}" property="Campaign.Tab.mailing"
           value="/Campaign/Forward/Update.do?dto(campaignName)=${app2:encode(tabHeaderValue)}&dto(campaignId)=${campaignId}"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="CAMPAIGNCRITERION" permission="VIEW">
    <c:set target="${tabItems}" property="Campaign.Tab.selection" value="/SelectionCriteria/List.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="CAMPAIGNCONTACTS" permission="VIEW">
    <c:set target="${tabItems}" property="Campaign.Tab.recipients" value="/Recipients/List.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="CAMPAIGNACTIVITY" permission="VIEW">
    <c:set target="${tabItems}" property="Campaign.Tab.Activity" value="/CampaignAvtivity/List.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="COMMUNICATION" permission="VIEW">
    <c:set target="${tabItems}" property="Contacts.Tab.communications" value="/Communication/List.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="CAMPAIGNTEMPLATE" permission="VIEW">
    <c:set target="${tabItems}" property="Campaign.Tab.templates" value="/Template/List.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="CAMPAIGNATTACH" permission="VIEW">
    <c:set target="${tabItems}" property="Campaign.Tab.Attach" value="/Attach/List.do"/>
</app2:checkAccessRight>
<%--<app2:checkAccessRight functionality="CAMPAIGNBUDGET" permission="VIEW">
    <c:set target="${tabItems}" property="Campaign.Tab.Budget" value="/Budget/List.do"/>
</app2:checkAccessRight>--%>
<app2:checkAccessRight functionality="CAMPAIGNSENTLOG" permission="VIEW">
    <c:set target="${tabItems}" property="Campaign.Tab.sentEmailLog" value="/CampaignSentLog/List.do"/>
</app2:checkAccessRight>

<jsp:useBean id="tabParams" class="java.util.LinkedHashMap" scope="request"/>

<c:import url="${sessionScope.layout}/submenu.jsp"/>



