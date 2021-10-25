<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="helpResourceKey" value="help.finances.report.contractToInvoice" scope="request"/>

<c:set var="pagetitle" value="SalesProcess.Report.ContractToInvoiceList" scope="request"/>
<c:set var="windowTitle" value="SalesProcess.Report.ContractToInvoiceList" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/sales/report/ContractToInvoiceReportList.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/sales/report/ContractToInvoiceReportList.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>