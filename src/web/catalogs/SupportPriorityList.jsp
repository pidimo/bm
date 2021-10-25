<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<c:set var="helpResourceKey" value="help.config.support.priority.list" scope="request"/>

<c:set var="pagetitle" value="Common.configuration" scope="request"/>
<c:set var="windowTitle" value="Priority.plural" scope="request"/>
<c:set var="delete" value="Support/Priority/Forward/Delete.do" scope="request"/>
<c:set var="edit" value="Support/Priority/Forward/Update.do" scope="request"/>
<c:set var="create" value="Support/Priority/Forward/Create.do" scope="request"/>
<c:set var="action" value="Support/PriorityList.do" scope="request"/>
<c:set var="function" value="SUPPORTPRIORITY" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/catalogs/SupportPriorityList.jsp" scope="request"/>
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/CatalogTabs.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>

    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/catalogs/SupportPriorityList.jsp" scope="request"/>
        <c:set var="tabs" value="/CatalogTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>
