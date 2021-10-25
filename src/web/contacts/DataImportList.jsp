<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.dataImport.list" scope="request"/>

<c:set var="windowTitle" value="dataImport.plural" scope="request"/>
<c:set var="pagetitle" value="dataImport.plural" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/contacts/DataImportList.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/contacts/DataImportList.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>