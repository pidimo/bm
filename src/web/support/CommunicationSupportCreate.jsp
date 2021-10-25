<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.communication.create" scope="request"/>

<fmt:message var="title" key="Communication.Title.new" scope="request"/>
<fmt:message var="button" key="Common.save" scope="request"/>
<c:set var="action" value="/MainCommunication/Create" scope="request"/>
<c:set var="op" value="create" scope="request"/>

<c:set var="windowTitle" value="Communication.Title.new" scope="request"/>
<c:set target="${mainCommunicationForm.dtoMap}" property="inOut" value="0"/>

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