<%@ include file="/Includes.jsp" %>
<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>

<c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
<c:set var="locale" value="${sessionScope.user.valueMap['locale']}"/>
<fmt:message var="dateTimePattern" key="dateTimePattern"/>


{
<c:set var="fantaListName" value="${productEventListName}" scope="request"/>
<c:import url="/bmapp/common/util/ResultListPageInfoFragment.jsp"/>

"list":
[
<c:forEach var="product" items="${productEventListResult}" varStatus="statusVar">
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

  "initDateTime" : "${product.initDateTime}",
  "initDateTimeFormated" : "${app2:getLocaleFormattedDateTime(product.initDateTime, timeZone, dateTimePattern, locale)}",
  "endDateTime" : "${product.endDateTime}",
  "endDateTimeFormated" : "${app2:getLocaleFormattedDateTime(product.endDateTime, timeZone, dateTimePattern, locale)}",
  "imageUrl" : "${(not empty productPictureDTO.freeTextId) ? imageUrlVar : ""}"
  }
</c:forEach>
]
}

