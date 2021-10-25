<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.companySettings.layout" scope="request"/>

<%--<fmt:message  var="title" key="Webmail.folder.new" scope="request"/>--%>
<fmt:message var="button" key="Common.save" scope="request"/>
<c:set var="action" value="/UIManager/Create/CompanyStyleSheet" scope="request"/>
<%--<c:set var="op" value="create" scope="request"/>--%>

<c:set var="windowTitle" value="Company.preferences" scope="request"/>
<c:set var="pagetitle" value="Company.preferences" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/CompanyPreferencesTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/uimanager/StyleConfigurable.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/CompanyPreferencesTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/uimanager/StyleConfigurable.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>
