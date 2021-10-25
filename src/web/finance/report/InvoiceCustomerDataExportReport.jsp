<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<c:set var="helpResourceKey" value="help.finances.report.invoiceCustomer" scope="request"/>

<fmt:message  var="title" key="Finance.report.invoiceCustomerDataExport" scope="request"/>
<c:set var="action" value="/Report/InvoiceCustomerDataExport/Execute" scope="request"/>

<c:set var="pagetitle" value="Finance.report.invoiceCustomerDataExport" scope="request"/>
<c:set var="windowTitle" value="Finance.report.invoiceCustomerDataExport" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/finance/report/InvoiceCustomerDataExportReport.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/finance/report/InvoiceCustomerDataExportReport.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>