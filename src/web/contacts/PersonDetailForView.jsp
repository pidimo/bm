<%@ include file="/Includes.jsp" %>

<fmt:message var="title" key="Person.Title.view" scope="request"/>

<c:set var="action" value="/Person/Delete" scope="request"/>
<c:set var="op" value="delete" scope="request"/>
<c:set var="onlyView" value="true" scope="request"/>

<c:set var="windowTitle" value="Person.Title.view" scope="request"/>
<c:set var="pagetitle" value="Contact.plural" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/contacts/Person.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/OnlyBody.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/contacts/Person.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/OnlyBody.jsp"/>
    </c:otherwise>
</c:choose>
