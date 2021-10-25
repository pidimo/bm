<%@ include file="/Includes.jsp" %>

<fmt:message var="save" key="Common.save" scope="request"/>
<c:remove var="index" scope="session"/>

<c:set var="windowTitle" value="Admin.Company" scope="request"/>
<c:set var="pagetitle" value="Common.company" scope="request"/>

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
