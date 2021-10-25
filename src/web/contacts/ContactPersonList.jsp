<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.contact.contactPerson.list" scope="request"/>

<c:set var="windowTitle" value="Contacts.Tab.contactPersons" scope="request"/>
<c:set var="pagetitle" value="Contact.plural" scope="request"/>


<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/contacts/ContactPersonList.jsp" scope="request"/>
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ContactTabs.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/contacts/ContactPersonList.jsp"  scope="request"/>
        <c:set var="tabs" value="/ContactTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>

