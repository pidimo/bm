<%@ page import="com.piramide.elwis.web.salesmanager.el.Functions" %>
<%@ include file="/Includes.jsp" %>

<%
    Functions.initializeContractInvoiceCreateForm(request);
%>
<c:set var="helpResourceKey" value="help.sale.contractToInvoice" scope="request"/>

<fmt:message var="title" key="Contract.salePosition.invoice.createInvoices" scope="request"/>

<c:set var="windowTitle" value="Contract.toInvoice.title" scope="request"/>
<c:set var="action" value="/SalesProcess/Sale/Contract/Invoice/Create" scope="request"/>

<%--define fail sale position or cotracts update urls--%>
<c:set var="eidtSalePositionExtUrl" value="/SalesProcess/SalePosition/Forward/Update.do?tabKey=SalesProcess.tab.sale" scope="request"/>
<c:set var="eidtContractExtUrl" value="/SalesProcess/ProductContractBySalePosition/Forward/Update.do?tabKey=SalesProcess.tab.sale" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/finance/ContractToInvoice.jsp" scope="request"/>
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/SalesProcessTabs.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/finance/ContractToInvoice.jsp" scope="request"/>
        <c:set var="tabs" value="/SalesProcessTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>