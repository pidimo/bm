<%@ include file="/Includes.jsp" %>

<c:set var="cityDTOList" value="${app2:findCountryCityDTOList(param.countryId, sessionScope.user.valueMap['companyId'])}"/>

{
  "cityArray" : [
  <c:forEach var="item" items="${cityDTOList}" varStatus="statusVar">
    <c:if test="${statusVar.index > 0}">, </c:if>
    {
    "cityId" : "${item.cityId}",
    "cityName" : "${app2:escapeJSON(item.cityName)}",
    "zip" : "${app2:escapeJSON(item.cityZip)}"
    }
  </c:forEach>
  ]
}