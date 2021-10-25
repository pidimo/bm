<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.company.edit" scope="request"/>

<fmt:message var="title" key="Company.information" scope="request"/>
<fmt:message var="button" key="Common.save" scope="request"/>
<c:set var="action" value="/Company/Update" scope="request"/>
<c:set var="op" value="update" scope="request"/>

<c:set var="windowTitle" value="Company.information" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/admin/Company.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/admin/Company.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>