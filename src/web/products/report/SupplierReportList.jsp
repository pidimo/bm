<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<c:set var="helpResourceKey" value="help.product.report.supplier" scope="request"/>

<c:set var="pagetitle" value="Product.Report.SupplierList" scope="request"/>
<c:set var="windowTitle" value="Product.Report.SupplierList" scope="request"/>

<c:choose>
    <c:when test="${sessionScope.isBootstrapUI}">
        <c:set var="body" value="/WEB-INF/jsp/products/report/SupplierReportList.jsp" scope="request"/>
        <c:import url="/WEB-INF/jsp/layout/ui/main.jsp"/>
    </c:when>
    <c:otherwise>
        <c:set var="body" value="/common/products/report/SupplierReportList.jsp" scope="request"/>
        <c:import url="${sessionScope.layout}/main.jsp"/>
    </c:otherwise>
</c:choose>