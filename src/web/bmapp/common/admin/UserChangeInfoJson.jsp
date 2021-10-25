<%@ include file="/Includes.jsp" %>

<c:set var="userInfoMap" value="${app2:getUserMap(sessionScope.user.valueMap['userId'])}"/>
<c:set var="personInfoMap" value="${app2:getAddressMap(userInfoMap['addressId'])}"/>

<c:set var="imageUrlVar" value="/contacts/DownloadAddressImage.do?dto(freeTextId)=${personInfoMap['imageId']}"/>

{
  "userInfo" : {
    "userId" : "${userInfoMap['userId']}",
    "userName" : "${app2:escapeJSON(personInfoMap['addressName'])}",
    "visibleMobileApp" : "${userInfoMap['visibleMobileApp']}",
    "imageId" : "${personInfoMap['imageId']}",
    "imageUrl" : "${(not empty personInfoMap['imageId']) ? imageUrlVar : ""}"
  }
}
