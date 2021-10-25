<%@ include file="/Includes.jsp" %>


<c:set var="userId"><%=request.getParameter("userId")%>
</c:set>
<c:set var="companyId" value="${sessionScope.user.valueMap['companyId']}" scope="request"/>

<c:set var="tabHeaderLabel" value="User.user" scope="request"/>
<c:set var="userName">
    <fanta:label listName="lightUserList" module="/admin" patron="0" columnOrder="userName">
        <fanta:parameter field="companyId" value="${sessionScope.user.valueMap['companyId']}"/>
        <fanta:parameter field="userId" value="${empty userId?0:userId}"/>
    </fanta:label>
</c:set>

<c:set var="tabHeaderValue" value="${userName}" scope="request"/>


<c:if test="${param.category != null}">
    <c:set var="category" value="${param.category}" scope="request"/>
</c:if>

<jsp:useBean id="tabItems" class="java.util.LinkedHashMap" scope="request"/>

<c:choose>
    <c:when test="${category == '1' || param.category == null}">
        <app2:checkAccessRight functionality="USER" permission="VIEW">
            <c:set target="${tabItems}"
                   property="Common.detail"
                   value="/User/Forward/Update.do?dto(userId)=${userId}&dto(companyId)=${companyId}&dto(userName)=${app2:encode(userName)}&dto(employeeName)=${app2:encode(userName)}"/>
        </app2:checkAccessRight>
        <app2:checkAccessRight functionality="USER" permission="UPDATE">
            <c:set target="${tabItems}"
                   property="User.password"
                   value="/User/Forward/UpdatePassword.do?dto(userId)=${userId}&dto(companyId)=${companyId}&dto(userName)=${app2:encode(userName)}&dto(employeeName)=${app2:encode(userName)}"/>
        </app2:checkAccessRight>
    </c:when>
</c:choose>


