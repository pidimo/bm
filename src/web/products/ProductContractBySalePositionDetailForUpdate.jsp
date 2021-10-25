<%@ include file="/Includes.jsp" %>

<c:set var="helpResourceKey" value="help.productContract.edit" scope="request"/>

<fmt:message var="title" key="ProductContract.Title.update" scope="request"/>
<fmt:message var="button" key="Common.save" scope="request"/>

<c:set var="action" value="/ProductContractBySalePosition/Update" scope="request"/>
<c:set var="op" value="update" scope="request"/>
<c:set var="windowTitle" value="ProductContract.Title.update" scope="request"/>

<!--Define module to build ajax configuration-->
<c:set var="fromModule" value="products" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/sales/ProductContract.jsp" scope="request"/>
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ProductTabs.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/sales/ProductContract.jsp" scope="request"/>
        <c:set var="tabs" value="/ProductTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>