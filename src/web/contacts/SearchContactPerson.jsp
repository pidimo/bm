<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="windowTitle" value="Contacts.Tab.contactPersons" scope="request"/>
<c:set var="pagetitle" value="Contact.plural" scope="request"/>


<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/contacts/SearchContactPerson.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/OnlyBody.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/contacts/SearchContactPerson.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/OnlyBody.jsp"/>
    </c:otherwise>
</c:choose>
