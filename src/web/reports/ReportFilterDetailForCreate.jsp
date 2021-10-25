<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.report.filter.create" scope="request"/>

<fmt:message   var="title" key="Report.title.filterCreate" scope="request"/>
<fmt:message   var="button" key="Common.save" scope="request"/>
<c:set var="action" value="/Report/Filter/Create" scope="request"/>
<c:set var="op" value="create" scope="request"/>

<c:set var="pagetitle" value="Report.filters" scope="request"/>
<c:set var="windowTitle" value="Report.title.filterCreate" scope="request"/>

<%
    //Algunas veces, hay problemas con los nodos seleccionados.. en la libreria
    //JavaScript del arbol, por esto se borrar las cookies necesarias..
    response.addCookie(new Cookie("csd", ""));
    response.addCookie(new Cookie("cod", ""));
    request.setAttribute("jsLoad", "onload=\"myload()\"");
%>



<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ReportTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/reports/ReportFilter.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/reports/ReportFilter.jsp" scope="request"/>
        <c:set var="tabs" value="/ReportTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>