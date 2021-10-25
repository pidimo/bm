<%@ include file="/Includes.jsp" %>

<fmt:message   var="title" key="Report.title.filterCreate" scope="request"/>
<fmt:message   var="button" key="Common.save" scope="request"/>
<c:set var="action" value="/Report/FilterTestValidate" scope="request"/>
<c:set var="op" value="create" scope="request"/>

<c:set var="pagetitle" value="Report.filters" scope="request"/>
<c:set var="windowTitle" value="Report.title.filterCreate" scope="request"/>
<c:set var="body" value="/common/reports/FilterTest.jsp" scope="request"/>
<%--<c:set var="tabs" value="/ReportTabs.jsp" scope="request"/>--%>
<%
    //Algunas veces, hay problemas con los nodos seleccionados.. en la libreria
    //JavaScript del arbol, por esto se borrar las cookies necesarias..
    response.addCookie(new Cookie("csd", ""));
    response.addCookie(new Cookie("cod", ""));
    request.setAttribute("jsLoad", "onload=\"myload()\"");
%>
<c:import url="${sessionScope.layout}/main.jsp"/>