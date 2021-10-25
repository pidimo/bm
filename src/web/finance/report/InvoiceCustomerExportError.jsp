<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>

<c:set var="action" value="/Report/InvoiceCustomerExport" scope="request"/>
<c:set var="pagetitle" value="Finance.report.invoiceCustomerExport" scope="request"/>
<c:set var="windowTitle" value="Finance.report.invoiceCustomerExport" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/finance/report/InvoiceExportError.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/finance/report/InvoiceExportError.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>
