<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<c:set var="helpResourceKey" value="help.finances.report.invoicePartner" scope="request"/>

<fmt:message  var="title" key="Finance.report.invoicePartnerDataExport" scope="request"/>

<c:set var="action" value="/Report/InvoicePartnerDataExport/Execute" scope="request"/>
<c:set var="pagetitle" value="Finance.report.invoicePartnerDataExport" scope="request"/>
<c:set var="windowTitle" value="Finance.report.invoicePartnerDataExport" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/finance/report/InvoicePartnerDataExportReport.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/finance/report/InvoicePartnerDataExportReport.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>