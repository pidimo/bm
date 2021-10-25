<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.company.delete.confirmation" scope="request"/>

<fmt:message var="title" key="Admin.Company.delete.confirmation" scope="request"/>
<fmt:message var="button" key="Company.delete" scope="request"/>
<c:set var="action" value="/Company/Delete" scope="request"/>
<c:set var="op" value="delete" scope="request"/>

<c:set var="pagetitle" value="Admin.title" scope="request"/>
<c:set var="windowTitle" value="Admin.Company.delete" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/admin/CompanyDeleteConfirmation.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/admin/CompanyDeleteConfirmation.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>