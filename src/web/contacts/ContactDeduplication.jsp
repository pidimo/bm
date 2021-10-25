<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.checkDuplicate.contact.merge" scope="request"/>

<fmt:message var="title" key="DedupliContact.deduplicate.title" scope="request"/>

<c:set var="action" value="/DedupliContact/Deduplication" scope="request"/>
<c:set var="op" value="update" scope="request"/>

<c:set var="windowTitle" value="DedupliContact.deduplicate.title" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/contacts/ContactDeduplication.jsp" scope="request"/>
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/DedupliContactTabs.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/contacts/ContactDeduplication.jsp" scope="request"/>
        <c:set var="tabs" value="/DedupliContactTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>