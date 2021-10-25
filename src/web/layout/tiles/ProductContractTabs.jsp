<%@ include file="/Includes.jsp" %>

<fanta:label var="tabNameLabel"
             listName="productContractSingleList" module="/sales"
             patron="0" columnOrder="addressName">
    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
    <fanta:parameter field="contractId" value="${param.contractId}"/>
</fanta:label>
<c:set var="tabHeaderLabel" value="ProductContract.headerLabel" scope="request"/>
<c:set var="tabHeaderValue" value="${tabNameLabel}" scope="request"/>

<c:import url="${layout}/TabHeader.jsp"/>

<jsp:useBean id="tabItems" class="java.util.LinkedHashMap" scope="request"/>

<c:if test="${empty detailURL}">
    <c:set var="detailURL" value="/ProductContract/Forward/Update.do"/>
</c:if>


<c:set target="${tabItems}" property="ProductContract.Tab.Detail" value="${detailURL}"/>
<c:set target="${tabItems}" property="ProductContract.Tab.Invoices" value="/InvoiceByContract/List.do"/>

<jsp:useBean id="tabParams" class="java.util.LinkedHashMap" scope="request"/>
<c:set target="${tabParams}" property="dto(contractId)" value="${param.contractId}"/>
<c:import url="${sessionScope.layout}/submenu.jsp"/>