<%@ include file="/Includes.jsp" %>

<fanta:label var="tabNameLabel"
             listName="saleSingleList" module="/sales"
             patron="0" columnOrder="title">
    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
    <fanta:parameter field="saleId" value="${param.saleId}"/>
</fanta:label>
<c:set var="tabHeaderLabel" value="Sale.headerLabel" scope="request"/>
<c:set var="tabHeaderValue" value="${tabNameLabel}" scope="request"/>

<c:import url="${layout}/TabHeader.jsp"/>

<jsp:useBean id="tabItems" class="java.util.LinkedHashMap" scope="request"/>
<app2:checkAccessRight functionality="SALE" permission="VIEW">
    <c:set target="${tabItems}" property="Sale.Tab.Detail" value="/Sale/Forward/Update.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="SALEPOSITION" permission="VIEW">
    <c:set target="${tabItems}" property="Sale.Tab.SalePosition" value="/SalePosition/List.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="INVOICE" permission="VIEW">
    <c:set target="${tabItems}" property="Sale.Tab.saleInvoices" value="/Sale/Invoiced/List.do"/>
</app2:checkAccessRight>

<jsp:useBean id="tabParams" class="java.util.LinkedHashMap" scope="request"/>
<c:set target="${tabParams}" property="dto(saleId)" value="${param.saleId}"/>
<c:import url="${sessionScope.layout}/submenu.jsp"/>