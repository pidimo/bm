<%@ include file="/Includes.jsp" %>

{
  "userInfo" : {
      "userId" : "${sessionScope.user.valueMap['userId']}",
      "locale" : "${sessionScope.user.valueMap['locale']}",
      "dateTimeZone" : "${sessionScope.user.valueMap['dateTimeZone']}"
  },

  <c:import url="/bmapp/common/admin/AccessRightJsonFragment.jsp"/>
}
