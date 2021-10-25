<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<c:set var="helpResourceKey" value="help.campaign.sentLog.contact.retrySendEmails" scope="request"/>

<fmt:message    var="title" key="Campaign.activity.emailSend.title" scope="request"/>

<c:set var="action" value="/Campaign/Retry/Email/Send" scope="request"/>
<c:set var="opSend" value="retry" scope="request"/>
<c:set var="pagetitle" value="Campaign.plural" scope="request"/>
<c:set var="windowTitle" value="Campaign.activity.emailSend.title" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/CampaignTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/campaign/GenerateEmailSend.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/CampaignTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/campaign/GenerateEmailSend.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>