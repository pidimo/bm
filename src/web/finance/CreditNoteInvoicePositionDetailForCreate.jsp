<%@ include file="/Includes.jsp" %>
<c:set var="helpResourceKey" value="help.invoice.position.copyPositions.list" scope="request"/>

<fmt:message var="title" key="CreditNoteInvoicePosition.Title.copy" scope="request"/>
<fmt:message var="button" key="Common.save" scope="request"/>

<c:set var="action" value="/CreditNoteInvoicePosition/Create.do" scope="request"/>
<c:set var="op" value="create" scope="request"/>

<c:set var="windowTitle" value="CreditNoteInvoicePosition.Title.copy" scope="request"/>


<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/InvoiceTabs.jsp" scope="request"/>
        <c:set var="body" value="/WEB-INF/jsp/finance/CreditNoteInvoicePosition.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/InvoiceTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/finance/CreditNoteInvoicePosition.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>