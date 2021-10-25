<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.supportCase.communication.list" scope="request"/>

<c:set var="isSupportCase" value="true" scope="request"/>
<c:set var="commURL" value="/support/MainCommunication/Download.do" scope="request"/>
<c:set var="windowTitle" value="Communication.Title.list" scope="request"/>
<c:set var="isSupport" value="true" scope="request"/>

<c:set var="downloadDocumentURL"
       value="/MainCommunication/Document/Download.do?caseId=${param.caseId}"
       scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/contacts/CommunicationList.jsp" scope="request"/>
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/CaseTabs.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/contacts/CommunicationList.jsp" scope="request"/>
        <c:set var="tabs" value="/CaseTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>
