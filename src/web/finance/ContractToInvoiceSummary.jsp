<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.contractToInvoice.generate.summary" scope="request"/>

<c:set var="pagetitle" value="Contract.plural" scope="request"/>
<c:set var="windowTitle" value="ContractToInvoice.summary.title" scope="request"/>

<app:url var="urlList" value="/Contract/ToInvoice/List.do"/>
<c:set var="urlAccept" value="${urlList}" scope="request"/>

<app:url var="mergeDocumentAppUrl" value="/Contract/Invoice/MergeDocument.do"/>
<c:set var="urlMergeDocument" value="${mergeDocumentAppUrl}" scope="request"/>

<c:set var="urlSendViaEmail" value="/finance/Contract/Invoice/SendViaEmail.do" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/finance/ContractToInvoiceSummary.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/finance/ContractToInvoiceSummary.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>