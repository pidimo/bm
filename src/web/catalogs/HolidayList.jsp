<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.config.scheduler.holiday.list" scope="request"/>

<c:set var="pagetitle" value="Common.configuration" scope="request"/>
<c:set var="windowTitle" value="holiday.plural" scope="request"/>
<c:set var="delete" value="Holiday/Forward/Delete.do" scope="request"/>
<c:set var="edit" value="Holiday/Forward/Update.do" scope="request"/>
<c:set var="create" value="Holiday/Forward/Create.do" scope="request"/>
<c:set var="action" value="Holiday/List.do" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/catalogs/HolidayList.jsp" scope="request"/>
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/CatalogTabs.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/catalogs/HolidayList.jsp" scope="request"/>
        <c:set var="tabs" value="/CatalogTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>