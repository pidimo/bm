<%@ include file="/Includes.jsp" %>

{
<c:set var="fantaListName" value="branchMemberList" scope="request"/>
<c:import url="/bmapp/common/util/ResultListPageInfoFragment.jsp"/>

"list":
[
<c:forEach var="branch" items="${branchMemberList.result}" varStatus="statusVar">
  <c:set var="imageUrlVar" value="/contacts/DownloadAddressImage.do?dto(freeTextId)=${branch.imageId}"/>
  <c:set var="personInfoMap" value="${app2:getAddressMap(branch.memberContactPersonId)}"/>

  <c:if test="${statusVar.index > 0}">, </c:if>
  {
  "branchId" : "${branch.branchId}",
  "companyId" : "${branch.companyId}",
  "addressType" : "${branch.addressType}",
  "memberAddressId" : "${branch.memberAddressId}",
  "memberContactPersonId" : "${branch.memberContactPersonId}",
  "organizationName" : "${app2:escapeJSON(branch.organizationName)}",
  "contactPersonName" : "${app2:escapeJSON(branch.contactPersonName)}",
  "titleName" : "${app2:escapeJSON(personInfoMap.titleName)}",
  "function" : "${app2:escapeJSON(branch.function)}",
  "imageId" : "${branch.imageId}",
  "imageUrl" : "${(not empty branch.imageId) ? imageUrlVar : ""}"
  }
</c:forEach>
]
}

