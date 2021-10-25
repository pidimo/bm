<%@ include file="/Includes.jsp" %>

{

"companyStylesArray" : [
<c:forEach var="item" items="${wvappCompanyStylesList}" varStatus="statusVar">
  <c:if test="${statusVar.index > 0}">, </c:if>
  {
  "className" : "${item.styleClassName}",
  "attributesArray" : [
    <c:forEach var="item2" items="${item.styleAttributesList}" varStatus="statusVar2">
      <c:if test="${statusVar2.index > 0}">, </c:if>
      {
      "attributeName" : "${item2.name}",
      "value" : "${app2:escapeJSON(item2.value)}"
      }
    </c:forEach>
    ]
  }
</c:forEach>
],

"userStylesArray" : [
<c:forEach var="item" items="${wvappUserStylesList}" varStatus="statusVar">
  <c:if test="${statusVar.index > 0}">, </c:if>
  {
  "className" : "${item.styleClassName}",
  "attributesArray" : [
    <c:forEach var="item2" items="${item.styleAttributesList}" varStatus="statusVar2">
      <c:if test="${statusVar2.index > 0}">, </c:if>
      {
      "attributeName" : "${item2.name}",
      "value" : "${app2:escapeJSON(item2.value)}"
      }
    </c:forEach>
    ]
  }
</c:forEach>
]

}
