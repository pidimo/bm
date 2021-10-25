<%@ include file="/Includes.jsp" %>

{
<c:set var="fantaListName" value="salePositionParticipantList" scope="request"/>
<c:import url="/bmapp/common/util/ResultListPageInfoFragment.jsp"/>

"list":
[
<c:forEach var="salePosition" items="${salePositionParticipantList.result}" varStatus="statusVar">
  <c:set var="imageUrlVar" value="/contacts/DownloadAddressImage.do?dto(freeTextId)=${salePosition.imageId}"/>
  <c:set var="personInfoMap" value="${app2:getAddressMap(salePosition.contactPersonId)}"/>

  <c:if test="${statusVar.index > 0}">, </c:if>
  {
  "salePositionId" : "${salePosition.salePositionId}",
  "customerAddressType" : "${salePosition.customerAddressType}",
  "customerId" : "${salePosition.customerId}",
  "contactPersonId" : "${salePosition.contactPersonId}",
  "customerName" : "${app2:escapeJSON(salePosition.customerName)}",
  "contactPersonName" : "${app2:escapeJSON(salePosition.contactPersonName)}",
  "titleName" : "${app2:escapeJSON(personInfoMap.titleName)}",
  "function" : "${app2:escapeJSON(salePosition.function)}",
  "imageId" : "${salePosition.imageId}",
  "imageUrl" : "${(not empty salePosition.imageId) ? imageUrlVar : ""}"
  }
</c:forEach>
]
}

