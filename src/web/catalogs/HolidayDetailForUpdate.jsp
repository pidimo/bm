<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.config.scheduler.holiday.edit" scope="request"/>

<fmt:message var="title" key="holiday.title.update" scope="request"/>
<fmt:message var="button" key="Common.save" scope="request"/>
<c:set var="action" value="/Holiday/Update" scope="request"/>
<c:set var="op" value="update" scope="request"/>

<c:set var="pagetitle" value="Common.configuration" scope="request"/>
<c:set var="windowTitle" value="holiday.title.update" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/catalogs/Holiday.jsp" scope="request"/>
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/CatalogTabs.jsp" scope="request"/>
        <c:set var="jsLoad" value="onLoad=\"enableRule(${HolidayForm.dtoMap.holidayType==0?'false':'true'})\" " scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/catalogs/Holiday.jsp" scope="request"/>
        <c:set var="tabs" value="/CatalogTabs.jsp" scope="request"/>
        <c:set var="jsLoad" value="onLoad=\"enableRule(${HolidayForm.dtoMap.holidayType==0?'false':'true'})\" " scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>
