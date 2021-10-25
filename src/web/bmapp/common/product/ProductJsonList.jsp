<%@ include file="/Includes.jsp" %>
<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>

{
<c:set var="fantaListName" value="productList" scope="request"/>
<c:import url="/bmapp/common/util/ResultListPageInfoFragment.jsp"/>

"list":
[
<c:forEach var="product" items="${productList.result}" varStatus="statusVar">
  <fmt:formatNumber var="productPrice" value="${product.price}" type="number" pattern="${numberFormat}"/>

  <c:set var="productPictureDTO" value="${app2:findProductPictureByProductIdFirst(product.id, pageContext.request)}"/>
  <c:set var="imageUrlVar" value="/contacts/DownloadAddressImage.do?dto(freeTextId)=${productPictureDTO.freeTextId}"/>

  <c:if test="${statusVar.index > 0}">, </c:if>
  {
  "productId" : "${product.id}",
  "productName" : "${app2:escapeJSON(product.name)}",
  "typeName" : "${app2:escapeJSON(product.type)}",
  "groupName" : "${app2:escapeJSON(product.group)}",
  "unitName" : "${app2:escapeJSON(product.unit)}",
  "price" : "${product.price}",
  "priceFormatted" : "${productPrice}",
  "imageUrl" : "${(not empty productPictureDTO.freeTextId) ? imageUrlVar : ""}"
  }
</c:forEach>
]
}

