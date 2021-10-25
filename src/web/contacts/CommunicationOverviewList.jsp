<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="helpResourceKey" value="help.communication.overview.list" scope="request"/>

<c:set var="pagetitle" value="Contact.plural" scope="request"/>
<c:set var="commURL" value="/contacts/MainCommunication/Download.do" scope="request" />
<c:set var="windowTitle" value="Communication.Title.overviewSearch" scope="request" />
<c:set var="isFromContacts" value="true" scope="request" />

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/contacts/CommunicationOverviewList.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/contacts/CommunicationOverviewList.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>
