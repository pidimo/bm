<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="helpResourceKey" value="help.contact.communication.list" scope="request"/>

<c:set var="pagetitle" value="Contact.plural" scope="request"/>
<c:set var="commURL" value="/contacts/MainCommunication/Download.do" scope="request"/>
<c:set var="windowTitle" value="Communication.Title.list" scope="request"/>
<c:set var="isFromContacts" value="true" scope="request"/>

<c:set var="downloadDocumentURL"
       value="/MainCommunication/Document/Download.do?contactId=${param.contactId}"
       scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/contacts/CommunicationList.jsp" scope="request"/>
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ContactTabs.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/contacts/CommunicationList.jsp" scope="request"/>
        <c:set var="tabs" value="/ContactTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>