<%@ page import="com.piramide.elwis.web.financemanager.form.ContractInvoiceCreateForm" %>
<%@ page import="com.piramide.elwis.web.salesmanager.el.Functions" %>
<%@ include file="/Includes.jsp" %>

<%
    ContractInvoiceCreateForm contractInvoiceCreateForm = Functions.initializeContractInvoiceCreateForm(request);
    contractInvoiceCreateForm.setDto("isFromContracts","true");
%>

<c:set var="helpResourceKey" value="help.contractToInvoice.generate" scope="request"/>

<c:set var="windowTitle" value="Contract.toInvoice.title" scope="request"/>
<c:set var="action" value="/Contract/Invoice/Create" scope="request"/>

<fmt:message var="title" key="Contract.invoice.createInvoices" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/finance/ContractToInvoice.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/finance/ContractToInvoice.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>