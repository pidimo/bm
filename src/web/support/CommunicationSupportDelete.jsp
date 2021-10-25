<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.communication.delete" scope="request"/>

<fmt:message var="title" key="Communication.Title.delete" scope="request"/>
<fmt:message var="button" key="Common.delete" scope="request"/>
<c:set var="action" value="/MainCommunication/Delete" scope="request"/>
<c:set var="op" value="delete" scope="request"/>
<c:set var="windowTitle" value="Communication.Title.delete" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/CaseTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/contacts/CommunicationTemplate.jsp" scope="request"/>
        <c:set var="communicationButtons" value="/WEB-INF/jsp/campaign/CommunicationCampaignButtons.jsp"
               scope="request"/>
        <c:set var="moduleCommunication" value="/WEB-INF/jsp/support/CommunicationSupport.jsp"
               scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/CaseTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/contacts/CommunicationTemplate.jsp" scope="request"/>
        <!--communication page-->
        <c:set var="communicationButtons"
               value="/common/campaign/CommunicationCampaignButtons.jsp"
               scope="request"/>
        <c:set var="moduleCommunication"
               value="/common/support/CommunicationSupport.jsp"
               scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>