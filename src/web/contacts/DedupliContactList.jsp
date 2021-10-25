<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.checkDuplicate.list" scope="request"/>

<c:set var="windowTitle" value="DedupliContact.checkDuplicates" scope="request"/>
<c:set var="pagetitle" value="DedupliContact.checkDuplicates" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/contacts/DedupliContactList.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/contacts/DedupliContactList.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>