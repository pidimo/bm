<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.dashboard.configuration" scope="request"/>

<c:set var="pagetitle" value="Common.home" scope="request"/>
<c:set var="windowTitle" value="Common.home" scope="request"/>


<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/DashboardConfigurationTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/dashboard/ContainerConfiguration.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/dashboard/ContainerConfiguration.jsp" scope="request"/>
        <c:set var="tabs" value="/DashboardConfigurationTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>