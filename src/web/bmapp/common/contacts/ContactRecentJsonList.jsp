<%@ include file="/Includes.jsp" %>

{
<c:set var="fantaListName" value="contactRecentList" scope="request"/>
<c:import url="/bmapp/common/util/ResultListPageInfoFragment.jsp"/>

"list":
[
<c:forEach var="contact" items="${contactRecentList.result}" varStatus="statusVar">
  <c:set var="imageUrlVar" value="/contacts/DownloadAddressImage.do?dto(freeTextId)=${contact.imageId}"/>

  <c:if test="${statusVar.index > 0}">, </c:if>
  {
  "addressId" : "${contact.addressId}",
  "contactPersonAddressId" : "",
  "addressName" : "${app2:escapeJSON(contact.addressName1)}",
  "contactPersonOfName" : "",
  "addressType" : "${contact.addressType}",
  "addressType2" : "",
  "countryCode" : "${app2:escapeJSON(contact.countryCode)}",
  "cityName" : "${app2:escapeJSON(contact.cityName)}",
  "zip" : "${app2:escapeJSON(contact.zip)}",
  "salutationLabel" : "${app2:escapeJSON(contact.salutationLabel)}",
  "titleText" : "${app2:escapeJSON(contact.titleText)}",
  "function" : "",
  "imageId" : "${contact.imageId}",
  "imageUrl" : "${(not empty contact.imageId) ? imageUrlVar : ""}"
  }
</c:forEach>
]
}

