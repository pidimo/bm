<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="helpResourceKey" value="help.calendar.monthlyOverview" scope="request"/>

<c:set var="pagetitle" value="Scheduler.day" scope="request"/>
<c:set var="windowTitle" value="Scheduler.Title.monthlyView" scope="request"/>

<c:set var="enableTooltip" value="true" scope="request"/>



<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/scheduler/MonthlyOverview.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/scheduler/MonthlyOverview.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>
