<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.dashboard.component.configuration" scope="request"/>

<c:set var="pagetitle" value="Common.home" scope="request"/>
<c:set var="windowTitle" value="Common.home" scope="request"/>

<c:set var="componentAction" value="/Birthday/Dashboard/Component/Configuration/save" scope="request"/>
<c:set var="preProcessSubmitJs" value="birthdaySubmitPreProcess();" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="staticFilterUrl" value="/WEB-INF/jsp/dashboard/BirthdayStaticFilterFragment.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/dashboard/ComponentConfiguration.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="staticFilterUrl" value="/common/dashboard/BirthdayStaticFilterFragment.jsp" scope="request"/>
        <c:set var="body" value="/common/dashboard/ComponentConfiguration.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>