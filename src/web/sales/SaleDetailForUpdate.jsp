<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.sale.edit" scope="request"/>

<fmt:message var="title" key="Sale.Title.update" scope="request"/>
<fmt:message var="button" key="Common.save" scope="request"/>

<c:set var="action" value="/Sale/Update" scope="request"/>
<c:set var="op" value="update" scope="request"/>

<c:set var="windowTitle" value="Sale.Title.update" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/SaleTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/sales/Sale.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/SaleTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/sales/Sale.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>