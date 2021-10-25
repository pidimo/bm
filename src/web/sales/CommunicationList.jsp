<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.salesProcess.communication.list" scope="request"/>

<c:set var="isFromSalesProcess" value="true" scope="request"/>
<c:set var="commURL" value="/sales/MainCommunication/Download.do" scope="request"/>
<c:set var="windowTitle" value="Communication.Title.list" scope="request"/>

<c:set var="downloadDocumentURL"
       value="/MainCommunication/Document/Download.do?processId=${param.processId}"
       scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/contacts/CommunicationList.jsp" scope="request"/>
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/SalesProcessTabs.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/contacts/CommunicationList.jsp" scope="request"/>
        <c:set var="tabs" value="/SalesProcessTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>
