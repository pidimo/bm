<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.campaign.activity.user.manualAssign.assign" scope="request"/>

<c:set var="pagetitle" value="CampaignActivity.plural" scope="request"/>
<fmt:message var="title" key="Campaign.activity.user.campaignContactList" scope="request"/>
<c:set var="windowTitle" value="Campaign.activity.user.campaignContactList" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/CampaignTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/campaign/ContactResponsibleAssignList.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/CampaignTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/campaign/ContactResponsibleAssignList.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>