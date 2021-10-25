<%@ include file="/Includes.jsp" %>

<c:set var="windowTitle" value="ImportRecord.duplicate.title.list" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/contacts/ImportRecordDupliContactPersonList.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/Iframe.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/contacts/ImportRecordDupliContactPersonList.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/Iframe.jsp"/>
    </c:otherwise>
</c:choose>
