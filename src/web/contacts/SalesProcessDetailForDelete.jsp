<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.salesProcess.delete" scope="request"/>

<fmt:message  var="title" key="SalesProcess.Title.delete" scope="request"/>
<fmt:message   var="button" key="Common.delete" scope="request"/>

<c:set var="action" value="/SalesProcess/Delete" scope="request"/>
<c:set var="op" value="delete" scope="request"/>
<c:set var="isSalesProcess" value="${true}" scope="request" />
<c:set var="windowTitle" value="SalesProcess.Title.delete" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/sales/SalesProcess.jsp" scope="request"/>
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ContactTabs.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/sales/SalesProcess.jsp"  scope="request"/>
        <c:set var="tabs" value="/ContactTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>
