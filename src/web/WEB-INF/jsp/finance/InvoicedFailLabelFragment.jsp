<%@ include file="/Includes.jsp" %>

<app2:checkAccessRight functionality="SALEPOSITION" permission="UPDATE" var="accessUpdateSalePosition"/>
<app2:checkAccessRight functionality="PRODUCTCONTRACT" permission="UPDATE" var="accessUpdateContract"/>

<%--set default update urls--%>
<c:if test="${empty eidtSalePositionExtUrl}">
    <c:set var="eidtSalePositionExtUrl" value="/sales/SalePosition/Forward/Update.do?tabKey=Sale.Tab.SalePosition"/>
    <c:set var="isContextRelative" value="true"/>
</c:if>

<c:if test="${empty eidtContractExtUrl}">
    <c:set var="eidtContractExtUrl" value="/sales/ProductContract/Forward/Update.do?index=0"/>
    <c:if test="${not empty item.salePositionId}">
        <%-- firts only as sale position from products--%>
        <c:set var="eidtContractExtUrl" value="/products/ProductContractBySalePosition/Forward/Update.do?tabKey=Product.Tab.SalePositions"/>
    </c:if>
    <c:if test="${not empty item.saleId}">
        <%-- sale position from sales--%>
        <c:set var="eidtContractExtUrl" value="/sales/ProductContractBySalePosition/Forward/Update.do?tabKey=Sale.Tab.SalePosition"/>
    </c:if>
    <c:set var="isContextRelative" value="true"/>
</c:if>

<fmt:message key="ContractToInvoice.summary.contract" var="contractLabel"/>
<fmt:message key="ContractToInvoice.summary.salePosition" var="salePositionLabel"/>

<%--compose url params, previos url should be with 'index' or 'tabKey' param--%>
<c:set var="urlParam" value="&productName=${app2:encode(item.productName)}"/>
<c:if test="${not empty item.saleId}">
    <c:set var="urlParam" value="${urlParam}&saleId=${item.saleId}&dto(saleId)=${item.saleId}"/>
</c:if>
<c:if test="${not empty item.salePositionId}">
    <c:set var="urlParam" value="${urlParam}&salePositionId=${item.salePositionId}&dto(salePositionId)=${item.salePositionId}"/>
</c:if>
<c:if test="${not empty item.contractId}">
    <c:set var="urlParam" value="${urlParam}&contractId=${item.contractId}&dto(contractId)=${item.contractId}"/>
</c:if>
<c:if test="${not empty item.productId}">
    <c:set var="urlParam" value="${urlParam}&productId=${item.productId}&dto(productId)=${item.productId}"/>
</c:if>
<c:if test="${not empty item.customerName}">
    <c:set var="urlParam" value="${urlParam}&customerName=${app2:encode(item.customerName)}"/>
</c:if>

<c:choose>
    <c:when test="${item.isContract}">
        <c:set var="failLabel" value="${contractLabel}: ${item.labelKey}"/>
        <c:choose>
            <c:when test="${accessUpdateContract}">
                <app:link action="${eidtContractExtUrl}${urlParam}"
                          styleClass=""
                          contextRelative="${not empty isContextRelative}">
                    <c:out value="${failLabel}"/>
                </app:link><br/>
            </c:when>
            <c:otherwise>
                <c:out value="${failLabel}"/><br/>
            </c:otherwise>
        </c:choose>
    </c:when>

    <c:otherwise>
        <c:set var="failLabel" value="${salePositionLabel}: ${item.labelKey}"/>
        <c:choose>
            <c:when test="${accessUpdateSalePosition}">
                <app:link action="${eidtSalePositionExtUrl}${urlParam}"
                          styleClass=""
                          contextRelative="${not empty isContextRelative}">
                    <c:out value="${failLabel}"/>
                </app:link> <br/>
            </c:when>
            <c:otherwise>
                <c:out value="${failLabel}"/><br/>
            </c:otherwise>
        </c:choose>
    </c:otherwise>
</c:choose>
