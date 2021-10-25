<%@ include file="/Includes.jsp" %>

<fanta:list listName="departmentBaseList" module="/contacts">
  <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
  <fanta:parameter field="addressId" value="${not empty organizationId ? organizationId : 0}"/>
</fanta:list>

<fanta:list listName="personTypeList" module="/catalogs">
  <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
</fanta:list>

"departmentArray" : [
<c:forEach var="item" items="${departmentBaseList.result}" varStatus="statusVar">
  <c:if test="${statusVar.index > 0}">, </c:if>
  {
  "departmentId" : "${item.departmentId}",
  "name" : "${app2:escapeJSON(item.name)}"
  }
</c:forEach>
],

"personTypeArray" : [
<c:forEach var="item" items="${personTypeList.result}" varStatus="statusVar">
  <c:if test="${statusVar.index > 0}">, </c:if>
  {
  "personTypeId" : "${item.id}",
  "name" : "${app2:escapeJSON(item.name)}"
  }
</c:forEach>
],

<c:import url="/bmapp/common/contacts/ContactCatalogsJsonFragment.jsp"/>