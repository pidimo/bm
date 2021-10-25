<%@ include file="/Includes.jsp" %>

<c:set var="userGroupId"><%=request.getParameter("userGroupId")%></c:set>
<c:set var="companyId" value="${sessionScope.user.valueMap['companyId']}" scope="request" />


<c:set var="tabHeaderLabel" value="Admin.UserGroup" scope="request"/>
<c:set var="groupName" scope="request" >
    <fanta:label listName="ligthGroupList" module="/admin" patron="0" columnOrder="groupName" >
        <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        <fanta:parameter field="groupId" value="${empty userGroupId?0:userGroupId}" />
    </fanta:label>
</c:set>

<c:set var="tabHeaderValue" value="${groupName}" scope="request" />
<c:import url="${layout}/TabHeader.jsp"/>
<c:if test="${param.category != null}">
    <c:set var="category" value="${param.category}" scope="request"/>
</c:if>
<jsp:useBean id="tabItems" class="java.util.LinkedHashMap" scope="request"/>
<c:choose>
    <c:when test="${category == '1' || param.category == null}">
        <c:set target="${tabItems}"
 property="Common.detail"
 value="/User/Forward/UpdateUserGroup.do?dto(userGroupId)=${userGroupId}&dto(companyId)=${companyId}&dto(groupName)=${groupName}"/>
        <c:set target="${tabItems}"
 property="Admin.UserGroup.member"
 value="/User/Forward/UserGroupImportList.do?dto(userGroupId)=${userGroupId}&dto(companyId)=${companyId}&dto(groupName)=${groupName}"/>
    </c:when>
</c:choose>
<c:import url="${sessionScope.layout}/submenu.jsp"/>