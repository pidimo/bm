<%@ include file="/Includes.jsp" %>

<fmt:message var="numberFormat" key="numberFormat.2DecimalPlaces"/>
<c:set var="timeZone" value="${sessionScope.user.valueMap['dateTimeZone']}"/>
<c:set var="locale" value="${sessionScope.user.valueMap['locale']}"/>
<c:set var="userAddressId" value="${sessionScope.user.valueMap['userAddressId']}"/>
<fmt:message var="dateTimePattern" key="dateTimePattern"/>

<fmt:formatNumber var="priceFormatted" value="${productForm.dtoMap['price']}" type="number" pattern="${numberFormat}"/>
<fmt:formatNumber var="priceGrossFormatted" value="${productForm.dtoMap['priceGross']}" type="number" pattern="${numberFormat}"/>

<c:set var="pictureDTOList" value="${app2:findProductPictureByProductIdDTOList(productForm.dtoMap['productId'], pageContext.request)}"/>

{
"entity" : {
  "productId" : "${productForm.dtoMap['productId']}",
  "companyId" : "${productForm.dtoMap['companyId']}",
  "productName" : "${app2:escapeJSON(productForm.dtoMap['productName'])}",
  "productGroupId" : "${productForm.dtoMap['productGroupId']}",
  "unitId" : "${productForm.dtoMap['unitId']}",
  "productTypeId" : "${productForm.dtoMap['productTypeId']}",
  "vatId" : "${productForm.dtoMap['vatId']}",
  "accountId" : "${productForm.dtoMap['accountId']}",
  "currentVersion" : "${app2:escapeJSON(productForm.dtoMap['currentVersion'])}",
  "price" : "${productForm.dtoMap['price']}",
  "priceFormatted" : "${priceFormatted}",
  "priceGross" : "${productForm.dtoMap['priceGross']}",
  "priceGrossFormatted" : "${priceGrossFormatted}",
  "productNumber" : "${app2:escapeJSON(productForm.dtoMap['productNumber'])}",
  "description" : "${app2:escapeJSON(productForm.dtoMap['description'])}",

  "eventMaxParticipant" : "${productForm.dtoMap['eventMaxParticipant']}",
  "nroOfParticipant" : "${app2:countSalePositionByProduct(productForm.dtoMap['productId'])}",
  "initDateTime" : "${productForm.dtoMap['initDateTime']}",
  "initDateTimeFormated" : "${app2:getLocaleFormattedDateTime(productForm.dtoMap['initDateTime'], timeZone, dateTimePattern, locale)}",
  "endDateTime" : "${productForm.dtoMap['endDateTime']}",
  "endDateTimeFormated" : "${app2:getLocaleFormattedDateTime(productForm.dtoMap['endDateTime'], timeZone, dateTimePattern, locale)}",
  "closingDateTime" : "${productForm.dtoMap['closingDateTime']}",
  "closingDateTimeFormated" : "${app2:getLocaleFormattedDateTime(productForm.dtoMap['closingDateTime'], timeZone, dateTimePattern, locale)}",
  "webSiteLink" : "${app2:escapeJSON(productForm.dtoMap['webSiteLink'])}",
  "eventAddress" : "${app2:escapeJSON(productForm.dtoMap['eventAddress'])}",
  "isParticipant" : "${app2:checkAlreadyIsParticipantInEvent(productForm.dtoMap['productId'], userAddressId, pageContext.request)}",
  "version" : "${productForm.dtoMap['version']}"
},

"productPictureArray" : [
  <c:forEach var="item" items="${pictureDTOList}" varStatus="statusVar">
    <c:set var="imageUrlVar" value="/contacts/DownloadAddressImage.do?dto(freeTextId)=${item.freeTextId}"/>

    <c:if test="${statusVar.index > 0}">, </c:if>
    {
    "freeTextId" : "${item.freeTextId}",
    "productPictureName" : "${app2:escapeJSON(item.productPictureName)}",
    "size" : "${item.size}",
    "imageUrl" : "${(not empty item.freeTextId) ? imageUrlVar : ""}"
    }
  </c:forEach>
],
<c:import url="/bmapp/common/product/ProductCatalogsJsonFragment.jsp"/>
}
