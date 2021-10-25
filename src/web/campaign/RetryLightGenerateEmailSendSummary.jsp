<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.campaign.sentLog.contact.retrySendEmails.summary" scope="request"/>

<fmt:message   var="button" key="Common.ok" scope="request"/>

<c:set var="pagetitle" value="Campaign.plural" scope="request"/>
<c:set var="windowTitle" value="Campaign.activity.emailSend.summary.title" scope="request"/>

<app:url var="urlGenerateEmailReturn" value="/SentLogContact/List.do?campaignSentLogId=${param.campaignSentLogId}" context="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/CampaignTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/campaign/GenerateEmailSendSummary.jsp" scope="request"/>
        <c:if test="${'true' eq sendInBackgroundSummary}">
            <c:set var="helpResourceKey" value="help.campaign.sentLog.contact.retrySendEmails.sendInBackGroud" scope="request"/>
            <c:set var="body" value="/WEB-INF/jsp/campaign/GenerateEmailSendInBackground.jsp" scope="request"/>
        </c:if>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>

    <c:otherwise>
        <c:set var="tabs" value="/CampaignTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/campaign/GenerateEmailSendSummary.jsp" scope="request"/>
        <c:if test="${'true' eq sendInBackgroundSummary}">
            <c:set var="body" value="/common/campaign/GenerateEmailSendInBackground.jsp" scope="request"/>
        </c:if>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>