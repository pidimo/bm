<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<c:set var="helpResourceKey" value="help.campaign.selectionCriteria.delete" scope="request"/>

<fmt:message var="title" key="Campaign.criterion.delete" scope="request"/>
<fmt:message var="button" key="Common.delete" scope="request"/>
<c:set var="action" value="/Campaign/SelectionCriteria/Delete" scope="request"/>
<c:set var="op" value="delete" scope="request"/>
<c:set var="ope" value="readDelete" scope="request"/>
<c:set var="windowTitle" value="Campaign.criterion.delete" scope="request"/>

<c:set var="pagetitle" value="Campaign.plural" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/CampaignTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/campaign/CampaignCriterionUpdate.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/CampaignTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/campaign/CampaignCriterionUpdate.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>
