<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="helpResourceKey" value="help.productContract.invoice.list" scope="request"/>

<c:set var="windowTitle" value="Invoice.InvoiceByContractList.Title" scope="request"/>
<c:set var="pagetitle" value="Invoice.InvoiceByContractList.Title" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/finance/InvoiceByContractList.jsp" scope="request"/>
        <c:set var="tabs" value="/WEB-INF/jsp/layout/tiles/ProductContractTabs.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="tabs" value="/ProductContractTabs.jsp" scope="request"/>
        <c:set var="body" value="/common/finance/InvoiceByContractList.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>