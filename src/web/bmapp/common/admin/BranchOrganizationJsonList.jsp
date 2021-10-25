<%@ include file="/Includes.jsp" %>

{
<c:set var="fantaListName" value="branchMemberOrganizationList" scope="request"/>
<c:import url="/bmapp/common/util/ResultListPageInfoFragment.jsp"/>

"list":
[
<c:forEach var="branch" items="${branchMemberOrganizationList.result}" varStatus="statusVar">
  <c:set var="imageUrlVar" value="/contacts/DownloadAddressImage.do?dto(freeTextId)=${branch.imageId}"/>
  <c:set var="orgInfoMap" value="${app2:getAddressMap(branch.addressId)}"/>

  <c:if test="${statusVar.index > 0}">, </c:if>
  {
  "branchId" : "${branch.branchId}",
  "companyId" : "${branch.companyId}",
  "addressType" : "${branch.addressType}",
  "addressId" : "${branch.addressId}",

  "organizationName" : "${app2:escapeJSON(branch.organizationName)}",
  "orgCountryCode" : "${app2:escapeJSON(orgInfoMap.countryCode)}",
  "orgCountryName" : "${app2:escapeJSON(orgInfoMap.countryName)}",
  "orgZip" : "${app2:escapeJSON(orgInfoMap.zip)}",
  "orgCity" : "${app2:escapeJSON(orgInfoMap.city)}",
  "orgStreet" : "${app2:escapeJSON(orgInfoMap.street)}",
  "orgHouseNumber" : "${app2:escapeJSON(orgInfoMap.houseNumber)}",

  "imageId" : "${branch.imageId}",
  "imageUrl" : "${(not empty branch.imageId) ? imageUrlVar : ""}"
  }
</c:forEach>
]
}

