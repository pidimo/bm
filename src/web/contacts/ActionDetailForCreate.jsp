<%@ include file="/Includes.jsp" %>

<fmt:message  var="title" key="SalesProcessAction.new" scope="request"/>
<fmt:message   var="button" key="Common.save" scope="request"/>

<c:set var="action" value="/SalesProcess/Action/Create" scope="request"/>
<c:set var="op" value="create" scope="request"/>

<c:set var="isSalesProcess" value="${false}" scope="request" />
<c:set var="windowTitle" value="SalesProcessAction.new" scope="request"/>

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