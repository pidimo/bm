<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ include file="/Includes.jsp" %>

<c:set var="tabHeaderLabel" value="Product.headerLabel" scope="request"/>

<c:set var="productId"><%=request.getParameter("productId")%>
</c:set>
<fanta:label var="productNameLabel" listName="productBaseList" module="/products" patron="0" columnOrder="name">
    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
    <fanta:parameter field="productId" value="${empty productId?0:productId}"/>
</fanta:label>

<c:set var="tabHeaderValue" value="${productNameLabel}" scope="request"/>

<!--set product name in request-->
<c:set var="productName" value="${productNameLabel}" scope="request"/>
<c:import url="${layout}/TabHeader.jsp"/>

<jsp:useBean id="tabItems" class="java.util.LinkedHashMap" scope="request"/>

<app2:checkAccessRight functionality="PRODUCT" permission="VIEW">
    <c:set target="${tabItems}" property="Product.Tab.Detail" value="/Product/Forward/Update.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="CATEGORYTAB" permission="VIEW">
    <%
        String productId = request.getParameter("productId");
        List paramsList = new ArrayList();
        paramsList.add(Integer.valueOf(productId));
        request.setAttribute("paramsList", paramsList);
    %>
    <c:set var="categoryTabs" value="${app2:getCategoryTabs('3', 'findValueByProductId', paramsList, pageContext.request)}"/>
    <c:forEach var="tab" items="${categoryTabs}">
        <c:set target="${tabItems}" property="001@100-${tab.label}"
               value="/CategoryTab/Forward/Update.do?categoryTabId=${tab.categoryTabId}&dto(categoryTabId)=${tab.categoryTabId}"/>
    </c:forEach>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="PRICING" permission="VIEW">
    <c:set target="${tabItems}" property="Product.Tab.Pricing" value="/Pricing/List.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="SUPPLIER" permission="VIEW">
    <c:set target="${tabItems}" property="Product.Tab.Supplier" value="/Supplier/List.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="SALEPOSITION" permission="VIEW">
    <c:set target="${tabItems}" property="Product.Tab.SalePositions" value="/SalePosition/List.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="COMPETITORPRODUCT" permission="VIEW">
    <c:set target="${tabItems}" property="Product.Tab.Competitors" value="/CompetitorProduct/List.do"/>
</app2:checkAccessRight>
<app2:checkAccessRight functionality="PICTURE" permission="VIEW">
    <c:set target="${tabItems}" property="Product.Tab.Pictures" value="/ProductPicture/List.do"/>
</app2:checkAccessRight>
<c:set target="${tabItems}" property="Product.Tab.ProductText" value="/ProductText/Forward/Update.do"/>

<jsp:useBean id="tabParams" class="java.util.LinkedHashMap" scope="request"/>
<c:set target="${tabParams}" property="dto(productId)" value="${param.productId}"/>
<c:import url="${sessionScope.layout}/submenu.jsp"/>



