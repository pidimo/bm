<%@ include file="/Includes.jsp" %>

<fanta:list listName="productGroupSimpleList" module="/catalogs">
  <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
</fanta:list>

<fanta:list listName="productUnitList" module="/catalogs">
  <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
</fanta:list>

<fanta:list listName="productTypeList" module="/catalogs">
  <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
</fanta:list>

<fanta:list listName="vatList" module="/catalogs">
  <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
</fanta:list>

<fanta:list listName="accountList" module="/catalogs">
  <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
</fanta:list>


"productGroupArray" : [
    <c:forEach var="item" items="${productGroupSimpleList.result}" varStatus="statusVar">
      <c:if test="${statusVar.index > 0}">, </c:if>
      {
      "productGroupId" : "${item.id}",
      "name" : "${app2:escapeJSON(item.name)}"
      }
    </c:forEach>
    ],

"productUnitArray" : [
    <c:forEach var="item" items="${productUnitList.result}" varStatus="statusVar">
      <c:if test="${statusVar.index > 0}">, </c:if>
      {
      "unitId" : "${item.id}",
      "name" : "${app2:escapeJSON(item.name)}"
      }
    </c:forEach>
    ],

"productTypeArray" : [
    <c:forEach var="item" items="${productTypeList.result}" varStatus="statusVar">
      <c:if test="${statusVar.index > 0}">, </c:if>
      {
      "productTypeId" : "${item.id}",
      "name" : "${app2:escapeJSON(item.name)}"
      }
    </c:forEach>
    ],

"vatArray" : [
    <c:forEach var="item" items="${vatList.result}" varStatus="statusVar">
      <c:if test="${statusVar.index > 0}">, </c:if>
      {
      "vatId" : "${item.id}",
      "name" : "${app2:escapeJSON(item.name)}"
      }
    </c:forEach>
    ],

"accountArray" : [
    <c:forEach var="item" items="${accountList.result}" varStatus="statusVar">
      <c:if test="${statusVar.index > 0}">, </c:if>
      {
      "accountId" : "${item.accountId}",
      "name" : "${app2:escapeJSON(item.name)}"
      }
    </c:forEach>
    ]

