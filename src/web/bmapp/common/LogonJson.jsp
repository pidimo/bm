<%@ include file="/Includes.jsp" %>

{
"userInfo" : {
    "userId" : "${sessionScope.user.valueMap['userId']}",
    "userAddressId" : "${sessionScope.user.valueMap['userAddressId']}",
    "companyId" : "${sessionScope.user.valueMap['companyId']}",
    "login" : "${sessionScope.user.valueMap['login']}",
    "locale" : "${sessionScope.user.valueMap['locale']}",
    "timeout" : "${sessionScope.user.valueMap['timeout']}",
    "dateTimeZone" : "${sessionScope.user.valueMap['dateTimeZone']}",
    "JSESSIONID" : "${loginJSessionId}"
},

<c:import url="/bmapp/common/admin/AccessRightJsonFragment.jsp"/>
}
