<%@ include file="/Includes.jsp" %>

<fanta:list listName="mailAccountList" module="/webmail">
  <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
  <fanta:parameter field="userMailId" value="${sessionScope.user.valueMap['userId']}"/>
</fanta:list>

"mailAccountArray" : [
    <c:forEach var="item" items="${mailAccountList.result}" varStatus="statusVar">
      <c:if test="${statusVar.index > 0}">, </c:if>
      {
      "mailAccountId" : "${item.mailAccountId}",
      "email" : "${app2:escapeJSON(item.email)}",
      "isDefaultAccount" : "${"1" eq item.defaultAccount}"
      }
    </c:forEach>
    ]

