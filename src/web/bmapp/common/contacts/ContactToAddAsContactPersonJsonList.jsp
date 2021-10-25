<%@ include file="/Includes.jsp" %>

{
<c:set var="fantaListName" value="personSearchList_CPerson" scope="request"/>
<c:import url="/bmapp/common/util/ResultListPageInfoFragment.jsp"/>

"list":
[
<c:forEach var="contact" items="${personSearchList_CPerson.result}" varStatus="statusVar">
  <c:set var="imageUrlVar" value="/contacts/DownloadAddressImage.do?dto(freeTextId)=${contact.imageId}"/>

  <c:if test="${statusVar.index > 0}">, </c:if>
  {
  "addressId" : "${contact.addressId}",
  "personName" : "${app2:escapeJSON(contact.personName)}",
  "addressType" : "${contact.addressType}",
  "name1" : "${app2:escapeJSON(contact.name1)}",
  "name2" : "${app2:escapeJSON(contact.name2)}",
  "countryCode" : "${app2:escapeJSON(contact.countryCode)}",
  "cityName" : "${app2:escapeJSON(contact.cityName)}",
  "zip" : "${app2:escapeJSON(contact.zip)}",
  "salutationLabel" : "${app2:escapeJSON(contact.salutationLabel)}",
  "titleText" : "${app2:escapeJSON(contact.titleText)}",
  "imageId" : "${contact.imageId}",
  "imageUrl" : "${(not empty contact.imageId) ? imageUrlVar : ""}"
  }
</c:forEach>
]
}

