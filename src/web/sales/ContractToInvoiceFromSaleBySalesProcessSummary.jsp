<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.sale.contractToInvoice.summary" scope="request"/>

<c:set var="pagetitle" value="Contract.plural" scope="request"/>
<c:set var="windowTitle" value="ContractToInvoice.summary.title" scope="request"/>

<app:url var="urlUpdate" value="/SalesProcess/Sale/Forward/Update.do?saleId=${param.saleId}&dto(saleId)=${param.saleId}" />
<c:set var="urlAccept" value="${urlUpdate}" scope="request"/>

<app:url var="mergeDocumentAppUrl" value="/SalesProcess/Sale/Contract/Invoice/MergeDocument.do"/>
<c:set var="urlMergeDocument" value="${mergeDocumentAppUrl}" scope="request"/>

<c:set var="urlSendViaEmail" value="/sales/SalesProcess/Sale/Contract/Invoice/SendViaEmail.do" scope="request"/>

<%--define fail sale position or cotracts update urls--%>
<c:set var="eidtSalePositionExtUrl" value="/SalesProcess/SalePosition/Forward/Update.do?tabKey=SalesProcess.tab.sale" scope="request"/>
<c:set var="eidtContractExtUrl" value="/SalesProcess/ProductContractBySalePosition/Forward/Update.do?tabKey=SalesProcess.tab.sale" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/finance/ContractToInvoiceSummary.jsp" scope="request"/>
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/SalesProcessTabs.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/finance/ContractToInvoiceSummary.jsp" scope="request"/>
        <c:set var="tabs" value="/SalesProcessTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>