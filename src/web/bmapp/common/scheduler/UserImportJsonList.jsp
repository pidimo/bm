<%@ include file="/Includes.jsp" %>

{
<c:set var="fantaListName" value="userAppointmentImportList" scope="request"/>
<c:import url="/bmapp/common/util/ResultListPageInfoFragment.jsp"/>

"list" : [
    <c:forEach var="user" items="${userAppointmentImportList.result}" varStatus="statusVar">
      <c:set var="imageUrlVar" value="/contacts/DownloadAddressImage.do?dto(freeTextId)=${user.imageId}"/>

      <c:if test="${statusVar.index > 0}">, </c:if>
      {
      "userId" : "${user.userId}",
      "userName" : "${app2:escapeJSON(user.userName)}",
      "login" : "${app2:escapeJSON(user.login)}",
      "type" : "${user.type}",
      "imageId" : "${user.imageId}",
      "imageUrl" : "${(not empty user.imageId) ? imageUrlVar : ""}"
      }
    </c:forEach>
    ]
}

