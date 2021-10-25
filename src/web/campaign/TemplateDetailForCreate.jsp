<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.campaign.template.create" scope="request"/>

<fmt:message var="button" key="Common.save" scope="request"/>
<fmt:message var="title" key="Template.Title.create" scope="request"/>
<c:set var="action" value="/Template/Create" scope="request"/>
<c:set var="op" value="create" scope="request"/>


<c:set var="windowTitle" value="Template.Title.create" scope="request"/>
<c:set var="pagetitle" value="Campaign.Template.plural" scope="request"/>



<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/CampaignTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/campaign/Template.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/CampaignTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/campaign/Template.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>