<%@ include file="/Includes.jsp" %>

{
<c:set var="fantaListName" value="contactPersonList" scope="request"/>
<c:import url="/bmapp/common/util/ResultListPageInfoFragment.jsp"/>

"list":
[
<c:forEach var="contactPerson" items="${contactPersonList.result}" varStatus="statusVar">
  <c:set var="imageUrlVar" value="/contacts/DownloadAddressImage.do?dto(freeTextId)=${contactPerson.imageId}"/>

  <c:if test="${statusVar.index > 0}">, </c:if>
  {
  "addressId" : "${contactPerson.addressId}",
  "contactPersonId" : "${contactPerson.contactPersonId}",
  "contactPersonName" : "${app2:escapeJSON(contactPerson.contactPersonName)}",
  "lastName" : "${app2:escapeJSON(contactPerson.lastName)}",
  "firstName" : "${app2:escapeJSON(contactPerson.firstName)}",
  "function" : "${app2:escapeJSON(contactPerson.function)}",
  "department" : "${app2:escapeJSON(contactPerson.department)}",
  "active" : "${'1' eq contactPerson.active}",
  "imageId" : "${contactPerson.imageId}",
  "imageUrl" : "${(not empty contactPerson.imageId) ? imageUrlVar : ""}"
  }
</c:forEach>
]
}

