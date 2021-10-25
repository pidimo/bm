<%@ include file="/Includes.jsp" %>


<fmt:message  var="title" key="SalesProcessAction.delete" scope="request"/>
<fmt:message   var="button" key="Common.delete" scope="request"/>

<c:set var="action" value="/SalesProcess/Action/Delete" scope="request"/>
<c:set var="op" value="delete" scope="request"/>

<c:set var="isSalesProcess" value="${true}" scope="request" />
<c:set var="windowTitle" value="SalesProcessAction.delete" scope="request"/>


<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ContactTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/sales/Action.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/ContactTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/sales/Action.jsp"  scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>