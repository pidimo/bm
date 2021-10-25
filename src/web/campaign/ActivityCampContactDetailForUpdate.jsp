<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.campaign.activity.contact.edit" scope="request"/>

<fmt:message   var="title" key="Campaign.activity.campContact.title.update" scope="request"/>
<fmt:message   var="button" key="Common.save" scope="request"/>
<c:set var="action" value="/Campaign/Activity/CampContact/Update" scope="request"/>
<c:set var="op" value="update" scope="request"/>

<c:set var="pagetitle" value="Campaign.plural" scope="request"/>
<c:set var="windowTitle" value="Campaign.activity.campContact.title.update" scope="request"/>



<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/CampaignTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/campaign/ActivityCampaignContact.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/campaign/ActivityCampaignContact.jsp" scope="request"/>
        <c:set var="tabs" value="/CampaignTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>