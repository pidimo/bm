<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.dataImport.duplicate.mergeContactPerson" scope="request"/>

<fmt:message var="title" key="ImportRecord.deduplicate.contactPerson.title" scope="request"/>

<c:set var="action" value="/ImportRecord/ContactPerson/Deduplication" scope="request"/>
<c:set var="op" value="update" scope="request"/>
<c:set var="windowTitle" value="ImportRecord.deduplicate.contactPerson.title" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/contacts/ImportRecordContactPersonDeduplication.jsp" scope="request"/>
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/DataImportTabs.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/contacts/ImportRecordContactPersonDeduplication.jsp" scope="request"/>
        <c:set var="tabs" value="/DataImportTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>
