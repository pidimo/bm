<%--
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>

<fmt:message var="title" key="Template.File.upload" scope="request"/>
<c:set var="pagetitle" value="Common.configuration" scope="request"/>
<c:set var="windowTitle" value="Template.plural" scope="request"/>
<c:set var="body" value="/common/catalogs/TemplateFileList.jsp" scope="request"/>
<c:set var="tabs" value="/CatalogTabs.jsp" scope="request"/>
<c:import url="${sessionScope.layout}/main.jsp"/>--%>


<%@ include file="/Includes.jsp" %>

<c:set var="windowTitle" value="Template.plural" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/catalogs/TemplateFileList.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/Iframe.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/catalogs/TemplateFileList.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/Iframe.jsp"/>
    </c:otherwise>
</c:choose>