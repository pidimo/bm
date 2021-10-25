<%@ include file="/Includes.jsp" %>

<c:set var="roleId"><%=request.getParameter("roleId")%>
</c:set>
<c:set var="isDefault"><%=request.getParameter("isDefault")%>
</c:set>

<c:set var="companyId" value="${sessionScope.user.valueMap['companyId']}" scope="request"/>


<c:set var="tabHeaderLabel" value="Admin.Role" scope="request"/>
<c:set var="roleName">
    <fanta:label listName="lightRoleList" module="/admin" patron="0" columnOrder="roleName">
        <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        <fanta:parameter field="roleId" value="${empty roleId?0:roleId}"/>
    </fanta:label>
</c:set>

<c:set var="tabHeaderValue" value="${roleName}" scope="request"/>


<c:if test="${param.category != null}">
    <c:set var="category" value="${param.category}" scope="request"/>
</c:if>
<jsp:useBean id="tabItems" class="java.util.LinkedHashMap" scope="request"/>
<c:choose>
    <c:when test="${category == '1' || param.category == null}">
        <c:set target="${tabItems}"
               property="Common.detail"
               value="/Role/Forward/Update.do?dto(roleId)=${roleId}&dto(roleName)=${app2:encode(roleName)}"/>

        <app2:checkAccessRight functionality="ACCESSRIGHT" permission="UPDATE">
            <c:set target="${tabItems}"
                   property="Admin.AccessRights.Title"
                   value="/Role/Forward/AccessRight/Read.do?dto(companyId)=${sessionScope.user.valueMap['companyId']}&dto(roleId)=${roleId}&dto(isDefault)=${isDefault}&roleName=${app2:encode(roleName)}"/>
        </app2:checkAccessRight>
        <app2:checkAccessRight functionality="USER" permission="VIEW">
            <c:set target="${tabItems}"
                   property="Admin.User.Title"
                   value="/RoleUser/RoleUserList.do?dto(companyId)=${sessionScope.user.valueMap['companyId']}&param(roleId)=${roleId}&roleName=${app2:encode(roleName)}"/>
        </app2:checkAccessRight>
    </c:when>
</c:choose>
