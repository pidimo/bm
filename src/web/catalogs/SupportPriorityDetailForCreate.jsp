<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<c:set var="helpResourceKey" value="help.config.support.priority.create" scope="request"/>

<fmt:message    var="title" key="Priority.Title.create" scope="request"/>
<fmt:message   var="button" key="Common.save" scope="request"/>
<c:set var="action" value="Support/Priority/Create" scope="request"/>
<c:set var="op" value="create" scope="request"/>
<c:set var="type" value="SUPPORT" scope="request"/>
<c:set var="function" value="SUPPORTPRIORITY" scope="request"/>
<c:set var="pagetitle" value="Common.configuration" scope="request"/>
<c:set var="windowTitle" value="Priority.Title.create" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/catalogs/SupportPriority.jsp" scope="request"/>
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/CatalogTabs.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/catalogs/SupportPriority.jsp" scope="request"/>
        <c:set var="tabs" value="/CatalogTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>