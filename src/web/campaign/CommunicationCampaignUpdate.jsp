<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.communication.edit" scope="request"/>

<fmt:message var="title" key="Communication.Title.update" scope="request"/>
<fmt:message var="button" key="Common.save" scope="request"/>

<c:set var="action" value="/MainCommunication/Update" scope="request"/>
<c:set var="op" value="update" scope="request"/>
<c:set var="windowTitle" value="Communication.Title.update" scope="request"/>

<!--communication page-->


<c:set var="downloadDocumentURL"
       value="/MainCommunication/Document/Download.do?communicationId=${mainCommunicationForm.dtoMap['contactId']}&dto(contactId)=${mainCommunicationForm.dtoMap['contactId']}&campaignId=${param.campaignId}&dto(activityId)=${mainCommunicationForm.dtoMap['activityId']}"
       scope="request"/>


<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/CampaignTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/contacts/CommunicationTemplate.jsp" scope="request"/>
        <!--communication page-->
        <c:set var="moduleCommunication"
               value="/WEB-INF/jsp/campaign/CommunicationCampaign.jsp"
               scope="request"/>
        <c:set var="communicationButtons"
               value="/WEB-INF/jsp/campaign/CommunicationCampaignButtons.jsp"
               scope="request"/>
        <%--end communication--%>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/CampaignTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/contacts/CommunicationTemplate.jsp" scope="request"/>
        <!--communication page-->
        <c:set var="moduleCommunication"
               value="/common/campaign/CommunicationCampaign.jsp"
               scope="request"/>
        <c:set var="communicationButtons"
               value="/common/campaign/CommunicationCampaignButtons.jsp"
               scope="request"/>
        <%--end communication--%>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>