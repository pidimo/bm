<%@ include file="/Includes.jsp" %>

{
<c:set var="fantaListName" value="userMemberList" scope="request"/>
<c:import url="/bmapp/common/util/ResultListPageInfoFragment.jsp"/>

"list":
[
<c:forEach var="user" items="${userMemberList.result}" varStatus="statusVar">
  <c:set var="imageUrlVar" value="/contacts/DownloadAddressImage.do?dto(freeTextId)=${user.imageId}"/>
  <c:set var="personInfoMap" value="${app2:getAddressMap(user.memberContactPersonId)}"/>

  <c:if test="${statusVar.index > 0}">, </c:if>
  {
  "userId" : "${user.userId}",
  "addressType" : "${user.addressType}",
  "memberAddressId" : "${user.memberAddressId}",
  "memberContactPersonId" : "${user.memberContactPersonId}",
  "organizationName" : "${app2:escapeJSON(user.organizationName)}",
  "contactPersonName" : "${app2:escapeJSON(user.contactPersonName)}",
  "titleName" : "${app2:escapeJSON(personInfoMap.titleName)}",
  "function" : "${app2:escapeJSON(user.function)}",
  "imageId" : "${user.imageId}",
  "imageUrl" : "${(not empty user.imageId) ? imageUrlVar : ""}"
  }
</c:forEach>
]
}

