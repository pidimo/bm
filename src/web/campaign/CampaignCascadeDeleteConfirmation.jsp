<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<c:set var="helpResourceKey" value="help.campaign.cascadeDelete.confirmation" scope="request"/>

<fmt:message    var="title" key="Campaign.cascadeDelete.confirmation.title" scope="request"/>

<c:set var="pagetitle" value="Campaign.plural" scope="request"/>
<c:set var="windowTitle" value="Campaign.cascadeDelete.confirmation.title" scope="request"/>

<c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/CampaignTabs.jsp" scope="request"/>
<c:set var="body" value="/WEB-INF/jsp/campaign/CampaignCascadeDeleteConfirmation.jsp" scope="request"/>
<c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>


