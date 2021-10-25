<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.productContract.create" scope="request"/>

<fmt:message var="title" key="ProductContract.Title.create" scope="request"/>
<fmt:message var="button" key="Common.save" scope="request"/>

<c:set var="action" value="/ProductContract/Create" scope="request"/>
<c:set var="op" value="create" scope="request"/>
<c:set var="windowTitle" value="ProductContract.Title.create" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/sales/ProductContract.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/sales/ProductContract.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>
