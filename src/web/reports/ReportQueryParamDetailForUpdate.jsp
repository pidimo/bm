<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.reportJrxml.queryParam.edit" scope="request"/>

<fmt:message   var="title" key="Report.filter.title.update" scope="request"/>
<fmt:message   var="button" key="Common.save" scope="request"/>
<c:set var="action" value="/Report/QueryParam/Update" scope="request"/>
<c:set var="op" value="update" scope="request"/>

<c:set var="pagetitle" value="Report.filters" scope="request"/>
<c:set var="windowTitle" value="Report.filter.title.update" scope="request"/>


<c:set var="hideTree" value="${true}" scope="request"/>

<c:set var="queryParamFunctionality" value="FILTER" scope="request"/>

<%
    //Algunas veces, hay problemas con los nodos seleccionados.. en la libreria
    //JavaScript del arbol, por esto se borrar las cookies necesarias..
    response.addCookie(new Cookie("csd", ""));
    response.addCookie(new Cookie("cod", ""));
    request.setAttribute("jsLoad", "onload=\"myload()\"");
%>



<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ReportJrxmlTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/reports/ReportQueryParam.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/reports/ReportQueryParam.jsp" scope="request"/>
        <c:set var="tabs" value="/ReportJrxmlTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>
