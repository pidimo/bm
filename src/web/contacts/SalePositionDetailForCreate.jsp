<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.salePosition.create" scope="request"/>

<fmt:message var="title" key="SalePosition.Title.create" scope="request"/>
<fmt:message var="button" key="Common.save" scope="request"/>

<c:set var="action" value="/SalePosition/Create" scope="request"/>
<c:set var="op" value="create" scope="request"/>
<c:set var="windowTitle" value="SalePosition.Title.create" scope="request"/>
<!--Disable Sale link-->
<c:set var="showSaleLink" value="${false}" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ContactTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/contacts/SalePosition.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/ContactTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/contacts/SalePosition.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>