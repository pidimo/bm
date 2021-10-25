<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="windowTitle" value="Common.globalError" scope="request"/>
<c:set var="pagetitle" value="Common.globalError" scope="request"/>
<%
    if(request.getHeader("isAjaxRequest")!=null && Boolean.valueOf(request.getHeader("isAjaxRequest"))){
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
%>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp?errorPage=true"/>
    </c:when>
    <c:otherwise>
        <c:import url="${layout}/main.jsp?errorPage=true"/>
    </c:otherwise>
</c:choose>
