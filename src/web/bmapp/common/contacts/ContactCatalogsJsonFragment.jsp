<%@ include file="/Includes.jsp" %>

<fanta:list listName="salutationBaseList" module="/catalogs">
    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
</fanta:list>

<fanta:list listName="titleList" module="/catalogs">
    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
</fanta:list>

<fanta:list listName="countryBasicList" module="/catalogs">
    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
</fanta:list>

<fanta:list listName="languageBaseList" module="/catalogs">
    <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
</fanta:list>

<c:set var="telecomTypes" value="${app2:getTelecomTypes(pageContext.request)}"/>

${app2:buildAddressUserGroupAccessRightValues(null, pageContext.request)}
<c:set var="dataAccessUserGroups" value="${availableUserGroupList}"/>


"salutationArray" : [
<c:forEach var="item" items="${salutationBaseList.result}" varStatus="statusVar">
    <c:if test="${statusVar.index > 0}">, </c:if>
    {
    "salutationId" : "${item.id}",
    "name" : "${app2:escapeJSON(item.label)}"
    }
</c:forEach>
],

"titleArray" : [
<c:forEach var="item" items="${titleList.result}" varStatus="statusVar">
    <c:if test="${statusVar.index > 0}">, </c:if>
    {
    "titleId" : "${item.id}",
    "name" : "${app2:escapeJSON(item.name)}"
    }
</c:forEach>
],

"countryArray" : [
<c:forEach var="item" items="${countryBasicList.result}" varStatus="statusVar">
    <c:if test="${statusVar.index > 0}">, </c:if>
    {
    "countryId" : "${item.id}",
    "name" : "${app2:escapeJSON(item.name)}"
    }
</c:forEach>
],

"languageArray" : [
<c:forEach var="item" items="${languageBaseList.result}" varStatus="statusVar">
    <c:if test="${statusVar.index > 0}">, </c:if>
    {
    "languageId" : "${item.id}",
    "name" : "${app2:escapeJSON(item.name)}"
    }
</c:forEach>
],

"telecomTypeArray" : [
<c:forEach var="item" items="${telecomTypes}" varStatus="statusVar">
    <c:if test="${statusVar.index > 0}">, </c:if>
    {
    "telecomTypeId" : "${item.value}",
    "telecomTypeName" : "${app2:escapeJSON(item.label)}"
    }
</c:forEach>
],

"dataAccessUserGroupArray" : [
<c:forEach var="item" items="${dataAccessUserGroups}" varStatus="statusVar">
    <c:if test="${statusVar.index > 0}">, </c:if>
    {
    "userGroupId" : "${item.value}",
    "name" : "${app2:escapeJSON(item.label)}"
    }
</c:forEach>
]
