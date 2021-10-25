<%@ include file="/Includes.jsp" %>

{
<c:set var="fantaListName" value="participantList" scope="request"/>
<c:import url="/bmapp/common/util/ResultListPageInfoFragment.jsp"/>

"list" : [
  <c:forEach var="participant" items="${participantList.result}" varStatus="statusVar">
    <c:set var="imageUrlVar" value="/contacts/DownloadAddressImage.do?dto(freeTextId)=${participant.imageId}"/>

    <c:if test="${statusVar.index > 0}">, </c:if>
    {
    "scheduledUserId" : "${participant.scheduledUserId}",
    "userName" : "${app2:escapeJSON(participant.userName)}",
    "userType" : "${participant.type}",
    "appointmentId" : "${participant.appId}",
    "groupId" : "${participant.groupId}",
    "groupName" : "${app2:escapeJSON(participant.groupName)}",
    "title" : "${app2:escapeJSON(participant.title)}",
    "canBeDeleted" : "${participant.appUserId != participant.userId && participant.appUserId == sessionScope.user.valueMap.schedulerUserId}",
    "imageId" : "${participant.imageId}",
    "imageUrl" : "${(not empty participant.imageId) ? imageUrlVar : ""}"
    }
  </c:forEach>
  ]
}




