<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.productContract.edit" scope="request"/>

<fmt:message var="title" key="ProductContract.Title.update" scope="request"/>
<fmt:message var="button" key="Common.save" scope="request"/>

<c:set var="action" value="/ProductContractByContractToInvoice/Update" scope="request"/>
<c:set var="op" value="update" scope="request"/>
<c:set var="productSalePositionAction" value="products/FromContractToInvoice/SalePosition/Forward/Update.do" scope="request"/>
<c:set var="windowTitle" value="ProductContract.Title.update" scope="request"/>

<%--use in ProductContractTabs.jsp to define detail tab url--%>
<c:set var="detailURL" value="/ProductContractByContractToInvoice/Forward/Update.do?dto(addressName)=${app2:encode(param['dto(addressName)'])}&dto(productName)=${app2:encode(param['dto(productName)'])}" scope="request"/>

<!--enable flag to show saleposition and sale information-->
<c:set var="showSaleInformation" value="true" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/sales/ProductContract.jsp" scope="request"/>
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ProductContractTabs.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/sales/ProductContract.jsp" scope="request"/>
        <c:set var="tabs" value="/ProductContractTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>