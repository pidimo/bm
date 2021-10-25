<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.contact.sale.invoice.summary" scope="request"/>

<c:set var="pagetitle" value="Contract.plural" scope="request"/>
<c:set var="windowTitle" value="ContractToInvoice.summary.title" scope="request"/>

<app:url var="urlUpdate" value="/Sale/Forward/Update.do?saleId=${param.saleId}&dto(saleId)=${param.saleId}" />
<c:set var="urlAccept" value="${urlUpdate}" scope="request"/>

<app:url var="mergeDocumentAppUrl" value="/Sale/Contract/Invoice/MergeDocument.do"/>
<c:set var="urlMergeDocument" value="${mergeDocumentAppUrl}" scope="request"/>

<c:set var="urlSendViaEmail" value="/contacts/Sale/Contract/Invoice/SendViaEmail.do" scope="request"/>

<%--define fail sale position or cotracts update urls in contact module--%>
<c:set var="eidtSalePositionExtUrl" value="/SalePositionBySale/Forward/Update.do?tabKey=Contacts.Tab.sale" scope="request"/>
<c:set var="eidtContractExtUrl" value="/ProductContractBySale/Forward/Update.do?tabKey=Contacts.Tab.sale" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/finance/ContractToInvoiceSummary.jsp" scope="request"/>
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ContactTabs.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/finance/ContractToInvoiceSummary.jsp" scope="request"/>
        <c:set var="tabs" value="/ContactTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>