<%@ include file="/Includes.jsp" %>

{
<c:set var="fantaListName" value="branchList" scope="request"/>
<c:import url="/bmapp/common/util/ResultListPageInfoFragment.jsp"/>

"list":
[
<c:forEach var="branch" items="${branchList.result}" varStatus="statusVar">

  <c:if test="${statusVar.index > 0}">, </c:if>
  {
  "branchId" : "${branch.id}",
  "companyId" : "${branch.companyId}",
  "branchName" : "${app2:escapeJSON(branch.name)}"
  }
</c:forEach>
]
}

