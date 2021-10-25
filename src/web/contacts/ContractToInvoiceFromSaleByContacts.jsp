<%@ page import="com.piramide.elwis.web.salesmanager.el.Functions" %>
<%@ include file="/Includes.jsp" %>

<%
    Functions.initializeContractInvoiceCreateForm(request);
%>
<c:set var="helpResourceKey" value="help.contact.sale.invoice" scope="request"/>

<fmt:message var="title" key="Contract.salePosition.invoice.createInvoices" scope="request"/>

<c:set var="windowTitle" value="Contract.toInvoice.title" scope="request"/>
<c:set var="action" value="/Sale/Contract/Invoice/Create" scope="request"/>

<%--define fail sale position or cotracts update urls in contact module--%>
<c:set var="eidtSalePositionExtUrl" value="/SalePositionBySale/Forward/Update.do?tabKey=Contacts.Tab.sale" scope="request"/>
<c:set var="eidtContractExtUrl" value="/ProductContractBySale/Forward/Update.do?tabKey=Contacts.Tab.sale" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/finance/ContractToInvoice.jsp" scope="request"/>
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ContactTabs.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/finance/ContractToInvoice.jsp" scope="request"/>
        <c:set var="tabs" value="/ContactTabs.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>